package com.creativemd.littletiles.common.utils.rotation;

import com.creativemd.creativecore.common.utils.Rotation;
import com.creativemd.creativecore.common.utils.RotationUtils;
import com.creativemd.littletiles.common.entity.EntityAnimation;

import net.minecraft.nbt.NBTTagCompound;

public class OrdinaryDoorTransformation extends DoorTransformation{
	
	public Rotation rotation;
	
	public OrdinaryDoorTransformation() {
		
	}
	
	public OrdinaryDoorTransformation(Rotation rotation) {
		this.rotation = rotation;
	}

	@Override
	public void performTransformation(EntityAnimation animation, double progress) {
		switch(rotation)
		{
		/*case EAST:
			animation.worldRotY = progress*90;
			break;
		case WEST:
			animation.worldRotY = progress*90;
			break;*/
		case NORTH:
			animation.worldRotY = -90+progress*90;
			break;
		case SOUTH:
			animation.worldRotY = (1-progress)*90;
			break;
		case UP:
			animation.worldRotZ = -90+progress*90;
			break;
		case DOWN:
			animation.worldRotZ = (1-progress)*90;
			break;	
		case UPX:
			animation.worldRotX = (1-progress)*90;
			break;
		case DOWNX:
			animation.worldRotX = -90+progress*90;
			break;
		}
	}

	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setInteger("rot", rotation.ordinal());
	}

	@Override
	protected void readFromNBT(NBTTagCompound nbt) {
		rotation = Rotation.getRotationByID(nbt.getInteger("rot"));
	}

}