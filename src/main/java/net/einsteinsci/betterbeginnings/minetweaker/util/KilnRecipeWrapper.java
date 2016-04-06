package net.einsteinsci.betterbeginnings.minetweaker.util;

import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;

public class KilnRecipeWrapper 
{
	private OreRecipeElement input;
	private ItemStack output;
	private float xp;
	
	public KilnRecipeWrapper(OreRecipeElement input, ItemStack output, float xp) 
	{
		this.input = input;
		this.output = output;
		this.xp = xp;
	}
	
	public OreRecipeElement getInput()
	{
		return input;
	}
	
	public ItemStack getOutput()
	{
		return output;
	}
	
	public float getXP()
	{
		return xp;
	}
	
	public void add() 
	{
		KilnRecipes.smelting().putLists(input, output, xp);
	}
}
