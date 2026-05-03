package com.waypoints.gui;

import com.waypoints.Waypoint;
import com.waypoints.WaypointsMod;
import java.io.IOException;
import java.util.List;

public class GuiWaypointList extends FixedScaleGuiScreen {
   private boolean settingsTab = false;
   private int draggingSlider = 0;

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.beginUi();
      int panelX = 150;
      int panelY = 120;
      int panelW = 660;
      int panelH = 300;
      this.drawRoundedPanel(panelX, panelY, panelX + panelW, panelY + panelH, 8, this.menuColor(1053209));
      this.drawRoundedPanel(panelX, panelY, panelX + 160, panelY + panelH, 8, this.sidebarColor(1514019));
      drawRect(panelX + 152, panelY, panelX + 160, panelY + panelH, this.sidebarColor(1514019));
      this.drawString(this.fontRendererObj, "Navigator", panelX + 18, panelY + 18, 15790320);
      this.navItem(panelX + 18, panelY + 48, "Waypoints (" + WaypointsMod.WAYPOINTS.all().size() + ")", !this.settingsTab);
      this.navItem(panelX + 18, panelY + 76, "Settings", this.settingsTab);
      this.drawString(this.fontRendererObj, this.settingsTab ? "Settings" : "Waypoints", panelX + 185, panelY + 20, 15790320);
      this.drawString(this.fontRendererObj, "x", panelX + panelW - 28, panelY + 18, 10132646);
      if (this.settingsTab) {
         this.drawSettings(panelX, panelY);
      } else {
         this.drawWaypoints(panelX, panelY, panelW, panelH);
      }

      this.endUi();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   private void drawWaypoints(int panelX, int panelY, int panelW, int panelH) {
      this.button(panelX + 18, panelY + panelH - 38, 126, 24, "Add Waypoint", true);
      this.button(panelX + panelW - 132, panelY + 18, 92, 18, WaypointsMod.WAYPOINTS.hasVisibleWaypoints() ? "Hide All" : "Show All", false);
      List<Waypoint> waypoints = WaypointsMod.WAYPOINTS.all();
      int rowY = panelY + 54;
      if (waypoints.isEmpty()) {
         this.drawString(this.fontRendererObj, "No waypoints yet.", panelX + 185, rowY, 10132646);
      }

      for (int i = 0; i < waypoints.size() && rowY < panelY + panelH - 32; i++) {
         Waypoint waypoint = waypoints.get(i);
         drawRect(panelX + 176, rowY - 8, panelX + panelW - 24, rowY + 28, 1713579827);
         drawRect(panelX + 188, rowY + 1, panelX + 196, rowY + 9, 0xFF000000 | waypoint.color);
         this.drawString(this.fontRendererObj, waypoint.name, panelX + 204, rowY, 15790320);
         String info = Math.round(waypoint.x)
            + ", "
            + Math.round(waypoint.y)
            + ", "
            + Math.round(waypoint.z)
            + "  "
            + waypoint.getDimensionName()
            + "  "
            + waypoint.owner;
         this.drawString(this.fontRendererObj, info, panelX + 204, rowY + 13, 10462386);
         this.button(panelX + panelW - 176, rowY - 1, 46, 18, waypoint.isVisible() ? "Hide" : "Show", false);
         this.button(panelX + panelW - 126, rowY - 1, 38, 18, "Edit", false);
         this.button(panelX + panelW - 82, rowY - 1, 48, 18, "Delete", false);
         rowY += 42;
      }
   }

   private void drawSettings(int panelX, int panelY) {
      int left = panelX + 185;
      this.drawString(this.fontRendererObj, "Waypoint Plate", left, panelY + 58, 15790320);
      this.drawString(this.fontRendererObj, "Label Scale: " + String.format("%.1f", WaypointsMod.SETTINGS.labelScale), left, panelY + 88, 13619931);
      this.slider(left + 160, panelY + 84, 180, this.normalized(WaypointsMod.SETTINGS.labelScale, 0.5F, 3.0F));
      this.drawString(this.fontRendererObj, "Plate Padding: " + WaypointsMod.SETTINGS.labelBoxPadding, left, panelY + 120, 13619931);
      this.slider(left + 160, panelY + 116, 180, this.normalized(WaypointsMod.SETTINGS.labelBoxPadding, 2.0F, 24.0F));
      this.drawString(this.fontRendererObj, "Plate Opacity: " + WaypointsMod.SETTINGS.labelBackgroundAlpha, left, panelY + 152, 13619931);
      this.slider(left + 160, panelY + 148, 180, this.normalized(WaypointsMod.SETTINGS.labelBackgroundAlpha, 0.0F, 255.0F));
      this.drawString(this.fontRendererObj, "Menu Background Opacity: " + WaypointsMod.SETTINGS.menuBackgroundAlpha, left, panelY + 184, 13619931);
      this.slider(left + 160, panelY + 180, 180, this.normalized(WaypointsMod.SETTINGS.menuBackgroundAlpha, 0.0F, 255.0F));
      this.drawString(this.fontRendererObj, "Saved automatically.", left, panelY + 216, 10462386);
   }

