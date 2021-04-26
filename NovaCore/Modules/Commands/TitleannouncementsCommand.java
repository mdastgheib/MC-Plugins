package net.novaprison.Modules.Commands;

//import net.novaprison.Modules.Title.Title;

/*
public class TitleannouncementsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player = null;
        if (cmd.getName().equalsIgnoreCase("titleannouncement")) {
            if (isPlayer) {
                player = (Player) cs;
                if(!player.hasPermission("nova.titleannouncement")) {
                    return true;
                }
            }
            switch (args.length) {
                case 2: {
                    if(args[0].equalsIgnoreCase("force")) {
                        try {
                            int i = Integer.parseInt(args[1]);
                            if(i <= 0) {
                                return true;
                            }
                            FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "titles.yml"));
                            if(!c.isSet("Titles." + i + ".A")) {
                                return true;
                            }
                            String title = ChatColor.translateAlternateColorCodes('&', c.getString("Titles." + i + ".A"));
                            String subtitle = ChatColor.translateAlternateColorCodes('&', c.getString("Titles." + i + ".B"));
                            for(World world : Bukkit.getWorlds()) {
                                for(Player p : world.getPlayers()) {
                                    NovaTitle.setTime(player, Title.in, Title.stay, Title.out);
                                    NovaTitle.sendTitle(p, title, subtitle);
                                }
                            }
                        } catch (NumberFormatException ex) {
                            usage(cs);
                        }
                    } else {
                        usage(cs);
                    }
                    return true;
                }
                default: {
                    usage(cs);
                }
                return true;
            }
        }
        return false;
    }

    public void usage(CommandSender cs)
    {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/titleannouncement force [number]"));
    }
}
*/
