package manaki.randomredirect.command;

import manaki.randomredirect.RandomRedirect;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PluginCommand extends Command {

    private RandomRedirect plugin;

    public PluginCommand(RandomRedirect plugin) {
        super("rtpadmin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/rtpadmin reload");
        }

        else if (args[0].equalsIgnoreCase("reload")) {
            this.plugin.getConfig().reload();
            sender.sendMessage("Config reloaded!");
        }
    }

}
