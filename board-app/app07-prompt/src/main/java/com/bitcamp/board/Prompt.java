/*
 * 키보드 입력을 받는 도구를 구비하고 있다.
 * 비트캠프-20220704
 */
package com.bitcamp.board;

import java.io.Closeable;

public class Prompt {

  static java.util.Scanner keyboardInput = new java.util.Scanner(System.in);

  static int inputInt () {
    String str = keyboardInput.nextLine();

    // "123" ==> 123, "5" ==> 5, "ok" ==> 실행 오류
    return Integer.parseInt(str); 
  }

  static String inputString() {
    return keyboardInput.nextLine();
  }

  static void close() {
    keyboardInput.close();
  }
}