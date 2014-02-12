package com.github.coegho.lingvo.api;

import java.util.HashMap;

/**
 *
 * @author coegho
 */
public interface ILangsLoader {
    
    /**
     *
     * @param language
     * @return
     */
    public ILanguageFile getLanguageFile(String language);
    
    /**
     *
     * @param language
     * @param langFile
     */
    public void setLanguageFile(String language, ILanguageFile langFile);

    /**
     *
     * @param defaultLang
     * @return
     */
    public boolean languageFileExists(String defaultLang);
}
