package com.github.coegho.lingvo.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author coegho
 */
public class GenericYAMLFile {
    protected JavaPlugin plugin;
    protected String dataFileName;
    protected FileConfiguration data;
    protected File dataFile;
    protected boolean exists;

    public GenericYAMLFile(JavaPlugin plugin, String dataFileName) {
        this.plugin = plugin;
        this.dataFileName = dataFileName;
        getData().options().copyDefaults(true);
    }
    
    public void reloadData() {
        if (dataFile == null) {
            dataFile = new File(plugin.getDataFolder(), dataFileName);
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
     
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(dataFileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            data.setDefaults(defConfig);
        }
        exists = (defConfigStream != null) || dataFile.exists();
    }
    public final FileConfiguration getData() {
        if (data == null) {
            this.reloadData();
        }
        return data;
    }
    public void saveData() {
        if (data == null || dataFile == null) {
        return;
        }
        try {
            getData().save(dataFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save data to " + dataFile, ex);
        }
    }
    public void saveDefaultData() {
        if (dataFile == null) {
            dataFile = new File(plugin.getDataFolder(), dataFileName);
        }
        if (!dataFile.exists()) {
             plugin.saveResource(dataFileName, false);
         }
    }
    
    public boolean exists() {
        return exists;
    }
}
