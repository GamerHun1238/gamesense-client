package com.gamesense.client.module.modules.hud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.gamesense.api.setting.values.ColorSetting;
import dev.gamesense.misc.GSColor;
import com.gamesense.client.clickgui.GameSenseGUI;
import com.gamesense.client.module.Category;
import com.gamesense.client.module.HUDModule;
import com.gamesense.client.module.Module;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IInterface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.setting.Labeled;
import com.lukflug.panelstudio.theme.ITheme;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

@Module.Declaration(name = "InventoryViewer", category = Category.HUD)
@HUDModule.Declaration(posX = 0, posZ = 10)
public class InventoryViewer extends HUDModule {

    ColorSetting fillColor = registerColor("Fill", new GSColor(0, 0, 0, 100));
    ColorSetting outlineColor = registerColor("Outline", new GSColor(255, 0, 0, 255));

    @Override
    public void populate(ITheme theme) {
        component = new InventoryViewerComponent(theme);
    }

    private class InventoryViewerComponent extends HUDComponent {

        public InventoryViewerComponent(ITheme theme) {
            super(new Labeled(getName(),null,()->true), InventoryViewer.this.position, getName());
        }

        @Override
        public void render(Context context) {
            super.render(context);
            // Render background
            Color bgcolor = new GSColor(fillColor.getValue(), 100);
            context.getInterface().fillRect(context.getRect(), bgcolor, bgcolor, bgcolor, bgcolor);
            // Render outline
            Color color = outlineColor.getValue();
            context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(context.getSize().width, 1)), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(1, context.getSize().height)), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x + context.getSize().width - 1, context.getPos().y), new Dimension(1, context.getSize().height)), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x, context.getPos().y + context.getSize().height - 1), new Dimension(context.getSize().width, 1)), color, color, color, color);
            // Render the actual items
            NonNullList<ItemStack> items = Minecraft.getMinecraft().player.inventory.mainInventory;
            for (int size = items.size(), item = 9; item < size; ++item) {
                int slotX = context.getPos().x + item % 9 * 18;
                int slotY = context.getPos().y + 2 + (item / 9 - 1) * 18;
                GameSenseGUI.renderItem(items.get(item), new Point(slotX, slotY));
            }
        }

        @Override
        public Dimension getSize(IInterface inter) {
            return new Dimension(162,56);
        }
    }
}