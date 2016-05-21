package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by einsteinsci on 11/18/2014.
 */
public class OreRecipeElement
{
	public int stackSize;
	ItemStack stack;
	String oreDictionaryEntry;
	
	private ItemStack[] EMPTY_ISTACK_ARRAY = new ItemStack[0];

	public OreRecipeElement(ItemStack stack)
	{
		this.stack = stack;
		oreDictionaryEntry = "";
		stackSize = stack.stackSize;
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
		List<ItemStack> buf = new ArrayList<ItemStack>();
		if(stack != null)
		{
			buf.add(stack);
		}
		if(!oreDictionaryEntry.equals(""))
		{
			buf.addAll(OreDictionary.getOres(oreDictionaryEntry));
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
		ItemStack zero = !oreDictionaryEntry.equals("") ? OreDictionary.getOres(oreDictionaryEntry).get(0) : stack;
		return new ItemStack(zero.getItem(), stackSize, zero.getItemDamage());
	}
}
