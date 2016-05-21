package net.einsteinsci.betterbeginnings.minetweaker;

import minetweaker.MineTweakerAPI;
import net.einsteinsci.betterbeginnings.minetweaker.util.MineTweakerUtil;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MineTweakerCompat 
{
	public static void register()
	{
		MineTweakerAPI.registerClass(CampfireTweaker.class);
		MineTweakerAPI.registerClass(KilnTweaker.class);
		MineTweakerAPI.registerClass(SmelterTweaker.class);
		MineTweakerAPI.registerClass(OvenTweaker.class);
		MineTweakerAPI.registerClass(AdvancedCraftingTweaker.class);
		test();
	}

	private static void test()
	{
		
	}
}
