package com.github.coegho.lingvo.api;

import com.github.coegho.lingvo.Lingvo;
import com.github.coegho.lingvo.config.LangsYAMLFile;
import com.github.coegho.lingvo.config.LanguageLinkNode;
import com.github.coegho.lingvo.config.LanguageYAMLFile;
import com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException;
import com.github.coegho.lingvo.exceptions.LangsFileException;
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
     * @throws com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException
     */
    public Translator(JavaPlugin plugin, 
            String langsFilePath, 
            String defaultLang, 
            Lingvo lingvo)
            throws DefaultLanguageNotFoundException {
        this.plugin = plugin;
        this.lingvo = lingvo;
        this.defaultLang = defaultLang;
        this.customLanguageFiles = false;
        this.languages = new HashMap<>();
        
        HashMap<String, String> paths;
        LangsYAMLFile langsFile;
        LanguageYAMLFile languageFileAux;
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
                //Prevent duplicates; name > alias
                if(!path2object.containsKey(paths.get(langName))) {
                    path2object.put(paths.get(langName), new LanguageYAMLFile(plugin, paths.get(langName)));
                }
            }
            
        } catch (LangsFileException ex) {
            lingvo.getServer().getLogger().log(Level.WARNING, ex.getMessage());
            lingvo.getServer().getLogger().log(Level.INFO, "Linking YAML language files by their name directly");
            //languages hashMap is empty
        }
        
        //must have default language
        if(!languages.containsKey(defaultLang)) {
            languageFileAux = new LanguageYAMLFile(plugin, defaultLang + ".yml");
            if(languageFileAux.getFile().exists()) {
                languages.put(defaultLang, languageFileAux);
            }
            else {
                throw new DefaultLanguageNotFoundException(defaultLang);
            }
        }
        
    }
    
    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param lingvo
     * @param languages
     * @throws com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException
     */
    public Translator(JavaPlugin plugin, 
            String langsFilePath, 
            String defaultLang, 
            Lingvo lingvo,
            HashMap<String, ILanguageFile> languages)
            throws DefaultLanguageNotFoundException {
        this.plugin = plugin;
        this.lingvo = lingvo;
        this.defaultLang = defaultLang;
        this.customLanguageFiles = true;
        this.languages = languages;
        if(!languages.containsKey(defaultLang)) {
            throw new DefaultLanguageNotFoundException(defaultLang);
        }
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
        ILanguageFile langFile = null;
        
        if(languages.containsKey(lang)) {
            if(languages.get(lang) != null) {
                langFile = languages.get(lang);
            }
        }
        else {
            notListedLang = new LanguageYAMLFile(plugin, lang + ".yml");
            if(notListedLang.getFile().exists()) {
                languages.put(lang, notListedLang);
                langFile = notListedLang;
            }
            else {
                languages.put(lang, null);
            }
        }
        if(langFile != null) {
            return langFile.getTranslation(key);
        }
        //Tries to use the default language
        
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