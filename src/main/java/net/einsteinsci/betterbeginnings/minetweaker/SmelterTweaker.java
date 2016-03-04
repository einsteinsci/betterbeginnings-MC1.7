package net.einsteinsci.betterbeginnings.minetweaker;

import java.util.ArrayList;
import java.util.Iterator;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterbeginnings.Smelter")
public class SmelterTweaker 
{
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input, float xp, int gravel, int bonus, float bonusChance)
	{
		MineTweakerAPI.apply(new AddSmelterRecipe(input, output, xp, gravel, bonus, bonusChance));
	}

	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new AddSmelterRecipe(input, output, 0.6f, 1, 1, 0.3f));
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new RemoveSmelterRecipe(input, output));
	}

	@ZenMethod
	public static void removeOutput(IItemStack output)
	{
		MineTweakerAPI.apply(new RemoveSmelterOutput(output));
	}

	private static class AddSmelterRecipe implements IUndoableAction
	{
		private IIngredient input;
		private ItemStack output;
		private float xp, bonusChance;
		private int gravel, bonus;
		private SmelterRecipe recipe;

		public AddSmelterRecipe(IIngredient input, IItemStack output, float xp, int gravel, int bonus, float bonusChance) 
		{
			this.input = input;
			this.output = MineTweakerMC.getItemStack(output);
			this.xp = xp;
			this.gravel = gravel;
			this.bonus = bonus;
			this.bonusChance = bonusChance;
		}

		@Override
		public void apply() 
		{
			if(input instanceof IOreDictEntry)
			{
				recipe = new SmelterRecipe(output, new OreRecipeElement(((IOreDictEntry) input).getName(), 1), xp, gravel, bonus, bonusChance);
				SmelterRecipeHandler.getRecipes().add(recipe);
			}
			else if(input instanceof IIngredient)
			{
				recipe = new SmelterRecipe(output, new OreRecipeElement(MineTweakerMC.getItemStack(input)), xp, gravel, bonus, bonusChance);
				SmelterRecipeHandler.getRecipes().add(recipe);
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
			SmelterRecipeHandler.getRecipes().remove(recipe);
		}

		@Override
		public String describe() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Adding recipe " + inputIDString 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Smelter";
		}

		@Override
		public String describeUndo() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Removing recipe " + inputIDString 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Smelter";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveSmelterRecipe implements IUndoableAction
	{
		private IIngredient input;
		private ItemStack output;
		private SmelterRecipe recipe;

		public RemoveSmelterRecipe(IIngredient input, IItemStack output) 
		{
			this.input = input;
			this.output = MineTweakerMC.getItemStack(output);

			if(input instanceof IOreDictEntry)
			{
				for(SmelterRecipe r : SmelterRecipeHandler.getRecipes())
				{
					if(ItemStack.areItemStacksEqual(r.getOutput(), this.output))
					{
						this.recipe = r;
					}
				}
			}
			else if(input instanceof IIngredient)
			{
				for(SmelterRecipe r : SmelterRecipeHandler.getRecipes())
				{
					if(r.getInput().matches(MineTweakerMC.getItemStack(input)) && ItemStack.areItemStacksEqual(r.getOutput(), this.output));
					{
						this.recipe = r;
					}
				}
			}	
			
		}

		@Override
		public void apply() 
		{
			System.out.println(recipe);
			SmelterRecipeHandler.getRecipes().remove(recipe);
		}

		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			SmelterRecipeHandler.getRecipes().add(recipe);
		}

		@Override
		public String describe() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Removing recipe " + inputIDString 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Smelter";
		}

		@Override
		public String describeUndo() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Adding recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Smelter";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveSmelterOutput implements IUndoableAction
	{
		private ItemStack output;
		private ArrayList<SmelterRecipe> removedRecipes;

		public RemoveSmelterOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			removedRecipes = new ArrayList<SmelterRecipe>();;
			for (Iterator<SmelterRecipe> recipeIter = SmelterRecipeHandler.getRecipes().iterator(); recipeIter.hasNext();)
			{
				SmelterRecipe recipe = recipeIter.next();
				if(ItemStack.areItemStacksEqual(recipe.getOutput(), output))
				{
					removedRecipes.add(recipe);
					recipeIter.remove();
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
			for(SmelterRecipe recipe : removedRecipes)
			{
				SmelterRecipeHandler.getRecipes().add(recipe);
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
