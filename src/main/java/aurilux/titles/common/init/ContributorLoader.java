package aurilux.titles.common.init;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.Titles;
import net.minecraft.item.EnumRarity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ContributorLoader {
    private volatile static Map<String, TitleInfo> contributorTitles = new HashMap<>();

    public static void init() {
        new ThreadContributorLoader();
    }

    public static TitleInfo getContributorTitle(String contributorName) {
        return contributorTitles.get(contributorName);
    }

    public static boolean contributorTitleExists(String contributorName) {
        return contributorTitles.containsKey(contributorName);
    }

    private static void load(Properties props) {
        for(String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            contributorTitles.put(key, new TitleInfo(value, EnumRarity.EPIC));
        }
    }

    private static class ThreadContributorLoader extends Thread {
        private ThreadContributorLoader() {
            setName("Titles Contributor Loader");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Aurilux/Titles/master/contributors.properties");
                Properties props = new Properties();
                InputStreamReader reader = new InputStreamReader(url.openStream());
                props.load(reader);
                load(props);
            }
            catch (IOException e) {
                Titles.console("Unable to load contributors list. Either you're offline or github is down.");
            }
        }
    }
}
