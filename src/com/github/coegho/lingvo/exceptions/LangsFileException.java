/*
 */

package com.github.coegho.lingvo.exceptions;

/**
 *
 * @author coegho
 */
public abstract class LangsFileException extends Exception {
    private final String fileName;
    public LangsFileException(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }
}
