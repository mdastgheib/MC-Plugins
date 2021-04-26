import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WhatIsAClass extends JavaPlugin {

    @Override
    public void onEnable()
    {

    }

    @Override
    public void onDisable()
    {

    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("resethomes"))
        {
            if(cs instanceof Player)
            {
                if(!cs.isOp() && !cs.hasPermission("nova.resethomes"))
                {
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNope."));
                    return true;
                }
            }
            if(!(args.length == 1))
            {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/resethomes [player]"));
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if(target != null)
            {

                Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
                User u = ess.getUser(target);
                for(String home : u.getHomes())
                {
                    try
                    {
                        u.delHome(home);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            else
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

                if(offlinePlayer != null)
                {

                    Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
                    User u = ess.getOfflineUser(offlinePlayer.getName());

                    if(u == null)
                    {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOffline player " + args[0] + " could not be found."));
                        return true;
                    }

                    for (String home : u.getHomes())
                    {
                        try
                        {
                            u.delHome(home);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemove all of " + args[0] + "'s homes!"));
                    return true;
                }


                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + args[0] + " is not online & could not fine offline player by such name!"));
                return true;
            }

            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRemove all of " + args[0] + "'s homes!"));
        }
        return false;
    }
}
