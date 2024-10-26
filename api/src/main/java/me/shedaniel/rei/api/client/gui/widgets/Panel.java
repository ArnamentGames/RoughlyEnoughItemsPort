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

package me.shedaniel.rei.api.client.gui.widgets;

import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.utils.PanelTextures;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;

public abstract class Panel extends WidgetWithBounds {
    public abstract void setTexture(ResourceLocation texture, ResourceLocation darkTexture);
    
    public final void setTexture(PanelTextures textures) {
        setTexture(textures.texture(), textures.darkTexture());
    }
    
    public final Panel texture(ResourceLocation texture, ResourceLocation darkTexture) {
        setTexture(texture, darkTexture);
        return this;
    }
    
    public final Panel texture(PanelTextures textures) {
        return texture(textures.texture(), textures.darkTexture());
    }
    
    public abstract int getColor();
    
    public abstract void setColor(int color);
    
    public final Panel color(int color) {
        setColor(color);
        return this;
    }
    
    public final Panel color(int lightColor, int darkColor) {
        return color(REIRuntime.getInstance().isDarkThemeEnabled() ? darkColor : lightColor);
    }
    
    public abstract Predicate<Panel> getRendering();
    
    public abstract void setRendering(Predicate<Panel> rendering);
    
    public final Panel rendering(Predicate<Panel> rendering) {
        setRendering(rendering);
        return this;
    }
}
