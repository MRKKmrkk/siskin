package org.esni.siskin_core.util;


public class ReflectUtil {

    public static <T> T reflect (String reference) {

        try {
            return (T) Class.forName(reference).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;

    }

}
