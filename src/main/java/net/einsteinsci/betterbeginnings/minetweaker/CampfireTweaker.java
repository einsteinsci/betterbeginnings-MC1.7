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
import net.einsteinsci.betterbeginnings.minetweaker.util.CampfireRecipeWrapper;
import net.einsteinsci.betterbeginnings.register.recipe.CampfirePanRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.CampfireRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterbeginnings.Campfire")
public class CampfireTweaker 
{
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input, float xp)
	{
		MineTweakerAPI.apply(new AddCampfireRecipe(input, output, xp, false));
	}

	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new AddCampfireRecipe(input, output, 0.2f, false));
	}

	@ZenMethod
	public static void addPanRecipe(IItemStack output, IIngredient input, float xp)
	{
		MineTweakerAPI.apply(new AddCampfireRecipe(input, output, xp, true));
	}

	@ZenMethod
	public static void addPanRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new AddCampfireRecipe(input, output, 0.2f, true));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new RemoveCampfireRecipe(input, output, false));
	}

	@ZenMethod
	public static void removePanRecipe(IItemStack output, IIngredient input)
	{
		MineTweakerAPI.apply(new RemoveCampfireRecipe(input, output, true));
	}

	@ZenMethod
	public static void removeOutput(IItemStack output)
	{
		MineTweakerAPI.apply(new RemoveCampfireOutput(output));
	}

	private static class AddCampfireRecipe implements IUndoableAction
	{
		private IIngredient input;
		private ItemStack output;
		private float xp;
		private boolean isPanRecipe;
		private OreRecipeElement inputAsORE;

		public AddCampfireRecipe(IIngredient input, IItemStack output, float xp, boolean isPanRecipe) 
		{
			this.input = input;
			this.output = MineTweakerMC.getItemStack(output);
			this.isPanRecipe = isPanRecipe;
			this.xp = isPanRecipe ? CampfirePanRecipes.smelting().giveExperience(this.output) : CampfireRecipes.smelting().giveExperience(this.output);
		}

		@Override
		public void apply() 
		{
			if(isPanRecipe)
			{
				if(input instanceof IOreDictEntry)
				{
					inputAsORE = new OreRecipeElement(((IOreDictEntry) input).getName(), 1);
					CampfirePanRecipes.getSmeltingList().put(inputAsORE, output);
					CampfirePanRecipes.getXPList().put(output, xp);
				}
				else if(input instanceof IIngredient)
				{
					inputAsORE = new OreRecipeElement(MineTweakerMC.getItemStack(input));
					CampfirePanRecipes.getSmeltingList().put(inputAsORE, output);
					CampfirePanRecipes.getXPList().put(output, xp);
				}
			}
			else
			{
				if(input instanceof IOreDictEntry)
				{
					inputAsORE = new OreRecipeElement(((IOreDictEntry) input).getName(), 1);
					CampfireRecipes.getSmeltingList().put(inputAsORE, output);
					CampfireRecipes.getXPList().put(output, xp);
				}
				else if(input instanceof IIngredient)
				{
					inputAsORE = new OreRecipeElement(MineTweakerMC.getItemStack(input));
					CampfireRecipes.getSmeltingList().put(inputAsORE, output);
					CampfireRecipes.getXPList().put(output, xp);
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
			if(isPanRecipe)
			{
				CampfirePanRecipes.getSmeltingList().remove(inputAsORE);
				CampfirePanRecipes.getXPList().remove(output);
			}
			else
			{
				CampfireRecipes.getSmeltingList().remove(inputAsORE);
				CampfireRecipes.getXPList().remove(output);
			}
		}

		@Override
		public String describe() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Adding recipe " + inputIDString 
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Campfire";
		}

		@Override
		public String describeUndo() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Removing recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Campfire";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class RemoveCampfireRecipe implements IUndoableAction
	{
		private IIngredient input;
		private ItemStack output;
		private float xp;
		private boolean isPanRecipe;
		private OreRecipeElement removedORE;

		public RemoveCampfireRecipe(IIngredient input, IItemStack output, boolean isPanRecipe) 
		{
			this.input = input;
			this.output = MineTweakerMC.getItemStack(output);
			this.isPanRecipe = isPanRecipe;
			this.xp = isPanRecipe ? CampfirePanRecipes.smelting().giveExperience(this.output) : CampfireRecipes.smelting().giveExperience(this.output);
		}

		@Override
		public void apply() 
		{
			ItemStack inputAsStack = MineTweakerMC.getItemStack(input);
			for (Iterator<OreRecipeElement> recipeIter = isPanRecipe ? CampfirePanRecipes.getSmeltingList().keySet().iterator() 
					: CampfireRecipes.getSmeltingList().keySet().iterator(); recipeIter.hasNext();) 
			{
				removedORE = recipeIter.next();
				if(removedORE.matches(inputAsStack))
				{
					recipeIter.remove();
					if(isPanRecipe) 
					{
						CampfirePanRecipes.getXPList().remove(output); 
					} 
					else 
					{ 
						CampfireRecipes.getXPList().remove(output);
					}
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
			if (isPanRecipe)  
			{
				CampfirePanRecipes.getSmeltingList().put(removedORE, output);
				CampfirePanRecipes.getXPList().put(output, xp);
			}
			else
			{
				CampfireRecipes.getSmeltingList().put(removedORE, output);
				CampfireRecipes.getXPList().put(output, xp);
			}
		}

		@Override
		public String describe() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Removing recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " from Campfire";
		}

		@Override
		public String describeUndo() 
		{
			String inputIDString = (input instanceof IOreDictEntry) ? ((IOreDictEntry) input).getName() : MineTweakerMC.getItemStack(input).getDisplayName();
			return "Readding recipe " + inputIDString
					+ " -> " + output.getDisplayName() + " * "  + output.stackSize + " to Campfire";
		}

		@Override
		public Object getOverrideKey() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static class RemoveCampfireOutput implements IUndoableAction
	{
		private ItemStack output;
		private List<CampfireRecipeWrapper> removedRecipes = new ArrayList<CampfireRecipeWrapper>();

		public RemoveCampfireOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			for (Iterator<Entry<OreRecipeElement, ItemStack>> recipeIter = CampfirePanRecipes.getSmeltingList().entrySet().iterator(); recipeIter.hasNext();) 
			{
				this.checkAndRemoveRecipe(recipeIter, true);
			}
			for (Iterator<Entry<OreRecipeElement, ItemStack>> recipeIter = CampfireRecipes.getSmeltingList().entrySet().iterator(); recipeIter.hasNext();) 
			{
				this.checkAndRemoveRecipe(recipeIter, false);
			}
		}

		// If the recipe is the target recipe, it is removed. 
		private void checkAndRemoveRecipe(Iterator<Entry<OreRecipeElement, ItemStack>> recipeIter, boolean isPanRecipe)
		{
			Entry<OreRecipeElement, ItemStack> entry = recipeIter.next();
			if(ItemStack.areItemStacksEqual(entry.getValue(), output))
			{
				removedRecipes.add(new CampfireRecipeWrapper(entry.getKey(), output, CampfireRecipes.smelting().giveExperience(output), isPanRecipe));
				recipeIter.remove();
				if (isPanRecipe) 
				{
					CampfirePanRecipes.getXPList().remove(output);
				} 
				else
				{ 
					CampfireRecipes.getXPList().remove(output);
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
			for(CampfireRecipeWrapper r : removedRecipes)
			{
				r.add();
			}
		}

		@Override
		public String describe() 
		{
			return "Removing recipes for " +  output.getDisplayName() + " * "  + output.stackSize + " from Campfire";
		}

		@Override
		public String describeUndo() 
		{
			return "Readding recipes for " + output.getDisplayName() + " * "  + output.stackSize + " to Campfire";
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

	}
}
