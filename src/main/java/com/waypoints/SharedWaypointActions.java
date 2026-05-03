package com.waypoints;

import com.waypoints.gui.GuiConfirmSharedWaypoint;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;

public final class SharedWaypointActions {
   private static final Map<String, Waypoint> PENDING = new HashMap<>();
   private static int nextId = 1;

   private SharedWaypointActions() {
   }

   public static String remember(Waypoint waypoint) {
      String id = String.valueOf(nextId++);
      PENDING.put(id, waypoint);
      return id;
   }

   public static void openConfirm(String id) {
      Waypoint waypoint = PENDING.get(id);
      if (waypoint != null) {
         Minecraft.getMinecraft().displayGuiScreen(new GuiConfirmSharedWaypoint(waypoint));
      }
   }
}
