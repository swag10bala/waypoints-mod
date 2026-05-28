package com.waypoints;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "waypoints", name = "Waypoints", version = "1.0.1", clientSideOnly = true, useMetadata = true)
public class WaypointsMod {
   public static final String MODID = "waypoints";
   public static final WaypointManager WAYPOINTS = new WaypointManager();
   public static final WaypointSettings SETTINGS = new WaypointSettings();

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      WAYPOINTS.load(Minecraft.getMinecraft());
      SETTINGS.load(Minecraft.getMinecraft());
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      WaypointKeyHandler keys = new WaypointKeyHandler();
      WaypointRenderer renderer = new WaypointRenderer();
      WaypointChatHandler chat = new WaypointChatHandler();
      MinecraftForge.EVENT_BUS.register(renderer);
      MinecraftForge.EVENT_BUS.register(chat);
      FMLCommonHandler.instance().bus().register(keys);
      ClientCommandHandler.instance.registerCommand(new WaypointAcceptCommand());
      keys.register();
   }
}
