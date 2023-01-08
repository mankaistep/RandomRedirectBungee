package manaki.randomredirect;

import manaki.randomredirect.command.PluginCommand;
import manaki.randomredirect.config.PluginConfig;
import manaki.randomredirect.request.Request;
import manaki.randomredirect.utils.PluginUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RandomRedirect extends Plugin implements Listener {

    public final String CHANNEL = "manaki:randomredirect";

    private PluginConfig config;
    private PluginCommand command;

    public PluginConfig getConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        // Register channel
        this.getProxy().registerChannel(CHANNEL);
        this.getLogger().info("Channel " + CHANNEL + " registered!");

        // Event listener register
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        // Load config
        this.config = new PluginConfig(this);
        this.command = new PluginCommand(this);

        // Register command
        ProxyServer.getInstance().getPluginManager().registerCommand(this, this.command);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {
        if (!e.getTag().equals(CHANNEL)) return;

        var in = new DataInputStream(new ByteArrayInputStream(e.getData()));
        var type = in.readUTF();
        var data = in.readUTF();

        if (type.equalsIgnoreCase("redirect")) {
            var request = Request.parse(data);
            var player = ProxyServer.getInstance().getPlayer(request.getPlayer());

            var randomList = new ArrayList<>(config.getRedirectableServers());
            randomList.remove(ProxyServer.getInstance().getPlayer(player.getName()).getServer().getInfo().getName());

            var toServer = PluginUtils.getRandom(randomList);
            player.connect(ProxyServer.getInstance().getServerInfo(toServer));
            this.getLogger().info("Redirected " + player + " to " + toServer);
        }
    }

}
