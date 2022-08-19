package com.bitcamp.board.domain;

import java.io.Serializable;

public class Member implements Serializable {
  private static final long serialVersionUID = 1L;

  public int no;
  public String name;
  public String email;
  public String password;
  public long createdDate;

  public static Member create(String csv) {
    String[] values = csv.split(",");

    Member member = new Member();
    member.no = Integer.parseInt(values[0]);
    member.name = values[1];
    member.email = values[2];
    member.password = values[3];
    member.createdDate = Long.parseLong(values[4]);

    return member;
  }

  public String toCsv () {
    return String.format("%d,%s,%s,%s,%d",
        this.no,
        this.name,
        this.email,
        this.password,
        this.createdDate);
  }
}
