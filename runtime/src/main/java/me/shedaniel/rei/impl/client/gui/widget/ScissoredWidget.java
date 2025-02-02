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

package me.shedaniel.rei.impl.client.gui.widget;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.CloseableScissors;
import me.shedaniel.rei.api.client.gui.widgets.DelegateWidget;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Supplier;

public class ScissoredWidget extends DelegateWidget {
    private final Supplier<Rectangle> bounds;
    
    public ScissoredWidget(Rectangle bounds, Widget widget) {
        super(widget);
        this.bounds = () -> bounds;
    }
    
    public ScissoredWidget(WidgetWithBounds widget) {
        super(widget);
        this.bounds = widget::getBounds;
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        try (CloseableScissors scissors = scissor(graphics, this.bounds.get())) {
            boolean containsMouse = this.delegate() instanceof WidgetWithBounds withBounds ? withBounds.containsMouse(mouseX, mouseY)
                    : this.bounds.get().contains(mouseX, mouseY);
            
            if (containsMouse) {
                super.render(graphics, mouseX, mouseY, delta);
            } else {
                super.render(graphics, Integer.MAX_VALUE, Integer.MAX_VALUE, delta);
            }
        }
    }
    
    @Override
    public Rectangle getBounds() {
        return bounds.get();
    }
    
    @Override
    public boolean containsMouse(double mouseX, double mouseY) {
        return getBounds().contains(mouseX, mouseY);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return containsMouse(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button);
    }
}
