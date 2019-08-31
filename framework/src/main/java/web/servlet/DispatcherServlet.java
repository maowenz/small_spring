package web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import web.handler.HandlerManager;
import web.handler.MappingHandler;

/**
 * @author wzm
 * @date 2019年08月30日 16:16
 */
public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("测试有多少个mappingHandler："+ HandlerManager.mappingHandlerList.size());
        for (MappingHandler mappingHandler : HandlerManager.mappingHandlerList) {
            try{
                if(mappingHandler.handle(servletRequest,servletResponse)) {
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
