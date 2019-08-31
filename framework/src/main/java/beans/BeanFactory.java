package beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import web.mvc.Controller;

/**
 * @author wzm
 * @date 2019年08月30日 15:10
 */
public class BeanFactory {

    private static Map<Class<?>,Object> classToBean = new ConcurrentHashMap<>();

    /**
     * 根据class类型获取bean
     * @param clazz
     * @return
     */
    public static Object getBean(Class<?> clazz) {
        return classToBean.get(clazz);
    }

    public static void initBean(List<Class<?>> classList) throws Exception {
        //创建副本
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for(int i = 0;i<toCreate.size();i++) {
                if(finishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            if(toCreate.size() == remainSize) {
                throw new Exception("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> aClass) throws IllegalAccessException,
            InstantiationException {
        //创建的bean实例仅包括Bean和Controller注释的类
        if(aClass.isAnnotationPresent(Bean.class) && aClass.isAnnotationPresent(Controller.class)) {
            return true;
        }
        //创建实例对象
        Object bean = aClass.newInstance();
        //看看实例对象是否需要执行依赖注入，注入其他bean
        for(Field field : aClass.getDeclaredFields()) {
            if(field.isAnnotationPresent(AutoWired.class)) {
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);
                //如果要注入的bean还未被创建就先跳过
                if(reliantBean == null) {
                    return false;
                }
                field.setAccessible(true);
                field.set(bean,reliantBean);
            }
        }

        classToBean.put(aClass,bean);
        return true;
    }

}
