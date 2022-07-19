package com.bitcamp.board;

public class BoardTest {
  public static void main(String[] args) {
    Board b1;
    Board b2; // b1, b2, b3 레퍼런스 변수

    b1 = new Board();
    b1.no = 1;

    b2 = b1;
    b2.no = 100;

    b1 = new Board();
    b2 = b1;

    System.out.println(b1.no);
  }
}
