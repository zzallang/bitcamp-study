package com.bitcamp.board.domain;

import java.io.Serializable;

// java.io.Serializable 인터페이스 (메소드 없음)
// - 인스턴스를 통째로 입출력 할 수 있도록 표시하는 용도
// - 인터페이스에 추상 메소드가 선언되어 있지 않기 때문에 따로 메소드를 구현 불필요
//
public class Board implements Serializable {
  // java.io.Serializable 인터페이스 구현
  // Board 클래스의 필드 값을 통째로 입출력 할 수 있다고 설정하는 용도로

  private static final long serialVersionUID = 1L;
  // serialBersionUID 필드 설정
  // - 인스턴스를 저장하고 읽을 때 클래스의 변화 여부를 검증하기 위해 버전 번호를 지정한다.
  // - 처음 버전 번호를 부여한 후 클래스에 필드를 추가하거나 삭제할 때 마다
  //   버전 번호를 증가시킨다.

  public int no;
  public String title;
  public String content;
  public String writer;
  public String password;
  public int viewCount;
  public long createdDate;

  @Override
  public String toString() {
    return "Board [no=" + no + ", title=" + title + ", content=" + content + ", writer=" + writer
        + ", password=" + password + ", viewCount=" + viewCount
        + ", createdDate=" + new java.sql.Date(createdDate) + "]";
  }

  // GoF의 Factory Method Pattern
  // - 객체의 생성 과정이 복잡할 때 별도의 메소드로 캡슐화 한다.
  //
  public static Board create(String csv) { // 인스턴스 만들어야 하고 호출해야 하니까 static
    String[] values = csv.split(",");

    Board board = new Board();
    board.no = Integer.parseInt(values[0]); // 문자열을 숫자로 바꾸는 거
    board.title = values[1];
    board.content = values[2];
    board.writer = values[3];
    board.password = values[4];
    board.viewCount = Integer.parseInt(values[5]);
    board.createdDate = Long.parseLong(values[6]);

    return board;
  }

  // GRASP의 Expert Pattern 
  // => 정보 생성은 해당 데이터를 갖고 있는 전문가에게 맡긴다.
  public String toCsv() { // 인스턴스 필드를 생성하는 코드
    return String.format("%d,%s,%s,%s,%s,%d,%d", // 문자열'만' 리턴하기 때문에 출력하는 쪽에서 \n
        this.no,
        this.title,
        this.content,
        this.writer,
        this.password,
        this.viewCount,
        this.createdDate);
  }
}
