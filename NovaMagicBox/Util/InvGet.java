
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvGet {

    public static Inventory getInventory() {
        List<String> data = new ArrayList<String>();
        for (int x = 0; x < 54; x++) {
            String line = MagicBoxPlugin.getInstance().getConfig().getString("Items." + x);
            if (line != null) {
                data.add(line);
            } else {
                data.add("null");
            }
        }
        return Serialization.toInventory(data);
    }

    public static void saveVault(Inventory inventory) throws IOException {
        List<String> list = Serialization.toString(inventory);
        String[] ser = list.toArray(new String[list.size()]);
        for (int x = 0; x < ser.length; x++) {
            if (!ser[x].equalsIgnoreCase("null")) {
               MagicBoxPlugin.getInstance().getConfig().set("Items." + x, ser[x]);
            } else {
                MagicBoxPlugin.getInstance().getConfig().set("Items." + x, null);
            }
        }
        MagicBoxPlugin.getInstance().saveConfig();
    }
}