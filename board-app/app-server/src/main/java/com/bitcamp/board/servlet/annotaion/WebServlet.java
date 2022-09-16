package com.bitcamp.board.servlet.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE) // 애노테이션을 붙일 수 있는 범위를 설정
@Retention(value = RetentionPolicy.RUNTIME) // 애노테이션 값으 추출할 때 지정
public @interface WebServlet {
  String value(); // 애노테이션 필수 속성을 설정

}
