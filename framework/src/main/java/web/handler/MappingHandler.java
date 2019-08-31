package web.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import beans.BeanFactory;

/**
 * @author wzm
 * @date 2019年08月30日 15:01
 */
public class MappingHandler {

    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    MappingHandler(String uri,Method method,Class<?> controller,String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    public boolean handle(ServletRequest request, ServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {

        //拿到请求的uri
        String requestUri = ((HttpServletRequest)request).getRequestURI();
        if(!uri.equals(requestUri)) {
            return false;
        }

        Object[] params = new Object[args.length];
        for (int i = 0;i<args.length;i++) {
            params[i] = request.getParameter(args[i]);
        }
        Object ctl = BeanFactory.getBean(controller);

        Object res = method.invoke(ctl,params);

        response.getWriter().println(res.toString());

        return true;

    }
}
