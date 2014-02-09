package com.github.coegho.lingvo;

import java.util.HashMap;

/**
 *
 * @author coegho
 */
public class UserLangData {
    private final HashMap<String, String> userLanguages;
    private String defaultLang;

    /**
     *
     * @param defaultLang
     */
    public UserLangData(String defaultLang) {
        userLanguages = new HashMap<>();
        this.defaultLang = defaultLang;
    }

    public void setLanguage(String name, String language) {
        userLanguages.put(name, language);  //TODO: add to users.yml file
    }
    
    public String getLanguage(String playerName) {
        if(!userLanguages.containsKey(playerName)) {
            userLanguages.put(playerName, defaultLang);
        }
        return userLanguages.get(playerName);
    }
    
    public HashMap<String, String> getLanguages() {
        return (HashMap<String, String>) userLanguages.clone();
    }

    /**
     *
     * @return
     */
    public String getDefaultLang() {
        return defaultLang;
    }
    
    /**
     *
     * @param defaultLang
     */
    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }
}
