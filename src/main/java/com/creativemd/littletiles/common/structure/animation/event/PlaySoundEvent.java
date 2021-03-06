package com.creativemd.littletiles.common.structure.animation.event;

import com.creativemd.creativecore.client.sound.EntitySound;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.entity.DoorController;
import com.creativemd.littletiles.common.entity.EntityAnimationController;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.type.door.LittleDoor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlaySoundEvent extends AnimationEvent {
    
    public SoundEvent sound;
    public float volume;
    public float pitch;
    public boolean opening;
    
    public PlaySoundEvent(int tick) {
        super(tick);
    }
    
    @Override
    public int getEventDuration(LittleStructure structure) {
        return 0;
    }
    
    @Override
    protected void write(NBTTagCompound nbt) {
        if (sound instanceof SoundEventMissing)
            nbt.setString("sound", ((SoundEventMissing) sound).location.toString());
        else
            nbt.setString("sound", sound.getRegistryName().toString());
        nbt.setFloat("volume", volume);
        nbt.setFloat("pitch", pitch);
        nbt.setBoolean("opening", opening);
    }
    
    @Override
    protected void read(NBTTagCompound nbt) {
        sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(nbt.getString("sound")));
        if (sound == null)
            sound = new SoundEventMissing(new ResourceLocation(nbt.getString("sound")));
        volume = nbt.getFloat("volume");
        pitch = nbt.getFloat("pitch");
        opening = nbt.getBoolean("opening");
    }
    
    @Override
    protected boolean run(EntityAnimationController controller) {
        if (controller.parent.world.isRemote && controller.getAimedState().name.equals(DoorController.openedState) == opening)
            playSound(controller);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void playSound(EntityAnimationController controller) {
        if (!(sound instanceof SoundEventMissing))
            GuiControl.playSound(new EntitySound(sound, controller.parent, volume, pitch, SoundCategory.NEUTRAL));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void runGui(AnimationGuiHandler handler) {
        if (opening && !(sound instanceof SoundEventMissing))
            GuiControl.playSound(sound, volume, pitch);
    }
    
    @Override
    public void invert(LittleDoor door, int duration) {
        //this.tick = duration - getMinimumRequiredDuration(door);
    }
    
    public static class SoundEventMissing extends SoundEvent {
        
        public final ResourceLocation location;
        
        public SoundEventMissing(ResourceLocation location) {
            super(location);
            this.location = location;
        }
    }
}
