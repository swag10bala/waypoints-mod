package com.waypoints;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaypointChatHandler {
   @SubscribeEvent
   public void onChat(ClientChatReceivedEvent event) {
      Minecraft minecraft = Minecraft.getMinecraft();
      WaypointsMod.WAYPOINTS.ensureContext(minecraft);
      String text = event.message.getUnformattedText();
      Waypoint waypoint = WaypointShare.tryParse(text);
      if (waypoint != null) {
         String id = SharedWaypointActions.remember(waypoint);
         ChatStyle hoverStyle = new ChatStyle()
            .setChatHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ChatComponentText("Click to review this shared waypoint")))
            .setChatClickEvent(new ClickEvent(net.minecraft.event.ClickEvent.Action.RUN_COMMAND, "/waypointshared " + id));
         ChatComponentText replacement = new ChatComponentText(EnumChatFormatting.GOLD + "A waypoint was shared by " + waypoint.owner);
         replacement.setChatStyle(hoverStyle);
         event.message = replacement;
      }
   }
}
