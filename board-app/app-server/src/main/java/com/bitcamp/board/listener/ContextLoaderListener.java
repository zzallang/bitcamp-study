package com.bitcamp.board.listener;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import com.bitcamp.board.config.AppConfig;
import com.bitcamp.board.filter.AdminCheckFilter;
import com.bitcamp.board.filter.LoginCheckFilter;

// 웹애플리케이션이 시작되었을 때 공유할 자원을 준비시키거나 해제하는 일을 한다.
//
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) 
@WebListener
public class ContextLoaderListener implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("공유 자원을 준비 중!!");
    try {
      // 웹 기능이 포함된 스프링 IoC 컨테이너 준비
      AnnotationConfigWebApplicationContext iocContainer = 
          new AnnotationConfigWebApplicationContext();
      iocContainer.register(AppConfig.class); // 자바 config 클래스의 설정된 대로 객체를 생성한다.

      ServletContext ctx = sce.getServletContext();

      // 자바 코드로 서블릿 객체를 직접 생성하여 서버에 등록하기
      DispatcherServlet servlet = new DispatcherServlet(iocContainer);
      Dynamic config = ctx.addServlet("app", servlet);
      config.addMapping("/app/*");
      config.setMultipartConfig(new MultipartConfigElement(
          this.getClass().getAnnotation(MultipartConfig.class)));
      config.setLoadOnStartup(1); // 웹 애플리케이션을 시작할 때 프론트 컨트롤러를 자동 생성.

      // "app" 이름의 프론트 컨트롤러에 필터를 붙인다.
      CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8");
      FilterRegistration.Dynamic filterConfig = ctx.addFilter("CharacterEncodingFilter", filter); // 필터를 설정하는 설정 객체를 리턴
      filterConfig.addMappingForServletNames( // 설정 객체에 서블릿 필터로 연결하겠다.
          EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
          false, // 서블릿을 사용하기 전에 이 필터를 적용하라
          "app"); // 필터 이름을 적용했기 때문에 다른 서블릿은 안됨!

      // 특정 URL에 필터를 붙인다.
      AdminCheckFilter adminFilter = new AdminCheckFilter();
      FilterRegistration.Dynamic adminFilterConfig = ctx.addFilter("AdminCheckFilter", adminFilter);
      adminFilterConfig.addMappingForUrlPatterns(
          EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
          false,
          "/app/member/*");

      LoginCheckFilter loginFilter = new LoginCheckFilter();
      FilterRegistration.Dynamic loginFilterConfig = ctx.addFilter("LoginCheckFilter", loginFilter);
      loginFilterConfig.addMappingForUrlPatterns(
          EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
          false,
          "/service/*");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
