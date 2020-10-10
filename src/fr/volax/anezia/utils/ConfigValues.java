package fr.volax.anezia.utils;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ConfigValues {
    private AneziaAddons main;

    public ConfigValues(AneziaAddons main) {
        this.main = main;
    }

    public Material getChunkBusterMaterial() {
        return this.main.getUtils().itemFromString(this.main.getConfig().getString("chunkbuster.material")).getType();
    }
    short getChunkBusterDamage() {
        return this.main.getUtils().itemFromString(this.main.getConfig().getString("chunkbuster.material")).getData().getData();
    }
    String getChunkBusterName() {
        return Utils.color(this.main.getConfig().getString("chunkbuster.name"));
    }
    List<String> getChunkBusterLore(int chunkRadius) {
        List<String> lore = this.main.getConfig().getStringList("chunkbuster.lore");
        for (int i = 0; i < lore.size(); i++)
            lore.set(i, Utils.color(lore.get(i)).replace("{area}", chunkRadius + "x" + chunkRadius));
        return lore;
    }
    public int getBlockPerTick() {
        return this.main.getConfig().getInt("blocks-removed-per-tick");
    }
    public boolean canPlaceInWilderness() {
        return this.main.getConfig().getBoolean("can-place-in-wilderness");
    }
    public int getChunkBusterWarmup() {
        return this.main.getConfig().getInt("warmup.seconds");
    }
    public int getGUIRows() {
        return this.main.getConfig().getInt("confirm-gui.rows");
    }
    public ItemStack getConfirmBlockItemStack() {
        return this.main.getUtils().itemFromString(this.main.getConfig().getString("confirm-gui.confirm-material"));
    }
    public ItemStack getCancelBlockItemStack() {
        return this.main.getUtils().itemFromString(this.main.getConfig().getString("confirm-gui.cancel-material"));
    }
    public ItemStack getFillItemStack() {
        return this.main.getUtils().itemFromString(this.main.getConfig().getString("confirm-gui.fill-material"));
    }
    public String getGUITitle() {
        return Utils.color(this.main.getConfig().getString("confirm-gui.title"));
    }
    public String getConfirmName() {
        return Utils.color(this.main.getConfig().getString("confirm-gui.confirm-block-name"));
    }
    public String getCancelName() {
        return Utils.color(this.main.getConfig().getString("confirm-gui.cancel-block-name"));
    }
    public String getFillName() {
        return Utils.color(this.main.getConfig().getString("confirm-gui.fill-name"));
    }
    public List<String> getConfirmLore() {
        return this.main.getUtils().colorLore(this.main.getConfig().getStringList("confirm-gui.confirm-block-lore"));
    }
    public List<String> getCancelLore() {
        return this.main.getUtils().colorLore(this.main.getConfig().getStringList("confirm-gui.cancel-block-lore"));
    }
    public List<String> getFillLore() {
        return this.main.getUtils().colorLore(this.main.getConfig().getStringList("confirm-gui.fill-lore"));
    }
    public boolean sendWarmupEverySecond() {
        return this.main.getConfig().getBoolean("warmup.send-message-every-second");
    }
    public boolean dropFullInv() {
        return this.main.getConfig().getBoolean("full-inv-drop-on-floor");
    }
    public String getMinimumRole() {
        return this.main.getConfig().getString("minimum-factions-role").toLowerCase();
    }
    public boolean warmupSoundEnabled() {
        return this.main.getConfig().getBoolean("warmup.warmup-sound-enabled");
    }
    public String getWarmupSoundString() {
        return this.main.getConfig().getString("warmup.warmup-sound");
    }
    public int getWarmupSoundInterval() {
        return this.main.getConfig().getInt("warmup.warmup-sound-interval");
    }
    public float getWarmupSoundVolume() {
        return (float)this.main.getConfig().getDouble("warmup.warmup-sound-volume");
    }
    public float getWarmupSoundPitch() {
        return (float)this.main.getConfig().getDouble("warmup.warmup-sound-pitch");
    }
    public boolean clearingSoundEnabled() {
        return this.main.getConfig().getBoolean("warmup.clearing-sound-enabled");
    }
    public String getClearingSoundString() {
        return this.main.getConfig().getString("warmup.clearing-sound");
    }
    public float getClearingSoundVolume() {
        return (float)this.main.getConfig().getDouble("warmup.clearing-volume");
    }
    public float getClearingSoundPitch() {
        return (float)this.main.getConfig().getDouble("warmup.clearing-pitch");
    }
    public boolean confirmSoundEnabled() {
        return this.main.getConfig().getBoolean("confirm-gui.confirm-sound-enabled");
    }

    public String getConfirmSoundString() {
        return this.main.getConfig().getString("confirm-gui.confirm-sound");
    }

    public float getConfirmSoundVolume() {
        return (float)this.main.getConfig().getDouble("confirm-gui.confirm-volume");
    }

    public float getConfirmSoundPitch() {
        return (float)this.main.getConfig().getDouble("confirm-gui.confirm-pitch");
    }
    public boolean cancelSoundEnabled() {
        return this.main.getConfig().getBoolean("confirm-gui.cancel-sound-enabled");
    }
    public String getCancelSoundString() {
        return this.main.getConfig().getString("confirm-gui.cancel-sound");
    }
    public float getCancelSoundVolume() {
        return (float)this.main.getConfig().getDouble("confirm-gui.cancel-volume");
    }
    public float getCancelSoundPitch() {
        return (float)this.main.getConfig().getDouble("confirm-gui.cancel-pitch");
    }

    Set<Material> getIgnoredBlocks() {
        List<String> rawMaterials = this.main.getConfig().getStringList("ignored-materials");
        Set<Material> materials = EnumSet.noneOf(Material.class);
        for (String rawMaterial : rawMaterials) {
            try {
                materials.add(Material.valueOf(rawMaterial));
            } catch (IllegalArgumentException illegalArgumentException) {}
        }
        return materials;
    }

    public int getCooldown() {
        return this.main.getConfig().getInt("cooldown");
    }

    int getMinimumY(Player p) {
        if (this.main.getConfig().getString("minimum-y").toLowerCase().contains("{player}"))
            return (int)p.getLocation().getY();
        try {
            return Integer.valueOf(this.main.getConfig().getString("minimum-y")).intValue();
        } catch (NumberFormatException ex) {
            this.main.getLogger().warning("Your minimum-y value is invalid, please either change this to an integer or '{player}' in the config.");
            return 0;
        }
    }

    int getMaximumY(Player p) {
        if (this.main.getConfig().getString("maximum-y").toLowerCase().contains("{player}"))
            return (int)p.getLocation().getY();
        try {
            return Integer.valueOf(this.main.getConfig().getString("maximum-y")).intValue();
        } catch (NumberFormatException ex) {
            this.main.getLogger().warning("Your maximum-y value is invalid, please either change this to an integer or '{player}' in the config.");
            return 255;
        }
    }

    public boolean factionsHookEnabled() {
        return this.main.getConfig().getBoolean("hooks.factions");
    }
    boolean worldborderHookEnabled() {
        return this.main.getConfig().getBoolean("hooks.worldborder");
    }
    public boolean worldguardHookEnabled() {
        return this.main.getConfig().getBoolean("hooks.worldguard");
    }
    public int getNoFallMillis() {
        return this.main.getConfig().getInt("no-fall-seconds") * 1000;
    }
    boolean itemShouldGlow() {
        return this.main.getConfig().getBoolean("chunkbuster.glow");
    }

    String getMessage(Message message, Object... params) {
        String messageText = Utils.color(this.main.getConfig().getString(message.getPath()));
        if (message == Message.GIVE) {
            messageText = messageText.replace("{player}", (String)params[0]).replace("{amount}", String.valueOf(params[1]));
        } else if (message == Message.RECEIVE) {
            messageText = messageText.replace("{amount}", String.valueOf(params[0]));
        } else if (message == Message.CLEARING_IN_SECONDS) {
            messageText = messageText.replace("{seconds}", String.valueOf(params[0]));
        } else if (message == Message.COOLDOWN) {
            messageText = messageText.replace("{minutes}", String.valueOf(params[0])).replace("{seconds}", String.valueOf(params[1]));
        }
        return messageText;
    }

    public enum Message {
        GIVE("give"),
        RECEIVE("receive"),
        NO_FACTION("no-faction"),
        CANNOT_PLACE("cannot-place"),
        CLEARING_CHUNKS("clearing-chunks"),
        COOLDOWN("cooldown"),
        NO_ITEM("no-item"),
        NO_PERMISSION_PLACE("no-permission-place"),
        NO_PERMISSION_COMMAND("no-permission-command"),
        CLEARING_IN_SECONDS("clearing-in-seconds"),
        NOT_MINIMUM_ROLE("not-minimum-role"),
        GUI_CANCEL("gui-cancel");

        private String path;
        Message(String path) {
            this.path = "messages." + path;
        }
        public String getPath() {
            return this.path;
        }
    }
}
