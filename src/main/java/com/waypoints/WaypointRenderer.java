package com.waypoints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaypointRenderer {
   private final Minecraft mc = Minecraft.getMinecraft();
   private static final double MAX_LABEL_DISTANCE = 72.0;

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         WaypointsMod.WAYPOINTS.ensureContext(this.mc);
         Entity view = this.mc.getRenderViewEntity();
         if (view != null) {
            double viewX = view.lastTickPosX + (view.posX - view.lastTickPosX) * event.partialTicks;
            double viewY = view.lastTickPosY + (view.posY - view.lastTickPosY) * event.partialTicks;
            double viewZ = view.lastTickPosZ + (view.posZ - view.lastTickPosZ) * event.partialTicks;

            for (Waypoint waypoint : WaypointsMod.WAYPOINTS.all()) {
               if (waypoint.isVisible()
                  && waypoint.dimension == this.mc.thePlayer.dimension
                  && this.isInFront(view, waypoint)
                  && (!waypoint.directionalOnly || this.isCenteredEnough(view, waypoint))) {
                  this.renderWaypoint(waypoint, viewX, viewY, viewZ);
               }
            }
         }
      }
   }

   private void renderWaypoint(Waypoint waypoint, double viewX, double viewY, double viewZ) {
      double actualX = waypoint.x + 0.5 - viewX;
      double actualY = waypoint.y + 1.4 - viewY;
      double actualZ = waypoint.z + 0.5 - viewZ;
      double distance = this.mc.thePlayer.getDistance(waypoint.x, waypoint.y, waypoint.z);
      double labelDistance = Math.max(0.001, Math.sqrt(actualX * actualX + actualY * actualY + actualZ * actualZ));
      double renderDistance = Math.min(labelDistance, MAX_LABEL_DISTANCE);
      double x = actualX / labelDistance * renderDistance;
      double y = actualY / labelDistance * renderDistance;
      double z = actualZ / labelDistance * renderDistance;
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(this.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
      double scaleDistance = Math.min(labelDistance, 100.0);
      float scale = WaypointsMod.SETTINGS.labelScale * (float)Math.max(0.03, scaleDistance * 0.0018 * Math.min(1.8, 1.0 + scaleDistance / 180.0));
      GlStateManager.scale(-scale, -scale, scale);
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      String text = waypoint.showText ? waypoint.name : "";
      if (waypoint.showDistance) {
         text = text.isEmpty() ? Math.round(distance) + "m" : text + " [" + Math.round(distance) + "m]";
      }

      FontRenderer font = this.mc.fontRendererObj;
      String sharedPrefix = waypoint.shared ? "(C) " : "";
      int prefixWidth = font.getStringWidth(sharedPrefix);
      int textWidth = prefixWidth + font.getStringWidth(text);
      int padding = WaypointsMod.SETTINGS.labelBoxPadding;
      int boxWidth = textWidth + 26 + padding * 2;
      int left = -boxWidth / 2;
      int top = -14;
      int alpha = (WaypointsMod.SETTINGS.labelBackgroundAlpha & 0xFF) << 24;
      Gui.drawRect(left, top, left + boxWidth, top + 18 + padding / 2, alpha | 1053209);
      Gui.drawRect(left, top + 17, left + boxWidth, top + 18, 0xFF000000 | waypoint.color);
      this.drawHead(waypoint.owner, left + padding / 2, top + 3);
      int textX = left + 20 + padding;
      if (waypoint.shared) {
         font.drawString(sharedPrefix, textX, top + 5, 5635962);
         textX += prefixWidth;
      }

      font.drawString(text, textX, top + 5, 16777215);
      GlStateManager.enableDepth();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   private void drawHead(String playerName, int x, int y) {
      ResourceLocation skin = null;
      if (this.mc.getNetHandler() != null) {
         for (NetworkPlayerInfo info : this.mc.getNetHandler().getPlayerInfoMap()) {
            if (info.getGameProfile().getName().equalsIgnoreCase(playerName)) {
               skin = info.getLocationSkin();
               break;
            }
         }
      }

      if (skin == null && this.mc.thePlayer != null && this.mc.thePlayer.getName().equalsIgnoreCase(playerName)) {
         skin = this.mc.thePlayer.getLocationSkin();
      }

      if (skin == null) {
         skin = AbstractClientPlayer.getLocationSkin(playerName);
         this.mc.getTextureManager().loadTexture(skin, AbstractClientPlayer.getDownloadImageSkin(skin, playerName));
      }

      this.mc.getTextureManager().bindTexture(skin);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, 14, 14, 64.0F, 64.0F);
   }

   private boolean isInFront(Entity view, Waypoint waypoint) {
      Vec3 look = this.getCameraLook();
      Vec3 toWaypoint = new Vec3(waypoint.x - view.posX, waypoint.y - view.posY, waypoint.z - view.posZ).normalize();
      return look.dotProduct(toWaypoint) > 0.05;
   }

   private boolean isCenteredEnough(Entity view, Waypoint waypoint) {
      Vec3 look = this.getCameraLook();
      Vec3 toWaypoint = new Vec3(waypoint.x - view.posX, waypoint.y - view.posY, waypoint.z - view.posZ).normalize();
      return look.dotProduct(toWaypoint) > 0.35;
   }

   private Vec3 getCameraLook() {
      float pitch = this.mc.getRenderManager().playerViewX;
      float yaw = this.mc.getRenderManager().playerViewY;
      float cosYaw = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
      float sinYaw = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
      float cosPitch = -MathHelper.cos(-pitch * 0.017453292F);
      float sinPitch = MathHelper.sin(-pitch * 0.017453292F);
      return new Vec3((double)(sinYaw * cosPitch), (double)sinPitch, (double)(cosYaw * cosPitch));
   }
}
