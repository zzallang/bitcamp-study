// 8) 익명 클래스를 람다 표현식으로 전환한다.
//
package com.eomcs.concurrent;

public class Exam0280 {

  public static void main(String[] args) {

    int count = 1000;

    new Thread(() ->{
      for (int i = 0; i < count; i++) {
        System.out.println("==> " + i);
      }
    }).start();

    for (int i = 0; i < count; i++) {
      System.out.println(">>> " + i);
    }
  }
}
