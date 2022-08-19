package com.bitcamp.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

// 사용자 요청을 다룰 객체의 사용법을 정의한다.
//
public interface Servlet {
  String SUCCESS = "success"; 
  String FAIL = "fail"; // 인터페이스에 선언된 필드는 무조건 public static final임 생략 가능
  void service(DataInputStream in, DataOutputStream out);
}
