package com.bitcamp.board.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

// 스프링 IoC 컨테이너의 설정을 수행하는 클래스
// 1) DB 커넥션 객체 관리자 준비 : DataSource
// 2) 트랜젝션 관리자 준비: TransactionManager
// 3) 어떤 패키지에 있는 객체를 자동으로 생성할 것인지 지정한다.

@ComponentScan(value="com.bitcamp.board")
// - com.bitcamp.board 패키지 및 그 하위 패키지에 소속된 클래스 중에서
//   @Component, @Controller, @Service, @Repository 등의 애노테이션이 붙은 클래스를 찾아
//   객체를 생성한다.
public class AppConfig {

  public AppConfig() {
    System.out.println("AppConfig() 생성자 호출됨!");
  }

  @Bean("TransactionManager")
  public PlatformTransactionManager createTransactionManager(DataSource ds) {
    // Spring IoC 컨테이너는 이 메소드를 호출하기 전에
    // 이 메소드가 원하는 파라미터 값인 DataSource를 먼저 생선한다.
    // => createDataSouce() 메소드를 먼저 호출한다.
    System.out.println("createTransactionManager() 호출됨!");

    return new DataSourceTransactionManager(ds);
  }

  // DataSource를 생성하는 메소드를 호출하라고 애노테이션으로 표시한다.
  // 메소드가 리턴한 객체는 @Bean 애노테이션에 지정된 이름으로 컨테이너에 보관될 것이디.
  @Bean("DataSource")
  public DataSource createDataSource() {
    System.out.println("createDataSource() 호출됨!");

    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.mariadb.jdbc.Driver");
    ds.setUrl("jdbc:mariadb://localhost:3306/studydb");
    ds.setUsername("study");
    ds.setPassword("1111");
    return ds;
  }

  // multipart/form-data 형식으로 보내온 요청 데이터를
  // 도메인 객체로 받는 일을 할 도우미 객체를 등록한다.
  // 이 객체가 등록된 경우 multipart/form-data를 도메인 객체로 받을 수 있다.
  @Bean("multipartResolver")
  public MultipartResolver createMultipartResolver() {
    return new StandardServletMultipartResolver();
  }

  // Spring WebMVC의 기본 ViewResolver를 교체한다.
  @Bean("viewResolver")
  public ViewResolver createViewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setViewClass(JstlView.class); // 주어진 URL을 처리할 객체 -> JSP를 실행시켜주는 객체
    viewResolver.setPrefix("/");
    viewResolver.setSuffix(".jsp");
    return viewResolver;
  }
}
