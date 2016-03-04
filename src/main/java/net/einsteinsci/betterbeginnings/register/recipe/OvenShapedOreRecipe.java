package net.einsteinsci.betterbeginnings.register.recipe;

import java.util.ArrayList;
import java.util.List;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class OvenShapedOreRecipe implements IBrickOvenRecipe
{
	Object[] inputs;
	ItemStack output;
	int recipeWidth;
	int recipeHeight;

	public OvenShapedOreRecipe(int width, int height, Object[] input, ItemStack output) 
	{
		recipeWidth = width;
		recipeHeight = height;
		this.inputs = input;
		this.output = output;
	}

	@Override
	public boolean matches(TileEntityBrickOven inv) 
	{
		for(int i = 0; i < recipeWidth; i++)
		{
			for(int j = 0; j < recipeHeight; j++)
			{
				if (ingredientMatches(inv, i, j))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean matches(TileEntityNetherBrickOven inv) 
	{
		for(int i = 0; i < recipeWidth; i++)
		{
			for(int j = 0; j < recipeHeight; j++)
			{
				if (ingredientMatches(inv, i, j))
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean ingredientMatches(TileEntityBrickOven inv, int i, int j)
	{
		Object recipeIngredient = inputs[(i - 1) * 3 + j];
		if(recipeIngredient instanceof ItemStack)
		{
			if(ItemStack.areItemStacksEqual((ItemStack) recipeIngredient, inv.getStackInRowAndColumn(i, j)))
			{
				return true;
			}
		}
		else if (recipeIngredient instanceof OreRecipeElement)
		{
			if(((OreRecipeElement) recipeIngredient).matches(inv.getStackInRowAndColumn(i, j)))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean ingredientMatches(TileEntityNetherBrickOven inv, int i, int j)
	{
		Object recipeIngredient = inputs[i * j];
		if(recipeIngredient instanceof ItemStack)
		{
			if(ItemStack.areItemStacksEqual((ItemStack) recipeIngredient, inv.getStackInRowAndColumn(i, j)))
			{
				return true;
			}
		}
		else if (recipeIngredient instanceof OreRecipeElement)
		{
			if(((OreRecipeElement) recipeIngredient).matches(inv.getStackInRowAndColumn(i, j)))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(TileEntityBrickOven inv) 
	{
		return output;
	}

	@Override
	public ItemStack getCraftingResult(TileEntityNetherBrickOven inv) {
		// TODO Auto-generated method stub
		return output;
	}

	@Override
	public int getRecipeSize() {
		// TODO Auto-generated method stub
		return recipeWidth * recipeHeight;
	}

	@Override
	public boolean contains(ItemStack stack) 
	{
		for (Object obj : this.inputs)
		{
			if(obj instanceof ItemStack && ItemStack.areItemStacksEqual(stack, (ItemStack) obj))
			{
				return true;
			}
			else if (obj instanceof OreRecipeElement && ((OreRecipeElement) obj).getValidItems().contains(stack))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return output;
	}

	@Override
	public ItemStack[] getInputs() 
	{
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		for(Object obj : this.inputs)
		{
			if(obj instanceof ItemStack)
			{
				inputs.add((ItemStack) obj);
			}
			else if (obj instanceof OreRecipeElement)
			{
				inputs.add(((OreRecipeElement) obj).getFirst());
			}
		}
		return (ItemStack[]) inputs.toArray();
	}

}
