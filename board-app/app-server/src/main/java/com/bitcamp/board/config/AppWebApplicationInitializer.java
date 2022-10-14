package com.bitcamp.board.config;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import com.bitcamp.board.filter.AdminCheckFilter;
import com.bitcamp.board.filter.LoginCheckFilter;

// 서블릿 컨테이너에서 웹 애플리캐이션을 시작할 때:
// ===> SpringServletContainerInitailizer.onStartUp() 호출
//      ===> WebApplicationInitiailizer 구현체의 onStartUp() 호출
// 
//@MultipartConfig(maxFileSize = 1024 * 1024 * 10) 
public class AppWebApplicationInitializer implements WebApplicationInitializer {
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    System.out.println("WebApplicationInitializer.onStart()");

    // 웹 관련 컴포넌트를 다룰 수 있는 기능이 포함된 스프링 IoC 컨테이너 준비
    AnnotationConfigWebApplicationContext iocContainer = 
        new AnnotationConfigWebApplicationContext();
    iocContainer.register(AppConfig.class);

    // 웹 애플리케이션의 루트 경로를 ServletContext 보관소에 저장해 둔다.
    servletContext.setAttribute("contextPath", servletContext.getContextPath());

    DispatcherServlet servlet = new DispatcherServlet(iocContainer);
    Dynamic config = servletContext.addServlet("app", servlet);
    config.setLoadOnStartup(1);
    config.addMapping("/app/*");

    // 1) 멀티파트 설정 정보를 애노테이션에서 가져오기
    //    config.setMultipartConfig(new MultipartConfigElement(
    //        this.getClass().getAnnotation(MultipartConfig.class)));
    // 2) 멀티파트 설정 정보를 직접 지정하기
    config.setMultipartConfig(new MultipartConfigElement(
        System.getProperty("java.io.tmpdir"), // 클라이언트가 보낸 파일을 임시 저장할 디렉토리
        1024 * 1024 * 10, // 파일 크기
        1024 * 1024 * 10 * 5, // 첨부 파일을 포함한 전체 요청 데이터의 크기
        1024 * 1024 // 첨부 파일을 일시적으로 보관할 버퍼 크기
        ));

    // "app" 이름의 프론트 컨트롤러에 필터를 붙인다.
    CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8");
    FilterRegistration.Dynamic filterConfig = servletContext.addFilter("CharacterEncodingFilter", filter); // 필터를 설정하는 설정 객체를 리턴
    filterConfig.addMappingForServletNames( // 설정 객체에 서블릿 필터로 연결하겠다.
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
        false, // 서블릿을 사용하기 전에 이 필터를 적용하라
        "app"); // 필터 이름을 적용했기 때문에 다른 서블릿은 안됨!

    // 특정 URL에 필터를 붙인다.
    AdminCheckFilter adminFilter = new AdminCheckFilter();
    FilterRegistration.Dynamic adminFilterConfig = servletContext.addFilter("AdminCheckFilter", adminFilter);
    adminFilterConfig.addMappingForUrlPatterns(
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
        false,
        "/app/member/*");

    LoginCheckFilter loginFilter = new LoginCheckFilter();
    FilterRegistration.Dynamic loginFilterConfig = servletContext.addFilter("LoginCheckFilter", loginFilter);
    loginFilterConfig.addMappingForUrlPatterns(
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
        false,
        "/app/*");
  }
}