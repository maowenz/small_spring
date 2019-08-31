package starter;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.IOException;
import java.util.List;

import beans.BeanFactory;
import core.ClassScanner;
import web.handler.HandlerManager;
import web.server.TomcatServer;

/**
 * @author wzm
 * @date 2019年08月30日 16:34
 */
public class SmallApplication {

    public static void run (Class<?> clazz,String[] args) {
        System.out.println("hello small_spring");
        TomcatServer tomcat = new TomcatServer(args);
        try {
            //启动tomcat
            tomcat.startServer();
            //扫描启动类下所有的.class文件
            System.out.println(clazz.getPackage().getName());
            List<Class<?>> classList = ClassScanner.scanClass(clazz.getPackage().getName());
            //初始化bean工厂
            BeanFactory.initBean(classList);
            //解析所有.class文件，获得mappingHandler集合
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
