package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by einsteinsci on 11/18/2014.
 */
public class OreRecipeElement
{
	public int stackSize;
	ItemStack stack;
	ItemStack[] validItems;
	String oreDictionaryEntry;

	private ItemStack[] EMPTY_ISTACK_ARRAY = new ItemStack[0];

	public OreRecipeElement(ItemStack stack)
	{
		this.stack = stack;
		oreDictionaryEntry = "";
		stackSize = stack.stackSize;
	}

	public OreRecipeElement(ItemStack[] stacks, int stackSize)
	{
		this.stack = stacks[0];
		oreDictionaryEntry = "";
		this.stackSize = stackSize;
		this.validItems = stacks;
	}

	public OreRecipeElement(String dictionaryEntry, int size)
	{
		oreDictionaryEntry = dictionaryEntry;
		stackSize = size;
	}

	public OreRecipeElement(ItemStack valid, String entry, int size)
	{
		stack = valid;
		oreDictionaryEntry = entry;
		stackSize = size;
	}

	public boolean matches(ItemStack stackGiven)
	{
		if(oreDictionaryEntry.equals(""))
		{
			if (stack.getItem() == stackGiven.getItem() && (stack.getItemDamage() == stackGiven.getItemDamage() ||
					stack.getItemDamage() == OreDictionary.WILDCARD_VALUE))
			{
				return true;
			}
		}
		for(ItemStack validItem : OreDictionary.getOres(oreDictionaryEntry))
		{
			if (validItem.getItem() == stackGiven.getItem() && (validItem.getItemDamage() == stackGiven.getItemDamage() ||
					validItem.getItemDamage() == OreDictionary.WILDCARD_VALUE))
			{
				return true;
			}
		}
		for (ItemStack valid : OreDictionary.getOres(oreDictionaryEntry))
		{
			if (valid.getItem() == stackGiven.getItem() && (valid.getItemDamage() == stackGiven.getItemDamage() ||
					valid.getItemDamage() == OreDictionary.WILDCARD_VALUE))
			{
				return true;
			}
		}

		return false;
	}

	public boolean matchesCheckSize(ItemStack stackGiven)
	{
		if(oreDictionaryEntry.equals(""))
		{
			if (stack.getItem() == stackGiven.getItem() && (stack.getItemDamage() == stackGiven.getItemDamage() ||
					stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) && stackSize <= stackGiven.stackSize)
			{
				return true;
			}
		}
		for(ItemStack validItem : OreDictionary.getOres(oreDictionaryEntry))
		{
			if (validItem.getItem() == stackGiven.getItem() && (validItem.getItemDamage() == stackGiven.getItemDamage() ||
					validItem.getItemDamage() == OreDictionary.WILDCARD_VALUE))
			{
				return true;
			}
		}
		for (ItemStack valid : OreDictionary.getOres(oreDictionaryEntry))
		{
			if (valid.getItem() == stackGiven.getItem() && (valid.getItemDamage() == stackGiven.getItemDamage() ||
					valid.getItemDamage() == OreDictionary.WILDCARD_VALUE) && stackSize <= stackGiven.stackSize)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean areOreRecipeElementsEqual(OreRecipeElement input1, OreRecipeElement input2)
	{
		if(input1 != null && input2 != null)
		{
			if(input1.stack != null && input2.stack != null)
			{
				if (input1.stack.getItem() == input2.stack.getItem() && (input1.stack.getItemDamage() == input2.stack.getItemDamage() 
						|| input1.stack.getItemDamage() == OreDictionary.WILDCARD_VALUE || input2.stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) && input1.oreDictionaryEntry.equals(input2.oreDictionaryEntry))
				{
					return true;
				}
			}
			else if(input1.oreDictionaryEntry.equals(input2.oreDictionaryEntry))
			{
				return true;
			}	
		}
		return false;
	}

	public ItemStack[] getValidItems()
	{
		return getValidItems(1);
	}
	
	//Used for catalysts, so that stacksizes are correct
	public ItemStack[] getValidItems(int stackSize)
	{
		List<ItemStack> buf = new ArrayList<ItemStack>();
		if (validItems != null)
		{
			Collections.addAll(buf, validItems);
		}
		if(stack != null)
		{
			buf.add(stack);
		}
		if(!oreDictionaryEntry.equals(""))
		{
			buf.addAll(OreDictionary.getOres(oreDictionaryEntry));
		}
		for(ItemStack validItem : buf)
		{
			validItem.stackSize = stackSize;
		}
		return buf.toArray(EMPTY_ISTACK_ARRAY);
	}

	public String getOreDictionaryEntry()
	{
		return oreDictionaryEntry;
	}

	public OreRecipeElement copy()
	{
		return new OreRecipeElement(stack, oreDictionaryEntry, stackSize);
	}

	public ItemStack getFirst()
	{
		ItemStack zero = (!oreDictionaryEntry.isEmpty() && oreDictionaryEntry != null) ? OreDictionary.getOres(oreDictionaryEntry).get(0) : stack;
		return new ItemStack(zero.getItem(), stackSize, zero.getItemDamage());
	}
}
