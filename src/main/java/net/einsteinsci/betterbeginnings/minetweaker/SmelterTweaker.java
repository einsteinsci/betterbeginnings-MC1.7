package net.einsteinsci.betterbeginnings.minetweaker;

import java.util.Iterator;
import java.util.Map;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipe;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;

@ZenClass("mods.betterbeginnings.Smelter")
public class SmelterTweaker 
{
	@ZenMethod
	public static void addRecipe(IIngredient input, IItemStack output, float xp, int gravel, int bonus, float bonusChance)
	{
		MineTweakerAPI.apply(new AddSmelterRecipe(input, output, xp, gravel, bonus, bonusChance));
	}

	@ZenMethod
	public static void addRecipe(IIngredient input, IItemStack output)
	{
		MineTweakerAPI.apply(new AddSmelterRecipe(input, output, 0.6f, 1, 1, 0.3f));
	}
	
	@ZenMethod
	public static void removeRecipe(IIngredient input, IItemStack output)
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
			for(IItemStack inputStack : input.getItems())
			{
				SmelterRecipeHandler.addRecipe(MineTweakerMC.getItemStack(inputStack), output, xp, gravel, bonus, bonusChance);
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
			for(IItemStack inputStack : input.getItems())
			{
				SmelterRecipeHandler.removeRecipe(MineTweakerMC.getItemStack(input), output);
			}
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
			for(SmelterRecipe r : SmelterRecipeHandler.getRecipes())
			{
				if(ItemStack.areItemStacksEqual(r.getInput(), MineTweakerMC.getItemStack(input)))
				{
					this.recipe = r;
				}
			}
		}

		@Override
		public void apply() 
		{
			for(IItemStack inputStack : input.getItems())
			{
				SmelterRecipeHandler.removeRecipe(MineTweakerMC.getItemStack(inputStack), output);
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
			for(IItemStack stack : input.getItems())
			{
				ItemStack inputStack = MineTweakerMC.getItemStack(stack);
				SmelterRecipeHandler.addRecipe(inputStack, output, recipe.getExperience()
						, recipe.getGravel(), recipe.getBonus(), recipe.getBonusChance());
			}
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
			ItemStack inputStack = MineTweakerMC.getItemStack(input);
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
		private Map<SmelterRecipe, Float> removedRecipes = Maps.newHashMap();

		public RemoveSmelterOutput(IItemStack output) 
		{
			this.output = MineTweakerMC.getItemStack(output);
		}

		public void apply() 
		{
			removedRecipes = SmelterRecipeHandler.removeOutput(output);
		}

		@Override
		public boolean canUndo() 
		{
			return true;
		}

		@Override
		public void undo() 
		{
			for(Iterator<SmelterRecipe> iter = removedRecipes.keySet().iterator(); iter.hasNext();)
			{
				SmelterRecipe recipe = iter.next();
				SmelterRecipeHandler.addRecipe(recipe, removedRecipes.get(recipe.getInput()));
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
