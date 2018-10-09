package com.creativemd.littletiles.common.utils.grid;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.littletiles.LittleTilesConfig;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.common.tiles.vec.LittleUtils;
import com.google.common.math.IntMath;

import net.minecraft.nbt.NBTTagCompound;

public class LittleGridContext {
	
	public static final int overallDefault = 16;
	
	@Deprecated
	public static final int oldHalfGridSize = 8;
	
	public static int[] gridSizes;
	public static int minSize;
	public static int multiplier;
	public static int defaultSize;
	private static int defaultSizeIndex;
	public static LittleGridContext[] context;
	
	public static LittleGridContext loadGrid(int min, int defaultGrid, int scale, int multiplier) {
		LittleGridContext.minSize = min;
		LittleGridContext.defaultSize = defaultGrid;
		LittleGridContext.gridSizes = new int[scale];
		LittleGridContext.context = new LittleGridContext[scale];
		LittleGridContext.multiplier = multiplier;
		int size = min;
		for (int i = 0; i < gridSizes.length; i++) {
			LittleGridContext.gridSizes[i] = size;
			LittleGridContext.context[i] = new LittleGridContext(size, i);
			if (LittleGridContext.context[i].isDefault)
				LittleGridContext.defaultSizeIndex = i;
			size *= multiplier;
		}
		
		return get();
	}
	
	public static List<String> getNames() {
		List<String> names = new ArrayList<>();
		for (int i = 0; i < context.length; i++) {
			names.add(context[i].size + "");
		}
		return names;
	}
	
	public static LittleGridContext get(int size) {
		if (defaultSize == size)
			return context[LittleGridContext.defaultSizeIndex];
		for (int i = 0; i < context.length; i++) {
			if (context[i].size == size)
				return context[i];
		}
		throw new RuntimeException("Invalid gridsize = '" + size + "'!");
	}
	
	public static LittleGridContext get() {
		return context[LittleGridContext.defaultSizeIndex];
	}
	
	public static LittleGridContext get(NBTTagCompound nbt) {
		if (nbt.hasKey("grid"))
			return LittleGridContext.get(nbt.getInteger("grid"));
		return LittleGridContext.get();
	}
	
	public static LittleGridContext getOverall(NBTTagCompound nbt) {
		if (nbt.hasKey("grid"))
			return LittleGridContext.get(nbt.getInteger("grid"));
		return LittleGridContext.get(overallDefault);
	}
	
	public static LittleGridContext getMin() {
		return context[0];
	}
	
	public static LittleGridContext getMax() {
		return context[context.length - 1];
	}
	
	/* There is no use for it, so I rather comment it out before somebody (I'm talking about myself) does something wrong
	 * 
	 * public static LittleGridContext min(LittleGridContext context, LittleGridContext context2) {
	 * if (context.size <= context2.size)
	 * return context;
	 * return context2;
	 * } */
	
	public static LittleGridContext max(LittleGridContext context, LittleGridContext context2) {
		if (context.size >= context2.size)
			return context;
		return context2;
	}
	
	public static void remove(NBTTagCompound nbt) {
		nbt.removeTag("grid");
	}
	
	public final int size;
	public final double gridMCLength;
	public final int minPos;
	public final int maxPos;
	public final int maxTilesPerBlock;
	public final double minimumTileSize;
	public final boolean isDefault;
	public final int index;
	
	/** doubled **/
	public final LittleTileVec rotationCenter;
	
	public final int[] minSizes;
	
	protected LittleGridContext(int gridSize, int index) {
		this.index = index;
		this.size = gridSize;
		this.gridMCLength = 1D / gridSize;
		this.minPos = 0;
		this.maxPos = gridSize;
		this.maxTilesPerBlock = gridSize * gridSize * gridSize;
		this.minimumTileSize = 1D / this.maxTilesPerBlock;
		this.isDefault = this.defaultSize == gridSize;
		
		this.minSizes = new int[this.size];
		this.minSizes[0] = minSize;
		for (int i = 1; i < this.minSizes.length; i++) {
			this.minSizes[i] = this.size / IntMath.gcd(i, this.size);
			if (this.minSizes[i] < minSize || this.minSizes[i] % minSize != 0)
				this.minSizes[i] = this.size;
		}
		
		this.rotationCenter = new LittleTileVec(this.size, this.size, this.size);
	}
	
	public void set(NBTTagCompound nbt) {
		if (!isDefault || LittleTilesConfig.core.forceToSaveDefaultSize)
			nbt.setInteger("grid", size);
		else
			nbt.removeTag("grid");
	}
	
	public void setOverall(NBTTagCompound nbt) {
		if (size != overallDefault)
			nbt.setInteger("grid", size);
	}
	
	public int getMinGrid(int value) {
		return minSizes[Math.abs(value % size)];
	}
	
	public double toVanillaGrid(double grid) {
		return grid * gridMCLength;
	}
	
	public float toVanillaGrid(float grid) {
		return (float) (grid * gridMCLength);
	}
	
	public double toVanillaGrid(long grid) {
		return grid * gridMCLength;
	}
	
	public double toVanillaGrid(int grid) {
		return grid * gridMCLength;
	}
	
	public int toBlockOffset(int grid) {
		if (grid > 0)
			return (int) (grid / size);
		return (int) Math.floor(grid / (double) size);
	}
	
	public boolean isAtEdge(double pos) {
		return pos % gridMCLength == 0;
	}
	
	public int toGrid(int pos) {
		return pos * size;
	}
	
	public long toGridAccurate(double pos) {
		pos = LittleUtils.round(pos * size);
		if (pos < 0)
			return (long) Math.floor(pos);
		return (long) pos;
	}
	
	public int toGrid(double pos) {
		pos = LittleUtils.round(pos * size);
		if (pos < 0)
			return (int) Math.floor(pos);
		return (int) pos;
	}
	
	public LittleGridContext ensureContext(LittleGridContext context) {
		if (context.size > this.size)
			return context;
		return this;
	}
	
	@Override
	public String toString() {
		return "" + size;
	}
}
