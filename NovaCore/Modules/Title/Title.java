package net.novaprison.Modules.Title;

/*
public class Title implements Listener {

    public static int toAnnounce;
    public static int counter;
    public static int message;

    public static int in;
    public static int stay;
    public static int out;

    public static void setup()
    {
        File file = new File(Core.getInstance().getDataFolder() + File.separator + "titles.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        if(c.isSet("AutomaticCountdown")) {
            toAnnounce = c.getInt("AutomaticCountdown");
        } else {
            toAnnounce = 300;
            c.set("AutomaticCountdown", 300);
        }
        c.addDefault("FadeInTicks", 10);
        c.addDefault("StayTicks", 60);
        c.addDefault("FadeOutTicks", 10);
        c.addDefault("Titles.1.A", "Hi!");
        c.addDefault("Titles.1.B", "HHello!");

        c.options().copyDefaults(true);
        try {
            c.save(file);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        in = c.getInt("FadeInTicks");
        stay = c.getInt("StayTicks");
        out = c.getInt("FadeOutTicks");
        message = 0;
    }

    @EventHandler
    public void onSecond(ServerSecondEvent e)
    {
        if(!(counter == toAnnounce)) {
            counter++;
            return;
        }
        counter = 0;
        message++;

        FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder() + File.separator + "titles.yml"));
        if(!c.isSet("Titles." + message + ".A")) {
            message = 1;
        }
        String title = ChatColor.translateAlternateColorCodes('&', c.getString("Titles." + message + ".A"));
        String subtitle = ChatColor.translateAlternateColorCodes('&', c.getString("Titles." + message + ".B"));
        for(World world : Bukkit.getWorlds()) {
            for(Player player : world.getPlayers()) {
                NovaTitle.setTime(player, in, stay, out);
                NovaTitle.sendTitle(player, title, subtitle);
            }
        }
    }
}
*/