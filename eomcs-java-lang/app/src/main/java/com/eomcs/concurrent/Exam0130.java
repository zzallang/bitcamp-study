// 3) 인스턴스 생성 후 즉시 메소드 호출하기
//
package com.eomcs.concurrent;

public class Exam0130 {

  public static void main(String[] args) {

    int count = 1000;

    new MyThread(count).start();

    for (int i = 0; i < count; i++) {
      System.out.println(">>> " + i);
    }
  }
}

