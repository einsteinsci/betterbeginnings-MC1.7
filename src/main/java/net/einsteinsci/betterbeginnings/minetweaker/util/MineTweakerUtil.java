package net.einsteinsci.betterbeginnings.minetweaker.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;

public class MineTweakerUtil 
{
	public static <T> T[] convert2dArrayTo1dArray(T[][] array, boolean cullNullRows)
	{
		List<T> tempList = new ArrayList<T>();
		int nullRows = 0;
		for(T[] childArray : array)
		{
			boolean rowIsAllNull = true;
			for(T object : childArray)
			{
				if(object != null)
				{
					rowIsAllNull = false;
					nullRows++;
				}
			}
			if(!rowIsAllNull)
			{
				tempList.addAll(Arrays.asList(childArray));
			}
		}
		if(!cullNullRows)
		{
			for(int nr = 0; nr < nullRows; nr++)
			{
				tempList.add(null);
				tempList.add(null);
				tempList.add(null);
			}
		}
		return tempList.toArray(array[0]);
	}

	public static int computeRecipeWidth(Object[][] inputs)
	{
		int width = 0;
		for(Object[] row : inputs)
		{
			for(int slot = 0; slot < 3; slot++)
			{
				if(row[slot] != null && width < 3)
				{
					width++;
				}
			}
			if(width == 3)
			{
				return width;
			}
		}
		return width;
	}

	public static int computeRecipeHeight(Object[][] inputs)
	{
		int height = 0;
		for(Object[] row : inputs)
		{
			boolean isAllNull = true;
			for(Object slot : row)
			{
				if(slot != null)
				{
					isAllNull = false;
				}
			}
			if(!isAllNull)
			{
				height++;
			}
		}
		return height;
	}
}
