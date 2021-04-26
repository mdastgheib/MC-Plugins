import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String commandLabel, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("novahaste"))
        {
            if(!(cs instanceof Player))
            {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lNovaHaste plugin, &aCreated by iMurder (Xx_iMurder_xX)"));
                return true;
            }

            Player p = (Player) cs;

            if(!p.hasPermission("novahaste.admin"))
            {
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("on")) {
                        if(Haste.toggle.containsKey(p.getUniqueId())) {
                            Haste.toggle.remove(p.getUniqueId());
                            Haste.toggle.put(p.getUniqueId(), true);
                            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
                            Haste.getStaticPlugin().saveConfig();
                        } else {
                            Haste.toggle.put(p.getUniqueId(), true);
                            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
                            Haste.getStaticPlugin().saveConfig();
                        }
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOn")
                                .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                        return true;
                    } else if(args[0].equalsIgnoreCase("off")) {
                        if(Haste.toggle.containsKey(p.getUniqueId())) {
                            Haste.toggle.remove(p.getUniqueId());
                            Haste.toggle.put(p.getUniqueId(), false);
                            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), false);
                            Haste.getStaticPlugin().saveConfig();
                        } else {
                            Haste.toggle.put(p.getUniqueId(), false);
                            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), false);
                            Haste.getStaticPlugin().saveConfig();
                        }
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOff")
                                .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                        return true;
                    } else if(args[0].equalsIgnoreCase("toggle")) {
                        if(Haste.toggle.containsKey(p.getUniqueId())) {
                            if(Haste.toggle.get(p.getUniqueId())) {
                                Haste.toggle.remove(p.getUniqueId());
                                Haste.toggle.put(p.getUniqueId(), false);
                                Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), false);
                                Haste.getStaticPlugin().saveConfig();

                                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOff")
                                        .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                            } else {
                                Haste.toggle.remove(p.getUniqueId());
                                Haste.toggle.put(p.getUniqueId(), true);
                                Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
                                Haste.getStaticPlugin().saveConfig();

                                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOn")
                                        .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                            }
                        }
                        return true;
                    } else {
                        getPlayerUsage(cs, cmd);
                        return true;
                    }
                } else {

                }
                return true;
            }

            if(args.length == 0)
            {
                getUsage(cs, cmd);
                return true;
            }
            else if(args.length == 1)
            {
                if (args[0].equalsIgnoreCase("reload"))
                {
                    Haste.getStaticPlugin().reloadConfig();
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Reloaded")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                    return true;
                }
                if(args[0].equalsIgnoreCase("on")) {
                    if(Haste.toggle.containsKey(p.getUniqueId())) {
                        Haste.toggle.remove(p.getUniqueId());
                        Haste.toggle.put(p.getUniqueId(), true);
                        Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
                        Haste.getStaticPlugin().saveConfig();
                    } else {
                        Haste.toggle.put(p.getUniqueId(), true);
                        Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
                        Haste.getStaticPlugin().saveConfig();
                    }
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOn")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                    return true;
                } else if(args[0].equalsIgnoreCase("off")) {
                    if(Haste.toggle.containsKey(p.getUniqueId())) {
                        Haste.toggle.remove(p.getUniqueId());
                        Haste.toggle.put(p.getUniqueId(), false);
                        Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), false);
                        Haste.getStaticPlugin().saveConfig();
                    } else {
                        Haste.toggle.put(p.getUniqueId(), false);
                        Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), false);
                        Haste.getStaticPlugin().saveConfig();
                    }
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOff")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                    return true;
                } else if(args[0].equalsIgnoreCase("toggle")) {
                    if(Haste.toggle.containsKey(p.getUniqueId())) {
                        if(Haste.toggle.get(p.getUniqueId())) {
                            Haste.toggle.remove(p.getUniqueId());
                            Haste.toggle.put(p.getUniqueId(), false);
                            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), false);
                            Haste.getStaticPlugin().saveConfig();

                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOff")
                                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                        } else {
                            Haste.toggle.remove(p.getUniqueId());
                            Haste.toggle.put(p.getUniqueId(), true);
                            Haste.getStaticPlugin().getConfig().set("Player." + p.getUniqueId().toString(), true);
                            Haste.getStaticPlugin().saveConfig();

                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.toggleOn")
                                    .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))));
                        }
                    }
                    return true;
                } else {
                    getUsage(cs, cmd);
                    return true;
                }
            }
            else if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("addworld"))
                {
                    List<String> worlds = Haste.getStaticPlugin().getConfig().getStringList("Nova.Worlds");
                    worlds.add(args[1]);
                    Haste.getStaticPlugin().getConfig().set("Nova.Worlds", worlds);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.AddWorld")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                            .replace("%world%", args[1])));
                    Haste.getStaticPlugin().saveConfig();
                    return true;
                }
                else if(args[0].equalsIgnoreCase("removeworld"))
                {
                    List<String> worlds = Haste.getStaticPlugin().getConfig().getStringList("Nova.Worlds");
                    worlds.remove(args[1]);
                    Haste.getStaticPlugin().getConfig().set("Nova.Worlds", worlds);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Haste.getStaticPlugin().getConfig().getString("Nova.Messages.RemoveWorld")
                            .replace("%prefix%", Haste.getStaticPlugin().getConfig().getString("Nova.Messages.Prefix"))
                            .replace("%world%", args[1])));
                    Haste.getStaticPlugin().saveConfig();
                    return true;
                }
                else
                {
                    getUsage(cs, cmd);
                    return true;
                }
            }
            else
            {
                getUsage(cs, cmd);
                return true;
            }
        }

        return false;
    }

    private void getUsage(CommandSender sender, org.bukkit.command.Command cmd) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " [on/off/toggle]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " addworld [world name]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " removeworld [world name]"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " reload"));
    }

    private void getPlayerUsage(CommandSender sender, org.bukkit.command.Command cmd) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + cmd.getName() + " [on/off/toggle]"));
    }
}
