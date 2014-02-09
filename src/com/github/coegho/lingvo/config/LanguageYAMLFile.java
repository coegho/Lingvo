package com.github.coegho.lingvo.config;

import com.github.coegho.lingvo.api.ILanguageFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author coegho
 */
public class LanguageYAMLFile extends GenericYAMLFile implements ILanguageFile {

    /**
     *
     * @param plugin
     * @param dataFileName
     */
    public LanguageYAMLFile(JavaPlugin plugin, String dataFileName) {
        super(plugin, dataFileName);
    }

    @Override
    public String getTranslation(String key) {
        return this.getData().getString(key);
    }
}
