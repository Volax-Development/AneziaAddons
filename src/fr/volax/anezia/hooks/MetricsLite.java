package fr.volax.anezia.hooks;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MetricsLite {
    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private boolean enabled;
    private static boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static String serverUUID;
    private final Plugin plugin;

    public MetricsLite(Plugin plugin) {
        if (plugin == null)
            throw new IllegalArgumentException("Plugin cannot be null!");
        this.plugin = plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", Boolean.valueOf(true));
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", Boolean.valueOf(false));
            config.addDefault("logSentData", Boolean.valueOf(false));
            config.addDefault("logResponseStatusText", Boolean.valueOf(false));
            config.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)")

                    .copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException iOException) {
            }
        }
        serverUUID = config.getString("serverUuid");
        logFailedRequests = config.getBoolean("logFailedRequests", false);
        this.enabled = config.getBoolean("enabled", true);
        if (this.enabled) {
            boolean found = false;
            for (Class<?> service : (Iterable<Class<?>>) Bukkit.getServicesManager().getKnownServices()) {
                try {
                    service.getField("B_STATS_VERSION");
                    found = true;
                    break;
                } catch (NoSuchFieldException noSuchFieldException) {
                }
            }
            Bukkit.getServicesManager().register(MetricsLite.class, this, plugin, ServicePriority.Normal);
            if (!found)
                startSubmitting();
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!MetricsLite.this.plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                Bukkit.getScheduler().runTask(MetricsLite.this.plugin, MetricsLite.this::submitData);
            }
        }, 300000L, 1800000L);
    }

    public JSONObject getPluginData() {
        JSONObject data = new JSONObject();
        String pluginName = this.plugin.getDescription().getName();
        String pluginVersion = this.plugin.getDescription().getVersion();
        data.put("pluginName", pluginName);
        data.put("pluginVersion", pluginVersion);
        JSONArray customCharts = new JSONArray();
        data.put("customCharts", customCharts);
        return data;
    }

    private JSONObject getServerData() {
        int playerAmount = Bukkit.getOnlinePlayers().size();
        int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        String bukkitVersion = Bukkit.getVersion();
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();
        JSONObject data = new JSONObject();
        data.put("serverUUID", serverUUID);
        data.put("playerAmount", Integer.valueOf(playerAmount));
        data.put("onlineMode", Integer.valueOf(onlineMode));
        data.put("bukkitVersion", bukkitVersion);
        data.put("javaVersion", javaVersion);
        data.put("osName", osName);
        data.put("osArch", osArch);
        data.put("osVersion", osVersion);
        data.put("coreCount", Integer.valueOf(coreCount));
        return data;
    }

    private void submitData() {
        JSONObject data = getServerData();
        JSONArray pluginData = new JSONArray();
        for (Class<?> service : (Iterable<Class<?>>) Bukkit.getServicesManager().getKnownServices()) {
            try {
                service.getField("B_STATS_VERSION");
                for (RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(service)) {
                    try {
                        pluginData.add(provider.getService().getMethod("getPluginData", new Class[0]).invoke(provider.getProvider(), new Object[0]));
                    } catch (NullPointerException | NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException nullPointerException) {
                    }
                }
            } catch (NoSuchFieldException noSuchFieldException) {
            }
        }
        data.put("plugins", pluginData);
        (new Thread(() -> {
            try {
                sendData(this.plugin, data);
            } catch (Exception e) {
                if (logFailedRequests)
                    this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), e);
            }
        })).start();
    }

    private static void sendData(Plugin plugin, JSONObject data) throws Exception {
        if (data == null)
            throw new IllegalArgumentException("Data cannot be null!");
        if (Bukkit.isPrimaryThread())
            throw new IllegalAccessException("This method must not be called from the main thread!");
        if (logSentData)
            plugin.getLogger().info("Sending data to bStats: " + data.toString());
        HttpsURLConnection connection = (HttpsURLConnection) (new URL("https://bStats.org/submitData/bukkit")).openConnection();
        byte[] compressedData = compress(data.toString());
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "MC-Server/1");
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(compressedData);
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
            builder.append(line);
        bufferedReader.close();
        if (logResponseStatusText)
            plugin.getLogger().info("Sent data to bStats and received response: " + builder.toString());
    }

    private static byte[] compress(String str) throws IOException {
        if (str == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.close();
        return outputStream.toByteArray();
    }
}