package fr.volax.anezia.nbt;

import java.lang.reflect.Constructor;

public enum ObjectCreator {
    NMS_NBTTAGCOMPOUND(ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), new Class[0]);

    private Constructor<?> construct;

    ObjectCreator(Class<?> clazz, Class<?>... args) {
        try {
            this.construct = clazz.getConstructor(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object getInstance(Object... args) {
        try {
            return this.construct.newInstance(args);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}