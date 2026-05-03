package com.waypoints.gui;

import com.waypoints.Waypoint;
import com.waypoints.WaypointsMod;
import java.io.IOException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class GuiConfirmSharedWaypoint extends FixedScaleGuiScreen {
   private final Waypoint waypoint;

   public GuiConfirmSharedWaypoint(Waypoint waypoint) {
      this.waypoint = waypoint;
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.beginUi();
      int panelX = 310;
      int panelY = 200;
      this.drawRoundedPanel(panelX, panelY, panelX + 340, panelY + 140, 8, (WaypointsMod.SETTINGS.menuBackgroundAlpha & 0xFF) << 24 | 1053209);
      this.drawString(this.fontRendererObj, "Create shared waypoint from: " + this.waypoint.owner + "?", panelX + 24, panelY + 28, 15790320);
      this.drawString(
         this.fontRendererObj,
         this.waypoint.name + "  " + Math.round(this.waypoint.x) + ", " + Math.round(this.waypoint.y) + ", " + Math.round(this.waypoint.z),
         panelX + 24,
         panelY + 52,
         13619931
      );
      this.button(panelX + 90, panelY + 94, 64, 24, "Yes", true);
      this.button(panelX + 186, panelY + 94, 64, 24, "No", false);
      this.endUi();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   @Override
   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      int ux = this.uiMouseX(mouseX);
      int uy = this.uiMouseY(mouseY);
      int panelX = 310;
      int panelY = 200;
      if (this.inside(ux, uy, panelX + 90, panelY + 94, 64, 24)) {
         if (!WaypointsMod.WAYPOINTS.containsShared(this.waypoint)) {
            WaypointsMod.WAYPOINTS.add(this.waypoint);
         }

         if (this.mc.thePlayer != null) {
            this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[Waypoints] " + EnumChatFormatting.WHITE + "Shared waypoint created."));
         }

         this.mc.displayGuiScreen(null);
      }

      if (this.inside(ux, uy, panelX + 186, panelY + 94, 64, 24)) {
         this.mc.displayGuiScreen(null);
      }
   }

   private void button(int left, int top, int width, int height, String text, boolean green) {
      drawRect(left, top, left + width, top + height, green ? -15963347 : -14342094);
      this.drawCenteredString(this.fontRendererObj, text, left + width / 2, top + 8, green ? 5046140 : 14014431);
   }

   private boolean inside(int mouseX, int mouseY, int left, int top, int width, int height) {
      return mouseX >= left && mouseY >= top && mouseX < left + width && mouseY < top + height;
   }
}
