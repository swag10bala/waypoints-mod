package com.waypoints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WaypointShare {
   private static final Pattern SAFE_SHARE_PATTERN = Pattern.compile(
      "\\[wp;(-?\\d+);(-?\\d+);(-?\\d+);(-?\\d+);(\\d+);(\\d+);(\\d+)\\]"
   );
   private static final Pattern LEGACY_SHARE_PATTERN = Pattern.compile(
      "(-?\\d+)\\.(-?\\d+)\\.(-?\\d+)\\.(-?\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)\\.w"
   );
   private static final Pattern OWNER_PATTERN = Pattern.compile("\\(([^)]+)\\)");

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
      Waypoint waypoint = tryParseSafe(message);
      return waypoint != null ? waypoint : tryParseLegacy(message);
   }

   public static String stripMarker(String message) {
      return message;
   }

   private static String clean(String value) {
      return clean(value, "Waypoint");
   }

   private static String clean(String value, String fallback) {
      return value != null && !value.trim().isEmpty() ? value.replace("(", "").replace(")", "").replace(",", "").trim() : fallback;
   }

   private static Waypoint tryParseSafe(String message) {
      Matcher matcher = SAFE_SHARE_PATTERN.matcher(message);
      if (!matcher.find()) {
         return null;
      }

      try {
         SharePrefix prefix = extractPrefix(message, matcher.start());
         return buildWaypoint(
            prefix,
            matcher.group(1),
            matcher.group(2),
            matcher.group(3),
            matcher.group(4),
            matcher.group(5),
            matcher.group(6),
            matcher.group(7)
         );
      } catch (Exception ignored) {
         return null;
      }
   }

   private static Waypoint tryParseLegacy(String message) {
      Matcher matcher = LEGACY_SHARE_PATTERN.matcher(message);
      if (!matcher.find()) {
         return null;
      }

      try {
         SharePrefix prefix = extractPrefix(message, matcher.start());
         return buildWaypoint(
            prefix,
            matcher.group(1),
            matcher.group(2),
            matcher.group(3),
            matcher.group(4),
            matcher.group(5),
            matcher.group(6),
            matcher.group(7)
         );
      } catch (Exception ignored) {
         return null;
      }
   }

   private static Waypoint buildWaypoint(
      SharePrefix prefix,
      String x,
      String y,
      String z,
      String dimension,
      String color,
      String flagsValue,
      String contextCodeValue
   ) {
      int flags = Integer.parseInt(flagsValue);
      int contextCode = Integer.parseInt(contextCodeValue);
      if (contextCode != WaypointsMod.WAYPOINTS.getActiveContextCode()) {
         return null;
      }

      Waypoint waypoint = new Waypoint(
         prefix.name,
         prefix.owner,
         Double.parseDouble(x),
         Double.parseDouble(y),
         Double.parseDouble(z),
         Integer.parseInt(dimension),
         "Shared",
         Integer.parseInt(color),
         true
      );
      waypoint.showText = (flags & 1) != 0;
      waypoint.showDistance = (flags & 4) != 0;
      waypoint.directionalOnly = (flags & 8) != 0;
      return waypoint;
   }

   private static SharePrefix extractPrefix(String message, int markerStart) {
      String prefix = message.substring(0, markerStart).trim();
      Matcher matcher = OWNER_PATTERN.matcher(prefix);
      String owner = "Unknown";
      int ownerEnd = -1;

      while (matcher.find()) {
         owner = clean(matcher.group(1), "Unknown");
         ownerEnd = matcher.end();
      }

      String name = ownerEnd >= 0 && ownerEnd < prefix.length() ? prefix.substring(ownerEnd) : prefix;
      name = name.replaceAll("^[\\s,:>\\-]+", "").replaceAll("[\\s,:>\\-]+$", "").trim();
      name = clean(name, "Waypoint");
      return new SharePrefix(owner, name);
   }

   private static final class SharePrefix {
      private final String owner;
      private final String name;

      private SharePrefix(String owner, String name) {
         this.owner = owner;
         this.name = name;
      }
   }
}
