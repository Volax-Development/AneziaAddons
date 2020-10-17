package fr.volax.anezia.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Logger;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.events.CHlistener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ConfigHandler {
    private AneziaAddons plugin;
    private FileConfiguration config;
    private File configFile;
    public double fee;
    public boolean useEco;
    public boolean useCrops;
    public boolean useMobDrops;
    public boolean usePerms;
    public static long time;
    public boolean cooldown;
    public static boolean retainTweak = true;
    public static boolean greedy;
    public static String prefix;
    public static HashMap<String, String> msgMap = new HashMap<>();
    public List<String> customitems;
    public String tempname;
    public List<String> hopperlore;
    public int maxamount;
    public static int chunkHopperLimit;
    public static ArrayList<ItemStack> itemList = new ArrayList<>();
    public static boolean pistonAllow;
    public static List<String> strPistonBlacklist;
    public static boolean waterAllow;
    public static ArrayList<Material> waterBlacklist = new ArrayList<>();
    public static boolean spawnerAllow;
    List<String> strSpawnerWhitelist;
    public static ArrayList<EntityType> spawnerWhitelist = new ArrayList<>();
    public static boolean cactusAllow;
    public static Logger log = AneziaAddons.log;

    public ConfigHandler(AneziaAddons instance) {
        this.plugin = instance;
        this.config = this.plugin.getConfig();
        createConfig();
    }

    public void createConfig() {
        File configFile = new File(this.plugin.getDataFolder() + File.separator +
                "config.yml");
        if (!configFile.exists()) {
            AneziaAddons.log.info("Cannot find config.yml, Generating now....");
            this.plugin.saveDefaultConfig();
            AneziaAddons.log.info("Config generated!");
        }
    }

    public void initDataFile() {
        File DataFile = new File(this.plugin.getDataFolder() + File.separator +
                "Data.yml");
        if (!DataFile.exists()) {
            AneziaAddons.log.info("Cannot find Data.yml, Generating now....");
            try {
                DataFile.createNewFile();
                AneziaAddons.log.info("Data file generated!");
            } catch (IOException e) {
                AneziaAddons.log.severe("Cannot generate Data.yml, reason: " + e.getMessage());
            }
        } else {
            YamlConfiguration DFile = YamlConfiguration.loadConfiguration(DataFile);
            Iterator<String> i = DFile.getKeys(true).iterator();
            while (i.hasNext()) {
                String key = i.next();
                List<String> coords = DFile.getStringList(key);
                List<Location> buffer = new ArrayList<>();
                World world = Bukkit.getServer().getWorld(key);
                if (world == null) {
                    AneziaAddons.log.info("Could not load the world " + key + "! Skipping...");
                    continue;
                }
                int n;
                for (n = 0; n < coords.size(); n++) {
                    String[] c = ((String) coords.get(n)).split(",");
                    double x = Double.parseDouble(c[0]);
                    double y = Double.parseDouble(c[1]);
                    double z = Double.parseDouble(c[2]);
                    Location loc = new Location(world, x, y, z);
                    buffer.add(loc);
                }
                for (n = 0; n < buffer.size(); n++) {
                    Location loc = buffer.get(n);
                    Map.Entry<UUID, String> entry = new AbstractMap.SimpleEntry<>(world.getUID(), loc.getChunk().toString());
                    if (!CHlistener.blockInfo.containsKey(entry)) {
                        List<Location> list = new ArrayList<>();
                        list.add(loc);
                        CHlistener.blockInfo.put(entry, list);
                    } else {
                        CHlistener.blockInfo.get(entry).add(loc);
                    }
                }
            }
        }
    }

    public void reloadConfig(CommandSender sender, String message) {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
        getConfigValues();
        initConfigValues();
        initMsgFile();
        sender.sendMessage(message);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public void initMsgFile() {
        File MsgFile = new File(this.plugin.getDataFolder() + File.separator + "messages.yml");
        if (!MsgFile.exists()) {
            AneziaAddons.log.info("Cannot find messages.yml, Generating now....");
            try {
                MsgFile.createNewFile();
                InputStream defMsgFile = this.plugin.getResource("messages.yml");
                Files.copy(defMsgFile, Paths.get(MsgFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
                defMsgFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AneziaAddons.log.info("Message file generated!");
        }
        InputStream def = this.plugin.getResource("messages.yml");
        YamlConfiguration defcfg = YamlConfiguration.loadConfiguration(new InputStreamReader(def));
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(MsgFile);
        if (!defcfg.getKeys(true).equals(cfg.getKeys(true))) {
            AneziaAddons.log.warning("Message File's keys are not the same.");
            AneziaAddons.log.warning("This can mean that your configuration file is corrupted or was tempered with wrongly.");
            AneziaAddons.log.warning("Please reset or remove the message file in order for it to work properly.");
        }
        prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Prefix"));
        ConfigurationSection cfgSec = cfg.getConfigurationSection("Msgs");
        Map<String, Object> values = cfgSec.getValues(true);
        for (Map.Entry<String, Object> val : values.entrySet()) {
            try {
                MemorySection memorySection = (MemorySection) val.getValue();
            } catch (ClassCastException e) {
                String valStr = (String) val.getValue();
                String msg = ChatColor.translateAlternateColorCodes('&', valStr);
                msg = msg.replaceAll("%hoppername%", AneziaAddons.CHname).replaceAll("%fee%", String.valueOf(this.fee))
                        .replaceAll("%maxamount%", String.valueOf(this.maxamount)).replaceAll("%chunklimit%", String.valueOf(chunkHopperLimit));
                msgMap.put(val.getKey(), msg);
            }
        }
    }

    public void getConfigValues() {
        YamlConfiguration defcfg = YamlConfiguration.loadConfiguration(new InputStreamReader(this.plugin.getResource("config.yml")));
        if (!defcfg.getKeys(true).equals(this.config.getKeys(true))) {
            AneziaAddons.log.warning("Config File's keys are not the same.");
            AneziaAddons.log.warning("This can mean that your configuration file is corrupted or was tempered with wrongly.");
            AneziaAddons.log.warning("Please reset or remove the config file in order for it to work properly.");
        }
        AneziaAddons.enabledWorlds = this.config.getStringList("Enabled-in-worlds");
        this.useEco = this.config.getBoolean("Eco.Use");
        this.fee = this.config.getDouble("Eco.Conversion-fee");
        this.customitems = this.config.getStringList("ItemList.Custom-items");
        this.usePerms = this.config.getBoolean("Use-permissions");
        this.tempname = "§c§l✸ §6§lHopper De Farm §c§l✸";
        time = this.config.getLong("Cooldown.Seconds") * 20L;
        this.cooldown = this.config.getBoolean("Cooldown.Enable");
        this.hopperlore = Arrays.asList("§7§m---------------------------------", "§eCette hopper est très spécial.", "§eIl peux trouver des items spécial.", "§eEt les téléporter très rapidement à lui.", "§7§m---------------------------------");
        this.useMobDrops = this.config.getBoolean("ItemList.Mob-drops");
        this.useCrops = this.config.getBoolean("ItemList.Crops");
        this.maxamount = this.config.getInt("Max-amount");
        chunkHopperLimit = this.config.getInt("Chunk-HopperLimit");
        greedy = this.config.getBoolean("Greedy-mode");
        retainTweak = this.config.getBoolean("Retain-tweaked");
        pistonAllow = this.config.getBoolean("Farm-tweaks.Piston-farms.Allow");
        strPistonBlacklist = this.config.getStringList("Farm-tweaks.Piston-farms.Blacklist");
        waterAllow = this.config.getBoolean("Farm-tweaks.Water-farms.Allow");
        this.strSpawnerWhitelist = this.config.getStringList("Farm-tweaks.Spawners.Whitelist ");
        spawnerAllow = this.config.getBoolean("Farm-tweaks.Spawners.Allow");
        cactusAllow = this.config.getBoolean("Farm-tweaks.Cactus-farms");
    }

    public void initConfigValues() {
        if (this.useEco &&
                !this.plugin.setupEconomy()) {
            log.severe("Disabling due to no Vault dependency or valid economy plugin found!");
            this.plugin.getServer().getPluginManager().disablePlugin((Plugin) this.plugin);
            return;
        }
        AneziaAddons.CHname = ChatColor.translateAlternateColorCodes('&', this.tempname);
        if (!itemList.isEmpty())
            itemList.clear();
        if (this.useCrops) {
            itemList.add(new ItemStack(Material.PUMPKIN));
            itemList.add(new ItemStack(Material.CACTUS));
            itemList.add(new ItemStack(Material.WHEAT));
            itemList.add(new ItemStack(Material.CARROT_ITEM));
            itemList.add(new ItemStack(Material.SUGAR_CANE));
            itemList.add(new ItemStack(Material.MELON));
            itemList.add(new ItemStack(Material.SEEDS));
            itemList.add(new ItemStack(Material.POTATO_ITEM));
            itemList.add(new ItemStack(Material.POISONOUS_POTATO));
            itemList.add(new ItemStack(Material.RED_MUSHROOM));
            itemList.add(new ItemStack(Material.BROWN_MUSHROOM));
            itemList.add(new ItemStack(Material.NETHER_STALK));
        }
        if (this.useMobDrops) {
            itemList.add(new ItemStack(Material.FEATHER));
            itemList.add(new ItemStack(Material.RAW_CHICKEN));
            itemList.add(new ItemStack(Material.LEATHER));
            itemList.add(new ItemStack(Material.SPIDER_EYE));
            itemList.add(new ItemStack(Material.ENDER_PEARL));
            itemList.add(new ItemStack(Material.RAW_BEEF));
            itemList.add(new ItemStack(Material.PORK));
            itemList.add(new ItemStack(Material.SLIME_BALL));
            itemList.add(new ItemStack(Material.WOOL));
            itemList.add(new ItemStack(Material.ARROW));
            itemList.add(new ItemStack(Material.SULPHUR));
            itemList.add(new ItemStack(Material.GOLD_NUGGET));
            itemList.add(new ItemStack(Material.IRON_INGOT));
            itemList.add(new ItemStack(Material.MUTTON));
            itemList.add(new ItemStack(Material.BONE));
            itemList.add(new ItemStack(Material.INK_SACK));
            itemList.add(new ItemStack(Material.BLAZE_ROD));
            itemList.add(new ItemStack(Material.ROTTEN_FLESH));
            itemList.add(new ItemStack(Material.STRING));
            itemList.add(new ItemStack(Material.PRISMARINE_SHARD));
            itemList.add(new ItemStack(Material.PRISMARINE_CRYSTALS));
            itemList.add(new ItemStack(Material.RAW_FISH));
        }
        if (!this.customitems.isEmpty())
            for (String item : this.customitems) {
                String itemname = null, dv = null;
                boolean nodv = false;
                try {
                    itemname = item.split(":")[0];
                    dv = item.split(":")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    itemname = item;
                    nodv = true;
                }
                itemname = itemname.toUpperCase();
                Material material = Material.getMaterial(itemname);
                if (nodv) {
                    itemList.add(new ItemStack(material));
                } else {
                    itemList.add(new ItemStack(material, 1, Short.parseShort(dv)));
                }
            }
        if (!waterBlacklist.isEmpty())
            waterBlacklist.clear();
        waterBlacklist = initMaterialList(this.config.getStringList("Farm-tweaks.Water-farms.Blacklist"));
        if (!spawnerWhitelist.isEmpty())
            spawnerWhitelist.clear();
        int i;
        for (i = 0; i < this.strSpawnerWhitelist.size(); i++) {
            String strType = this.strSpawnerWhitelist.get(i);
            try {
                EntityType type = EntityType.valueOf(strType.trim());
                spawnerWhitelist.add(type);
            } catch (IllegalArgumentException e) {
                log.info("Unknown Entity type " + strType + "! Skipping...");
            }
        }
        if (!AneziaAddons.hopperlore.isEmpty())
            AneziaAddons.hopperlore.clear();
        if (!this.hopperlore.isEmpty())
            for (i = 0; i < this.hopperlore.size(); i++) {
                String lore = this.hopperlore.get(i);
                lore = ChatColor.translateAlternateColorCodes('&', lore);
                AneziaAddons.hopperlore.add(lore);
            }
    }

    private static ArrayList<Material> initMaterialList(List<String> input) {
        if (!input.isEmpty()) {
            ArrayList<Material> output = new ArrayList<>();
            for (String strMat : input) {
                Material mat = Material.getMaterial(strMat);
                output.add(mat);
            }
            return output;
        }
        return new ArrayList<>();
    }
}