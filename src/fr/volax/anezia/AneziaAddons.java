package fr.volax.anezia;

import fr.volax.anezia.commands.*;
import fr.volax.anezia.crafts.HammerCraft;
import fr.volax.anezia.crafts.NewCrafts;
import fr.volax.anezia.events.*;
import fr.volax.anezia.events.EventListener;
import fr.volax.anezia.hooks.HookUtils;
import fr.volax.anezia.hooks.MetricsLite;
import fr.volax.anezia.utils.*;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public class AneziaAddons extends JavaPlugin {
    private final ConfigValues configValues = new ConfigValues(this);
    private final Utils utils = new Utils(this);
    private HookUtils hookUtils;
    private static AneziaAddons instance;
    public static Economy economy = null;
    public static Permission permission = null;

    public HashMap<UUID, Boolean> safemode = new HashMap<>();
    public HashMap<UUID, Boolean> getSafeMode() {
        return this.safemode;
    }
    public HashMap<UUID, Long> poisonSword;
    public HashMap<UUID, Long> repair;

    private ItemStack item;
    private int maxAmount;
    private String useMessage;

    public static Logger log;
    public CHlistener listener = new CHlistener(this);
    public HLRCommandMain HLRCM = new HLRCommandMain(this);
    public HLRSubCommand HLRSC = new HLRSubCommand(this);
    public static String ver;
    public static String CHname;
    public static List<String> hopperlore = new ArrayList<>();
    public static List<String> enabledWorlds;
    public ConfigHandler configHandler;
    String[] commandAliases = new String[] { "chopper" };

    @Override
    public void onEnable() {
        this.hookUtils = new HookUtils(this);
        instance = this;
        saveDefaultConfig();
        this.reloadConfig();
        this.poisonSword = new HashMap<>();

        HammerCraft.setOldestCraft();
        new MetricsLite(this);
        NewCrafts.getInstance().crafts();

        this.item = new ItemBuilder(Material.WATCH, 1).setName(l("§c§l✸ §6§lUnclaim Finder §c§l✸")).setLore("§7§m------------------------------------", "§ePermet de voir les coffres", "§edans un rayon de §6§l100 §eblocs.", "§cClic droit !", "§7§m------------------------------------").toItemStack();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.item);
        NBTTagCompound itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        itemCompound.set("wacfinder-durability", new NBTTagInt(getConfig().getInt("durability")));
        nmsItem.setTag(itemCompound);
        this.item = CraftItemStack.asBukkitCopy(nmsItem);
        this.maxAmount = getConfig().getInt("max-amount");
        this.useMessage = l(getConfig().getString("use-message"));
        this.safemode = new HashMap<>();
        this.repair = new HashMap<>();

        setupPermissions();
        CustomConfig.setup();
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();
        for (String s : CustomConfig.get().getKeys(false)) {
            UUID uuid = UUID.fromString(s);
            this.safemode.put(uuid, CustomConfig.get().getBoolean(s));
        }

        log = getLogger();
        this.configHandler = new ConfigHandler(this);
        this.configHandler.initDataFile();
        this.configHandler.getConfigValues();
        this.configHandler.initConfigValues();
        this.configHandler.initMsgFile();
        ver = getServer().getVersion().split("MC: ")[1].replaceAll("\\)", "");

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerEvents(this), this);
        pluginManager.registerEvents(new OtherEvents(this), this);
        pluginManager.registerEvents(new EauListener(), this);
        pluginManager.registerEvents(new LaveListener(), this);
        pluginManager.registerEvents(new EventListener(this), this);
        pluginManager.registerEvents(new DynamiteEvents(), this);
        pluginManager.registerEvents(new OrbeEvents(this), this);
        pluginManager.registerEvents(new SellWandListener(this), this);
        pluginManager.registerEvents(new FinderUseEvents(this), this);
        pluginManager.registerEvents(new HarvestHoeListeners(this), this);
        pluginManager.registerEvents(new PoisonSwordListeners(this), this);
        (new SellWandListener(this)).setupItems();
        pluginManager.registerEvents(this.listener, this);
        pluginManager.registerEvents(new ChestViewerListeners(this), this);
        pluginManager.registerEvents(new KillListener(), this);

        ChunkBusterCommand chunkBusterCommand = new ChunkBusterCommand(this);
        this.getCommand("chunkbuster").setExecutor(chunkBusterCommand);
        this.getCommand("chunkbuster").setTabCompleter(chunkBusterCommand);
        this.getCommand("bhammer").setExecutor(new CommandList(this));
        this.getCommand("creepereau").setExecutor(new CreeperEauCommand());
        this.getCommand("creeperlave").setExecutor(new CreeperLaveCommand());
        this.getCommand("givedynamite").setExecutor(new DynamiteCommand());
        this.getCommand("orbespeed").setExecutor(new OrbeCommand());
        this.getCommand("orberesi").setExecutor(new OrbeCommand());
        this.getCommand("orbeforce").setExecutor(new OrbeCommand());
        this.getCommand("orbefall").setExecutor(new OrbeCommand());
        this.getCommand("sellwand").setExecutor(new SellWandCmd(this));
        this.getCommand("giveunclaimfinder").setExecutor(new FinderCommand(this.item));
        this.getCommand("hh").setExecutor(new HarvestHoeCommand());
        this.getCommand("givepoisonsword").setExecutor(new GivePoisonSwordCommand());
        this.getCommand("giveend").setExecutor(new GiveEndCommand());
        this.getCommand("givechestviewer").setExecutor(new GiveChestViewerCommand());
        this.getCommand("converthopper").setExecutor(this.HLRCM);
        this.getCommand("converthopper").setAliases(Arrays.asList(this.commandAliases));
        this.getCommand("hlr").setExecutor(this.HLRSC);
        this.getCommand("chestviewer").setExecutor(new ChestViewerCommand(this));
        this.getCommand("repair").setExecutor(new RepairCommand());
    }

    @Override
    public void onDisable() {
        CustomConfig.setup();
        for (Map.Entry<UUID, Boolean> safe : this.safemode.entrySet())
            CustomConfig.get().set((safe.getKey()).toString(), safe.getValue());
        CustomConfig.save();
    }

    public Utils getUtils() {
        return this.utils;
    }
    public ConfigValues getConfigValues() {
        return this.configValues;
    }
    public HookUtils getHookUtils() {
        return hookUtils;
    }
    public static AneziaAddons getInstance() {
        return instance;
    }
    private String l(String s) {
        return s.replace("&", "§");
    }
    public int getMaxAmount() {
        return this.maxAmount;
    }
    public ItemStack getItem() {
        return this.item;
    }
    public String getUseMessage() {
        return this.useMessage;
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
        if (eco != null) economy = eco.getProvider();
        return (economy != null);
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) permission = permissionProvider.getProvider();
        return (permission != null);
    }

    public ArrayList<String> getLore(String s) {
        ArrayList<String> lore = (ArrayList<String>)instance.getConfig().getList(s);
        for (int i = 0; i < lore.size(); i = i + 1) {
            String str = lore.get(i);
            lore.set(i, str.replace("&", "§"));
        }
        return lore;
    }

    public boolean isEnabledIn(String world) {
        return enabledWorlds.contains(world);
    }
    public static double getMoney(Player p) {
        return economy.getBalance(p);
    }
}
