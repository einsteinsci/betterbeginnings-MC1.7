package net.einsteinsci.betterbeginnings.minetweaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.einsteinsci.betterbeginnings.minetweaker.util.MineTweakerUtil;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapelessRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.IBrickOvenRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterbeginnings.Oven")
public class OvenTweaker 
{
	@ZenMethod
	public static void addShapedRecipe(IItemStack output, IIngredient[][] inputs)
	{
		MineTweakerAPI.apply(new AddShapedOvenRecipe(output, inputs));
	}

	@ZenMethod
	public static void removeShapedRecipe(IItemStack output, IIngredient[][] inputs)
	{
		MineTweakerAPI.apply(new RemoveShapedOvenRecipe(output, inputs));
	}

	@ZenMethod
	public static void addShapelessRecipe(IItemStack output, IIngredient[] inputs)
	{
		MineTweakerAPI.apply(new AddShapelessOvenRecipe(output, inputs));
	}

	@ZenMethod
	public static void removeShapelessRecipe(IItemStack output, IIngredient[] inputs)
	{
		MineTweakerAPI.apply(new RemoveShapelessOvenRecipe(output, inputs));
	}

	@ZenMethod
	public static void removeOutput(IItemStack output)
	{
		MineTweakerAPI.apply(new RemoveOvenOutput(output));
	}

