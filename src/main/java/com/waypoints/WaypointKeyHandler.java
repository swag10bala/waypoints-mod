package com.waypoints;

import com.waypoints.gui.GuiCreateWaypoint;
import com.waypoints.gui.GuiWaypointList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class WaypointKeyHandler {
   private final KeyBinding createWaypoint = new KeyBinding("key.waypoints.create", 49, "key.categories.waypoints");
   private final KeyBinding listWaypoints = new KeyBinding("key.waypoints.list", 50, "key.categories.waypoints");

   public void register() {
      ClientRegistry.registerKeyBinding(this.createWaypoint);
      ClientRegistry.registerKeyBinding(this.listWaypoints);
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         Minecraft minecraft = Minecraft.getMinecraft();
         if (minecraft.thePlayer != null && minecraft.theWorld != null && minecraft.currentScreen == null) {
            WaypointsMod.WAYPOINTS.ensureContext(minecraft);
            if (this.createWaypoint.isPressed()) {
               minecraft.displayGuiScreen(new GuiCreateWaypoint());
            }

            if (this.listWaypoints.isPressed()) {
               minecraft.displayGuiScreen(new GuiWaypointList());
            }
         }
      }
   }
}
