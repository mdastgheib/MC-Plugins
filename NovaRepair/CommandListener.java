package me.nova.novarepair;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandListener implements CommandExecutor {
    private NovaRepair plugin = null;

    public CommandListener(NovaRepair pluginInstance) {
        this.plugin = pluginInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("novarepair")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    repairPickaxe(player);
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
                }
            } else if (args.length == 1) {
                String strCommand = args[0];
                if (strCommand.equalsIgnoreCase("reload")) {
                    if (sender.hasPermission(this.getPlugin().reloadPermission)) {
                        try {
                            this.getPlugin().reloadConfig();
                            this.getPlugin().loadConfiguration();
                            Lang.init(this.getPlugin());

                            sender.sendMessage(ChatColor.GOLD + "Successfully reloaded NovaRepair.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(ChatColor.RED + "Failed to reload NovaRepair.");
                        }
                    } else {
                        Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSIONS);
                    }
                } else if (strCommand.equalsIgnoreCase("on")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (sender.hasPermission(this.getPlugin().togglePermission)) {
                            if (this.getPlugin().isIgnorer(player)) {
                                this.getPlugin().removeIgnorer(player);
                                Lang.sendMessage(player, Lang.COMMAND_TOGGLE_ON_TOGGLED);
                            } else {
                                Lang.sendMessage(player, Lang.COMMAND_TOGGLE_ON_ALREADY_NOT_IGNORER);
                            }
                        } else {
                            Lang.sendMessage(player, Lang.COMMAND_NO_PERMISSIONS);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
                    }
                } else if (strCommand.equalsIgnoreCase("off")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (sender.hasPermission(this.getPlugin().togglePermission)) {
                            if (!this.getPlugin().isIgnorer(player)) {
                                this.getPlugin().addIgnorer(player);
                                if (this.getPlugin().repairAuto.contains(player.getUniqueId()))
                                    this.getPlugin().repairAuto.remove(player.getUniqueId());
                                Lang.sendMessage(player, Lang.COMMAND_TOGGLE_OFF_TOGGLED);
                            } else {
                                Lang.sendMessage(player, Lang.COMMAND_TOGGLE_OFF_ALREADY_IGNORER);
                            }
                        } else {
                            Lang.sendMessage(player, Lang.COMMAND_NO_PERMISSIONS);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
                    }
                } else if (strCommand.equalsIgnoreCase("auto")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (sender.hasPermission(this.getPlugin().autoPermission)) {
                            if (this.getPlugin().repairAuto.contains(player.getUniqueId())) {
                                this.getPlugin().repairAuto.remove(player.getUniqueId());
                                Lang.sendMessage(player, Lang.COMMAND_TOGGLE_AUTO_OFF_TOGGLED);
                            } else {
                                this.getPlugin().repairAuto.add(player.getUniqueId());
                                Lang.sendMessage(player, Lang.COMMAND_TOGGLE_AUTO_ON_TOGGLED);
                            }
                        } else {
                            Lang.sendMessage(player, Lang.COMMAND_NO_PERMISSIONS);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
                    }
                } else {
                    sender.sendMessage(this.getHelpMessage());
                }
            } else {
                sender.sendMessage(this.getHelpMessage());
            }
            return true;
        }
        return false;
    }

    protected NovaRepair getPlugin() {
        return this.plugin;
    }


    public static void repairPickaxe(Player player) {
        repairPickaxe(player, true);
    }

    public static void repairPickaxe(Player player, boolean sendMessages) {
        if (player != null) {
            if (player.hasPermission(NovaRepair.getInstance().repairPermission)) {
                ItemStack itemInHand = player.getInventory().getItemInHand();
                if (itemInHand != null && itemInHand.getType().toString().toLowerCase().endsWith("pickaxe")) {
                    if (!NovaRepair.getInstance().isIgnorer(player)) {
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            if (itemInHand.getType().getMaxDurability() - itemInHand.getDurability() <= NovaRepair.getInstance().getRepairDurability()) {
                                double repairCost = NovaRepair.getInstance().getCost(itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) : 0);
                                double playerBalance = NovaRepair.getEconomy().getBalance(player);
                                if (playerBalance >= repairCost) {
                                    NovaRepair.getEconomy().withdrawPlayer(player, repairCost);
                                    itemInHand.setDurability((short) 0);
                                    player.getInventory().setItemInHand(itemInHand);
                                    player.updateInventory();
                                    if (sendMessages) Lang.sendMessage(player, Lang.REPAIR_REPAIRED);
                                } else {
                                    if (sendMessages)
                                        Lang.sendMessage(player, Lang.REPAIR_NOT_ENOUGH_MONEY, String.format("%.1f", repairCost - playerBalance));
                                }
                            } else {
                                if (sendMessages) Lang.sendMessage(player, Lang.REPAIR_NOT_DAMAGED);
                            }
                        } else {
                            if (sendMessages) Lang.sendMessage(player, Lang.COMMAND_REPAIR_CREATIVE);
                        }
                    } else {
                        if (sendMessages) Lang.sendMessage(player, Lang.REPAIR_IGNORER);
                    }
                } else {
                    if (sendMessages)
                        Lang.sendMessage(player, Lang.REPAIR_NOT_PICKAXE, (itemInHand != null ? WordUtils.capitalizeFully(itemInHand.getType().toString().toLowerCase().replace("_", " ")) : "Air"));
                }
            } else {
                Lang.sendMessage(player, Lang.COMMAND_NO_PERMISSIONS);
            }
        }
    }
}
