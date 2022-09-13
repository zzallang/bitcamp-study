package com.bitcamp.board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Stack;
import com.bitcamp.util.Prompt;

public class ClientApp {

  public static Stack<String> breadcrumbMenu = new Stack<>();

  public static void main(String[] args) {

    System.out.println("[게시글 관리 클라이언트]");
    System.out.println();

    try (Socket socket = new Socket("localhost", 8888);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

      String reponse = null;

      while (true) {
        reponse = in.readUTF();
        System.out.println(reponse);

        String input = Prompt.inputString("> ");
        out.writeUTF(input);

        if (input.equals("quit")) {
          break;
        }
      }

    } catch (Exception e) {
      System.out.println("서버와 통신 중 오류 발생!");
      e.printStackTrace();
    }

    //    loop: while (true) {
    //
    //      // 메인 메뉴 출력
    //      printTitle();
    //      printMenus(menus);
    //      System.out.println();
    //
    //      try { // 특정 명령어에 대한 예외처리 try
    //        int mainMenuNo = Prompt.inputInt(String.format(
    //            "메뉴를 선택하세요[1..%d](0: 종료) ", handlers.size()));
    //
    //        if (mainMenuNo < 0 || mainMenuNo > menus.length) {
    //          System.out.println("메뉴 번호가 옳지 않습니다!");
    //          continue; // while 문의 조건 검사로 보낸다.
    //
    //        } else if (mainMenuNo == 0) {
    //          break loop;
    //        }
    //
    //        // 메뉴에 진입할 때 breadcrumb 메뉴바에 그 메뉴를 등록한다.
    //        breadcrumbMenu.push(menus[mainMenuNo - 1]);
    //
    //        // 메뉴 번호로 Handler 레퍼런스에 들어있는 객체를 찾아 실행한다.
    //        handlers.get(mainMenuNo - 1).execute();
    //
    //        breadcrumbMenu.pop();
    //
    //      } catch (Exception ex) {
    //        System.out.println("입력 값이 옳지 않습니다.");
    //      } // 안쪽 try
    //
    //    } // while
    Prompt.close();

    System.out.println("종료!");
  } // 바깥쪽 try
} 
