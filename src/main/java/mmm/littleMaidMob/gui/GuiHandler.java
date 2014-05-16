package mmm.littleMaidMob.gui;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.inventory.ContainerInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player,
			World world, int x, int y, int z) {
		if (ID == 0) {
			Entity lentity = world.getEntityByID(x);
			if (lentity instanceof EntityLittleMaidBase) {
				ContainerInventory lci = new ContainerInventory((EntityLittleMaidBase)lentity, player);
				lci.player = (EntityPlayerMP)player;
				return lci;
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player,
			World world, int x, int y, int z) {
		if (ID == 0) {
			Entity lentity = world.getEntityByID(x);
			if (lentity instanceof EntityLittleMaidBase) {
				return new GuiLittleMaidInventory((EntityLittleMaidBase)lentity, player);
			}
		}
		return null;
//		return new GuiLittleMaidInventory(player.inventory, world, x, y, z);
	}

}
