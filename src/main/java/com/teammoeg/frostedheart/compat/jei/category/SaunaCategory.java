/*
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Immersive Industry.
 *
 * Immersive Industry is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Immersive Industry is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Immersive Industry. If not, see <https://www.gnu.org/licenses/>.
 */

package com.teammoeg.frostedheart.compat.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.teammoeg.frostedheart.FHBlocks;
import com.teammoeg.frostedheart.FHMain;
import com.teammoeg.frostedheart.client.util.GuiUtils;
import com.teammoeg.frostedheart.compat.jei.StaticBlock;
import com.teammoeg.frostedheart.content.steamenergy.sauna.SaunaRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaunaCategory implements IRecipeCategory<SaunaRecipe> {
    public static ResourceLocation UID = new ResourceLocation(FHMain.MODID, "sauna");
    private IDrawable BACKGROUND;
    private IDrawable ICON;
    private StaticBlock sauna = new StaticBlock(FHBlocks.sauna.getDefaultState().with(BlockStateProperties.FACING, Direction.EAST));

    public SaunaCategory(IGuiHelper guiHelper) {
        this.ICON = guiHelper.createDrawableIngredient(new ItemStack(FHBlocks.sauna));
        this.BACKGROUND = new EmptyBackground(177, 70);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SaunaRecipe> getRecipeClass() {
        return SaunaRecipe.class;
    }


    public String getTitle() {
        return (new TranslationTextComponent("gui.jei.category." + FHMain.MODID + ".sauna").getString());
    }

    @Override
    public void draw(SaunaRecipe recipe, MatrixStack transform, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SLOT.draw(transform, 43, 4);
        AllGuiTextures.JEI_DOWN_ARROW.draw(transform, 67, 7);
        AllGuiTextures.JEI_SHADOW.draw(transform, 72 - 17, 42 + 13);

//        AllGuiTextures.JEI_DOWN_ARROW.draw(transform, 112, 30);
//        AllGuiTextures.JEI_SLOT.draw(transform, 117, 47);

        sauna.draw(transform, 72, 42);
    }

    @Override
    public IDrawable getBackground() {
        return BACKGROUND;
    }

    @Override
    public IDrawable getIcon() {
        return ICON;
    }

    @Override
    public void setIngredients(SaunaRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(recipe.input.getMatchingStacks())));
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SaunaRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 43, 4);
//        itemStacks.init(1, false, 117, 47);
        itemStacks.set(ingredients);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(SaunaRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> tooltip = new ArrayList<>();
        if (isMouseIn(mouseX, mouseY, 43+18, 4+18, 36, 36)) {
            tooltip.add(recipe.effect.getDisplayName());
        }
        return tooltip;
    }

    public boolean isMouseIn(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseY >= y
                && mouseX < x + w && mouseY < y + h;
    }
}