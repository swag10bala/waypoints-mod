package com.waypoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class WaypointManager {
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final Type WAYPOINT_LIST = (new TypeToken<List<Waypoint>>() {}).getType();
   private final List<Waypoint> waypoints = new ArrayList<>();
   private File file;
   private String activeContext = "";

   public void load(Minecraft minecraft) {
      File dir = new File(minecraft.mcDataDir, "config/waypoints");
      if (!dir.exists()) {
         dir.mkdirs();
      }

      this.activeContext = this.resolveContext(minecraft);
      this.file = new File(dir, this.sanitize(this.activeContext) + ".json");
      if (!this.file.exists()) {
         this.save();
      } else {
         try (FileReader reader = new FileReader(this.file)) {
            List<Waypoint> loaded = (List<Waypoint>)GSON.fromJson(reader, WAYPOINT_LIST);
            this.waypoints.clear();
            if (loaded != null) {
               this.waypoints.addAll(loaded);
               this.normalize();
            }
         } catch (Exception e) {
            System.err.println("[Waypoints] Failed to load waypoints");
            e.printStackTrace();
         }
      }
   }

   public void ensureContext(Minecraft minecraft) {
      String context = this.resolveContext(minecraft);
      if (!context.equals(this.activeContext)) {
         this.load(minecraft);
      }
   }

   public void save() {
      if (this.file != null) {
         try (FileWriter writer = new FileWriter(this.file)) {
            GSON.toJson(this.waypoints, WAYPOINT_LIST, writer);
         } catch (Exception e) {
            System.err.println("[Waypoints] Failed to save waypoints");
            e.printStackTrace();
         }
      }
   }

   public List<Waypoint> all() {
      return Collections.unmodifiableList(this.waypoints);
   }

   public void add(Waypoint waypoint) {
      if (waypoint.visible == null) {
         waypoint.visible = true;
      }

      this.waypoints.add(waypoint);
      this.save();
   }

   public void remove(Waypoint waypoint) {
      this.waypoints.remove(waypoint);
      this.save();
   }

   public void changed() {
      this.save();
   }

   public int getActiveContextCode() {
      return Math.abs(this.activeContext.hashCode());
   }

   public void setAllVisible(boolean visible) {
      for (Waypoint waypoint : this.waypoints) {
         waypoint.visible = visible;
      }

      this.save();
   }

   public boolean hasVisibleWaypoints() {
      for (Waypoint waypoint : this.waypoints) {
         if (waypoint.isVisible()) {
            return true;
         }
      }

      return false;
   }

   public boolean containsShared(Waypoint waypoint) {
      for (Waypoint existing : this.waypoints) {
         if (existing.shared
            && existing.owner.equalsIgnoreCase(waypoint.owner)
            && Math.round(existing.x) == Math.round(waypoint.x)
            && Math.round(existing.y) == Math.round(waypoint.y)
            && Math.round(existing.z) == Math.round(waypoint.z)
            && existing.dimension == waypoint.dimension) {
            return true;
         }
      }

      return false;
   }

   private String resolveContext(Minecraft minecraft) {
      if (minecraft == null) {
         return "global";
      }

      try {
         ServerData server = minecraft.getCurrentServerData();
         if (server != null && server.serverIP != null && !server.serverIP.trim().isEmpty()) {
            return "server_" + server.serverIP.trim().toLowerCase();
         }

         if (minecraft.isSingleplayer() && minecraft.getIntegratedServer() != null) {
            return "world_" + minecraft.getIntegratedServer().getFolderName();
         }
      } catch (Exception ignored) {
      }

      return "global";
   }

   private String sanitize(String value) {
      return value.replaceAll("[^a-zA-Z0-9._-]", "_");
   }

   private void normalize() {
      for (Waypoint waypoint : this.waypoints) {
         if (waypoint.visible == null) {
            waypoint.visible = true;
         }
      }
   }
}
