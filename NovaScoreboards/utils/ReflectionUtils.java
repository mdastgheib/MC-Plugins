package com.nova.novascoreboards.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


@SuppressWarnings("rawtypes")
public class ReflectionUtils {
    private static String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
    private static String NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");

    /**
     * Get a class from OBC.
     *
     * @param className - The name of the class.
     * @return The class from OBC package.
     */
    public static Class getBukkitClass(String className) {
        try {
            return Class.forName(OBC_PREFIX + "." + className);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get a field from a class by the field name.
     *
     * @param clazz - The class.
     * @param fieldName - The name of the method.
     * @return The field from a class.
     */
    public static FieldAccess getField(Class clazz, String fieldName) {
        if (clazz != null && fieldName != null) {
            do {
                try {
                    Field field = clazz.getField(fieldName);
                    if (field == null) field = clazz.getDeclaredField(fieldName);
                    if (field != null) return new FieldAccess(field);
                } catch (NoSuchFieldException ex) {
                    try {
                        Field field = clazz.getDeclaredField(fieldName);
                        if (field != null) return new FieldAccess(field);
                    } catch (Exception ex2) {
                    }
                } catch (Exception ex) {
                }
            } while (clazz.getSuperclass() != Object.class && ((clazz = clazz.getSuperclass()) != null));
        }
        return null;
    }

    /**
     * Get a method from a class by the method name.
     *
     * @param clazz - The class.
     * @param methodName - The name of the method.
     * @return The method from a class.
     */
    @SuppressWarnings("unchecked")
    public static MethodInvoker getMethod(Class clazz, String methodName, Class... parameterTypes) {
        if (clazz != null && methodName != null) {
            methodName = methodName.replace("(", "");
            methodName = methodName.replace(")", "");
            try {
                Method method = clazz.getMethod(methodName, parameterTypes);
                if (method == null) method = clazz.getDeclaredMethod(methodName, parameterTypes);
                if (method != null) return new MethodInvoker(method);
                else if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class)
                    return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
            } catch (NoSuchMethodException ex) {
                try {
                    Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    if (method != null) return new MethodInvoker(method);
                    else if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class)
                        return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
                } catch (Exception ex2) {
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
     * Get a class from NMS.
     *
     * @param className - The name of the class.
     * @return The class from NMS package.
     */
    public static Class getMinecraftClass(String className) {
        try {
            return Class.forName(NMS_PREFIX + "." + className);
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Check if a class is a subclass of a super class.
     * @param clazz - The class.
     * @param superClass - The super class.
     * @return Whether the class is a subclass of a super class.
     */
    public static boolean instanceOf(Class clazz, Class superClass) {
        return clazz != null && superClass != null && clazz.isAssignableFrom(superClass);
    }

    public static interface ReflectionAccess {
        boolean wasAccessible = false;

        public boolean isAccessible();

        public ReflectionAccess setAccessible();

        public ReflectionAccess setAccessible(boolean flag);

        public boolean wasAccessible();
    }

    public static class MethodInvoker implements ReflectionAccess {
        private Method method = null;
        private boolean wasAccessible = false;

        public MethodInvoker(Method method) {
            this(method, method.isAccessible());
        }

        public MethodInvoker(Method method, boolean wasAccessible) {
            this.method = method;
            this.wasAccessible = wasAccessible;
        }

        public Method getMethod() {
            return this.method;
        }

        public Object invoke(Object instance, Object... paramValues) throws Exception {
            this.method.setAccessible(true);
            Object invoked = this.method.invoke(instance, paramValues);
            this.method.setAccessible(this.wasAccessible());
            return invoked;
        }

        @Override
        public boolean isAccessible() {
            return this.method.isAccessible();
        }

        @Override
        public MethodInvoker setAccessible() {
            return this.setAccessible(this.wasAccessible);
        }

        @Override
        public MethodInvoker setAccessible(boolean flag) {
            this.method.setAccessible(flag);
            return this;
        }

        @Override
        public boolean wasAccessible() {
            return this.wasAccessible;
        }
    }

    public static class FieldAccess implements ReflectionAccess {
        private Field field = null;
        private boolean wasAccessible = false;

        public FieldAccess(Field method) {
            this(method, method.isAccessible());
        }

        public FieldAccess(Field method, boolean wasAccessible) {
            this.field = method;
            this.wasAccessible = wasAccessible;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(Class unused) throws Exception {
            return unused == String.class ? (T) this.getObject(null).toString() : (unused == Integer.class ? (T) new Integer(Integer.parseInt(this.getObject(null).toString())) : unused == Boolean.class ? (T) new Boolean(Boolean.parseBoolean(this.getObject(null).toString())) : (T) this.getObject(null));
        }

        public Object getObject(Object instance) throws Exception {
            this.field.setAccessible(true);
            try {
                Object value = this.field.get(instance);
                this.field.setAccessible(this.wasAccessible());
                return value;
            } catch (Exception ex) {
                this.field.setAccessible(this.wasAccessible());
                throw ex;
            }
        }

        public Field getField() {
            return this.field;
        }

        @Override
        public boolean isAccessible() {
            return this.field.isAccessible();
        }

        public void set(Object value) throws Exception {
            this.set(null, value);
        }

        public void set(Object instance, Object value) throws Exception {
            this.field.setAccessible(true);
            this.field.set(instance, value);
            this.field.setAccessible(this.wasAccessible());
        }

        @Override
        public FieldAccess setAccessible() {
            return this.setAccessible(this.wasAccessible);
        }

        @Override
        public FieldAccess setAccessible(boolean flag) {
            this.field.setAccessible(flag);
            return this;
        }

        @Override
        public boolean wasAccessible() {
            return this.wasAccessible;
        }
    }
}