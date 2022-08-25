// 5) 로컬 클래스의 특징을 활용하여 바깥 변수에 값을 받는 코드를 제거한다.
//    왜? 컴파일러가 자동으로 그런 일을 할 코드를 생성해주기 때문이다.
package com.eomcs.concurrent;

public class Exam0250 {

  public static void main(String[] args) {

    int count = 1000;

    class MyRunnable implements Runnable {
      @Override
      public void run() {
        for (int i = 0; i < count; i++) {
          System.out.println("==> " + i);
        }
      }
    }

    //    new Thread(new MyRunnable(count)).start();
    new Thread(new MyRunnable()).start(); // 자동으로 count 준다

    for (int i = 0; i < count; i++) {
      System.out.println(">>> " + i);
    }
  }


}
