package com.waypoints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WaypointShare {
   private static final Pattern SAFE_SHARE_PATTERN = Pattern.compile(
      "\\(([^)]+)\\)\\s+(.+?),\\s+\\[wp;(-?\\d+);(-?\\d+);(-?\\d+);(-?\\d+);(\\d+);(\\d+);(\\d+)\\]"
   );
   private static final Pattern LEGACY_SHARE_PATTERN = Pattern.compile(
      "\\(([^)]+)\\)\\s+(.+?),\\s+(-?\\d+)\\.(-?\\d+)\\.(-?\\d+)\\.(-?\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)\\.w"
   );

   private WaypointShare() {
   }

   public static String createMessage(Waypoint waypoint) {
      return "(" + clean(waypoint.owner) + ") " + clean(waypoint.name) + ", [wp;" + encode(waypoint) + "]";
   }

   public static String encode(Waypoint waypoint) {
      int flags = 0;
      if (waypoint.showText) {
         flags |= 1;
      }

      if (waypoint.showDistance) {
         flags |= 4;
      }

      if (waypoint.directionalOnly) {
         flags |= 8;
      }

      return Math.round(waypoint.x)
         + ";"
         + Math.round(waypoint.y)
         + ";"
         + Math.round(waypoint.z)
         + ";"
         + waypoint.dimension
         + ";"
         + (waypoint.color & 16777215)
         + ";"
         + flags
         + ";"
         + WaypointsMod.WAYPOINTS.getActiveContextCode();
   }

   public static Waypoint tryParse(String message) {
      Waypoint waypoint = tryParse(message, SAFE_SHARE_PATTERN);
      return waypoint != null ? waypoint : tryParse(message, LEGACY_SHARE_PATTERN);
   }

   public static String stripMarker(String message) {
      return message;
   }

   private static String clean(String value) {
      return value != null && !value.trim().isEmpty() ? value.replace("(", "").replace(")", "").replace(",", "").trim() : "Waypoint";
   }

   private static Waypoint tryParse(String message, Pattern pattern) {
      Matcher matcher = pattern.matcher(message);
      if (!matcher.find()) {
         return null;
      }

      try {
         int flags = Integer.parseInt(matcher.group(8));
         int contextCode = Integer.parseInt(matcher.group(9));
         if (contextCode != WaypointsMod.WAYPOINTS.getActiveContextCode()) {
            return null;
         }

         Waypoint waypoint = new Waypoint(
            matcher.group(2).trim(),
            matcher.group(1).trim(),
            Double.parseDouble(matcher.group(3)),
            Double.parseDouble(matcher.group(4)),
            Double.parseDouble(matcher.group(5)),
            Integer.parseInt(matcher.group(6)),
            "Shared",
            Integer.parseInt(matcher.group(7)),
            true
         );
         waypoint.showText = (flags & 1) != 0;
         waypoint.showDistance = (flags & 4) != 0;
         waypoint.directionalOnly = (flags & 8) != 0;
         return waypoint;
      } catch (Exception ignored) {
         return null;
      }
   }
}
