/*
 */

package com.github.coegho.lingvo.exceptions;

/**
 *
 * @author coegho
 */
public class LangsFileNotFoundException extends LangsFileException {

    /**
     *
     * @param fileName
     */
    public LangsFileNotFoundException(String fileName) {
        super("Bad format for Langs file: " + fileName);
    }
}
