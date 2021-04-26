package net.novaprison.coupon;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Formatter;
import java.util.Random;

public class Coupon extends JavaPlugin {
    
    Connection con = null;
    Statement stmt;
    ResultSet res;
 
    @Override
    public void onEnable() {
        if (this.getConfig().getBoolean("config.debug")) {
            System.out.println("[NovaCoupon - DEBUG] Debug mode enabled!");
            System.out.println("[NovaCoupon - DEBUG] Loading config ...");
        }

        loadConfig();

        if (this.getConfig().getBoolean("config.debug")) {
            System.out.println("[NovaCoupon - DEBUG] Loading config finished.");
        }


        System.out.println("[NovaCoupon] Plugin by " + this.getDescription().getAuthors());

        connect_to_database();

        if (this.getConfig().getBoolean("config.debug")) {
            System.out.println("[NovaCoupon - DEBUG] Enable Listener ...");
        }

        if (this.getConfig().getBoolean("config.debug")) {
            System.out.println("[NovaCoupon - DEBUG] Listener enabled.");
            System.out.println("[NovaCoupon - DEBUG] Check for Plugin Metrics ...");
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("coupon")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "-----------------------------------------------------");
                sender.sendMessage(ChatColor.GREEN + this.getDescription().getFullName() + " by " + this.getDescription().getAuthors());
                sender.sendMessage(ChatColor.GREEN + "Type /coupon help for help");
                sender.sendMessage(ChatColor.GREEN + "Type /coupon perms for permissions");
                sender.sendMessage(ChatColor.GREEN + "-----------------------------------------------------");
                if (this.getConfig().getString("config.datebase").equalsIgnoreCase("mysql")) {
                    if (con == null)
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cMySQL STATUS: Connection failed"));
                    else
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMySQL STATUS: Connection ready"));
                } else {
                    if (con == null)
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSQLite STATUS: Connection failed"));
                    else
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSQLite STATUS: Connection ready"));
                }
                sender.sendMessage(ChatColor.GREEN + "-----------------------------------------------------");
            } else {


                if (con == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.mysqlconnection")));
                    return true;
                }

                if (args[0].equalsIgnoreCase("create")) {


                    if (args.length == 5 || args.length == 6) {
                        if (this.getConfig().getBoolean("config.debug")) {
                            System.out.println("[NovaCoupon - DEBUG] Coupon generation: Check permission");
                        }
                        if (sender instanceof Player) {
                            if (!permCheck((Player) sender, "novacoupon.create")) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.nopermission")));
                                return true;
                            }
                        }

                        try {

                            if (this.getConfig().getBoolean("config.debug")) {
                                System.out.println("[NovaCoupon - DEBUG] Coupon generation: Set current time");
                            }

                            long time_expire = System.currentTimeMillis() / 1000;
                            String[] zahl;
                            String code;

                            if (this.getConfig().getBoolean("config.debug")) {
                                System.out.println("[NovaCoupon - DEBUG] Coupon generation: Generate/Set code");
                            }

                            if (args.length == 5) {
                                code = generateCouponCode(this.getConfig().getInt("config.cuponcode.length"), this.getConfig().getString("config.cuponcode.allowedchars"));
                            } else {
                                if (!coupon_code_inUse(args[5])) {
                                    code = args[5];
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.codealreadyinuse").replace("%code%", args[5])));
                                    return true;
                                }
                            }

                            if (this.getConfig().getBoolean("config.debug")) {
                                System.out.println("[NovaCoupon - DEBUG] Coupon generation: add validity period");
                            }

                            if (args[2].endsWith("seconds") || args[2].endsWith("second") || args[2].endsWith("sec")) {
                                zahl = args[2].split("sec");
                                time_expire = time_expire + Integer.parseInt(zahl[0]);
                            }

                            if (args[2].endsWith("minutes") || args[2].endsWith("minute") || args[2].endsWith("min")) {
                                zahl = args[2].split("min");
                                time_expire = time_expire + Integer.parseInt(zahl[0]) * 60;
                            }

                            if (args[2].endsWith("hours") || args[2].endsWith("hour") || args[2].endsWith("h")) {
                                zahl = args[2].split("h");
                                time_expire = time_expire + Integer.parseInt(zahl[0]) * 3600;
                            }

                            if (args[2].endsWith("days") || args[2].endsWith("day") || args[2].endsWith("d")) {
                                zahl = args[2].split("d");
                                time_expire = time_expire + Integer.parseInt(zahl[0]) * 86400;
                            }

                            if (args[2].endsWith("weeks") || args[2].endsWith("week") || args[2].endsWith("w")) {
                                zahl = args[2].split("w");
                                time_expire = time_expire + Integer.parseInt(zahl[0]) * 604800;
                            }

                            if (args[2].endsWith("months") || args[2].endsWith("month") || args[2].endsWith("mon")) {
                                zahl = args[2].split("mon");
                                time_expire = time_expire + Integer.parseInt(zahl[0]) * 18144000;
                            }
                            int multible_use = 0;
                            if (args[4].equalsIgnoreCase("true")) {
                                multible_use = 1;
                            } else {
                                if (args[4].equalsIgnoreCase("false")) {
                                    multible_use = 0;
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Paramter <multible_use> was wrong."));
                                    return true;
                                }
                            }

                            if (this.getConfig().getBoolean("config.debug")) {
                                System.out.println("[NovaCoupon - DEBUG] Coupon generation: Write date to database");
                            }

                            if (this.getConfig().getString("config.datebase").equalsIgnoreCase("mysql")) {
                                stmt.execute("INSERT INTO `novacoupon` (`ID`, `voucher_code`, `usage_left`, `valid_through`, `command`, `multible_use`) VALUES (NULL, '" + code + "', '" + args[1] + "', '" + time_expire + "', '" + args[3].replace('?', ' ') + "', " + multible_use + ");");
                            }
                            if (this.getConfig().getString("config.datebase").equalsIgnoreCase("sqlite")) {
                                res = stmt.executeQuery("SELECT COUNT( * ) ID FROM novacoupon");
                                stmt.execute("insert into novacoupon (ID, voucher_code, usage_left, valid_through, command, multible_use, used_by) values (" + (res.getInt("ID") + 1) + ", '" + code + "', '" + args[1] + "', '" + time_expire + "', '" + args[3].replace('?', ' ') + "', " + multible_use + ", ';');");
                            }

                            if (this.getConfig().getBoolean("config.debug")) {
                                System.out.println("[NovaCoupon - DEBUG] Coupon generation: Data is wirtten to database");
                            }

                            sender.sendMessage(ChatColor.GREEN + this.getConfig().getString("config.messages.coupongenerated") + " " + code);
                        } catch (SQLException ex) {
                            sender.sendMessage(ChatColor.RED + this.getConfig().getString("config.errormessages.coupongenerateerror"));
                            sender.sendMessage("FAIL: " + ex);
                        }

                        return true;
                    } else {
                        sender.sendMessage("Use: /coupon create <usage> <valid trough> <command> <multible use> (<custom_code>)");
                        return true;
                    }
                }


