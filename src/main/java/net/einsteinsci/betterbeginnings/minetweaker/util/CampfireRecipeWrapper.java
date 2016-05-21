package net.einsteinsci.betterbeginnings.minetweaker.util;

import org.lwjgl.Sys;

import net.einsteinsci.betterbeginnings.register.recipe.CampfirePanRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.CampfireRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;

public class CampfireRecipeWrapper 
{
	private OreRecipeElement input;
	private ItemStack output;
	private float xp;
	private boolean isPanRecipe;
	
	public CampfireRecipeWrapper(OreRecipeElement input, ItemStack output, float xp, boolean isPanRecipe) 
	{
		this.input = input;
		this.output = output;
		this.xp = xp;
		this.isPanRecipe = isPanRecipe;
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
	
	public boolean isPanRecipe() 
	{
		return isPanRecipe;
	}

	public void add() 
	{
		if(isPanRecipe())
		{
			CampfirePanRecipes.smelting().putLists(input, output, xp);
		}
		else
		{
			CampfireRecipes.smelting().putLists(input, output, xp);
		}
	}
	
}
