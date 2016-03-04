package net.einsteinsci.betterbeginnings.minetweaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.einsteinsci.betterbeginnings.minetweaker.util.KilnRecipeWrapper;
import net.einsteinsci.betterbeginnings.register.recipe.CampfireRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterbeginnings.Kiln")
public class KilnTweaker 
{
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input, float xp)
	{
		MineTweakerAPI.apply(new AddKilnRecipe(input, output, xp));
	}
	
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new AddKilnRecipe(input, output, 0.2f));
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new RemoveKilnRecipe(input, output));
	}
	
	@ZenMethod
	public static void removeOutput(IItemStack output)
	{
		MineTweakerAPI.apply(new RemoveKilnOutput(output));
	}
	
	private static class AddKilnRecipe implements IUndoableAction
	{
		private IIngredient input;
		private ItemStack output;
		private float xp;
		private OreRecipeElement inputAsORE;
		
		public AddKilnRecipe(IIngredient input, IItemStack output, float xp) 
		{
			this.input = input;
			this.output = MineTweakerMC.getItemStack(output);
			this.xp = xp;
		}

		@Override
		public void apply() 
		{
			if(input instanceof IOreDictEntry)
			{
				inputAsORE = new OreRecipeElement(((IOreDictEntry) input).getName(), 1);
				KilnRecipes.getSmeltingList().put(inputAsORE, output);
				KilnRecipes.getXPList().put(output, xp);
			}
			else
			{
				inputAsORE = new OreRecipeElement(MineTweakerMC.getItemStack(input));
				KilnRecipes.getSmeltingList().put(inputAsORE, output);
				KilnRecipes.getXPList().put(output, xp);
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
			KilnRecipes.getSmeltingList().remove(inputAsORE);
			KilnRecipes.getXPList().remove(output);
		}

		@Override
		public String describe() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Adding recipe " + inputIDString 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Kiln";
		}

		@Override
		public String describeUndo() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Removing recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Kiln";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class RemoveKilnRecipe implements IUndoableAction
	{
		private IIngredient input;
		private ItemStack output;
		private float xp;
		private OreRecipeElement inputAsORE;

		public RemoveKilnRecipe(IIngredient input, IItemStack output) 
		{
			this.input = input;
			this.output = MineTweakerMC.getItemStack(output);
			this.xp = KilnRecipes.smelting().giveExperience(MineTweakerMC.getItemStack(input));
		}
		
		@Override
		public void apply() 
		{
			ItemStack inputAsStack = MineTweakerMC.getItemStack(input);
			for (Iterator<OreRecipeElement> recipeIter = KilnRecipes.getSmeltingList().keySet().iterator(); recipeIter.hasNext();) 
			{
				if(recipeIter.next().matches(inputAsStack))
				{
					recipeIter.remove();
					KilnRecipes.getXPList().remove(output);
				}
			}
		}

		@Override
		public boolean canUndo() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void undo() 
		{
			if(input instanceof IOreDictEntry)
			{
				inputAsORE = new OreRecipeElement(((IOreDictEntry) input).getName(), 1);
				KilnRecipes.getSmeltingList().put(inputAsORE, output);
				KilnRecipes.getXPList().put(output, xp);
			}
			else
			{
				inputAsORE = new OreRecipeElement(MineTweakerMC.getItemStack(input));
				KilnRecipes.getSmeltingList().put(inputAsORE, output);
				KilnRecipes.getXPList().put(output, xp);
			}
		}

		@Override
		public String describe() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Removing recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Kiln";
		}

		@Override
		public String describeUndo() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Readding recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Kiln";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class RemoveKilnOutput implements IUndoableAction
	{
		private ItemStack output;
		private List<KilnRecipeWrapper> removedRecipes = new ArrayList<KilnRecipeWrapper>();

		public RemoveKilnOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			removedRecipes = new ArrayList<KilnRecipeWrapper>();
			for (Iterator<Entry<OreRecipeElement, ItemStack>> recipeIter = KilnRecipes.getSmeltingList().entrySet().iterator(); recipeIter.hasNext();)
			{
				Entry<OreRecipeElement, ItemStack> entry = recipeIter.next();
				if(ItemStack.areItemStacksEqual(entry.getValue(), output))
				{
					removedRecipes.add(new KilnRecipeWrapper(entry.getKey(), output, CampfireRecipes.smelting().giveExperience(output)));
					recipeIter.remove();
					KilnRecipes.getXPList().remove(output);
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
			for(KilnRecipeWrapper r : removedRecipes)
			{
				r.add();
			}
		}

		@Override
		public String describe() 
		{
			return "Removing recipes for " +  output.getDisplayName() + " * "  + output.stackSize + " from Kiln";
		}

		@Override
		public String describeUndo() 
		{
			return "Readding recipes for " + output.getDisplayName() + " * "  + output.stackSize + " to Kiln";
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

	}
}
