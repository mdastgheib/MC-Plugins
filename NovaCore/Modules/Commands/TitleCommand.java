package net.novaprison.Modules.Commands;

/*
public class TitleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        boolean isPlayer = (cs instanceof Player);
        Player player = null;
        if (cmd.getName().equalsIgnoreCase("title")) {
            if (isPlayer) {
                player = (Player) cs;
                if(!player.hasPermission("nova.title")) {
                    return true;
                }
            }
            if(args.length > 3) {
                if(args[0].equalsIgnoreCase("send")) {
                    int in;
                    int stay;
                    int out;
                    String title = "";
                    String subtitle = "";
                    String msg = "";
                    try {
                        String[] numbs = args[2].split(":");
                        in = Integer.parseInt(numbs[0]);
                        stay = Integer.parseInt(numbs[1]);
                        out = Integer.parseInt(numbs[2]);

                        for(int i = 3; i < args.length; i++) {
                            String arg = args[i] + " ";
                            msg = msg + arg;
                        }
                        if(msg.contains("~")) {
                            String[] msgs = msg.split("~");
                            title = ChatColor.translateAlternateColorCodes('&', msgs[0]);
                            subtitle = ChatColor.translateAlternateColorCodes('&', msgs[1]);
                        } else {
                            title = ChatColor.translateAlternateColorCodes('&', msg);
                        }
                    } catch(Exception ex) {
                        usage(cs);
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("all")) {
                        for(World world : Bukkit.getWorlds()) {
                            for(Player p : world.getPlayers()) {
                                NovaTitle.setTime(p, in, stay, out);
                                NovaTitle.sendTitle(p, title, subtitle);
                            }
                        }
                    } else if(Bukkit.getPlayerExact(args[1]) != null) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        NovaTitle.setTime(target, in, stay, out);
                        NovaTitle.sendTitle(target, title, subtitle);
                    } else {
                        usage(cs);
                    }
                } else {
                    usage(cs);
                }
            } else {
                usage(cs);
            }
            return true;
        }
        return false;
    }

    public void usage(CommandSender cs)
    {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/title send [player/all] [time] [title~subtitle]"));
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTime (in ticks) = in:stay:out , eg. 10:50:20"));
    }
}
*/