   @Override
   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      int ux = this.uiMouseX(mouseX);
      int uy = this.uiMouseY(mouseY);
      int panelX = 150;
      int panelY = 120;
      int panelW = 660;
      int panelH = 300;
      if (this.inside(ux, uy, panelX + panelW - 34, panelY + 12, 24, 24)) {
         this.mc.displayGuiScreen(null);
      } else if (this.inside(ux, uy, panelX + 18, panelY + 48, 126, 20)) {
         this.settingsTab = false;
      } else if (this.inside(ux, uy, panelX + 18, panelY + 76, 126, 20)) {
         this.settingsTab = true;
      } else if (this.settingsTab) {
         this.handleSettingsClick(ux, uy, panelX, panelY);
      } else if (this.inside(ux, uy, panelX + panelW - 132, panelY + 18, 92, 18)) {
         WaypointsMod.WAYPOINTS.setAllVisible(!WaypointsMod.WAYPOINTS.hasVisibleWaypoints());
      } else if (this.inside(ux, uy, panelX + 18, panelY + panelH - 38, 126, 24)) {
         this.mc.displayGuiScreen(new GuiCreateWaypoint());
      } else {
         int rowY = panelY + 54;
         List<Waypoint> waypoints = WaypointsMod.WAYPOINTS.all();

         for (int i = 0; i < waypoints.size() && rowY < panelY + panelH - 32; i++) {
            if (this.inside(ux, uy, panelX + panelW - 176, rowY - 1, 46, 18)) {
               Waypoint waypoint = waypoints.get(i);
               waypoint.visible = !waypoint.isVisible();
               WaypointsMod.WAYPOINTS.changed();
               return;
            }

            if (this.inside(ux, uy, panelX + panelW - 126, rowY - 1, 38, 18)) {
               this.mc.displayGuiScreen(new GuiCreateWaypoint(waypoints.get(i)));
               return;
            }

            if (this.inside(ux, uy, panelX + panelW - 82, rowY - 1, 48, 18)) {
               WaypointsMod.WAYPOINTS.remove(waypoints.get(i));
               return;
            }

            rowY += 42;
         }
      }
   }

   private void handleSettingsClick(int mouseX, int mouseY, int panelX, int panelY) {
      int left = panelX + 185;
      if (this.inside(mouseX, mouseY, left + 160, panelY + 78, 180, 18)) {
         this.draggingSlider = 1;
      }

      if (this.inside(mouseX, mouseY, left + 160, panelY + 110, 180, 18)) {
         this.draggingSlider = 2;
      }

      if (this.inside(mouseX, mouseY, left + 160, panelY + 142, 180, 18)) {
         this.draggingSlider = 3;
      }

      if (this.inside(mouseX, mouseY, left + 160, panelY + 174, 180, 18)) {
         this.draggingSlider = 4;
      }

      this.updateDraggedSlider(mouseX, panelX);
   }

   @Override
   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      if (this.draggingSlider != 0) {
         this.updateDraggedSlider(this.uiMouseX(mouseX), 150);
      }
   }

   @Override
   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
      this.draggingSlider = 0;
   }

   private void updateDraggedSlider(int mouseX, int panelX) {
      if (this.draggingSlider != 0) {
         int left = panelX + 185 + 160;
         float value = Math.max(0.0F, Math.min(1.0F, (mouseX - left) / 180.0F));
         if (this.draggingSlider == 1) {
            WaypointsMod.SETTINGS.setLabelScale(0.5F + value * 2.5F);
         }

         if (this.draggingSlider == 2) {
            WaypointsMod.SETTINGS.setLabelBoxPadding(Math.round(2.0F + value * 22.0F));
         }

         if (this.draggingSlider == 3) {
            WaypointsMod.SETTINGS.setLabelBackgroundAlpha(Math.round(value * 255.0F));
         }

         if (this.draggingSlider == 4) {
            WaypointsMod.SETTINGS.setMenuBackgroundAlpha(Math.round(value * 255.0F));
         }
      }
   }

   private void slider(int left, int top, int width, float value) {
      int trackY = top + 8;
      drawRect(left, trackY - 2, left + width, trackY + 2, -14342094);
      drawRect(left, trackY - 2, left + (int)(width * value), trackY + 2, -15963347);
      int knobX = left + (int)(width * value);
      drawRect(knobX - 4, top + 2, knobX + 4, top + 14, -2762785);
   }

   private float normalized(float value, float min, float max) {
      return Math.max(0.0F, Math.min(1.0F, (value - min) / (max - min)));
   }

   private void navItem(int left, int top, String text, boolean selected) {
      if (selected) {
         drawRect(left, top, left + 126, top + 20, -14342094);
      }

      this.drawString(this.fontRendererObj, text, left + 10, top + 6, selected ? 15790320 : 12172744);
   }

   private void button(int left, int top, int buttonWidth, int buttonHeight, String text, boolean green) {
      drawRect(left, top, left + buttonWidth, top + buttonHeight, green ? -15963347 : -14342094);
      this.drawCenteredString(this.fontRendererObj, text, left + buttonWidth / 2, top + 7, green ? 5046140 : 14014431);
   }

   private boolean inside(int mouseX, int mouseY, int left, int top, int buttonWidth, int buttonHeight) {
      return mouseX >= left && mouseY >= top && mouseX < left + buttonWidth && mouseY < top + buttonHeight;
   }

   private int menuColor(int rgb) {
      return (WaypointsMod.SETTINGS.menuBackgroundAlpha & 0xFF) << 24 | rgb;
   }

   private int sidebarColor(int rgb) {
      return (Math.min(255, WaypointsMod.SETTINGS.menuBackgroundAlpha + 17) & 0xFF) << 24 | rgb;
   }
}
