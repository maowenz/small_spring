package web.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import web.mvc.Controller;
import web.mvc.RequestMapping;
import web.mvc.RequestParam;

/**
 * @author wzm
 * @date 2019年08月30日 15:37
 */
public class HandlerManager {

    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 处理类文件集合，挑出MappingHandler
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for(Class<?> clazz : classList) {
            if(clazz.isAnnotationPresent(Controller.class)) {
                parseHandlerFromController(clazz);
            }
        }
    }

    private static void parseHandlerFromController(Class<?> clazz) {
        //获取该controller中的所有方法
        Method[] methods = clazz.getMethods();
        //从中找出被RequestMapping的方法
        for(Method method : methods) {
            if(!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //拿到RequestMapping定义的uri
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //保存参数
            List<String> paramNameList = new ArrayList<>();
            for(Parameter parameter : method.getParameters()) {
                if(parameter.isAnnotationPresent(RequestParam.class)) {
                    //把有被RequestParam注解的参数添加入集合
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            //把参数集合转为数组，用于反射
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            //反射生成MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri,method,clazz,params);
            //把mappingHandler装入集合中
            mappingHandlerList.add(mappingHandler);
        }
    }

}
