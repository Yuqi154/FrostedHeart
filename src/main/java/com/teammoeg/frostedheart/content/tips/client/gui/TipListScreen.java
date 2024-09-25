package com.teammoeg.frostedheart.content.tips.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammoeg.frostedheart.FHMain;
import com.teammoeg.frostedheart.content.tips.client.TipElement;
import com.teammoeg.frostedheart.content.tips.client.UnlockedTipManager;
import com.teammoeg.frostedheart.content.tips.client.gui.widget.IconButton;
import com.teammoeg.frostedheart.content.tips.client.util.AnimationUtil;
import com.teammoeg.frostedheart.content.tips.client.util.GuiUtil;
import com.teammoeg.frostedheart.content.tips.client.util.TipDisplayUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import com.teammoeg.frostedheart.util.TranslateUtils;

import dev.ftb.mods.ftblibrary.ui.GuiHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TipListScreen extends Screen {
    private final boolean background;
    private final Map<String, List<String>> customTipList = new HashMap<>();

    private List<String> tipList;
    private TipElement selectEle = null;
    private int GuiHeight = 0;
    private int listHeight = 0;
    private int textHeight = 0;
    private double lastMouseY = 0;
    private double listScroll = 0;
    private double textScroll = 0;
    private double displayListScroll = 0;
    private double displayTextScroll = 0;
    public List<Button> buttons;
    public static String select = "";

    public TipListScreen(boolean background) {
        super(TranslateUtils.str(""));
        this.background = background;
    }

    @Override
	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
		if(pWidget instanceof Button) {
			buttons.add((Button) pWidget);
		}
		return super.addRenderableWidget(pWidget);
	}

	@Override
    public void init() {

        this.addRenderableWidget(new IconButton(0, 0, IconButton.ICON_CROSS, 0xFFC6FCFF, TranslateUtils.translate(FHMain.MODID + ".tips.gui.close"), (button) -> {
            onClose();
        }));
        this.addRenderableWidget(new IconButton(0, 0, IconButton.ICON_LOCK, 0xFFC6FCFF, TranslateUtils.translate(FHMain.MODID + ".tips.gui.pin"), (button) -> {
            TipDisplayUtil.forceAdd(selectEle, true);
        }));

        tipList = new ArrayList<>(UnlockedTipManager.manager.getVisible());
        UnlockedTipManager.manager.getCustom().forEach((c) -> {
            customTipList.put(c.get(0), c);
            tipList.add(c.get(0));
        });

        if (!tipList.contains(select)) {
            select = "";
        }
        GuiHeight = (int)(height*0.8F);
        listHeight = tipList.size()*16;
        textHeight = 0;
        setSelect(select);

        super.init();
    }

    @Override
    public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
        float fadeIn = AnimationUtil.calcFadeIn(400, "TipListGuiFading", false);
        int BGColor = (int)(fadeIn * (background ? 128 : 77)) << 24;
        int x = width - (int)(width*0.6F*fadeIn);
        int y = height - (int)(height*0.9F*fadeIn);
        int lx = (int)(width*0.99F);
        int ly = (int)(height*0.9F);

        if (background) {
            renderBackground(ms);
        }

        ms.fill(x, y-16, lx, y-2, BGColor);
        ms.fill(lx, y-16, lx+1, y-2, 0xFFC6FCFF);
        ms.fill(x, y, lx, ly, BGColor);
        ms.fill(lx, ly, lx+1, y, 0xFFC6FCFF);
        if (fadeIn == 1.0F && !select.isEmpty()) {
            renderTipContent(ms, lx, y);
        }

        IconButton closeButton = (IconButton)this.buttons.get(0);
        closeButton.setAlpha(fadeIn);
        closeButton.setXY(lx-12, y-14);

        IconButton lockButton = (IconButton)this.buttons.get(1);
        lockButton.setAlpha(fadeIn);
        lockButton.setXY(lx-27, y-14);

        if (fadeIn > 0.5F && !tipList.isEmpty()) {
            ms.pose().pushPose();
            ms.pose().translate(0, displayListScroll, 0);
            ms.enableScissor(0, height/10, width, ((GuiHeight +16)));
            renderList(ms, tipList, (int)(width*0.05F), (int)(height*0.1F)-16, mouseX, mouseY);
            ms.disableScissor();
            ms.pose().popPose();
            //TODO widget
            if (listHeight > GuiHeight +16) {
                renderScrollBar(ms, mouseX, mouseY, (int)(width*0.05F)-8, (int)(height*0.1F)-16, 4, GuiHeight +16, listHeight);
            } else {
                setListScroll(0);
            }
        }
        
        displayListScroll = displayListScroll + (listScroll - displayListScroll)*0.1;
        displayTextScroll = displayTextScroll + (textScroll - displayTextScroll)*0.1;
        lastMouseY = mouseY;

        super.render(ms, mouseX, mouseY, partialTicks);
    }

    private void renderList(GuiGraphics ms, List<String> list, int x, int y, int mouseX, int mouseY) {
        int BGOutline = -4;

        for (int i = 0; i < list.size(); i++) {
            if (y+i*16+ displayListScroll > height*0.9F || y+i*16+ displayListScroll +32 < height*0.1F) {
                continue; //超出绘制区域
            }
            int BGWidth = (int)(width*0.3);
            float progress = 0;

            if (i == 0) {
                progress = AnimationUtil.calcFadeIn(300, "TipListGuiList" + list.get(i), false);
            } else if (AnimationUtil.getProgress("TipListGuiList" + list.get(i-1)) > 0.075F || y+i*16+ displayListScroll < height*0.1F+16) {
                progress = AnimationUtil.calcFadeIn(300, "TipListGuiList" + list.get(i), false);
            }

            if (progress != 0) {
                int fontColor = Mth.clamp((int)(progress * 255), 0x04, 0xFF) << 24 | 0xFFC6FCFF & 0x00FFFFFF;
                int BGAlpha = background ? 128 : 77;
                int BGColor = Mth.clamp((int) (progress * BGAlpha), 0x04, 0xFF) << 24;
                float selOffset;

                if (list.get(i).equals(select)) {
                    AnimationUtil.removeAnimation("TipListListSelD" + list.get(i));
                    selOffset = AnimationUtil.calcFadeIn(100, "TipListListSel" + list.get(i), false) * 10;
                    if (selOffset == 0) {
                        AnimationUtil.removeAnimation("TipListSelColor");
                    }

                } else if (GuiUtil.isMouseIn(mouseX, mouseY, x, (int)(y + i*16 + displayListScroll), BGWidth+3, 9-BGOutline)) {
                    AnimationUtil.removeAnimation("TipListListSelD" + list.get(i));
                    selOffset = AnimationUtil.calcFadeIn(100, "TipListListSel" + list.get(i), false) * 10;
                    if (GuiUtil.isLeftClicked()) {
                        setSelect(list.get(i));
                        AnimationUtil.removeAnimation("TipListSelColor");
                    }

                } else {
                    float last = AnimationUtil.getFadeIn("TipListListSel" + list.get(i));
                    float cal = last-(AnimationUtil.calcFadeOut(100, "TipListListSelD" + list.get(i), false));
                    selOffset = Math.max(0, cal*10);
                    if (selOffset == 0) {
                        AnimationUtil.removeAnimation("TipListListSel" + list.get(i));
                        AnimationUtil.removeAnimation("TipListListSelD" + list.get(i));
                    }
                }

                ms.pose().pushPose();
                ms.pose().translate(x*progress + selOffset-BGOutline, y-BGOutline + i*16, 0);
                if (list.get(i).equals(select)) {
                    float selColorP = AnimationUtil.calcFadeIn(200, "TipListSelColor", false);
                    int selColor = Mth.clamp((int)(selColorP * BGAlpha), 0x04, 0xFF) << 24 | 0xFFC6FCFF & 0x00FFFFFF;
                    ms.fill(BGOutline, BGOutline, BGWidth, 10, selColor);
                } else {
                    ms.fill(BGOutline, BGOutline, BGWidth, 10, BGColor);
                }
                ms.fill(BGOutline, BGOutline, BGOutline+1, BGOutline + 10-BGOutline, fontColor);

                String text = list.get(i);
                if (text.startsWith("*custom*")) {
                    text = text.substring(8);
                } else {
                    text = I18n.get("tips." + FHMain.MODID + "." + list.get(i) + ".title");
                }

                if (font.width(text) > BGWidth) {
                    text = text.substring(0, Math.min(text.length(), BGWidth/6)) + "...";
                }

                if (list.get(i).equals(select)) {
                	ms.drawString(minecraft.font, text, 0, 0, fontColor);
                } else {
                	ms.drawString(minecraft.font, text, 0, 0, fontColor,false);
                }
                ms.pose().popPose();
            }
        }
    }

    private void renderTipContent(GuiGraphics ms, int x, int y) { //TODO 搜索和分组
        boolean custom = select.startsWith("*custom*");
        if (selectEle == null || !selectEle.ID.equals(select)) {
            if (custom) {
                TipElement ele = new TipElement();
                try {
                    ele.ID = customTipList.get(select).get(0);
                    ele.visibleTime = Integer.parseInt(customTipList.get(select).get(1));
                    for (int i = 2; i < customTipList.get(select).size(); i++) {
                        ele.contents.add(TranslateUtils.str(customTipList.get(select).get(i)));
                    }
                    selectEle = ele;
                } catch (Exception e) {
                    //移除有问题的自定义提示
                    remove(customTipList.get(select).get(0));
                    return;
                }

            } else {
                selectEle = TipDisplayUtil.getTipEle(select);
            }
        }

        //移除不应该存在的提示
        if (selectEle.hide) {
            remove(selectEle.ID);
            return;
        }

        float textFading = AnimationUtil.calcFadeIn(200, "TipListTextFading", false);
        int textColor = Math.max((int)(textFading * 255), 0x04) << 24 | selectEle.fontColor & 0x00FFFFFF;
        int boxWidth = (int)(width*0.4F);

        ms.pose().pushPose();
        if (font.width(selectEle.contents.get(0).getString()) > x-32 - boxWidth) {
            ms.pose().translate(0, displayTextScroll, 0);
            ms.enableScissor(0, height/10+4, width, GuiHeight -8);
            int line = 0;
            for (int i = 0; i < selectEle.contents.size(); i++) {
                line += 1 + GuiUtil.formatAndDraw(selectEle.contents.get(i), ms, boxWidth + 4, y+4 + line*12,
                        x-8 - boxWidth, textColor, 12, false);
            }
            textHeight = line*12;

        } else if (selectEle.contents.size() > 1) {
        	ms.drawString(minecraft.font, selectEle.contents.get(0), boxWidth + 4, y - 12, textColor);
            ms.pose().translate(0, displayTextScroll, 0);
            ms.enableScissor(0, height/10+4, width, GuiHeight -8);
            int line = 0;
            for (int i = 1; i < selectEle.contents.size(); i++) {
                line += 1 + GuiUtil.formatAndDraw(selectEle.contents.get(i), ms, boxWidth + 4, y+4 + line*12,
                        x-8 - boxWidth, textColor, 12, false);
            }
            textHeight = line*12;

        } else {
        	ms.drawString(minecraft.font, selectEle.contents.get(0), boxWidth + 4, y - 12, textColor);
        }
        RenderSystem.disableScissor();
        ms.pose().popPose();

        //文本高度超出屏幕时渲染箭头
        if (textHeight > GuiHeight && -displayTextScroll < textHeight-GuiHeight-1) {
            float animation = AnimationUtil.calcBounce(1000, "TipListDownArrow", true)*2;
            ms.pose().pushPose();
            ms.pose().translate(width*0.99F-14, height*0.9F-16-animation, 0);
            GuiUtil.renderIcon(ms, IconButton.ICON_DOWN, 0, 0, 0xFFC6FCFF);
            ms.pose().popPose();
        }
    }

    private void renderScrollBar(GuiGraphics ms, int mouseX, int mouseY, int x, int y, int w, int h, int totalHeight){
        float maxHeight = totalHeight-h;
        int barHeight = (int)Math.max(32, h/(maxHeight+h) * h);
        float barY = (float)((-(displayListScroll-1)/maxHeight)*(h-barHeight));

        if (isDragging() || GuiUtil.isMouseIn(mouseX, mouseY, x, y+(int)barY, w, barHeight) && GuiUtil.isLeftClicked()) {
            setDragging(GuiUtil.isLeftDown());
            if (GuiUtil.isMouseIn(mouseX, mouseY, 0, y, width, h)) {
                setListScroll(listScroll - (mouseY-lastMouseY)*(maxHeight/(h-barHeight)));
            }
        }

        ms.fill(x, y, x+w, y+h, (background ? 128 : 77) << 24);

        ms.fill(x, y+(int)barY, x+w, y+barHeight+(int)barY, 0xFFC6FCFF);
    }

    private void setSelect(String s) {
        if (!s.isEmpty()) {
            int target = tipList.indexOf(s) * 16;
            if (target >= -16) {
                if (target + listScroll < 0) {
                    setListScroll(listScroll + (-listScroll - target));
                } else if (target + listScroll > GuiHeight) {
                    setListScroll(listScroll + (-listScroll - target + GuiHeight));
                }
            }
        } else {
            setListScroll(listScroll);
        }
        select = s;
        setTextScroll(0);

        IconButton button = (IconButton)this.buttons.get(1);
        button.visible = !select.isEmpty();
    }

    private void remove(String ID) {
        UnlockedTipManager.manager.removeUnlocked(ID);
        tipList.remove(select);
        setSelect("");
        listHeight = tipList.size()*16;
        selectEle = null;
    }

    private void setListScroll(double listScroll) {
        this.listScroll = listScroll == 0 ? 0 : Mth.clamp(listScroll, -listHeight + GuiHeight +16, 0);
    }
    
    private void setTextScroll(double textScroll) {
        this.textScroll = textScroll == 0 ? 0 : Mth.clamp(textScroll, -textHeight + GuiHeight, 0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (listHeight > GuiHeight +16) {
            if (GuiUtil.isMouseIn((int)mouseX, (int)mouseY, 0, (int)(height*0.1F)-16, (int)(width*0.4F), GuiHeight +16)) {
                setListScroll(listScroll + delta*48);
            }
        }
        if (textHeight > GuiHeight) {
            if (GuiUtil.isMouseIn((int)mouseX, (int)mouseY, (int)(width*0.4F), (int)(height*0.1F), (int)(width*0.59F), GuiHeight)) {
                setTextScroll(textScroll + delta*32);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (tipList.isEmpty()) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        int sel = tipList.indexOf(select);
        switch (keyCode) {
            case 258:
                if (modifiers == 1) {
                    if (sel > 0) {
                        setSelect(tipList.get(sel-1));
                    } else {
                        setSelect(tipList.get(tipList.size()-1));
                    }
                } else {
                    if (sel < tipList.size()-1) {
                        setSelect(tipList.get(sel+1));
                    } else {
                        setSelect(tipList.get(0));
                    }
                }
                return true;
            case 83:
            case 264:
                if (sel < tipList.size()-1) {
                    setSelect(tipList.get(sel+1));
                } else {
                    setSelect(tipList.get(0));
                }
                return true;
            case 87:
            case 265:
                if (sel > 0) {
                    setSelect(tipList.get(sel-1));
                } else {
                    setSelect(tipList.get(tipList.size()-1));
                }
                return true;
            case 69:
                onClose();
                return true;
            default:
                return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void removed() {
        AnimationUtil.removeAnimation("TipListSelColor");
        AnimationUtil.removeAnimation("TipListDownArrow");
        AnimationUtil.removeAnimation("TipListGuiFading");
        AnimationUtil.removeAnimation("TipListTextFading");
        tipList.forEach((name) -> {
            AnimationUtil.removeAnimation("TipListGuiList" + name);
            AnimationUtil.removeAnimation("TipListListSel" + name);
            AnimationUtil.removeAnimation("TipListListSelD" + name);
        });

        TipDisplayUtil.resetTipAnimation();
    }

    @Override
    public boolean isPauseScreen() {
        return background;
    }
}