package com.creativemd.littletiles.client.render;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.creativemd.littletiles.client.LittleTilesClient;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockLayerRenderBuffer {
	
	private AtomicBoolean isDrawing = new AtomicBoolean(false);
	
	public synchronized void setDrawing() throws RenderOverlapException
	{
		if(isDrawing.get())
			throw new RenderOverlapException();
		isDrawing.set(true);
	}
	
	public synchronized void setFinishedDrawing()
	{
		isDrawing.set(false);
	}
	
	public synchronized boolean isDrawing()
	{
		return isDrawing.get();
	}
	
	public final VertexFormat format;
	
	public final int bufferSizePerQuad;
	
	public BlockLayerRenderBuffer() {
		this(LittleTilesClient.getBlockVertexFormat());
	}
	
	public BlockLayerRenderBuffer(VertexFormat format) {
		this.format = format;
		bufferSizePerQuad = format.getIntegerSize() * 4;
	}
	
	private VertexBuffer solid;
	private VertexBuffer cutout_mipped;
	private VertexBuffer cutout;
	private VertexBuffer translucent;
	
	public int getBufferSizeForLayer(int tilesOfType)
	{
		return bufferSizePerQuad * 6 * tilesOfType;
	}
	
	public VertexBuffer createVertexBuffer(int tilesOfType)
	{
		return LittleTilesClient.createVertexBuffer(getBufferSizeForLayer(tilesOfType));
	}
	
	/*public VertexBuffer getTemporaryBufferByLayer(BlockRenderLayer layer)
	{
		switch(layer)
		{
		case SOLID:
			return solidTemp;
		case CUTOUT_MIPPED:
			return cutout_mippedTemp;
		case CUTOUT:
			return cutoutTemp;
		case TRANSLUCENT:
			return translucentTemp;
		}
		return null;
	}
	
	public void setTemporaryBufferByLayer(VertexBuffer buffer, BlockRenderLayer layer)
	{
		switch(layer)
		{
		case SOLID:
			solidTemp = buffer;
			break;
		case CUTOUT_MIPPED:
			cutout_mippedTemp = buffer;
			break;
		case CUTOUT:
			cutoutTemp = buffer;
			break;
		case TRANSLUCENT:
			translucentTemp = buffer;
			break;
		}
	}*/
	
	public VertexBuffer getBufferByLayer(BlockRenderLayer layer)
	{
		switch(layer)
		{
		case SOLID:
			return solid;
		case CUTOUT_MIPPED:
			return cutout_mipped;
		case CUTOUT:
			return cutout;
		case TRANSLUCENT:
			return translucent;
		}
		return null;
	}
	
	public void setBufferByLayer(VertexBuffer buffer, BlockRenderLayer layer)
	{
		switch(layer)
		{
		case SOLID:
			solid = buffer;
			break;
		case CUTOUT_MIPPED:
			cutout_mipped = buffer;
			break;
		case CUTOUT:
			cutout = buffer;
			break;
		case TRANSLUCENT:
			translucent = buffer;
			break;
		}
	}
	
	public void clear()
	{
		/*if(solid != null)
			solid.deleteGlBuffers();
		if(cutout_mipped != null)
			cutout_mipped.deleteGlBuffers();
		if(cutout != null)
			cutout.deleteGlBuffers();
		if(translucent != null)
			translucent.deleteGlBuffers();*/
		
		//TODO Check if this causes memory leaks
		solid = null;
		cutout_mipped = null;
		cutout = null;
		translucent = null;		
	}
	
	public static class RenderOverlapException extends Exception{
		
		public RenderOverlapException() {
			super("Buffer is already rendering!");
		}
		
	}
	
}
