package net.minecraft.src;

public class LMM_EntityMode_BookDecorder extends LMM_EntityModeBase {

	public LMM_EntityMode_BookDecorder(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 8000;
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
	}

	@Override
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		if (pitemstack.getItem() instanceof ItemWritableBook) {
			ItemWritableBook lwb = (ItemWritableBook)pitemstack.getItem();
			if (lwb.validBookTagPages(pitemstack.getTagCompound())) {
				NBTTagList llist = pitemstack.getTagCompound().getTagList("pages");
				String ls = "";
				for (int li = 0; li < llist.tagCount(); li++) {
					NBTTagString ltex = (NBTTagString)llist.tagAt(li);
					ls += ltex.data;
				}
				
				String lcommands[] = ls.split(";");
				String lcom[];
				MMM_TextureBoxBase lboxs[] = new MMM_TextureBoxBase[] {
						owner.textureBox[0],
						owner.textureBox[1]
				};
				int lcolor = owner.getColor();
				boolean lflag = false;
				for (String lt : lcommands) {
					lcom = lt.split(":");
					if (lcom.length > 1) {
						lcom[0] = lcom[0].trim();
						lcom[1] = lcom[1].trim();
						
						try {
							if (lcom[0].equals("color")) {
								lcolor = Integer.valueOf(lcom[1]) & 0x0f;
								lflag = true;
							}
							else if (lcom[0].equals("texture")) {
								MMM_TextureBoxBase lbox = MMM_TextureManager.instance.getTextureBox(lcom[1]);
								if (lbox != null) {
									lboxs[0] = lbox;
									lflag = true;
								}
							}
							else if (lcom[0].equals("armor")) {
								MMM_TextureBoxBase lbox = MMM_TextureManager.instance.getTextureBox(lcom[1]);
								if (lbox != null) {
									lboxs[1] = lbox;
									lflag = true;
								}
							}
							else if (lcom[0].equals("dominantArm")) {
								int li = Integer.valueOf(lcom[1]);
								owner.setDominantArm(li);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (pentityplayer.worldObj.isRemote) {
					if (lflag) {
						MMM_TextureManager.instance.postSetTexturePack(owner, lcolor, lboxs);
					}
				}
				return true;
			}
		}
		return false;
	}

}
