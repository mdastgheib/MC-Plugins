package com.nova.sellall;
import com.nova.sellall.hooks.MultiplierAPI;
import com.nova.sellall.hooks.RankupAPI;
import com.nova.sellall.hooks.Vault;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.api.BossShopAPI;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSEnums;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.events.BSPlayerPurchaseEvent;
import org.black_ixx.bossshop.events.BSPlayerPurchasedEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class SellAll extends JavaPlugin {
    private static SellAll instance = null;
    private BossShop bossShop = null;
    private BossShopAPI bossShopAPI = null;

    private Permission sellAllPermission = new Permission("novasellall.sellall");

    @Override
    public void onEnable() {
        if (!this.getServer().getPluginManager().isPluginEnabled("BossShop") || !(this.getServer().getPluginManager().getPlugin("BossShop") instanceof org.black_ixx.bossshop.BossShop)) {
            this.getServer().getLogger().severe("*** BossShop could not be found! ***");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        instance = this;
        Vault.setupEconomy();
        Vault.setupPermissions();
        this.bossShop = (BossShop) this.getServer().getPluginManager().getPlugin("BossShop");
        this.bossShopAPI = this.bossShop.getAPI();
        Lang.init(this);

        this.getCommand("sellall").setExecutor(this);
        this.getCommand("sall").setExecutor(this);

        this.getServer().getPluginManager().addPermission(this.sellAllPermission);
    }

    @Override
    public void onDisable() {
        this.getServer().getPluginManager().removePermission(this.sellAllPermission);

        Vault.resetInstance();
        this.bossShopAPI = null;
        this.bossShop = null;
        instance = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("sellall") || cmd.getName().equals("sall")) {
            return this.runSellCommand(sender, args);
        }
        return false;
    }

    public boolean runSellCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission(this.sellAllPermission)) {
                    String playerRank = RankupAPI.getRank(player);
                    BSShop rankShop = this.bossShopAPI.getShop(playerRank);
                    if (rankShop == null) {
                        rankShop = this.bossShopAPI.getShop("Z");
                        if (rankShop == null) {
                            Lang.sendMessage(sender, Lang.NO_SHOP, "Z");
                            return true;
                        }
                    }
                    int amountSold = 0;
                    double amountReceived = 0D;

                    ItemStack[] itemContents = player.getInventory().getContents();
                    double playerMultiplier = MultiplierAPI.getMultiplier(player);
                    for (int itemIndex = 0; itemIndex < itemContents.length; itemIndex++) {
                        ItemStack playerItem = itemContents[itemIndex];
                        if (playerItem != null && playerItem.getType() != Material.AIR) {
                            try {
                                BSBuy item = null;
                                for (Map.Entry<ItemStack, BSBuy> itemEntry : rankShop.getItems().entrySet()) {
                                    if (itemEntry.getKey() != null && itemEntry.getKey().getType() == playerItem.getType() && itemEntry.getKey().getDurability() == playerItem.getDurability()) {
                                        item = itemEntry.getValue();
                                        break;
                                    }
                                }

                                if (item != null) {
                                    BSPlayerPurchaseEvent purchaseEvent = new BSPlayerPurchaseEvent(player, rankShop, item);
                                    this.getServer().getPluginManager().callEvent(purchaseEvent);
                                    if (!purchaseEvent.isCancelled() && item.getBuyType() == BSEnums.BSBuyType.Money) {
                                        Object objReward = item.getReward();
                                        double itemReward = objReward instanceof Double ? (Double) objReward : (objReward instanceof Integer ? ((Integer) objReward).doubleValue() : 0D);
                                        double reward = ((itemReward / 64) * playerItem.getAmount()) * playerMultiplier;
                                        itemContents[itemIndex] = new ItemStack(Material.AIR);
                                        if (reward < 0D) Vault.getEconomy().withdrawPlayer(player, reward);
                                        else Vault.getEconomy().depositPlayer(player, reward);
                                        if (this.bossShop.getClassManager().getSettings().getTransactionLogEnabled())
                                            this.bossShop.getClassManager().getTransactionLog().addTransaction(player, item);

                                        try {
                                            BSPlayerPurchasedEvent purchasedEvent = new BSPlayerPurchasedEvent(player, rankShop, item);
                                            this.getServer().getPluginManager().callEvent(purchasedEvent);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        amountSold++;
                                        amountReceived += reward;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                continue;
                            }
                        }
                    }
                    if (amountSold > 0) {
                        player.getInventory().setContents(itemContents);
                        player.updateInventory();
                    }

                    Lang.sendReplacedMessage(sender, Lang.SOLD_ITEMS, "<amount>", amountSold, "<money>", String.format("%.1f", amountReceived));
                    if (playerMultiplier != 1D)
                        Lang.sendReplacedMessage(sender, Lang.MULTIPLIER, "<multiplier>", String.format("%.1f", playerMultiplier));
                } else {
                    Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSIONS);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to use that command.");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.isOp()) {
                    this.reloadConfig();
                    Lang.init(this);
                    sender.sendMessage(ChatColor.GREEN + this.getDescription().getFullName() + " reloaded.");
                } else {
                    sender.sendMessage(ChatColor.GREEN + "NovaSellAll");
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + "NovaSellAll");
            }
        } else {
            sender.sendMessage(ChatColor.GREEN + "NovaSellAll");
        }
        return true;
    }

    public static SellAll getInstance() {
        return instance;
    }

}
