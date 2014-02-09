/*
 */

package com.github.coegho.lingvo.config;

import com.github.coegho.lingvo.Lingvo;
import com.github.coegho.lingvo.UserLangData;

/**
 *
 * @author coegho
 */
public class UserLangDataFile extends GenericYAMLFile {

    public UserLangDataFile(Lingvo plugin) {
        super(plugin, "users.yml");
    }
    
    /**
     *
     * @param data
     */
    public void saveUserLangData(UserLangData data) {
        getData().createSection("users", data.getLanguages());
        saveData();
    }
}
