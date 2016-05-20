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
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedCraftingHandler;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.IBrickOvenRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterbeginnings.AdvancedCrafting")
public class AdvancedCraftingTweaker 
{
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[][] inputs, IIngredient[] catalysts)
	{
		MineTweakerAPI.apply(new AddAdvancedCraftingRecipe(output, inputs, catalysts, false));
	}
	
	@ZenMethod
	public static void addHiddenRecipe(IItemStack output, IIngredient[][] inputs, IIngredient[] catalysts)
	{
		MineTweakerAPI.apply(new AddAdvancedCraftingRecipe(output, inputs, catalysts, true));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output, IIngredient[][] inputs, IIngredient[] catalysts)
	{
		MineTweakerAPI.apply(new RemoveAdvancedCraftingRecipe(output, inputs, catalysts));
	}

	@ZenMethod
	public static void removeOutput(IItemStack output)
	{
		MineTweakerAPI.apply(new RemoveAdvancedCraftingOutput(output));
	}
	
	private static OreRecipeElement[][] convertIngredients(IIngredient[][] inputs)
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
	
	private static OreRecipeElement[] convertCatalysts(IIngredient[] catalysts)
	{
		OreRecipeElement[] convertedCatalysts = new OreRecipeElement[catalysts.length];
		for(int i = 0; i < catalysts.length; i++)
		{
			IIngredient ingredient = catalysts[i];
			if(ingredient instanceof IOreDictEntry)
			{
				convertedCatalysts[i] = new OreRecipeElement(((IOreDictEntry) ingredient).getName(), 1);
			}
			else if(ingredient instanceof IItemStack)
			{
				convertedCatalysts[i] = new OreRecipeElement(MineTweakerMC.getItemStack(ingredient));
			}
		}
		return convertedCatalysts;
	}
	
	private static String describeAction(ItemStack output, OreRecipeElement[][] ingredients, OreRecipeElement[] catalysts, boolean add)
	{
		StringBuilder sb = new StringBuilder(add ? "Adding recipe [" : "Removing recipe [");
		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 3; col++)
			{
				OreRecipeElement ore = ingredients[row][col];
				if(ore != null)
				{
					if(ore.getOreDictionaryEntry().equals(""))
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
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("], [");
		for(int i = 0; i < catalysts.length - 1; i++)
		{
			OreRecipeElement ore = catalysts[i];
			if(ore != null)
			{
				if(ore.getOreDictionaryEntry().equals(""))
				{
					sb.append(ore.getOreDictionaryEntry() + ", ");
				}
				else
				{
					sb.append(ore.getFirst().getDisplayName() + ", ");
				}
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("] -> " + output.getDisplayName() + " x" + output.stackSize + (add ? " to Advanced Crafting Table" : "from Advanced Crafting Table"));
		return sb.toString();
	}
	
	private static AdvancedRecipe constructRecipe(OreRecipeElement[][] ingredients, ItemStack output, OreRecipeElement[] catalysts, boolean hide)
	{
		return new AdvancedRecipe(MineTweakerUtil.computeRecipeWidth(ingredients), MineTweakerUtil.computeRecipeHeight(ingredients), MineTweakerUtil.convert2dArrayTo1dArray(ingredients, false), output, catalysts, hide);
	}

	private static class AddAdvancedCraftingRecipe implements IUndoableAction
	{
		private ItemStack output;
		private OreRecipeElement[][] ingredients;
		private OreRecipeElement[] catalysts;
		boolean hide;
		
		private AdvancedRecipe recipe;

		public AddAdvancedCraftingRecipe(IItemStack output, IIngredient[][] inputs, IIngredient[] catalysts, boolean hide) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.ingredients = AdvancedCraftingTweaker.convertIngredients(inputs);
			this.catalysts = AdvancedCraftingTweaker.convertCatalysts(catalysts);
			this.hide = hide;
		}

		@Override
		public void apply() 
		{
			recipe = AdvancedCraftingTweaker.constructRecipe(ingredients, output, catalysts, hide);
			AdvancedCraftingHandler.getRecipeList().add(recipe);
		}

		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			AdvancedCraftingHandler.getRecipeList().remove(recipe);
		}

		@Override
		public String describe() 
		{
			return AdvancedCraftingTweaker.describeAction(output, ingredients, catalysts, true);
		}
		@Override
		public String describeUndo() 
		{
			return AdvancedCraftingTweaker.describeAction(output, ingredients, catalysts, false);
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveAdvancedCraftingRecipe implements IUndoableAction
	{
		private ItemStack output;
		private OreRecipeElement[][] ingredients;
		private OreRecipeElement[] catalysts;
		
		private AdvancedRecipe recipe;

		public RemoveAdvancedCraftingRecipe(IItemStack output, IIngredient[][] inputs, IIngredient[] catalysts) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.ingredients = AdvancedCraftingTweaker.convertIngredients(inputs);
			this.catalysts = AdvancedCraftingTweaker.convertCatalysts(catalysts);
		}

		@Override
		public void apply() 
		{
			OreRecipeElement[] inputs = MineTweakerUtil.convert2dArrayTo1dArray(ingredients, true);
			recipes: 
				for(Iterator<AdvancedRecipe> iter = AdvancedCraftingHandler.getRecipeList().iterator(); iter.hasNext();)
				{
					AdvancedRecipe recipe = iter.next();
					if(!(recipe instanceof AdvancedRecipe))
					{
						continue;
					}
					if(!ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output))
					{
						continue;
					}
					for (int o = 0; o < inputs.length; o++)
					{
						if(!OreRecipeElement.areOreRecipeElementsEqual(inputs[o], recipe.getRecipeItems()[o]))
						{
							System.out.println(recipe.getNeededMaterials().length + "," + catalysts.length);
							for (int m = 0; m < catalysts.length; m++)
							{
								if(!OreRecipeElement.areOreRecipeElementsEqual(catalysts[m], recipe.getNeededMaterials()[m]))
								{
									break recipes;
								}
							}
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
			recipe = AdvancedCraftingTweaker.constructRecipe(ingredients, output, catalysts, false);
			AdvancedCraftingHandler.getRecipeList().add(recipe);
		}

		@Override
		public String describe() 
		{
			return AdvancedCraftingTweaker.describeAction(output, ingredients, catalysts, false);
		}
		@Override
		public String describeUndo() 
		{
			return AdvancedCraftingTweaker.describeAction(output, ingredients, catalysts, true);
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveAdvancedCraftingOutput implements IUndoableAction
	{
		private ItemStack output;
		private List<AdvancedRecipe> removedRecipes;

		public RemoveAdvancedCraftingOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			removedRecipes = new ArrayList<AdvancedRecipe>();
			for (Iterator<AdvancedRecipe> iter = AdvancedCraftingHandler.getRecipeList().iterator(); iter.hasNext();)
			{
				AdvancedRecipe advancedRecipe = iter.next();
				if(ItemStack.areItemStackTagsEqual(advancedRecipe.getRecipeOutput(), output) && output.isItemEqual(advancedRecipe.getRecipeOutput()))
				{
					removedRecipes.add(advancedRecipe);
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
			for(AdvancedRecipe r : removedRecipes)
			{
				AdvancedCraftingHandler.getRecipeList().add(r);
			}
		}

		@Override
		public String describe() 
		{
			return "Removing recipes for " +  output.getDisplayName() + " * "  + output.stackSize + " from Advanced Crafting Table";
		}

		@Override
		public String describeUndo() 
		{
			return "Readding recipes for " + output.getDisplayName() + " * "  + output.stackSize + " to Advanced Crafting Table";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
