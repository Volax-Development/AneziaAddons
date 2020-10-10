package fr.volax.anezia.nbt;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.Stack;

class NBTReflectionUtil {
    static Object getItemRootNBTTagCompound(Object nmsitem) {
        Class<?> clazz = nmsitem.getClass();
        try {
            Method method = clazz.getMethod("getTag", new Class[0]);
            return method.invoke(nmsitem, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getSubNBTTagCompound(Object compound, String name) {
        Class<?> c = compound.getClass();
        try {
            Method method = c.getMethod("getCompound", new Class[]{String.class});
            return method.invoke(compound, new Object[]{name});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Boolean valideCompound(NBTCompound comp) {
        Object root = comp.getCompound();
        if (root == null)
            root = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        return Boolean.valueOf((gettoCompount(root, comp) != null));
    }

    private static Object gettoCompount(Object nbttag, NBTCompound comp) {
        Stack<String> structure = new Stack<>();
        while (comp.getParent() != null) {
            structure.add(comp.getName());
            comp = comp.getParent();
        }
        while (!structure.isEmpty()) {
            nbttag = getSubNBTTagCompound(nbttag, structure.pop());
            if (nbttag == null)
                return null;
        }
        return nbttag;
    }

    static String getContent(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null)
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        if (!valideCompound(comp).booleanValue())
            return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        try {
            Method method = workingtag.getClass().getMethod("get", new Class[]{String.class});
            return method.invoke(workingtag, new Object[]{key}).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static void remove(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null)
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        if (!valideCompound(comp).booleanValue())
            return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        ReflectionMethod.COMPOUND_REMOVE_KEY.run(workingtag, new Object[]{key});
        comp.setCompound(rootnbttag);
    }

    static Set<String> getKeys(NBTCompound comp) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null)
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        if (!valideCompound(comp).booleanValue())
            return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        return (Set<String>) ReflectionMethod.COMPOUND_GET_KEYS.run(workingtag, new Object[0]);
    }

    static void setData(NBTCompound comp, ReflectionMethod type, String key, Object data) {
        if (data == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null)
            rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        if (!valideCompound(comp).booleanValue())
            return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        type.run(workingtag, new Object[]{key, data});
        comp.setCompound(rootnbttag);
    }

    static Object getData(NBTCompound comp, ReflectionMethod type, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null)
            return null;
        if (!valideCompound(comp).booleanValue())
            return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        return type.run(workingtag, new Object[]{key});
    }
}