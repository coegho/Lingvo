package com.github.coegho.lingvo;

import com.github.coegho.lingvo.commands.LingvoCommandExecutor;
import com.github.coegho.lingvo.config.UserLangDataFile;
import com.github.coegho.lingvo.api.ILanguageFile;
import com.github.coegho.lingvo.api.ITranslator;
import com.github.coegho.lingvo.api.Translator;
import com.github.coegho.lingvo.config.GenericYAMLFile;
import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;
/**
 *
 * @author coegho
 */
public class Lingvo extends JavaPlugin {
    private UserLangData userLangData = null;
    private GenericYAMLFile config;
    private UserLangDataFile users;
    private HashMap<String, ITranslator> translators;
    private ITranslator selfTranslator;

    /**
     *
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = new GenericYAMLFile(this, this.getConfig().getName());
        users = new UserLangDataFile(this);
        userLangData = new UserLangData(getConfig().getString("default-language"));
        translators = new HashMap<>();
        selfTranslator = getTranslator(this, "langs.yml", getUserLangData().getDefaultLang());
        getCommand("lingvo").setExecutor(new LingvoCommandExecutor(this));
    }
    
    /**
     *
     */
    @Override
    public void onDisable() {
        super.onDisable();
        users.saveUserLangData(getUserLangData());
        config = null;
        userLangData = null;
    }

    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @return
     */
    public ITranslator getTranslator(JavaPlugin plugin,
                                    String langsFilePath,
                                    String defaultLang) {
        if(translators.containsKey(plugin.getName())) {
            return translators.get(plugin.getName());
        }
        else {
            ITranslator translator = new Translator(plugin, langsFilePath, defaultLang, this);
            translators.put(plugin.getName(), translator);
            return translator;
        }
    }
    
    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param languages
     * @return
     */
    public ITranslator getTranslator(JavaPlugin plugin,
                                    String langsFilePath,
                                    String defaultLang,
                                    HashMap<String, ILanguageFile> languages) {
        if(translators.containsKey(plugin.getName())) {
            return translators.get(plugin.getName());
        }
        else {
            ITranslator translator = new Translator(plugin, langsFilePath, defaultLang, this, languages);
            translators.put(plugin.getName(), translator);
            return translator;
        }
    }
    
    /**
     *
     * @param plugin
     * @param translator
     */
    public void setTranslator(JavaPlugin plugin, ITranslator translator) {
        translators.put(plugin.getName(), translator);
    }
    
    /**
     *
     * @return
     */
    public UserLangData getUserLangData() {
        return userLangData;
    }

    /**
     *
     * @return
     */
    public ITranslator getSelfTranslator() {
        return selfTranslator;
    }
}