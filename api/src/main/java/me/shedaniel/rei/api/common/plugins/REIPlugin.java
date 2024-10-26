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

package me.shedaniel.rei.api.common.plugins;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.comparison.FluidComparatorRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.entry.settings.EntrySettingsAdapterRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.fluid.FluidSupportProvider;
import me.shedaniel.rei.api.common.registry.ReloadStage;
import me.shedaniel.rei.api.common.registry.Reloadable;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessorRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Collections;

/**
 * Base interface for a REI plugin.
 *
 * @see REICommonPlugin
 * @see REIClientPlugin
 */
@ApiStatus.OverrideOnly
public interface REIPlugin<P extends REIPlugin<?>> extends Comparable<REIPlugin<P>>, REIPluginProvider<P> {
    /**
     * @return the priority of the plugin, the smaller the priority, the earlier it is called.
     */
    default double getPriority() {
        return 0;
    }
    
    @Override
    default int compareTo(REIPlugin o) {
        return Double.compare(getPriority(), o.getPriority());
    }
    
    @ApiStatus.OverrideOnly
    default void preStage(PluginManager<P> manager, ReloadStage stage) {
    }
    
    @ApiStatus.OverrideOnly
    default void postStage(PluginManager<P> manager, ReloadStage stage) {
    }
    
    @Override
    default Collection<P> provide() {
        return Collections.singletonList((P) this);
    }
    
    @ApiStatus.Internal
    @ApiStatus.Experimental
    default boolean shouldBeForcefullyDoneOnMainThread(Reloadable<?> reloadable) {
        return false;
    }
}
