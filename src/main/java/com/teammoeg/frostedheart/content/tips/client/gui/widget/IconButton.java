package com.teammoeg.frostedheart.content.tips.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammoeg.frostedheart.FHMain;
import com.teammoeg.frostedheart.util.client.FHGuiHelper;
import com.teammoeg.frostedheart.util.client.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Tooltip;

public class IconButton extends Button {
    public static final ResourceLocation ICON_LOCATION = new ResourceLocation(FHMain.MODID, "textures/gui/hud_icon.png");
    public static final Point ICON_MOUSE_LEFT    = new Point(0 ,0 );
    public static final Point ICON_MOUSE_RIGHT   = new Point(10,0 );
    public static final Point ICON_MOUSE_MIDDLE  = new Point(20,0 );
    public static final Point ICON_LOCK          = new Point(10,10);
    public static final Point ICON_RIGHT         = new Point(40,10);
    public static final Point ICON_DOWN          = new Point(50,10);
    public static final Point ICON_LEFT          = new Point(60,10);
    public static final Point ICON_TOP           = new Point(70,10);
    public static final Point ICON_BOX           = new Point(0 ,30);
    public static final Point ICON_BOX_ON        = new Point(10,30);
    public static final Point ICON_CROSS         = new Point(20,30);
    public static final Point ICON_HISTORY       = new Point(30,30);
    public static final Point ICON_TRASH_CAN     = new Point(50,30);

    public final Point currentIcon;
    public final int color;

    public IconButton(int x, int y, Point icon, int color, Component title, OnPress pressedAction) {
        super(builder(title, pressedAction).bounds(x, y, 10, 10).tooltip(Tooltip.create(title)));
        this.color = color;
        this.currentIcon = icon;
    }

    public void setXY(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void renderWidget(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (isHovered()) {
        	matrixStack.fill( getX(), getY(), getX()+width, getY()+height, 50 << 24 | color & 0x00FFFFFF);
        }
        
        FHGuiHelper.bindTexture(ICON_LOCATION);
        FHGuiHelper.blitColored(matrixStack.pose(), getX(), getY(), 10, 10, currentIcon.getX(), currentIcon.getY(), 10, 10, 80, 80, color,alpha);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 258) {
            return false;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }
}
