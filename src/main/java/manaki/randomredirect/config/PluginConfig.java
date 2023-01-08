package manaki.randomredirect.config;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class PluginConfig {

    private Plugin plugin;
    private Configuration config;

    private List<String> redirectableServers;

    public PluginConfig(Plugin plugin) {
        this.plugin = plugin;
        try {
            this.makeConfig();
            this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        this.reload();
    }

    public Configuration getConfig() {
        return config;
    }

    public List<String> getRedirectableServers() {
        return redirectableServers;
    }

    public void reload() {
        this.redirectableServers = config.getStringList("redirectable-servers")
                .stream()
                .filter(sv ->  {
                    if (ProxyServer.getInstance().getServers().containsKey(sv)) return true;
                    else {
                        plugin.getLogger().warning("Server " + sv + " doesn't exist!");
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public void makeConfig() throws IOException {
        // Create plugin config folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created config folder: " + plugin.getDataFolder().mkdir());
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");

        // Copy default config if it doesn't exist
        if (!configFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(configFile); // Throws IOException
            InputStream in = plugin.getResourceAsStream("config.yml"); // This file must exist in the jar resources folder
            in.transferTo(outputStream); // Throws IOException
        }
    }

}
