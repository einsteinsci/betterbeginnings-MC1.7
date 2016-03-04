package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.minetweaker.util.KilnRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.Map.Entry;

import org.lwjgl.Sys;

import com.google.common.collect.Maps;

public class KilnRecipes
{
	private static final KilnRecipes SMELTINGBASE = new KilnRecipes();

	private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();

	private KilnRecipes()
	{
		// nothing here
	}

	public static void addRecipe(Item input, ItemStack output, float experience)
	{
		smelting().addLists(input, output, experience);
	}

	public void addLists(Item input, ItemStack itemStack, float experience)
	{
		putLists(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), itemStack, experience);
	}

	public static KilnRecipes smelting()
	{
		return SMELTINGBASE;
	}

	public void putLists(ItemStack itemStack, ItemStack itemStack2, float experience)
	{
		smeltingList.put(itemStack, itemStack2);
		experienceList.put(itemStack2, experience);
	}

	public static void addRecipe(String input, ItemStack output, float experience)
	{
		for (ItemStack stack : OreDictionary.getOres(input))
		{
			smelting().putLists(stack, output, experience);
		}
	}

	public static void addRecipe(Block input, ItemStack output, float experience)
	{
		smelting().addLists(Item.getItemFromBlock(input), output, experience);
	}

	public static void addRecipe(ItemStack input, ItemStack output, float experience)
	{
		smelting().putLists(input, output, experience);
	}

	/*The second parameter is to prevent conflicts with the next method,
	 * it's never used because there can only be one kiln recipe per input*/
	public static void removeRecipe(ItemStack input, ItemStack output)
	{
		smelting().smeltingList.remove(input);
		smelting().experienceList.remove(input);
		System.out.println(smelting().smeltingList);
	}
	
	public static List<KilnRecipeWrapper> removeOutput(ItemStack output)
	{
		List<KilnRecipeWrapper> removedRecipes = new ArrayList<KilnRecipeWrapper>();
		for (Iterator<ItemStack> iter = smelting().smeltingList.keySet().iterator(); iter.hasNext();)
		{
			ItemStack rStack = iter.next();
			if(smelting().smeltingList.get(rStack) == output)
			{
				removedRecipes.add(new KilnRecipeWrapper(rStack, output, smelting().giveExperience(rStack)));
				iter.remove();
				smelting().experienceList.remove(rStack);
			}
		}
		return removedRecipes;
	}

	public ItemStack getSmeltingResult(ItemStack stack)
	{
		Iterator iterator = smeltingList.entrySet().iterator();
		Entry entry;

		do
		{
			if (!iterator.hasNext())
			{
				return null;
			}

			entry = (Entry)iterator.next();
		} while (!canBeSmelted(stack, (ItemStack)entry.getKey()));

		return (ItemStack)entry.getValue();
	}

	private boolean canBeSmelted(ItemStack stack, ItemStack stack2)
	{
		return stack2.getItem() == stack.getItem()
				&& (stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == stack
				.getItemDamage());
	}

	public float giveExperience(ItemStack stack)
	{
		Iterator iterator = experienceList.entrySet().iterator();
		Entry entry;

		do
		{
			if (!iterator.hasNext())
			{
				return 0.0f;
			}

			entry = (Entry)iterator.next();
		} while (!canBeSmelted(stack, (ItemStack)entry.getKey()));

		if (stack.getItem().getSmeltingExperience(stack) != -1)
		{
			return stack.getItem().getSmeltingExperience(stack);
		}

		return (Float)entry.getValue();
	}

	public static Map getSmeltingList()
	{
		return smelting().smeltingList;
	}
	
	public static Map getXPList()
	{
		return smelting().experienceList;
	}
}
