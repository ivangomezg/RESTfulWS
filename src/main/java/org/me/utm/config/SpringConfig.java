package org.me.utm.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.MultipartConfigElement;

import org.me.utm.config.RootContextConfig;
import org.me.utm.config.ServletContextConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringConfig implements WebApplicationInitializer{
	
	@Override
	public void onStartup(ServletContext ctx) throws ServletException {
		AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext();
		rootCtx.register(RootContextConfig.class);
        ctx.addListener(new ContextLoaderListener(rootCtx));
        
        AnnotationConfigWebApplicationContext servletCtx = new AnnotationConfigWebApplicationContext();
        servletCtx.register(ServletContextConfig.class);
        
        rootCtx.setServletContext(ctx);
        
        ServletRegistration.Dynamic servlet = ctx.addServlet("dispatcher", new DispatcherServlet(rootCtx));
        servlet.setLoadOnStartup(2);
        servlet.addMapping("/");
        
        System.out.println("Initialize H2 console");
        ServletRegistration.Dynamic h2ConsoleServlet = ctx.addServlet("H2Console", new org.h2.server.web.WebServlet());
        h2ConsoleServlet.addMapping("/h2-console/*");
        h2ConsoleServlet.setLoadOnStartup(1);
        
        servlet.setMultipartConfig(new MultipartConfigElement(
                null, 20_971_520L, 41_943_040L, 512_000
        ));
	}

}
