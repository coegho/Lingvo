/*
 */

package com.github.coegho.lingvo.api;

import com.github.coegho.lingvo.exceptions.DefaultLanguageNotFoundException;

/**
 *
 * @author coegho
 */
public interface ITranslator {
    public String translate(String playerName, String key);
}
