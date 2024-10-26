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

package me.shedaniel.rei.impl.client.gui.widget.basewidgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.shedaniel.rei.api.client.gui.DrawableConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class TexturedDrawableConsumer implements DrawableConsumer {
    private ResourceLocation identifier;
    private int x, y, width, height, uWidth, vHeight, textureWidth, textureHeight;
    private float u, v;
    
    public TexturedDrawableConsumer(ResourceLocation identifier, int x, int y, int width, int height, float u, float v, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.drawSpecial(source -> {
            innerBlit(source.getBuffer(RenderType.guiTextured(identifier)), graphics.pose().last().pose(), x, x + width, y, y + height, 0, uWidth, vHeight, u, v, textureWidth, textureHeight);
        });
    }
    
    private static void innerBlit(VertexConsumer consumer, Matrix4f matrix, int xStart, int xEnd, int yStart, int yEnd, int z, int width, int height, float u, float v, int texWidth, int texHeight) {
        innerBlit(consumer, matrix, xStart, xEnd, yStart, yEnd, z, u / texWidth, (u + width) / texWidth, v / texHeight, (v + height) / texHeight);
    }
    
    private static void innerBlit(VertexConsumer consumer, Matrix4f matrix, int xStart, int xEnd, int yStart, int yEnd, int z, float uStart, float uEnd, float vStart, float vEnd) {
        consumer.addVertex(matrix, xStart, yEnd, z).setUv(uStart, vEnd).setColor(-1);
        consumer.addVertex(matrix, xEnd, yEnd, z).setUv(uEnd, vEnd).setColor(-1);
        consumer.addVertex(matrix, xEnd, yStart, z).setUv(uEnd, vStart).setColor(-1);
        consumer.addVertex(matrix, xStart, yStart, z).setUv(uStart, vStart).setColor(-1);
    }
}
