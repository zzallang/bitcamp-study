// 4) static 중첩 클래스를 로컬 클래스로 만든다.
package com.eomcs.concurrent;

public class Exam0240 {

  public static void main(String[] args) {

    class MyRunnable implements Runnable {

      int count;
      public MyRunnable(int count) {
        this.count = count;
      }
      @Override
      public void run() {

        for (int i = 0; i < count; i++) {
          System.out.println("==> " + i);
        }

      }
    }

    int count = 1000;

    new Thread(new MyRunnable(count)).start();

    for (int i = 0; i < count; i++) {
      System.out.println(">>> " + i);
    }
  }


}
