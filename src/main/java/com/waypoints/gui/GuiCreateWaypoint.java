package com.waypoints.gui;

import com.waypoints.Waypoint;
import com.waypoints.WaypointShare;
import com.waypoints.WaypointsMod;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class GuiCreateWaypoint extends FixedScaleGuiScreen {
   private final Waypoint editing;
   private GuiTextField name;
   private GuiTextField group;
   private GuiTextField x;
   private GuiTextField y;
   private GuiTextField z;
   private int dimension;
   private int color = 16734885;
   private boolean showText = true;
   private boolean showDistance = true;
   private boolean directionalOnly = false;

   public GuiCreateWaypoint() {
      this(null);
   }

   public GuiCreateWaypoint(Waypoint editing) {
      this.editing = editing;
   }

   @Override
   public void initGui() {
      int panelX = 240;
      int panelY = 140;
      this.name = new GuiTextField(0, this.fontRendererObj, panelX + 28, panelY + 62, 270, 22);
      this.group = new GuiTextField(1, this.fontRendererObj, panelX + 310, panelY + 62, 140, 22);
      this.x = new GuiTextField(2, this.fontRendererObj, panelX + 28, panelY + 124, 80, 22);
      this.y = new GuiTextField(3, this.fontRendererObj, panelX + 118, panelY + 124, 80, 22);
      this.z = new GuiTextField(4, this.fontRendererObj, panelX + 208, panelY + 124, 80, 22);
      this.name.setMaxStringLength(40);
      this.group.setMaxStringLength(32);
      this.x.setMaxStringLength(12);
      this.y.setMaxStringLength(12);
      this.z.setMaxStringLength(12);
      if (this.editing != null) {
         this.name.setText(this.editing.name);
         this.group.setText(this.editing.group);
         this.x.setText(String.valueOf(Math.round(this.editing.x)));
         this.y.setText(String.valueOf(Math.round(this.editing.y)));
         this.z.setText(String.valueOf(Math.round(this.editing.z)));
         this.dimension = this.editing.dimension;
         this.color = this.editing.color;
         this.showText = this.editing.showText;
         this.showDistance = this.editing.showDistance;
         this.directionalOnly = this.editing.directionalOnly;
      } else if (this.mc.thePlayer != null) {
         this.group.setText("Default");
         this.x.setText(String.valueOf(Math.round(this.mc.thePlayer.posX)));
         this.y.setText(String.valueOf(Math.round(this.mc.thePlayer.posY)));
         this.z.setText(String.valueOf(Math.round(this.mc.thePlayer.posZ)));
         this.dimension = this.mc.thePlayer.dimension;
      }

      this.name.setFocused(true);
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.beginUi();
      int panelX = 240;
      int panelY = 140;
      this.drawRoundedPanel(panelX, panelY, panelX + 480, panelY + 260, 8, this.menuColor(1053209));
      this.drawString(this.fontRendererObj, this.editing == null ? "Create Waypoint" : "Edit Waypoint", panelX + 28, panelY + 18, 15790320);
      this.drawString(this.fontRendererObj, "x", panelX + 450, panelY + 18, 10132646);
      this.label("Name", panelX + 28, panelY + 48);
      this.label("Group", panelX + 310, panelY + 48);
      this.label("Coordinates", panelX + 28, panelY + 110);
      this.label("Dimension", panelX + 310, panelY + 110);
      this.label("Display Options", panelX + 28, panelY + 172);
      this.fieldBack(this.name);
      this.fieldBack(this.group);
      this.fieldBack(this.x);
      this.fieldBack(this.y);
      this.fieldBack(this.z);
      this.button(panelX + 310, panelY + 124, 140, 22, this.getDimensionName(), false);
      this.checkbox(panelX + 28, panelY + 196, "Show Text", this.showText);
      this.checkbox(panelX + 170, panelY + 196, "Show Distance", this.showDistance);
      this.checkbox(panelX + 28, panelY + 220, "Direction Only", this.directionalOnly);
      drawRect(panelX + 312, panelY + 196, panelX + 334, panelY + 218, 0xFF000000 | this.color);
      this.button(panelX + 344, panelY + 196, 72, 22, "Color", false);
      if (this.editing != null) {
         this.button(panelX + 266, panelY + 224, 112, 24, "Copy Waypoint", false);
      }

      this.button(panelX + 390, panelY + 224, 60, 24, this.editing == null ? "Create" : "Save", true);
      this.name.drawTextBox();
      this.group.drawTextBox();
      this.x.drawTextBox();
      this.y.drawTextBox();
      this.z.drawTextBox();
      this.endUi();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   @Override
   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      this.name.textboxKeyTyped(typedChar, keyCode);
      this.group.textboxKeyTyped(typedChar, keyCode);
      this.x.textboxKeyTyped(typedChar, keyCode);
      this.y.textboxKeyTyped(typedChar, keyCode);
      this.z.textboxKeyTyped(typedChar, keyCode);
   }

   @Override
   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      int ux = this.uiMouseX(mouseX);
      int uy = this.uiMouseY(mouseY);
      this.name.mouseClicked(ux, uy, mouseButton);
      this.group.mouseClicked(ux, uy, mouseButton);
      this.x.mouseClicked(ux, uy, mouseButton);
      this.y.mouseClicked(ux, uy, mouseButton);
      this.z.mouseClicked(ux, uy, mouseButton);
      int panelX = 240;
      int panelY = 140;
      if (this.inside(ux, uy, panelX + 450, panelY + 12, 20, 20)) {
         this.mc.displayGuiScreen(null);
      }

      if (this.inside(ux, uy, panelX + 310, panelY + 124, 140, 22)) {
         this.cycleDimension();
      }

      if (this.inside(ux, uy, panelX + 28, panelY + 196, 120, 14)) {
         this.showText = !this.showText;
      }

      if (this.inside(ux, uy, panelX + 170, panelY + 196, 140, 14)) {
         this.showDistance = !this.showDistance;
      }

      if (this.inside(ux, uy, panelX + 28, panelY + 220, 130, 14)) {
         this.directionalOnly = !this.directionalOnly;
      }

      if (this.inside(ux, uy, panelX + 344, panelY + 196, 72, 22)) {
         this.cycleColor();
      }

      if (this.editing != null && this.inside(ux, uy, panelX + 266, panelY + 224, 112, 24)) {
         this.copyWaypoint();
      }

      if (this.inside(ux, uy, panelX + 390, panelY + 224, 60, 24)) {
         this.createWaypoint();
      }
   }

   private void copyWaypoint() {
      try {
         WaypointsMod.WAYPOINTS.ensureContext(this.mc);
         Waypoint waypoint = this.buildWaypointPreview();
         GuiScreen.setClipboardString(WaypointShare.createMessage(waypoint));
         if (this.mc.thePlayer != null) {
            this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[Waypoints] " + EnumChatFormatting.WHITE + "Waypoint copied to clipboard."));
         }
      } catch (NumberFormatException ignored) {
         this.name.setText("Invalid coordinates");
      }
   }

   private void createWaypoint() {
      if (this.mc.thePlayer != null) {
         try {
            Waypoint waypoint = this.editing == null ? this.buildWaypointPreview() : this.editing;
            Waypoint preview = this.buildWaypointPreview();
            waypoint.name = preview.name;
            waypoint.x = preview.x;
            waypoint.y = preview.y;
            waypoint.z = preview.z;
            waypoint.dimension = preview.dimension;
            waypoint.group = preview.group;
            waypoint.color = preview.color;
            waypoint.showText = this.showText;
            waypoint.showDistance = this.showDistance;
            waypoint.directionalOnly = this.directionalOnly;
            if (this.editing == null) {
               WaypointsMod.WAYPOINTS.add(waypoint);
            } else {
               WaypointsMod.WAYPOINTS.changed();
            }

            this.mc.displayGuiScreen(new GuiWaypointList());
         } catch (NumberFormatException ignored) {
            this.name.setText("Invalid coordinates");
         }
      }
   }

   private Waypoint buildWaypointPreview() {
      String waypointName = this.name.getText().trim();
      if (waypointName.isEmpty()) {
         waypointName = "Waypoint";
      }

      String owner = this.editing != null ? this.editing.owner : this.mc.thePlayer.getName();
      Waypoint waypoint = new Waypoint(
         waypointName,
         owner,
         Double.parseDouble(this.x.getText()),
         Double.parseDouble(this.y.getText()),
         Double.parseDouble(this.z.getText()),
         this.dimension,
         this.group.getText().trim().isEmpty() ? "Default" : this.group.getText().trim(),
         this.color,
         this.editing != null && this.editing.shared
      );
      waypoint.showText = this.showText;
      waypoint.showDistance = this.showDistance;
      waypoint.directionalOnly = this.directionalOnly;
      return waypoint;
   }

   private void cycleDimension() {
      if (this.dimension == 0) {
         this.dimension = -1;
      } else if (this.dimension == -1) {
         this.dimension = 1;
      } else {
         this.dimension = 0;
      }
   }

   private void cycleColor() {
      int[] colors = new int[]{16734885, 5635962, 5949695, 16769370, 16739179};

      for (int i = 0; i < colors.length; i++) {
         if (colors[i] == this.color) {
            this.color = colors[(i + 1) % colors.length];
            return;
         }
      }

      this.color = colors[0];
   }

   private String getDimensionName() {
      if (this.dimension == -1) {
         return "Nether";
      }

      return this.dimension == 1 ? "End" : "Overworld";
   }

   private void label(String text, int left, int top) {
      this.drawString(this.fontRendererObj, text, left, top, 13093329);
   }

   private void fieldBack(GuiTextField field) {
      drawRect(field.xPosition - 1, field.yPosition - 1, field.xPosition + field.width + 1, field.yPosition + field.height + 1, -14342094);
   }

   private void button(int left, int top, int buttonWidth, int buttonHeight, String text, boolean green) {
      drawRect(left, top, left + buttonWidth, top + buttonHeight, green ? -15963347 : -14342094);
      int textColor = green ? 5046140 : 14014431;
      this.drawCenteredString(this.fontRendererObj, text, left + buttonWidth / 2, top + 7, textColor);
   }

   private void checkbox(int left, int top, String text, boolean checked) {
      drawRect(left, top, left + 12, top + 12, checked ? -9406844 : -14342094);
      if (checked) {
         this.drawString(this.fontRendererObj, "v", left + 3, top + 2, 16777215);
      }

      this.drawString(this.fontRendererObj, text, left + 18, top + 2, 13619931);
   }

   private boolean inside(int mouseX, int mouseY, int left, int top, int buttonWidth, int buttonHeight) {
      return mouseX >= left && mouseY >= top && mouseX < left + buttonWidth && mouseY < top + buttonHeight;
   }

   private int menuColor(int rgb) {
      return (WaypointsMod.SETTINGS.menuBackgroundAlpha & 0xFF) << 24 | rgb;
   }
}
