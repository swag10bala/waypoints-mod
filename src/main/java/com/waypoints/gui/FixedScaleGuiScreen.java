package com.waypoints.gui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public abstract class FixedScaleGuiScreen extends GuiScreen {
   protected static final int UI_WIDTH = 960;
   protected static final int UI_HEIGHT = 540;
   private float uiScale = 1.0F;

   protected void beginUi() {
      this.uiScale = Math.min(1.0F, Math.min(this.width / 960.0F, this.height / 540.0F));
      if (this.uiScale <= 0.0F) {
         this.uiScale = 1.0F;
      }

      GL11.glPushMatrix();
      GL11.glScalef(this.uiScale, this.uiScale, 1.0F);
   }

   protected void endUi() {
      GL11.glPopMatrix();
   }

   protected int uiMouseX(int mouseX) {
      return (int)(mouseX / this.uiScale);
   }

   protected int uiMouseY(int mouseY) {
      return (int)(mouseY / this.uiScale);
   }

   protected void drawRoundedPanel(int left, int top, int right, int bottom, int radius, int color) {
      drawRect(left + radius, top, right - radius, bottom, color);
      drawRect(left, top + radius, right, bottom - radius, color);
      drawRect(left + 2, top + 4, left + radius, top + radius, color);
      drawRect(right - radius, top + 4, right - 2, top + radius, color);
      drawRect(left + 2, bottom - radius, left + radius, bottom - 4, color);
      drawRect(right - radius, bottom - radius, right - 2, bottom - 4, color);
      drawRect(left + 4, top + 2, left + radius, top + 4, color);
      drawRect(right - radius, top + 2, right - 4, top + 4, color);
      drawRect(left + 4, bottom - 4, left + radius, bottom - 2, color);
      drawRect(right - radius, bottom - 4, right - 4, bottom - 2, color);
   }
}
