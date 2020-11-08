/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020 shedaniel
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

package me.shedaniel.rei.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.ScissorsScreen;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.function.DoubleSupplier;

public class TransformingScreen extends DelegateScreen implements ScissorsScreen {
    private final DoubleSupplier xTransformer;
    private final DoubleSupplier yTransformer;
    private final Screen lastScreen;
    
    public TransformingScreen(Screen parent, Screen lastScreen, Runnable init, DoubleSupplier xTransformer, DoubleSupplier yTransformer) {
        super(parent);
        this.lastScreen = lastScreen;
        this.xTransformer = xTransformer;
        this.yTransformer = yTransformer;
        init.run();
    }
    
    @Override
    public void init(Minecraft minecraft, int i, int j) {
        super.init(minecraft, i, j);
        if (lastScreen != null) {
            lastScreen.init(minecraft, i, j);
        }
    }
    
    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (lastScreen != null) {
            RenderSystem.pushMatrix();
            RenderSystem.translated(0, 0, -400);
            lastScreen.render(poseStack, -1, -1, 0);
            RenderSystem.popMatrix();
        }
        RenderSystem.pushMatrix();
        RenderSystem.translated(xTransformer.getAsDouble(), yTransformer.getAsDouble(), 0);
        super.render(poseStack, i, j, f);
        RenderSystem.popMatrix();
    }
    
    @Override
    public @Nullable Rectangle handleScissor(@Nullable Rectangle rectangle) {
        if (rectangle != null)
            rectangle.translate((int) xTransformer.getAsDouble(), (int) yTransformer.getAsDouble());
        return rectangle;
    }
}
