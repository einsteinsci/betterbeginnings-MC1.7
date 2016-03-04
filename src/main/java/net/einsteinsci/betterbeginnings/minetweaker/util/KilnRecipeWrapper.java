package net.einsteinsci.betterbeginnings.minetweaker.util;

import net.minecraft.item.ItemStack;

public class KilnRecipeWrapper 
{
	private ItemStack input;
	private ItemStack output;
	private float xp;
	
	public KilnRecipeWrapper(ItemStack input, ItemStack output, float xp) 
	{
		this.input = input;
		this.output = output;
		this.xp = xp;
	}
	
	public ItemStack getInput()
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
}