                if (args[0].equalsIgnoreCase("remove")) {

                    if (sender instanceof Player) {
                        if (!permCheck((Player) sender, "novacoupon.remove")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.nopermission")));
                            return true;
                        }
                    }

                    try {
                        stmt.execute("DELETE FROM `novacoupon` WHERE `voucher_code` = '" + args[1] + "'");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.messages.couponremoved")));
                    } catch (SQLException ex) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.coupongenerateerror")));
                    }

                    return true;
                }

                if (args[0].equalsIgnoreCase("list")) {

                    if (sender instanceof Player) {
                        if (!permCheck((Player) sender, "novacoupon.list")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.nopermission")));
                            return true;
                        }
                    }

                    try {
                        res = stmt.executeQuery("SELECT * FROM `novacoupon`");
                        int counter = 1;
                        sender.sendMessage(ChatColor.GREEN + "COUPONS LIST");
                        sender.sendMessage(ChatColor.GREEN + "-----------------------------------------------------");
                        while (res.next()) {
                            if (res.getLong("valid_through") > System.currentTimeMillis() / 1000L) {
                                if (res.getBoolean("multible_use")) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + counter + " " + res.getString("voucher_code") + " - " + res.getInt("usage_left") + " - " + res.getString("command") + ChatColor.BLUE + " (Multible use)"));
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + counter + " " + res.getString("voucher_code") + " - " + res.getInt("usage_left") + " - " + res.getString("command")));
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + counter + " " + res.getString("voucher_code") + " - " + res.getInt("usage_left") + " - " + res.getString("command") + ChatColor.RED + " (Expired)"));
                            }
                            counter++;
                        }
                    } catch (SQLException ex) {
                    }

                    return true;
                }

                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.GREEN + "Apply Coupon: /coupon <couponcode>");
                    sender.sendMessage(ChatColor.GREEN + "Create Coupon: /coupon create <applications> <valid_trough> <command> <multible use> (<custom_code>)");
                    sender.sendMessage(ChatColor.GREEN + "Remove Coupon: /coupon remove <couponcode>");
                    sender.sendMessage(ChatColor.GREEN + "List Coupons: /coupon list");
                    sender.sendMessage(ChatColor.GREEN + "Update NovaCoupon: /coupon update");
                    return true;
                }

                if (args[0].equalsIgnoreCase("perms")) {
                    sender.sendMessage(ChatColor.GREEN + "Use NovaCoupon: novacoupon.use");
                    sender.sendMessage(ChatColor.GREEN + "List Coupons: novacoupon.list");
                    sender.sendMessage(ChatColor.GREEN + "Create Coupons: novacoupon.create");
                    sender.sendMessage(ChatColor.GREEN + "Remove Coupons: novacoupon.remove");
                    return true;
                }


                //Ab hier wird Coupon bearbeitet!
                if (sender instanceof Player) {
                    if (!permCheck((Player) sender, "novacoupon.use")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.nopermission")));
                        return true;
                    }

                    try {
                        res = stmt.executeQuery("SELECT * FROM `novacoupon`");
                        String[] users_used = null;
                        int i = 1;
                        while (res.next()) {
                            if (args[0].equals(res.getString(2))) {
                                users_used = res.getString("used_by").split(";");
                                System.out.println(res.getBoolean(6));
                                if (!res.getBoolean(6)) {
                                    for (i = 1; i < users_used.length; i++) {
                                        if (users_used[i].equals(sender.getName())) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.couponused")));
                                            return true;
                                        }
                                    }
                                }
                                if (res.getInt(3) != 0) {
                                    if (res.getInt(4) > System.currentTimeMillis() / 1000L) {
                                        String[] commands = res.getString("command").split(";");
                                        for (i = 0; i < commands.length; i++) {
                                            getServer().dispatchCommand(getServer().getConsoleSender(), commands[i].replaceAll("%player%", sender.getName()));
                                        }
                                        String used = res.getString(7) + sender.getName() + ";";
                                        int id = res.getInt(1);
                                        if (res.getInt(3) > 0)
                                            stmt.execute("UPDATE `novacoupon` SET `usage_left` = '" + (res.getInt(3) - 1) + "' WHERE `ID` = " + id);
                                        stmt.execute("UPDATE `novacoupon` SET `used_by` = '" + used + "' WHERE `ID` = " + id);
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.messages.couponused")));
                                        res.close();
                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.couponexpired")));
                                    }

                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.couponnoapplications")));
                                }

                            }
                        }

                    } catch (SQLException ex) {

                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("config.errormessages.couponnotfound")));
                }
            }
        }

        return true;
    }
    
    
    private void loadConfig(){
       this.getConfig().options().header("MINECOUPON CONFIGURATION");
       this.getConfig().addDefault("config.datebase", "sqlite");
       this.getConfig().addDefault("config.mysql.host", "localhost");
       this.getConfig().addDefault("config.mysql.port", 3306);
       this.getConfig().addDefault("config.mysql.database", "minecraft");
       this.getConfig().addDefault("config.mysql.username", "root");
       this.getConfig().addDefault("config.mysql.password", "");
       this.getConfig().addDefault("config.cuponcode.length", 4);
       this.getConfig().addDefault("config.cuponcode.allowedchars", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
       this.getConfig().addDefault("config.messages.coupongenerated", "The coupon was generated successfully. Code:");
       this.getConfig().addDefault("config.messages.couponremoved", "The coupon was successfully removed.");
       this.getConfig().addDefault("config.messages.couponused", "The coupon has been applied successfully.");
       this.getConfig().addDefault("config.errormessages.coupongenerateerror", "There was an erroy while edit the coupon.");
       this.getConfig().addDefault("config.errormessages.couponexpired", "Coupon has expired.");
       this.getConfig().addDefault("config.errormessages.couponnoapplications", "The maximum number of applications has been reached.");
       this.getConfig().addDefault("config.errormessages.couponnotfound", "This coupon code is invalid.");
       this.getConfig().addDefault("config.errormessages.couponused", "You have already used this coupon code.");
       this.getConfig().addDefault("config.errormessages.codealreadyinuse", "The code %code% is already in use.");
       this.getConfig().addDefault("config.errormessages.nopermission", "You don't have the required permissons.");
       this.getConfig().addDefault("config.errormessages.mysqlconnection", "There is no MySQL connection.");
       this.getConfig().addDefault("config.errormessages.downloadfailed", "There was an erroy while downloading the update.");
       this.getConfig().addDefault("config.errormessages.installfailed", "There was an erroy while installing the update.");
       this.getConfig().addDefault("config.debug", false);
       
       this.getConfig().options().copyDefaults(true);
       this.saveConfig();
    }

    public boolean permCheck(Player player, String permission){
        if(player.isOp() || player.hasPermission(permission)){
            return true;
        }
        return false;
    }

    private static String generateCouponCode(int length, String allowedChars) {
        Random random = new Random();
        int max = allowedChars.length();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int value = random.nextInt(max);
            buffer.append(allowedChars.charAt(value));
        }
        buffer.append("-");
        for (int i = 0; i < length; i++) {
            int value = random.nextInt(max);
            buffer.append(allowedChars.charAt(value));
        }
        buffer.append("-");
        for (int i = 0; i < length; i++) {
            int value = random.nextInt(max);
            buffer.append(allowedChars.charAt(value));
        }
        return buffer.toString();
    }
        
    public boolean connect_to_database() {

        if (this.getConfig().getBoolean("config.debug")) {
            System.out.println("[NovaCoupon - DEBUG] Getting type of database.");
        }

        if (this.getConfig().getString("config.datebase").equalsIgnoreCase("mysql")) {
            if (this.getConfig().getBoolean("config.debug")) {
                System.out.println("[NovaCoupon - DEBUG] MySQL selected");
            }

            System.out.println("[NovaCoupon] Connecting to MySQL Database...");

            try {
                con = DriverManager.getConnection("jdbc:mysql://" + this.getConfig().getString("config.mysql.host") + ":" + this.getConfig().getInt("config.mysql.port") + "/" + this.getConfig().getString("config.mysql.database"), this.getConfig().getString("config.mysql.username"), this.getConfig().getString("config.mysql.password"));

                stmt = con.createStatement();

                stmt.execute("CREATE TABLE IF NOT EXISTS `novacoupon` (`ID` int(11) NOT NULL AUTO_INCREMENT,`voucher_code` varchar(200) NOT NULL,`usage_left` int(11) NOT NULL,`valid_through` bigint(20) NOT NULL,`command` varchar(500) NOT NULL,`multible_use` BOOLEAN NOT NULL DEFAULT FALSE,`used_by` varchar(1000) NOT NULL DEFAULT ';',PRIMARY KEY (`ID`)) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;");
                // Update elder Databaseversions
                change_database_strukture("ALTER TABLE `novacoupon` ADD `multible_use` BOOLEAN NOT NULL DEFAULT FALSE AFTER  `command`");
                change_database_strukture("ALTER TABLE  `novacoupon` CHANGE  `valid_throug`  `valid_through` BIGINT( 20 ) NOT NULL");
                // Update elder Databaseversions
                System.out.println("[NovaCoupon] Connected to MySQL Database.");
                return true;
            } catch (Exception e) {
                System.out.println("[NovaCoupon] Failed to connect to MySQL Database.");
                System.out.println("[NovaCoupon] Disable NovaCoupon ... ");

            }
        } else {
            if (this.getConfig().getString("config.datebase").equalsIgnoreCase("sqlite")) {

                //Try to create database file
                if (this.getConfig().getBoolean("config.debug")) {
                    System.out.println("[NovaCoupon - DEBUG] Check if SQLite Database-File is needed ...");
                }
                File file = new File(this.getDataFolder() + "/novacoupon.db");
                if (!file.exists()) {
                    try {
                        Formatter CHRIS = new Formatter(this.getDataFolder() + "/novacoupon.db");
                        System.out.println("[NovaCoupon] SQLite Database-File created.");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (this.getConfig().getBoolean("config.debug")) {
                        System.out.println("[NovaCoupon - DEBUG] Database-File already exists.");
                    }
                }
                //Try to create database file

                if (this.getConfig().getBoolean("config.debug")) {
                    System.out.println("[NovaCoupon - DEBUG] SQLite selected");
                }

                System.out.println("[NovaCoupon] Connecting to SQLite Database...");
                try {
                    Class.forName("org.sqlite.JDBC");
                    con = DriverManager.getConnection("jdbc:sqlite:" + this.getDataFolder() + "/novacoupon.db");
                    stmt = con.createStatement();
                    stmt.execute("CREATE TABLE IF NOT EXISTS novacoupon (ID, voucher_code, usage_left, valid_through, command, multible_use, used_by);");
                    System.out.println("[NovaCoupon] Connected to SQLite Database.");
                    return true;
                } catch (Exception e) {
                    System.out.println("[NovaCoupon] Failed to connect to SQLite Database.");
                    System.out.println("[NovaCoupon] Disable NovaCoupon ... ");
                }
            } else {
                System.out.println("[NovaCoupon] Wrong Databasetype.");
                System.out.println("[NovaCoupon] Disable NovaCoupon ... ");
            }
        }
        return false;
    }
    
    public void change_database_strukture(String SQLcommand) {
        try {
            if (this.getConfig().getBoolean("config.debug")) {
                System.out.println("[NovaCoupon - DEBUG] Try to update database ...");
            }
            stmt.execute(SQLcommand);
            System.out.println("[NovaCoupon - DEBUG] Database updated.");
        } catch (Exception e) {
            if (this.getConfig().getBoolean("config.debug")) {
                System.out.println("[NovaCoupon - DEBUG] Database update failed. Already updated?");
            }
        }
    }
    
    public boolean coupon_code_inUse(String code) {
        try {
            res = stmt.executeQuery("SELECT * FROM `novacoupon`");
            while (res.next()) {
                if (res.getString(2).equalsIgnoreCase(code)) {
                    res.close();
                    return true;
                }
            }
            res.close();
            return false;
        } catch (Exception e) { /* IGNORED */ }
        return true;
    }
}
