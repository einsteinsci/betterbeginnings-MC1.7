package net.einsteinsci.betterbeginnings.register.recipe;

import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Maps;

public class KilnRecipes
{
	private static final KilnRecipes SMELTINGBASE = new KilnRecipes();

	private Map<OreRecipeElement, ItemStack> smeltingList = Maps.newHashMap();
	private Map<ItemStack, Float> experienceList = Maps.newHashMap();

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
		putLists(new OreRecipeElement(new ItemStack(input)), itemStack, experience);
	}

	public static KilnRecipes smelting()
	{
		return SMELTINGBASE;
	}

	public void putLists(OreRecipeElement input, ItemStack itemStack2, float experience)
	{
		smeltingList.put(input, itemStack2);
		experienceList.put(itemStack2, experience);
	}

	public static void addRecipe(String input, ItemStack output, float experience)
	{
		smelting().putLists(new OreRecipeElement(input, 1), output, experience);
	}

	public static void addRecipe(Block input, ItemStack output, float experience)
	{
		smelting().addLists(Item.getItemFromBlock(input), output, experience);
	}

	public static void addRecipe(ItemStack input, ItemStack output, float experience)
	{
		smelting().putLists(new OreRecipeElement(input), output, experience);
	}

	public ItemStack getSmeltingResult(ItemStack stack)
	{
		Iterator<Entry<OreRecipeElement, ItemStack>> iterator = smeltingList.entrySet().iterator();
		Entry<OreRecipeElement, ItemStack> entry;

		do
		{
			if (!iterator.hasNext())
			{
				return null;
			}

			entry = iterator.next();
		} while (!canBeSmelted(stack, entry.getKey().getFirst()));

		return entry.getValue();
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

	public static Map<OreRecipeElement, ItemStack> getSmeltingList()
	{
		return smelting().smeltingList;
	}

	public static Map<ItemStack, Float> getXPList()
	{
		return smelting().experienceList;
	}
}
