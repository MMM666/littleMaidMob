package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

public class LMM_EntityMode_Test extends LMM_EntityModeBase implements ICommand {

	public static boolean isEnable = false;
	
	
	/**
	 * 各種実験用。 
	 */
	public LMM_EntityMode_Test(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}
	
	@Override
	public void init() {
		ModLoader.addCommand(this);
	}

	@Override
	public int priority() {
		return 9900;
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {

	}

	@Override
	public void showSpecial(LMM_RenderLittleMaid prenderlittlemaid, double px, double py, double pz) {
		if (!isEnable) return;
		
		// 名前とかの表示用
		List<String> llist = new ArrayList<String>();
		double ld;
		
		if (owner.maidDominantArm == 0) {
			llist.add(String.format("[R]:%d, L:%d, I:%d", owner.mstatSwingStatus[0].index, owner.mstatSwingStatus[1].index, owner.maidInventory.currentItem));
			llist.add(String.format("swing[R]:%b:%f", owner.getSwingStatusDominant().isSwingInProgress, owner.getSwingStatusDominant().swingProgress));
		} else {
			llist.add(String.format("R:%d, [L]:%d, I:%d", owner.mstatSwingStatus[0].index, owner.mstatSwingStatus[1].index, owner.maidInventory.currentItem));
			llist.add(String.format("swing[L]:%b:%f", owner.getSwingStatusDominant().isSwingInProgress, owner.getSwingStatusDominant().swingProgress));
		}
		llist.add(String.format("health:%d, death:%d, Exp:%d", owner.health, owner.deathTime, owner.experienceValue));
//		llist.add("stat:" + owner.statusMessage);
		llist.add(String.format("working:%b, sneak:%b, sugar:%b", owner.isWorking(), owner.isSneaking(), owner.isLookSuger()));
		llist.add(String.format("%s[%s]", owner.getMaidModeString(), owner.maidActiveModeClass == null ? "" : owner.maidActiveModeClass.getClass().getSimpleName()));
		llist.add(String.format("Limit: %b[%b]", owner.isMaidContract(), owner.isMaidContractEX()));
		int li = owner.dataWatcher.getWatchableObjectInt(LMM_EntityLittleMaid.dataWatch_Texture);
		llist.add(String.format("Texture=%s(%x/ %x), %s(%x / %x)",
				owner.textureBox[0].textureName, owner.textureIndex[0], li & 0xffff,
				owner.textureBox[1].textureName, owner.textureIndex[1], (li >>> 16)
				));
		
		ld = (double)llist.size() * 0.25D - 0.5D;
		for (String ls : llist) {
			prenderlittlemaid.renderLivingLabel(owner, ls, px, py + ld, pz, 64);
			ld -= 0.25D;
		}
/*		
		GL11.glPushMatrix();
		GL11.glTranslatef(1.5F * MathHelper.cos(MMM_Helper.mc.thePlayer.rotationYaw), 0F, 1.5F * MathHelper.sin(MMM_Helper.mc.thePlayer.rotationYaw));
//		GL11.glRotatef(owner.rotationYaw, 0F, 1F, 0F);
		llist.clear();
		llist.add(String.format("motX:%+10.2f", owner.motionX));
		llist.add(String.format("motY:%+10.2f", owner.motionY));
		llist.add(String.format("motZ:%+10.2f", owner.motionZ));

		ld = (double)llist.size() * 0.25D - 1.5D;
		for (String ls : llist) {
			prenderlittlemaid.renderLivingLabel(owner, ls, px, py + ld, pz, 64);
			ld -= 0.25D;
		}
		GL11.glPopMatrix();
*/
	}

	
	// デバッグ表示コマンド追加用
	
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "LMMtest";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
//		return "";
		return "/" + this.getCommandName() + " <0-4>";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var2.length > 0) {
			switch (Integer.valueOf(var2[0])) {
			case 0:
				isEnable = false;
				CommandBase.notifyAdmins(var1, 0, "LMM TestMessage Disable", new Object[] {});
				break;
			case 1:
				isEnable = true;
				CommandBase.notifyAdmins(var1, 0, "LMM TestMessage Enable", new Object[] {});
				break;
			case 2:
				// textureIndex
				var1.sendChatToPlayer("textureServer:");
				for (int li = 0; li < MMM_TextureManager.textureServer.size(); li++) {
					MMM_TextureBoxServer lb = MMM_TextureManager.getTextureBoxServer(li);
					var1.sendChatToPlayer(String.format("%4d : %04x : %s", li, lb.wildColor, lb.textureName));
				}
				break;
			case 3:
				// textures
				var1.sendChatToPlayer("textures:");
				for (MMM_TextureBox ltb : MMM_TextureManager.textures) {
					var1.sendChatToPlayer(ltb.textureName);
				}
				break;
			case 4:
				// textures
				var1.sendChatToPlayer("textureServerIndex:");
				for (Entry<MMM_TextureBox, Integer> ltb : MMM_TextureManager.textureServerIndex.entrySet()) {
					var1.sendChatToPlayer(String.format("%04x, %s", ltb.getValue(), ltb.getKey().textureName));
				}
				break;
			}
			
//			isEnable = var2[0].equalsIgnoreCase("0") ? false : true;
//			CommandBase.notifyAdmins(var1, 0, "LMM TestMessage" + (isEnable ? "Enable" : "Disable"), new Object[] {});
		} else {
			throw new WrongUsageException(getCommandUsage(var1), new Object[0]);
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		// 特に変換しない
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] var1, int var2) {
		return false;
	}

	@Override
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		if (pitemstack.itemID == Item.slimeBall.itemID) {
//		if (pitemstack.itemID == Item.slimeBall.itemID && owner.maidContractLimit > 0) {
			owner.maidContractLimit -= 24000;
			return true;
		}
		return false;
	}

}
