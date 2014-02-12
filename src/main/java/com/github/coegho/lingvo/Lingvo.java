package com.github.coegho.lingvo;

import com.github.coegho.lingvo.api.ILangsLoader;
import com.github.coegho.lingvo.api.ITranslator;
import com.github.coegho.lingvo.commands.LingvoCommandExecutor;
import com.github.coegho.lingvo.config.GenericYAMLFile;
import com.github.coegho.lingvo.config.UserLangDataFile;
import com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException;
import com.github.coegho.lingvo.translator.TranslatorFrame;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
/**
 *
 * @author coegho
 */
public class Lingvo extends JavaPlugin {
    private UserLangData userLangData = null;
    private UserLangDataFile users;
    private HashMap<String, ITranslator> translators;
    private ITranslator selfTranslator;

    /**
     *
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        users = new UserLangDataFile(this);
        userLangData = new UserLangData(getConfig().getString("default-language"));
        translators = new HashMap<>();
        getCommand("lingvo").setExecutor(new LingvoCommandExecutor(this));
        try {
            selfTranslator = generateTranslator(this, "langs.yml", getUserLangData().getDefaultLang());
        } catch (DefaultLanguageNotFoundException ex) {
            this.getServer().getLogger().log(Level.SEVERE, ex.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    /**
     *
     */
    @Override
    public void onDisable() {
        super.onDisable();
        users.saveUserLangData(getUserLangData());
        userLangData = null;
    }

    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @return
     * @throws com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException
     */
    public ITranslator generateTranslator(JavaPlugin plugin,
                                    String langsFilePath,
                                    String defaultLang)
            throws DefaultLanguageNotFoundException {

        ITranslator translator = new TranslatorFrame(plugin, langsFilePath, defaultLang, this);
        translators.put(plugin.getName(), translator);
        return translator;
    }

    /**
     *
     * @param plugin
     * @param langsFilePath
     * @param defaultLang
     * @param langsLoader
     * @return
     * @throws DefaultLanguageNotFoundException
     */
    public ITranslator generateTranslator(JavaPlugin plugin,
                                    String langsFilePath,
                                    String defaultLang,
                                    ILangsLoader langsLoader)
            throws DefaultLanguageNotFoundException {

        ITranslator translator = new TranslatorFrame(plugin, langsFilePath, defaultLang, this, langsLoader);
        translators.put(plugin.getName(), translator);
        return translator;

    }
    
    /**
     *
     * @param plugin
     * @return
     */
    public ITranslator getTranslator(JavaPlugin plugin) {
        return translators.get(plugin.getName());
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
