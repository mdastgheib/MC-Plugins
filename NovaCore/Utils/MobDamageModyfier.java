package net.novaprison.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;

public class MobDamageModyfier {

    private static Object damageField;
    private static Method getLivingEntityHandle;
    private static Method getAttributeInstance;
    private static Method setValue;

    static{
        try{
            damageField = getMCClass("GenericAttributes").getDeclaredField("e").get(null);
            getLivingEntityHandle = getCraftClass("entity.CraftLivingEntity").getMethod("getHandle");
            getAttributeInstance = getMCClass("EntityLiving").getMethod("getAttributeInstance", getMCClass("IAttribute"));
            setValue = getMCClass("AttributeInstance").getMethod("setValue", double.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void setDamage(LivingEntity e, double damage){
        try {
            Object handle = getLivingEntityHandle.invoke(e);
            Object attributeInstance = getAttributeInstance.invoke(handle, damageField);
            setValue.invoke(attributeInstance, damage);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private static Class<?> getMCClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String className = "net.minecraft.server." + version + name;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private static Class<?> getCraftClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String className = "org.bukkit.craftbukkit." + version + name;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}