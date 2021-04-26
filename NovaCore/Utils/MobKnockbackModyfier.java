package net.novaprison.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;

public class MobKnockbackModyfier {

    private static Object KnockBackField;
    private static Method getLivingEntityHandle;
    private static Method getAttributeInstance;
    private static Method setValue;

    static{
        try{
            KnockBackField = getMCClass("GenericAttributes").getDeclaredField("c").get(null);
            getLivingEntityHandle = getCraftClass("entity.CraftLivingEntity").getMethod("getHandle");
            getAttributeInstance = getMCClass("EntityLiving").getMethod("getAttributeInstance", getMCClass("IAttribute"));
            setValue = getMCClass("AttributeInstance").getMethod("setValue", double.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void setKnockBackResistance(LivingEntity e, double KnockBack){
        try {
            Object handle = getLivingEntityHandle.invoke(e);
            Object attributeInstance = getAttributeInstance.invoke(handle, KnockBackField);
            setValue.invoke(attributeInstance, KnockBack);
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