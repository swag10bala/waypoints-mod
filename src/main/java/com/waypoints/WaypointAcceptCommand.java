package com.waypoints;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class WaypointAcceptCommand extends CommandBase {
   @Override
   public String getCommandName() {
      return "waypointshared";
   }

   @Override
   public String getCommandUsage(ICommandSender sender) {
      return "/waypointshared <id>";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public void processCommand(ICommandSender sender, String[] args) {
      if (args.length > 0) {
         SharedWaypointActions.openConfirm(args[0]);
      }
   }
}
