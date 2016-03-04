package net.einsteinsci.betterbeginnings.minetweaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.einsteinsci.betterbeginnings.minetweaker.util.MineTweakerUtil;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapedRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenShapelessRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.IBrickOvenRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterbeginnings.Oven")
public class OvenTweaker 
{
	@ZenMethod
	public static void addShapedRecipe(IItemStack output, IItemStack[][] inputs)
	{
		MineTweakerAPI.apply(new AddOvenRecipe(output, inputs, true));
	}
	
	@ZenMethod
	public static void addShapelessRecipe(IItemStack output, IItemStack[][] inputs)
	{
		MineTweakerAPI.apply(new AddOvenRecipe(output, inputs, false));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output, IItemStack[][] inputs)
	{
		MineTweakerAPI.apply(new RemoveOvenRecipe(output, inputs));
	}

	@ZenMethod
	public static void removeOutput(IItemStack output)
	{
		MineTweakerAPI.apply(new RemoveOvenOutput(output));
	}

	private static class AddOvenRecipe implements IUndoableAction
	{
		private IItemStack[][] inputs;
		private ItemStack output;
		private boolean shaped;
		
		public AddOvenRecipe(IItemStack output, IItemStack[][] inputs, boolean shaped) 
		{
			this.inputs = inputs;
			this.output = MineTweakerMC.getItemStack(output);
			this.shaped = shaped;
		}

		@Override
		public void apply() 
		{
			if (shaped)
			{
		
			}
			else 
			{
				
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
			
		}

		@Override
		public String describe() 
		{
			return "";
		}

		@Override
		public String describeUndo() 
		{
			return "";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveOvenRecipe implements IUndoableAction
	{
		private IIngredient[] inputs;
		private ItemStack output;

		public RemoveOvenRecipe(IItemStack output, IItemStack[][] inputs) 
		{
			this.output = MineTweakerMC.getItemStack(output);
			this.inputs = (IIngredient[]) MineTweakerUtil.convert2dArrayTo1dArray(inputs);
		}

		@Override
		public void apply() 
		{
			
		}

		@Override
		public boolean canUndo() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void undo() 
		{
			
		}

		@Override
		public String describe() 
		{
			return "Removing recipe " + "" 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Oven";
		}

		@Override
		public String describeUndo() 
		{
			return "Readding recipe " + "" 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Oven";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveOvenOutput implements IUndoableAction
	{
		private ItemStack output;
		private List<IBrickOvenRecipe> removedRecipes = new ArrayList<IBrickOvenRecipe>();

		public RemoveOvenOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			removedRecipes = BrickOvenRecipeHandler.removeOutput(output);
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
				BrickOvenRecipeHandler.instance().getRecipeList().add(r);
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
