package manaki.randomredirect.utils;

import java.util.List;
import java.util.Random;

public class PluginUtils {

    public static String getRandom(List<String> servers) {
        return servers.get(new Random().nextInt(servers.size()));
    }

}
