package com.waypoints;

public class Waypoint {
   public String name;
   public String owner;
   public double x;
   public double y;
   public double z;
   public int dimension;
   public String group;
   public int color;
   public boolean showText;
   public boolean showDistance;
   public boolean directionalOnly;
   public boolean shared;
   public Boolean visible;

   public Waypoint() {
   }

   public Waypoint(String name, String owner, double x, double y, double z, int dimension, String group, int color, boolean shared) {
      this.name = name;
      this.owner = owner;
      this.x = x;
      this.y = y;
      this.z = z;
      this.dimension = dimension;
      this.group = group;
      this.color = color;
      this.showText = true;
      this.showDistance = true;
      this.directionalOnly = false;
      this.shared = shared;
      this.visible = true;
   }

   public boolean isVisible() {
      return this.visible == null || this.visible;
   }

   public String getDimensionName() {
      if (this.dimension == -1) {
         return "Nether";
      } else {
         return this.dimension == 1 ? "End" : "Overworld";
      }
   }
}
