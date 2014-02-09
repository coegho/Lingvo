package com.github.coegho.lingvo.api;

import com.github.coegho.lingvo.Lingvo;
import com.github.coegho.lingvo.config.LangsYAMLFile;
import com.github.coegho.lingvo.config.LanguageLinkNode;
import com.github.coegho.lingvo.config.LanguageYAMLFile;
import com.github.coegho.lingvo.exceptions.LangsFileException;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;


    
/**
 *
 * @author coegho
 */
public class Translator implements ITranslator {

    private final JavaPlugin plugin;
    private final Lingvo lingvo;
    private final HashMap<String, ILanguageFile> languages;
    private final String defaultLang;
    private final boolean customLanguageFiles;
    
    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param lingvo
     */
    public Translator(JavaPlugin plugin, 
            String langsFilePath, 
            String defaultLang, 
            Lingvo lingvo) {
        this.plugin = plugin;
        this.lingvo = lingvo;
        this.defaultLang = defaultLang;
        this.customLanguageFiles = false;
        this.languages = new HashMap<>();
        
        HashMap<String, String> paths;
        LangsYAMLFile langsFile;
        HashMap<String, LanguageLinkNode> languageLinkNodes;
        
        paths = new HashMap<>();
        try {
            langsFile = new LangsYAMLFile(plugin, langsFilePath);
            languageLinkNodes = langsFile.getLanguageLinkNodes();
            
            for(String langName : languageLinkNodes.keySet()) {
                paths.put(langName, languageLinkNodes.get(langName).getPath());

                for(String alias : languageLinkNodes.get(langName).getAliases()) {
                    if(!paths.containsKey(alias)) {
                        paths.put(alias, languageLinkNodes.get(langName).getPath());
                    }
                }
            }

            HashMap<String, LanguageYAMLFile> path2object = new HashMap<>();
            for(String langName : paths.keySet()) {
                if(!path2object.containsKey(paths.get(langName))) {
                    path2object.put(paths.get(langName), new LanguageYAMLFile(plugin, paths.get(langName)));
                }
                languages.put(langName, path2object.get(paths.get(langName)));
            }
            
        } catch (LangsFileException ex) {
            lingvo.getServer().getLogger().log(Level.WARNING, ex.getMessage());
            lingvo.getServer().getLogger().log(Level.INFO, "Linking YAML language files by their name directly");
            //languages hashMap is empty
        }
        

        
    }
    
    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param lingvo
     * @param languages
     */
    public Translator(JavaPlugin plugin, 
            String langsFilePath, 
            String defaultLang, 
            Lingvo lingvo,
            HashMap<String, ILanguageFile> languages) {
        this.plugin = plugin;
        this.lingvo = lingvo;
        this.defaultLang = defaultLang;
        this.customLanguageFiles = true;
        this.languages = languages;
    }

    /**
     *
     * @param playerName
     * @param key
     * @return
     */
    @Override
    public String translate(String playerName, String key) {
        String lang = lingvo.getUserLangData().getLanguage(playerName);
        LanguageYAMLFile notListedLang;
        if(languages.containsKey(lang)) {
            if(languages.get(lang) != null) {
                return languages.get(lang).getTranslation(key);
            }
        }
        else {
            notListedLang = new LanguageYAMLFile(plugin, lang + ".yml");
            if(notListedLang.getFile().exists()) {
                languages.put(lang, notListedLang);
                return notListedLang.getTranslation(key);
            }
            else {
                languages.put(lang, null);
            }
        }
        return languages.get(defaultLang).getTranslation(key);
    }

    /**
     *
     * @return
     */
    public boolean areCustomLanguageFiles() {
        return customLanguageFiles;
    }
}