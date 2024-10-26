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

package me.shedaniel.rei.api.common.display;

import com.mojang.serialization.Codec;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.impl.display.DisplaySpec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A display, holds ingredients and information for {@link me.shedaniel.rei.api.client.registry.display.DisplayCategory}
 * to set-up widgets for.
 *
 * @see me.shedaniel.rei.api.common.display.basic.BasicDisplay
 * @see me.shedaniel.rei.api.client.registry.display.DisplayRegistry
 */
public interface Display extends DisplaySpec {
    static Codec<Display> codec() {
        return DisplaySerializerRegistry.getInstance().codec();
    }
    
    static StreamCodec<RegistryFriendlyByteBuf, Display> streamCodec() {
        return DisplaySerializerRegistry.getInstance().streamCodec();
    }
    
    /**
     * Returns the list of inputs for this display. This only affects the stacks resolving for the display,
     * and not necessarily the stacks that are displayed.
     *
     * @return a list of inputs
     */
    List<EntryIngredient> getInputEntries();
    
    /**
     * Returns the list of inputs for this display, aligned for the menu. This only affects the stacks resolving for the display,
     * and not necessarily the stacks that are displayed.
     * <p>
     * Each ingredient is also provided with the corresponding index slot-wise. The order of the list does not matter.
     *
     * @return a list of inputs
     */
    default List<InputIngredient<EntryStack<?>>> getInputIngredients(@Nullable AbstractContainerMenu menu, @Nullable Player player) {
        return CollectionUtils.mapIndexed(getInputEntries(), InputIngredient::of);
    }
    
    /**
     * Returns the list of outputs for this display. This only affects the stacks resolving for the display,
     * and not necessarily the stacks that are displayed.
     *
     * @return a list of outputs
     */
    List<EntryIngredient> getOutputEntries();
    
    /**
     * Returns the list of required inputs for this display. This only affects the craftable filter.
     *
     * @return a list of required inputs
     */
    default List<EntryIngredient> getRequiredEntries() {
        return getInputEntries();
    }
    
    /**
     * Returns the identifier of the category this display belongs to.
     *
     * @return the identifier of the category
     */
    CategoryIdentifier<?> getCategoryIdentifier();
    
    /**
     * Returns the display location from data packs.
     *
     * @return the display location
     */
    Optional<ResourceLocation> getDisplayLocation();
    
    /**
     * Returns the serializer for this display.
     * <p>
     * Returning {@code null} is allowed but not preferred, only if this display is never going to be synced from the server.
     * <p>
     * Any serializer returned here must be registered with {@link DisplaySerializerRegistry}.
     * Plugins that would like to save / read recipes should use the generic {@link #codec()} and {@link #streamCodec()} instead.
     *
     * @return the serializer for this display
     */
    @Nullable
    DisplaySerializer<? extends Display> getSerializer();
    
    @Override
    @ApiStatus.NonExtendable
    default Display provideInternalDisplay() {
        return this;
    }
    
    @Override
    @ApiStatus.NonExtendable
    default Collection<ResourceLocation> provideInternalDisplayIds() {
        Optional<ResourceLocation> location = getDisplayLocation();
        if (location.isPresent()) {
            return Collections.singletonList(location.get());
        } else {
            return Collections.emptyList();
        }
    }
}
