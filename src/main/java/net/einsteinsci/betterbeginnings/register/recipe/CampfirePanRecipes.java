package net.einsteinsci.betterbeginnings.register.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CampfirePanRecipes
{
	private static final CampfirePanRecipes SMELTINGBASE = new CampfirePanRecipes();

	private Map<OreRecipeElement, ItemStack> smeltingList = new HashMap<OreRecipeElement, ItemStack>();
	private Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();

	private CampfirePanRecipes()
	{
		// nothing here
	}

	public static void addRecipe(Item input, ItemStack output, float experience)
	{
		smelting().addLists(new OreRecipeElement(new ItemStack(input)), output, experience);
	}
	
	public static void addRecipe(String oreDictEntry, ItemStack output, float experience)
	{
		smelting().addLists(new OreRecipeElement(oreDictEntry, 1), output, experience);
	}

	public void addLists(OreRecipeElement input, ItemStack itemStack, float experience)
	{
		putLists(input, itemStack, experience);
	}

	public static CampfirePanRecipes smelting()
	{
		return SMELTINGBASE;
	}

	public void putLists(OreRecipeElement input, ItemStack itemStack2, float experience)
	{
		smeltingList.put(input, itemStack2);
		experienceList.put(itemStack2, Float.valueOf(experience));
	}

	public static void addRecipe(Block input, ItemStack output, float experience)
	{
		smelting().addLists(new OreRecipeElement(new ItemStack(Item.getItemFromBlock(input))), output, experience);
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
		} while (!canBeSmelted(stack, entry.getKey()));

		return (ItemStack)entry.getValue();
	}

	private boolean canBeSmelted(ItemStack stack, OreRecipeElement ore)
	{
		return ore.matches(stack);
	}
	
	private boolean canBeSmelted(ItemStack stack, ItemStack stack2) 
	{
		return stack2.getItem() == stack.getItem()
				&& (stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == stack
				.getItemDamage());
	}

	public float giveExperience(ItemStack stack)
	{
		Iterator<Entry<ItemStack, Float>> iterator = experienceList.entrySet().iterator();
		Entry<ItemStack, Float> entry;

		do
		{
			if (!iterator.hasNext())
			{
				return 0.0f;
			}

			entry = iterator.next();
		} while (!canBeSmelted(stack, entry.getKey()));

		if (stack.getItem().getSmeltingExperience(stack) != -1)
		{
			return stack.getItem().getSmeltingExperience(stack);
		}

		return entry.getValue();
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
