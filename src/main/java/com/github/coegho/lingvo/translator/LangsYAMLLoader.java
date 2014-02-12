package com.github.coegho.lingvo.translator;

import com.github.coegho.lingvo.Lingvo;
import com.github.coegho.lingvo.api.ILangsLoader;
import com.github.coegho.lingvo.api.ILanguageFile;
import com.github.coegho.lingvo.config.LangsYAMLFile;
import com.github.coegho.lingvo.config.LanguageYAMLFile;
import com.github.coegho.lingvo.exceptions.LangsFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author coegho
 */
public final class LangsYAMLLoader implements ILangsLoader {
    Lingvo lingvo;
    JavaPlugin plugin;
    HashMap<String, ILanguageFile> languages;
    List<String> failed;

    public LangsYAMLLoader(JavaPlugin plugin, String langsFilePath, Lingvo lingvo) {
        this.lingvo = lingvo;
        this.plugin = plugin;
        this.languages = new HashMap<>();
        this.failed = new ArrayList<>();
        
        HashMap<String, String> paths;
        LangsYAMLFile langsFile;
        HashMap<String, LanguageLinkNode> languageLinkNodes;
        HashMap<String, LanguageYAMLFile> path2file = new HashMap<>();
        
        paths = new HashMap<>();
        try {
            langsFile = new LangsYAMLFile(plugin, langsFilePath);
            languageLinkNodes = langsFile.getLanguageLinkNodes();
            
            for(String langName : languageLinkNodes.keySet()) {
                paths.put(langName, languageLinkNodes.get(langName).getPath());
                plugin.getServer().getLogger().log(Level.CONFIG, "Found {0} on {1}", new Object[]{langName, paths.get(langName)});

                for(String alias : languageLinkNodes.get(langName).getAliases()) {
                    if(!paths.containsKey(alias)) { //name > alias
                        paths.put(alias, languageLinkNodes.get(langName).getPath());
                    }
                }
            }
            
            //save files from paths to prevent duplicate objects
            for(String langName : paths.keySet()) {
                if(!path2file.containsKey(paths.get(langName))) {
                    path2file.put(paths.get(langName), new LanguageYAMLFile(plugin, paths.get(langName)));
                }
            }
            
            //save language files
            for(String langName : paths.keySet()) {
                if(path2file.get(paths.get(langName)).exists()) {
                    languages.put(langName, path2file.get(paths.get(langName)));
                }
                else {
                    if(!failed.contains(langName)) {
                        lingvo.getServer().getLogger().log(Level.WARNING, "Cannot find {0} in {1}", new Object[]{langName, paths.get(langName)});
                    }
                    failed.add(langName);
                }
            }
            
        } catch (LangsFileException ex) {
            lingvo.getServer().getLogger().log(Level.WARNING, ex.getMessage());
            lingvo.getServer().getLogger().log(Level.INFO, "Linking YAML language files by their name directly");
            //languages hashMap is empty
        }
    }

    
    @Override
    public ILanguageFile getLanguageFile(String language) {
        if(languages.containsKey(language)) {
            return languages.get(language);
        }
        else if(failed.contains(language)) {
            return null;
        }
        else {
            LanguageYAMLFile notListedLang;
            notListedLang = new LanguageYAMLFile(plugin, language + ".yml");
            if(notListedLang.exists()) {
                languages.put(language, notListedLang);
                plugin.getServer().getLogger().log(Level.CONFIG, "Found {0} on {0}.yml", new Object[]{language});
                return notListedLang;
            }
            else {
                failed.add(language);
                return null;
            }
        }
    }

    @Override
    public void setLanguageFile(String language, ILanguageFile langFile) {
        languages.put(language, langFile);
    }

    @Override
    public boolean languageFileExists(String language) {
        return (getLanguageFile(language) != null);
        
    }
    
}
