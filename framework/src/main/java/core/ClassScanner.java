package core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wzm
 * @date 2019年08月30日 14:34
 */
public class ClassScanner {

    public static List<Class<?>> scanClass(String packageName) throws IOException,
            ClassNotFoundException {
        //保存结果
        List<Class<?>> classList = new ArrayList<>();
        //文件名改为文件路径
        String path = packageName.replace(".","/");
        //获取默认的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //通过文件路径获取该文件夹下所有资源的URL
        Enumeration<URL> resources = classLoader.getResources(path);

        int index = 0;

        while (resources.hasMoreElements()) {
            //拿到下一个资源
            URL resource = resources.nextElement();
            //先判断是否是jar包，因为默认.class文件会被打包为jar
            if(resource.getProtocol().contains("jar")) {
                //把URL强转为jar包链接
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                //根据jar包获取jar包路径名
                String jarPath = jarURLConnection.getJarFile().getName();
                //把jar包下的所有类添加到保存结果的容器中
                classList.addAll(getClassFormJar(jarPath,path));
            }else{
                //TODO 也有可能不是jar文件
            }
        }

        return classList;
    }

    private static List<Class<?>> getClassFormJar(String jarPath, String path) throws IOException, ClassNotFoundException {
        //保存结果
        List<Class<?>> classes = new ArrayList<>();
        //创建对应jar包的句柄
        JarFile jarFile = new JarFile(jarPath);
        //拿到jar包中的所有文件
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            //拿到一个jar文件
            JarEntry jarEntry = jarEntries.nextElement();
            //获取文件名
            String entryName = jarEntry.getName();
            //判断是否是类文件
            if(entryName.startsWith(path) && entryName.endsWith(".class")) {
                //类路径
                String classFullName = entryName.replace(".","/").substring(0,entryName.length()-6);
                classes.add(Class.forName(classFullName));
            }
        }

        return classes;
    }

}
