package com.github.coegho.lingvo.config;

import java.util.List;

/**
 *
 * @author coegho
 */
public class LanguageLinkNode {
    private String name;
    private List<String> aliases;
    private String path;
    
    public LanguageLinkNode(String name, List<String> aliases, String path) {
        this.name = name;
        this.aliases = aliases;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
