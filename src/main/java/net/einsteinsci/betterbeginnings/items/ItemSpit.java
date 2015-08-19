package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemSpit extends ItemPan
{
	public ItemSpit()
	{
		super();
		setUnlocalizedName("spit");
		setMaxDamage(4);
		setMaxStackSize(1);
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
