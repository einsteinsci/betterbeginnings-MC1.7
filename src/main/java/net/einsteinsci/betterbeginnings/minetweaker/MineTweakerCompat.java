package net.einsteinsci.betterbeginnings.minetweaker;

import cpw.mods.fml.common.registry.GameRegistry;
import minetweaker.MineTweakerAPI;
import net.einsteinsci.betterbeginnings.minetweaker.util.MineTweakerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MineTweakerCompat 
{
	public static void register()
	{
		MineTweakerAPI.registerClass(KilnTweaker.class);
		MineTweakerAPI.registerClass(OvenTweaker.class);
		MineTweakerAPI.registerClass(SmelterTweaker.class);
		test();
	}

	private static void test()
	{
		ItemStack stickStack = new ItemStack(Items.stick);
		ItemStack appleStack = new ItemStack(Items.apple);;
		/*GameRegistry.addShapedRecipe(new ItemStack(Items.beef), MineTweakerUtil.formatShapedRecipeInputs(new Object[][]
				{
				{stickStack, stickStack, stickStack},
				{stickStack, "ingotIron", stickStack},
				{stickStack, stickStack,stickStack}
				}));*/
		GameRegistry.addShapedRecipe(new ItemStack(Items.beef), new Object[]{"aaa", "aba", "aaa", 'a', Items.stick, 'b', "ingotIron"});
	}
}
