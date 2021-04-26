package Nova.Tag.Commands;
import Nova.Tag.Tag;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdNovaTag implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("novatag")) {
            if (!(cs instanceof Player)) {
                return true;
            }
            Player p = (Player) cs;
            if(args.length == 0) {
                if(Tag.getTaggedPlayers().containsKey(p.getName())) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &cYou are currently in combat! Do &c&lNOT&c log out!"));
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &aYou are not currently in combat."));
                }
                return true;
            }
                else if(args[0].equalsIgnoreCase("sethellloc")) {
                    if(p.hasPermission("novatag.admin")) {
                        Tag.getInstance().getConfig().set("Nova.punish.world", p.getWorld().toString());
                        Tag.getInstance().getConfig().set("Nova.punish.x", p.getLocation().getX());
                        Tag.getInstance().getConfig().set("Nova.punish.y", p.getLocation().getY());
                        Tag.getInstance().getConfig().set("Nova.punish.z", p.getLocation().getZ());

                        Tag.getInstance().saveConfig();
                        return true;
                    } else {
                        if(Tag.getTaggedPlayers().containsKey(p.getName())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &cYou are currently in combat! Do &c&lNOT&c log out!"));
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &aYou are not currently in combat."));
                        }
                        return true;
                    }
                } else if(args[0].equalsIgnoreCase("reload")) {
                    if(p.hasPermission("novatag.admin")) {
                        Tag.getInstance().reloadConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &aReloaded configuration!"));
                        return true;
                    } else {
                        if(Tag.getTaggedPlayers().containsKey(p.getName())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &cYou are currently in combat! Do &c&lNOT&c log out!"));
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &aYou are not currently in combat."));
                        }
                        return true;
                    }
                }
            } else {
                if(Tag.getTaggedPlayers().containsKey(p.getName())) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &cYou are currently in combat! Do &c&lNOT&c log out!"));
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Tag.getInstance().getConfig().getString("Nova.Prefix") + " &aYou are not currently in combat."));
                }
                return true;
            }
        }
        return false;
    }
