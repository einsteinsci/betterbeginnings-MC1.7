package net.einsteinsci.betterbeginnings.minetweaker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.item.ItemStack;

public class MineTweakerUtil 
{
	public static <T> T[] convert2dArrayTo1dArray(T[][] array)
	{
		List<T> tempList = new ArrayList<T>();
		for(T[] childArray : array)
		{
			for(T object : childArray)
			{
				tempList.add(object);
			}
		}
		return tempList.toArray(array[0]);
	}

	public static Object[] formatShapedRecipeInputs(Object[][] inputs)
	{
		Character[] inputSymbols = new Character[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
		Map<Object, Character> uniqueInputs = new HashMap<Object, Character>();
		for(int xa = 0; xa < 3; xa++)
		{
			for(int ya = 0; ya < 3; ya++)
			{
				if(!uniqueInputs.containsKey(inputs[xa][ya]))
				{
					uniqueInputs.put(inputs[xa][ya], inputSymbols[uniqueInputs.size()]);
				}
			}
		}
		Character[][] craftingGrid = new Character[3][3];
		for(int xb = 0; xb < inputs.length; xb++)
		{
			for(int yb = 0; yb < inputs[xb].length; yb++)
			{
				craftingGrid[xb][yb] = uniqueInputs.get(inputs[xb][yb]);
			}
		}
		List<Object> formattedInputs = new ArrayList<Object>();
		for(Character[] charArray : craftingGrid)
		{
			
			formattedInputs.add(new String(ArrayUtils.toPrimitive(charArray)));
		}
		for(Iterator<Object> iter = uniqueInputs.keySet().iterator(); iter.hasNext();)
		{
			Object obj = iter.next();
			formattedInputs.add(uniqueInputs.get(obj));
			formattedInputs.add(obj);
		}
		return formattedInputs.toArray();
	}
}
