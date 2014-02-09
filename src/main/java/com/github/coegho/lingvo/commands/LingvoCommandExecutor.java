package com.github.coegho.lingvo.commands;

import com.github.coegho.lingvo.Lingvo;
import com.github.coegho.lingvo.UserLangData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author coegho
 */
public class LingvoCommandExecutor implements CommandExecutor {

    Lingvo plugin = null;
    UserLangData userLangData = null;
    
    public LingvoCommandExecutor(Lingvo plugin) {
        this.plugin = plugin;
        userLangData = plugin.getUserLangData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String message;
        if(args.length > 1) {
            return false;
        }
        else if(args.length == 0) {
            message = plugin.getSelfTranslator().translate(sender.getName(), "messages.your-lang");
            sender.sendMessage(String.format(message, userLangData.getLanguage(sender.getName())));
        }
        else {
            userLangData.setLanguage(sender.getName(), args[0]);
            message = plugin.getSelfTranslator().translate(sender.getName(), "messages.lang-changed");
            sender.sendMessage(String.format(message, args[0]));
        }
        return true;
    }

}
