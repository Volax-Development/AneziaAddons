package fr.volax.anezia;

import fr.volax.anezia.commands.*;
import fr.volax.anezia.crafts.HammerCraft;
import fr.volax.anezia.crafts.NewCrafts;
import fr.volax.anezia.events.*;
import fr.volax.anezia.hooks.HookUtils;
import fr.volax.anezia.hooks.MetricsLite;
import fr.volax.anezia.utils.ConfigValues;
import fr.volax.anezia.utils.ItemBuilder;
import fr.volax.anezia.utils.Utils;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AneziaAddons extends JavaPlugin {
    private ConfigValues configValues = new ConfigValues(this);
    private Utils utils = new Utils(this);
    private HookUtils hookUtils;
    private static AneziaAddons instance;

    private ItemStack item;
    private int maxAmount;
    private String useMessage;

    @Override
    public void onEnable() {
        this.hookUtils = new HookUtils(this);
        instance = this;
        saveDefaultConfig();
        this.reloadConfig();

        HammerCraft.setOldestCraft();
        new MetricsLite(this);
        NewCrafts.getInstance().crafts();

        this.item = new ItemBuilder(Material.getMaterial(getConfig().getInt("item.material")), 1, (short)getConfig().getInt("item.data")).setName(l(getConfig().getString("item.name"))).setLore(getConfig().getStringList("item.lore")).toItemStack();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.item);
        NBTTagCompound itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        itemCompound.set("wacfinder-durability", new NBTTagInt(getConfig().getInt("durability")));
        nmsItem.setTag(itemCompound);
        this.item = CraftItemStack.asBukkitCopy(nmsItem);
        this.maxAmount = getConfig().getInt("max-amount");
        this.useMessage = l(getConfig().getString("use-message"));


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

        (new SellWandListener(this)).setupItems();

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
}
