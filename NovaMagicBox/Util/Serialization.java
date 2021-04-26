

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.Map.Entry;

/**
 * Fancy JSON serialization mostly by evilmidget38.
 *
 * @author evilmidget38, gomeow
 */
public class Serialization {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    public static List<String> toString(Inventory inv) {
        List<String> result = new ArrayList<String>();
        List<ConfigurationSerializable> items = new ArrayList<ConfigurationSerializable>();
        Collections.addAll(items, inv.getContents());
        for (ConfigurationSerializable cs : items) {
            if (cs == null) {
                result.add("null");
            } else {
                result.add(new JSONObject(serialize(cs)).toString());
            }
        }
        return result;
    }

    public static Inventory toInventory(List<String> stringItems) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&b&lMagicBox Items&e&l"));
        List<ItemStack> contents = new ArrayList<ItemStack>();
        for (String piece : stringItems) {
            if (piece.equalsIgnoreCase("null")) {
                contents.add(null);
            } else {
                try {
                    ItemStack item = (ItemStack) deserialize(toMap(new JSONObject(piece)));
                    contents.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ItemStack[] items = new ItemStack[contents.size()];
        for (int x = 0; x < contents.size(); x++) {
            items[x] = contents.get(x);
        }
        inv.setContents(items);
        return inv;
    }

    public static Map<String, Object> serialize(ConfigurationSerializable cs) {
        Map<String, Object> returnVal = handleSerialization(cs.serialize());
        returnVal.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(cs.getClass()));
        return returnVal;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> handleSerialization(Map<String, Object> map) {
        Map<String, Object> serialized = recreateMap(map);
        for (Entry<String, Object> entry : serialized.entrySet()) {
            if (entry.getValue() instanceof ConfigurationSerializable) {
                entry.setValue(serialize((ConfigurationSerializable) entry.getValue()));
            } else if (entry.getValue() instanceof Iterable<?>) {
                List<Object> newList = new ArrayList<Object>();
                for (Object object : ((Iterable) entry.getValue())) {
                    if (object instanceof ConfigurationSerializable) {
                        object = serialize((ConfigurationSerializable) object);
                    }
                    newList.add(object);
                }
                entry.setValue(newList);
            } else if (entry.getValue() instanceof Map<?, ?>) {
                // unchecked cast here.  If you're serializing to a non-standard Map you deserve ClassCastExceptions
                entry.setValue(handleSerialization((Map<String, Object>) entry.getValue()));
            }
        }
        return serialized;
    }

    public static Map<String, Object> recreateMap(Map<String, Object> original) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(original);
        return map;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ConfigurationSerializable deserialize(Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map && ((Map) entry.getValue()).containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                entry.setValue(deserialize((Map) entry.getValue()));
            } else if (entry.getValue() instanceof Iterable) {
                entry.setValue(convertIterable((Iterable) entry.getValue()));
            }
        }
        return ConfigurationSerialization.deserializeObject(map);
    }

    private static List<?> convertIterable(Iterable<?> iterable) {
        List<Object> newList = new ArrayList<Object>();
        for (Object object : iterable) {
            if (object instanceof Map) {
                object = deserialize((Map<String, Object>) object);
            } else if (object instanceof List) {
                object = convertIterable((Iterable) object);
            }
            newList.add(object);
        }
        return newList;
    }
}

