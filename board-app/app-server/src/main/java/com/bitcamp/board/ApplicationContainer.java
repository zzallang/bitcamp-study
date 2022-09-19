package com.bitcamp.board;

import static org.reflections.scanners.Scanners.TypesAnnotated;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import com.bitcamp.board.handler.ErrorHandler;
import com.bitcamp.board.servlet.Servlet;
import com.bitcamp.board.servlet.annotaion.Repository;
import com.bitcamp.board.servlet.annotaion.WebServlet;

public class ApplicationContainer {

  // 객체(DAO, Servlet)를 보관할 맵을 준비
  Map<String,Object> objMap = new HashMap<>();
  Reflections reflections;

  public ApplicationContainer(String packageName) throws Exception {
    reflections = new Reflections(packageName); 

    // Dao가 사용할 Connection 객체 준비
    Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb","study","1111");

    // DAO 객체를 찾아 맵에 보관한다.
    Set<Class<?>> classes = reflections.get(TypesAnnotated.with(Repository.class).asClass()); 
    for (Class<?> clazz : classes) {
      String objName = clazz.getAnnotation(Repository.class).value();
      Constructor<?> constructor = clazz.getConstructor(Connection.class);
      objMap.put(objName, constructor.newInstance(con));
    }

    Set<Class<?>> servlets = reflections.get(TypesAnnotated.with(WebServlet.class).asClass());
    for (Class<?> servlet : servlets) {
      String servletPath = servlet.getAnnotation(WebServlet.class).value();

      Constructor<?> constructor = servlet.getConstructors()[0];
      Parameter[] params = constructor.getParameters();

      if (params.length == 0) {
        objMap.put(servletPath, constructor.newInstance());
      } else {
        Object argument = findObject(objMap, params[0].getType());
        if (argument != null) {
          objMap.put(servletPath, constructor.newInstance(argument));
        }
      }
    }
  }

  public void execute(String path, String query, PrintWriter out) throws Exception {

    ErrorHandler errorHandler = new ErrorHandler();

    // query string을 분석하여 파라미터값을 맵에 저장한다.
    Map<String,String> paramMap = new HashMap<>();
    if (query != null && query.length() > 0) {
      String[] entries = query.split("&");
      for (String entry : entries) { // 예) no=1
        String[] kv = entry.split("=");
        paramMap.put(kv[0], URLDecoder.decode(kv[1],"UTF-8"));
      }
    }
    //    System.out.println(paramMap);

    //  경로에 해당하는 서블릿 객체를 찾아 실행한다.
    Servlet servlet = (Servlet) objMap.get(path);
    if (servlet != null) {
      servlet.service(paramMap, out);
    } else {
      errorHandler.service(paramMap, out);
    }
  }

  private static Object findObject(Map<String, Object> objMap, Class<?> type) {

    // 맵에 들어있는 객체를 모두 꺼낸다.
    Collection<Object> values = objMap.values();

    // 꺼낸 객체들 중에 해당 타입의 인스턴스가 있는지 알아 본다.
    for (Object value : values) {
      if (type.isInstance(value)) { // 주어진 타입과 일치하는 객체를 찾았다면
        return value; // 해당 객체를 리턴한다
      }
    }
    return null; // 못찾았으면 null을 리턴한다.
  }

}
