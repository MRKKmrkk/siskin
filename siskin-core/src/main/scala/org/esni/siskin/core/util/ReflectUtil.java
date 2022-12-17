package org.esni.siskin.core.util;

import org.esni.siskin.core.conf.SiskinConf;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.Objects;

public class ReflectUtil {

    private static final SiskinConf conf = SiskinConf.getOrCreate();

    private static void curLoadJar(File file, Method method, URLClassLoader loader) throws MalformedURLException, InvocationTargetException, IllegalAccessException {

        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.getName().endsWith(".jar")) {
                method.invoke(loader, f.toURI().toURL());
            }
            if (f.isDirectory()) {
                curLoadJar(f, method, loader);
            }
        }

    }

    /**
     * 将指定目录下的jar包动态载入当前jvm进程
     */
    public static void loadJar(String path) throws FileNotFoundException, NotDirectoryException, NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("path " + file.toString() + " not exists");
        }
        if (!file.isDirectory()) {
            throw new NotDirectoryException("path " + file.toString() + " need a directory");
        }

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        // 递归加载Jar包
        curLoadJar(file, method, loader);

        method.setAccessible(accessible);
    }

    /**
     * 通过反射，使用全类名实例化对象
     * 被实例化的类必须要有空参的构造方法
     *
     * @param className 需要实例化的全类名
     * @param <T> 实例化后的类型
     * @return 实例化后的对象
     */
    public static <T> T reflect(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (T) Class.forName(className).newInstance();
    }

    public static <T> T reflectAndReload(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, FileNotFoundException, NotDirectoryException, MalformedURLException, NoSuchMethodException {
        loadJar(conf.getString("lib.path", Paths.get(System.getenv("SISKIN_HOME"), "lib").toString()));
        return reflect(className);
    }

}
