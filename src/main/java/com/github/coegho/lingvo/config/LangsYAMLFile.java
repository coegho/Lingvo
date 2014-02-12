package com.github.coegho.lingvo.config;

import com.github.coegho.lingvo.translator.LanguageLinkNode;
import com.github.coegho.lingvo.exceptions.LangsFileBadFormatException;
import com.github.coegho.lingvo.exceptions.LangsFileNotFoundException;
import java.util.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author coegho
 */
public class LangsYAMLFile extends GenericYAMLFile {

    public LangsYAMLFile(JavaPlugin plugin, String dataFileName) throws LangsFileNotFoundException {
        super(plugin, dataFileName);
        if(plugin.getResource(dataFileName) == null) {
            throw new LangsFileNotFoundException(dataFileName);
        }
    }
    
    /**
     *
     * @return
     */
    public HashMap<String, LanguageLinkNode> getLanguageLinkNodes() throws LangsFileBadFormatException {
        HashMap<String, LanguageLinkNode> map = new HashMap<>();
        ConfigurationSection langSection;
        LanguageLinkNode lang;
        List<String> aliases;
        String path;
        
        ConfigurationSection langs = getData().getConfigurationSection("languages");
        if(langs == null) {
            throw new LangsFileBadFormatException(this.dataFileName);
        }
        for(String key : langs.getKeys(false)) { //read the different languages
            langSection = langs.getConfigurationSection(key);
            
            aliases = langSection.getStringList("aliases");
            path = langSection.getString("path");
            
            if(path == null) {
                throw new LangsFileBadFormatException(this.dataFileName);
            }
            
            lang = new LanguageLinkNode(key, aliases, path);
            
            map.put(key, lang);
        }
        return map;
    }
}