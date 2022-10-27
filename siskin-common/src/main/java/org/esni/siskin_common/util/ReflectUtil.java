package org.esni.siskin_common.util;


public class ReflectUtil {

    @SuppressWarnings("unchecked")
    public static <T> T reflect (String reference) {

        try {
            return (T) Class.forName(reference).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;

    }

}
