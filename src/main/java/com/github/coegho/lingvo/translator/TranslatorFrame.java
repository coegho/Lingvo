package com.github.coegho.lingvo.translator;

import com.github.coegho.lingvo.Lingvo;
import com.github.coegho.lingvo.api.ILangsLoader;
import com.github.coegho.lingvo.api.ITranslator;
import com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException;
import org.bukkit.plugin.java.JavaPlugin;


    
/**
 *
 * @author coegho
 */
public class TranslatorFrame implements ITranslator {
    private ILangsLoader langsLoader;
    private JavaPlugin plugin;
    private Lingvo lingvo;
    private String defaultLang;
    
    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param lingvo
     * @throws com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException
     */
    public TranslatorFrame(JavaPlugin plugin, 
            String langsFilePath, 
            String defaultLang, 
            Lingvo lingvo)
            throws DefaultLanguageNotFoundException {
        this.plugin = plugin;
        this.lingvo = lingvo;
        this.defaultLang = defaultLang;
        
        langsLoader = new LangsYAMLLoader(plugin, langsFilePath, lingvo);
        
        //must have default language
        if(!langsLoader.languageFileExists(defaultLang)) {
            throw new DefaultLanguageNotFoundException(defaultLang);
        }       
    }
    

    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param lingvo
     * @param langsLoader
     * @throws DefaultLanguageNotFoundException
     */
    public TranslatorFrame(JavaPlugin plugin, 
            String langsFilePath, 
            String defaultLang, 
            Lingvo lingvo,
            ILangsLoader langsLoader)
            throws DefaultLanguageNotFoundException {
        this.plugin = plugin;
        this.lingvo = lingvo;
        this.defaultLang = defaultLang;
        this.langsLoader = langsLoader;
        //must have default language
        if(!langsLoader.languageFileExists(defaultLang)) {
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
        
        if(langsLoader.languageFileExists(lang)) {
            return langsLoader.getLanguageFile(lang).getTranslation(key);
        }
        else {
            return langsLoader.getLanguageFile(defaultLang).getTranslation(key);
        } 
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getDefaultLang() {
        return defaultLang;
    }
    
    public void setDefaultLang(String lang) {
        this.defaultLang = lang;
    }
}