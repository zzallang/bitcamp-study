package com.bitcamp.board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

public class ServerApp {

  public static Stack<String> breadcrumbMenu = new Stack<>();

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(8888)) {
      System.out.println("서버 실행 중...");

      while (true) {
        Socket socket = serverSocket.accept();

        new Thread(() -> {
          // 스레드를 시작하는 순간, 별도의 실행 흐름에서 병행으로 실행된다.

          try (
              DataOutputStream out = new DataOutputStream(socket.getOutputStream());
              DataInputStream in = new DataInputStream(socket.getInputStream())) {
            System.out.println("클라이언트 접속!");

            boolean first = true;

            while (true) {
              StringWriter strOut = new StringWriter();
              PrintWriter tempOut = new PrintWriter(strOut);

              if (first) {
                welcome(tempOut);
                first = false;
              }

              printMainMenus(tempOut);
              out.writeUTF(strOut.toString());
              // 클라이언트로 응답한 후에 새 출력 스트림으로 교체한다.

              String request = in.readUTF();
              if (request.equals("quit")) {
                break;
              }

              out.writeUTF(request);
            }

            System.out.println("클라이언트에게 접속 종료!");

          } catch (Exception e) {
            System.out.println("클라이언트와 통신 중 오류 발생");
            e.printStackTrace();
          }
        }).start();
      }

      //      System.out.println("서버 종료!");

    } catch (Exception e) {
      System.out.println("서버 실행 중 오류 발생!");
      e.printStackTrace();
    }
  }

  /*
  public static void main2(String[] args) {
    try (
        // Dao가 사용할 Connection 객체 준비
        Connection con = DriverManager.getConnection(
            "jdbc:mariadb://localhost:3306/studydb","study","1111")) {

      System.out.println("[게시글 관리 클라이언트]");
      System.out.println();

      welcome();

      // DAO 객체를 준비한다.
      MariaDBMemberDao memberDao = new MariaDBMemberDao(con);
      MariaDBBoardDao boardDao = new MariaDBBoardDao(con);

      // 핸들러를 담을 컬렉션을 준비한다.
      ArrayList<Handler> handlers = new ArrayList<>();
      handlers.add(new BoardHandler(boardDao));
      handlers.add(new MemberHandler(memberDao));

      // "메인" 메뉴의 이름을 스택에 등록한다.
      breadcrumbMenu.push("메인");
      System.out.println();


      loop: while (true) {

        // 메인 메뉴 출력
        printTitle();
        printMenus(menus);
        System.out.println();

        try { // 특정 명령어에 대한 예외처리 try

          if (mainMenuNo < 0 || mainMenuNo > menus.length) {
            System.out.println("메뉴 번호가 옳지 않습니다!");
            continue; // while 문의 조건 검사로 보낸다.

          } else if (mainMenuNo == 0) {
            break loop;
          }

          // 메뉴에 진입할 때 breadcrumb 메뉴바에 그 메뉴를 등록한다.
          breadcrumbMenu.push(menus[mainMenuNo - 1]);

          // 메뉴 번호로 Handler 레퍼런스에 들어있는 객체를 찾아 실행한다.
          handlers.get(mainMenuNo - 1).execute();

          breadcrumbMenu.pop();

        } catch (Exception ex) {
          System.out.println("입력 값이 옳지 않습니다.");
        } // 안쪽 try

      } // while
      Prompt.close();

      System.out.println("종료!");

    } catch (Exception e) {
      System.out.println("시스템 오류 발생");
      e.printStackTrace();
    } // 바깥쪽 try
  }
   */

  static void welcome(PrintWriter out) throws Exception {
    out.println("[게시판 애플리케이션]");
    out.println();
    out.println("환영합니다!");
    out.println();
  }


  static void printMainMenus(PrintWriter out) {
    // 메인 메뉴 목록 준비
    String[] menus = {"게시판", "회원"};

    // 메뉴 목록 출력
    for (int i = 0; i < menus.length; i++) {
      out.printf("  %d: %s\n", i + 1, menus[i]);
    }

    // 메뉴 번호 입력을 요구하는 문장 출력
    out.printf("메뉴를 선택하세요[1..%d](0: 종료) ", menus.length);
  }

  protected static void printTitle() {
    StringBuilder builder = new StringBuilder();
    for (String title : breadcrumbMenu) {
      if (!builder.isEmpty()) {
        builder.append(" > ");
      }
      builder.append(title);
    }
    System.out.printf("%s:\n", builder.toString());
  }

} 
