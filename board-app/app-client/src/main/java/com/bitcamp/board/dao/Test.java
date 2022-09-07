package com.bitcamp.board.dao;

import java.util.List;
import com.bitcamp.board.domain.Member;

public class Test {

  public static void main(String[] args) throws Exception {
    MariaDBMemberDao dao = new MariaDBMemberDao();
    List<Member> list = dao.findAll();
    for (Member m : list) {
      System.out.println(m);
    }
    System.out.println("------------------------------------");

    //    Member memeber = new Member();
    //    memeber.name = "홍길동";
    //    memeber.email = "hong@test.com";
    //    memeber.password = "1111";
    //    dao.insert(memeber);

    //    dao.delete(6);

    Member member = new Member();
    member.no = 1;
    member.name = "xxxx";
    member.email = "xxxx@test.com";
    member.password = "2222";
    dao.update(member);

    Member member2 = dao.findByNo(1);
    System.out.println(member2);

    //    Member member = dao.findByNo(1);
    //    System.out.println(member);

    //    list = dao.findAll();
    //    for (Member m : list) {
    //      System.out.println(m);
    //    }
    //    System.out.println("------------------------------------");
  }

}
