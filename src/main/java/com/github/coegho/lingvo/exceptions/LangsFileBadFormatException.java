/*
 */

package com.github.coegho.lingvo.exceptions;

/**
 *
 * @author coegho
 */
public class LangsFileBadFormatException extends LangsFileException {
    public LangsFileBadFormatException(String fileName) {
        super("Bad format for Langs file: " + fileName);
    }
}
