/*
 */

package com.github.coegho.lingvo.exceptions;

/**
 *
 * @author coegho
 */
public class DefaultLanguageNotFoundException extends Exception {
    String defaultLang;
    public DefaultLanguageNotFoundException(String defaultLang) {
        super("Cannot find default language: " + defaultLang);
        this.defaultLang = defaultLang;
    }
}
