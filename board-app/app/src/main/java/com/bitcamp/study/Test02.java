package com.bitcamp.study;

import com.bitcamp.board.domain.Board;
import com.google.gson.Gson;

public class Test02 {

  public static void main(String[] args) {

    Board[] arr = new Board[3];

    Board b = new Board();
    b.no = 101;
    b.title = "제목";
    b.content = "내용";
    b.writer = "홍길동";
    b.password = "1111";
    b.viewCount = 11;
    b.createdDate = System.currentTimeMillis();
    arr[0] = b;


    b = new Board();
    b.no = 102;
    b.title = "제목2";
    b.content = "내용2";
    b.writer = "임꺽정";
    b.password = "1111";
    b.viewCount = 22;
    b.createdDate = System.currentTimeMillis();
    arr[1] = b;

    b = new Board();
    b.no = 103;
    b.title = "제목3";
    b.content = "내용3";
    b.writer = "유관순";
    b.password = "1111";
    b.viewCount = 3;
    b.createdDate = System.currentTimeMillis();
    arr[2] = b;

    // 배열 --> JSON 문자열
    Gson gson = new Gson();

    String json = gson.toJson(arr);
    System.out.println(json);

    // JSON --> 배열
    Board[] arr2 = gson.fromJson(json, Board[].class);
    for (Board e : arr2) {
      System.out.println(e);
    }

    System.out.println(arr == arr2);
  }
}
