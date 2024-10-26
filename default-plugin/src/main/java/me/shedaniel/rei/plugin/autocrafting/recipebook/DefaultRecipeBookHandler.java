/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022, 2023 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.shedaniel.rei.plugin.autocrafting.recipebook;

import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.plugin.client.displays.ClientsidedCraftingDisplay;
import me.shedaniel.rei.plugin.client.displays.ClientsidedRecipeBookDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

@Environment(EnvType.CLIENT)
public class DefaultRecipeBookHandler implements TransferHandler {
    @Override
    public ApplicabilityResult checkApplicable(Context context) {
        if (context.getDisplay() instanceof SimpleGridMenuDisplay && ClientHelper.getInstance().canUseMovePackets())
            return ApplicabilityResult.createNotApplicable();
        Display display = context.getDisplay();
        if (!(context.getMenu() instanceof RecipeBookMenu container))
            return ApplicabilityResult.createNotApplicable();
        if (container == null)
            return ApplicabilityResult.createNotApplicable();
        return ApplicabilityResult.createApplicable();
    }
    
    @Override
    public Result handle(Context context) {
        RecipeBookMenu container = (RecipeBookMenu) context.getMenu();
        Display display = context.getDisplay();
        if (display instanceof ClientsidedCraftingDisplay craftingDisplay) {
            if (craftingDisplay.recipeDisplayId().isPresent()) {
                int h = -1, w = -1;
                if (container instanceof CraftingMenu) {
                    h = 3;
                    w = 3;
                } else if (container instanceof InventoryMenu) {
                    h = 2;
                    w = 2;
                }
                if (h == -1 || w == -1)
                    return Result.createNotApplicable();
                RecipeDisplayId id = craftingDisplay.recipeDisplayId().get();
                if (craftingDisplay.getHeight() > h || craftingDisplay.getWidth() > w)
                    return Result.createFailed(Component.translatable("error.rei.transfer.too_small", h, w));
                if (!context.isActuallyCrafting())
                    return Result.createSuccessful();
                context.getMinecraft().setScreen(context.getContainerScreen());
                if (context.getContainerScreen() instanceof AbstractRecipeBookScreen<?> screen)
                    screen.recipeBookComponent.ghostSlots.clear();
                context.getMinecraft().gameMode.handlePlaceRecipe(container.containerId, id, context.isStackedCrafting());
                return Result.createSuccessful();
            }
        } else if (display instanceof ClientsidedRecipeBookDisplay defaultDisplay) {
            if (defaultDisplay.recipeDisplayId().isPresent()) {
                RecipeDisplayId id = defaultDisplay.recipeDisplayId().get();
                if (!context.isActuallyCrafting())
                    return Result.createSuccessful();
                context.getMinecraft().setScreen(context.getContainerScreen());
                if (context.getContainerScreen() instanceof AbstractRecipeBookScreen<?> screen)
                    screen.recipeBookComponent.ghostSlots.clear();
                context.getMinecraft().gameMode.handlePlaceRecipe(container.containerId, id, context.isStackedCrafting());
                return Result.createSuccessful();
            }
        }
        return Result.createNotApplicable();
    }
    
    @Override
    public double getPriority() {
        return -20;
    }
}
