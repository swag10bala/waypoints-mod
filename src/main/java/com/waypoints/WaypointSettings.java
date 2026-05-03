package com.waypoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;

public class WaypointSettings {
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   public float labelScale = 1.0F;
   public int labelBoxPadding = 8;
   public int labelBackgroundAlpha = 170;
   public int menuBackgroundAlpha = 153;
   private File file;

   public void load(Minecraft minecraft) {
      File dir = new File(minecraft.mcDataDir, "config/waypoints");
      if (!dir.exists()) {
         dir.mkdirs();
      }

      this.file = new File(dir, "settings.json");
      if (!this.file.exists()) {
         this.save();
      } else {
         try (FileReader reader = new FileReader(this.file)) {
            WaypointSettings loaded = (WaypointSettings)GSON.fromJson(reader, WaypointSettings.class);
            if (loaded != null) {
               this.labelScale = this.clamp(loaded.labelScale, 0.5F, 3.0F);
               this.labelBoxPadding = (int)this.clamp(loaded.labelBoxPadding, 2.0F, 24.0F);
               this.labelBackgroundAlpha = (int)this.clamp(loaded.labelBackgroundAlpha, 0.0F, 255.0F);
               this.menuBackgroundAlpha = (int)this.clamp(loaded.menuBackgroundAlpha, 0.0F, 255.0F);
            }
         } catch (Exception e) {
            System.err.println("[Waypoints] Failed to load settings");
            e.printStackTrace();
         }
      }
   }

   public void save() {
      if (this.file != null) {
         try (FileWriter writer = new FileWriter(this.file)) {
            GSON.toJson(this, WaypointSettings.class, writer);
         } catch (Exception e) {
            System.err.println("[Waypoints] Failed to save settings");
            e.printStackTrace();
         }
      }
   }

   public void changeLabelScale(float amount) {
      this.setLabelScale(this.labelScale + amount);
   }

   public void setLabelScale(float value) {
      this.labelScale = this.clamp(value, 0.5F, 3.0F);
      this.save();
   }

   public void changeLabelBoxPadding(int amount) {
      this.setLabelBoxPadding(this.labelBoxPadding + amount);
   }

   public void setLabelBoxPadding(int value) {
      this.labelBoxPadding = (int)this.clamp(value, 2.0F, 24.0F);
      this.save();
   }

   public void changeLabelBackgroundAlpha(int amount) {
      this.setLabelBackgroundAlpha(this.labelBackgroundAlpha + amount);
   }

   public void setLabelBackgroundAlpha(int value) {
      this.labelBackgroundAlpha = (int)this.clamp(value, 0.0F, 255.0F);
      this.save();
   }

   public void setMenuBackgroundAlpha(int value) {
      this.menuBackgroundAlpha = (int)this.clamp(value, 0.0F, 255.0F);
      this.save();
   }

   private float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }
}
