// 7) 익명 클래스를 코드를 메소드 파라미터에 직접 넣는다. 
//
package com.eomcs.concurrent;

public class Exam0270 {

  public static void main(String[] args) {

    int count = 1000;

    new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < count; i++) {
          System.out.println("==> " + i);
        }
      }
    }).start();
    // 인스턴스 주소만 넘어가는 거지 코드(인스턴스 클래스)가 넘어가는 것이 아님!

    for (int i = 0; i < count; i++) {
      System.out.println(">>> " + i);
    }
  }
}