	private static OreRecipeElement[][] convertShapedIngredients(IIngredient[][] inputs)
	{
		OreRecipeElement[][] convertedIngredients = new OreRecipeElement[3][3];
		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 3; col++)
			{
				IIngredient ingredient = inputs[row][col];
				if(ingredient instanceof IOreDictEntry)
				{
					convertedIngredients[row][col] = new OreRecipeElement(((IOreDictEntry) ingredient).getName(), 1);
				}
				else if(ingredient instanceof IItemStack)
				{
					convertedIngredients[row][col] = new OreRecipeElement(MineTweakerMC.getItemStack(ingredient));
				}
			}
		}
		return convertedIngredients;
	}

	private static OreRecipeElement[] convertShapelessIngredients(IIngredient[] inputs)
	{	
		OreRecipeElement[] convertedIngredients = new OreRecipeElement[inputs.length];
		for(int i = 0; i < inputs.length; i++)
		{
			IIngredient ingredient = inputs[i];
			if(ingredient instanceof IOreDictEntry)
			{
				convertedIngredients[i] = new OreRecipeElement(((IOreDictEntry) ingredient).getName(), 1);
			}
			else if(ingredient instanceof IItemStack)
			{
				convertedIngredients[i] = new OreRecipeElement(MineTweakerMC.getItemStack(ingredient));
			}
		}
		return convertedIngredients;
	}

	private static String describeShapedAction(ItemStack output, OreRecipeElement[][] ingredients, boolean add)
	{

		StringBuilder sb = new StringBuilder(add ? "Adding recipe [" : "Removing recipe [");
		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 3; col++)
			{
				OreRecipeElement ore = ingredients[row][col];
				if(ore != null)
				{
					if(!ore.getOreDictionaryEntry().equals(""))
					{
						sb.append(ore.getOreDictionaryEntry() + ", ");
					}
					else
					{
						sb.append(ore.getFirst().getDisplayName() + ", ");
					}
				}
			}
		}
		sb.append("] -> " + output.getDisplayName() + " x" + output.stackSize + (add ? " to Oven" : "from Oven"));
		return sb.toString();
	}

	private static String describeShapelessAction(ItemStack output, OreRecipeElement[] ingredients, boolean add)
	{
		StringBuilder sb = new StringBuilder(add ? "Adding recipe [" : "Removing recipe [");
		for(OreRecipeElement ore : ingredients)
		{
			if(ore != null)
			{
				if(!ore.getOreDictionaryEntry().equals(""))
				{
					sb.append(ore.getOreDictionaryEntry() + ", ");
				}
				else
				{
					sb.append(ore.getFirst().getDisplayName() + ", ");
				}
			}
		}
		sb.append("] -> " + output.getDisplayName() + " x" + output.stackSize + (add ? " to Oven" : "from Oven"));
		return sb.toString();
	}

	private static BrickOvenShapedRecipe constructShapedRecipe(OreRecipeElement[][] ingredients, ItemStack output)
	{
		return new BrickOvenShapedRecipe(MineTweakerUtil.computeRecipeWidth(ingredients), MineTweakerUtil.computeRecipeHeight(ingredients), MineTweakerUtil.convert2dArrayTo1dArray(ingredients, false), output);
	}

	private static BrickOvenShapelessRecipe constructShapelessRecipe(OreRecipeElement[] ingredients, ItemStack output)
	{
		return new BrickOvenShapelessRecipe(output, Arrays.asList(ingredients));
	}

	private static class AddShapedOvenRecipe implements IUndoableAction
	{
		private OreRecipeElement[][] ingredients;
		private ItemStack output;
		private BrickOvenShapedRecipe recipe;

		public AddShapedOvenRecipe(IItemStack output, IIngredient[][] inputs) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.ingredients = OvenTweaker.convertShapedIngredients(inputs);
		}

		@Override
		public void apply() 
		{
			recipe = OvenTweaker.constructShapedRecipe(ingredients, output);
			BrickOvenRecipeHandler.getRecipeList().add(recipe);
		}

		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			BrickOvenRecipeHandler.getRecipeList().remove(recipe);
		}

		@Override
		public String describe() 
		{
			return OvenTweaker.describeShapedAction(output, ingredients, true);
		}
		@Override
		public String describeUndo() 
		{
			return OvenTweaker.describeShapedAction(output, ingredients, false);
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveShapedOvenRecipe implements IUndoableAction
	{
		private OreRecipeElement[][] ingredients;
		private ItemStack output;
		private BrickOvenShapedRecipe recipe;

		public RemoveShapedOvenRecipe(IItemStack output, IIngredient[][] inputs) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.ingredients = OvenTweaker.convertShapedIngredients(inputs);
		}

		@Override
		public void apply() 
		{
			OreRecipeElement[] inputs = MineTweakerUtil.convert2dArrayTo1dArray(ingredients, true);
			recipes: 
				for(Iterator<IBrickOvenRecipe> iter = BrickOvenRecipeHandler.getRecipeList().iterator(); iter.hasNext();)
				{
					IBrickOvenRecipe recipe = iter.next();
					if(!(recipe instanceof BrickOvenShapedRecipe))
					{
						continue;
					}
					if(!ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output))
					{
						continue;
					}
					for (int o = 0; o < inputs.length; o++)
					{
						if(!OreRecipeElement.areOreRecipeElementsEqual(inputs[o], recipe.getInputs()[o]))
						{
							break recipes;
						}
					}
					this.recipe = (BrickOvenShapedRecipe) recipe;
					iter.remove();
				}
		}

		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			BrickOvenRecipeHandler.getRecipeList().add(recipe);
		}

		@Override
		public String describe() 
		{
			return OvenTweaker.describeShapedAction(output, ingredients, false);
		}
		@Override
		public String describeUndo() 
		{
			return OvenTweaker.describeShapedAction(output, ingredients, true);
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class AddShapelessOvenRecipe implements IUndoableAction
	{
		private OreRecipeElement[] ingredients;
		private ItemStack output;
		private BrickOvenShapelessRecipe recipe;

		public AddShapelessOvenRecipe(IItemStack output, IIngredient[] inputs) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.ingredients = convertShapelessIngredients(inputs);
		}

		@Override
		public void apply() 
		{
			recipe = OvenTweaker.constructShapelessRecipe(ingredients, output);
			BrickOvenRecipeHandler.getRecipeList().add(recipe);
		}
		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			BrickOvenRecipeHandler.getRecipeList().remove(recipe);
		}

		@Override
		public String describe() 
		{
			return OvenTweaker.describeShapelessAction(output, ingredients, true);
		}

		@Override
		public String describeUndo() 
		{return OvenTweaker.describeShapelessAction(output, ingredients, false);
		}

		@Override
		public Object getOverrideKey() 
		{
			return null;
		}

	}

	private static class RemoveShapelessOvenRecipe implements IUndoableAction
	{
		private OreRecipeElement[] ingredients;
		private ItemStack output;
		private BrickOvenShapelessRecipe recipe;

		public RemoveShapelessOvenRecipe(IItemStack output, IIngredient[] inputs) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.ingredients = OvenTweaker.convertShapelessIngredients(inputs);
		}

		@Override
		public void apply() 
		{
			for(Iterator<IBrickOvenRecipe> iter = BrickOvenRecipeHandler.getRecipeList().iterator(); iter.hasNext();)
			{
				IBrickOvenRecipe recipe = iter.next();
				if(!(recipe instanceof BrickOvenShapelessRecipe))
				{
					continue;
				}
				if(!ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output) || !(ingredients.length == recipe.getRecipeSize()))
				{
					continue;
				}
				checkIngredients:
					for (int o = 0; o < ingredients.length; o++)
					{
						boolean found = false;
						for (int i = 0; i < recipe.getRecipeSize(); i++)
						{
							if(OreRecipeElement.areOreRecipeElementsEqual(ingredients[o], recipe.getInputs()[i]))
							{
								found = true;
								break checkIngredients;
							}
						}
						if(!found)
						{
							return;
						}
					}
				iter.remove();
			}
		}
		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			recipe = OvenTweaker.constructShapelessRecipe(ingredients, output);
			BrickOvenRecipeHandler.getRecipeList().add(recipe);
		}

		@Override
		public String describe() 
		{
			return OvenTweaker.describeShapelessAction(output, ingredients, false);
		}

		@Override
		public String describeUndo() 
		{
			return OvenTweaker.describeShapelessAction(output, ingredients, true);
		}

		@Override
		public Object getOverrideKey() 
		{
			return null;
		}

	}

	private static class RemoveOvenOutput implements IUndoableAction
	{
		private ItemStack output;
		private List<IBrickOvenRecipe> removedRecipes;

		public RemoveOvenOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			removedRecipes = new ArrayList<IBrickOvenRecipe>();
			for (Iterator<IBrickOvenRecipe> iter = BrickOvenRecipeHandler.getRecipeList().iterator(); iter.hasNext();)
			{
				IBrickOvenRecipe ovenRecipe = iter.next();
				if(ItemStack.areItemStackTagsEqual(ovenRecipe.getRecipeOutput(), output) && output.isItemEqual(ovenRecipe.getRecipeOutput()))
				{
					removedRecipes.add(ovenRecipe);
					iter.remove();
				}
			}
		}

		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			for(IBrickOvenRecipe r : removedRecipes)
			{
				BrickOvenRecipeHandler.getRecipeList().add(r);
			}
		}

		@Override
		public String describe() 
		{
			return "Removing recipes for " +  output.getDisplayName() + " * "  + output.stackSize + " from Smelter";
		}

		@Override
		public String describeUndo() 
		{
			return "Readding recipes for " + output.getDisplayName() + " * "  + output.stackSize + " to Smelter";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
