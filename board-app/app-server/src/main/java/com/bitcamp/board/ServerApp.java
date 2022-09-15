package com.bitcamp.board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import com.bitcamp.board.dao.BoardDao;
import com.bitcamp.board.dao.MariaDBBoardDao;
import com.bitcamp.board.dao.MariaDBMemberDao;
import com.bitcamp.board.dao.MemberDao;
import com.bitcamp.board.handler.BoardHandler;
import com.bitcamp.board.handler.MemberHandler;
import com.bitcamp.handler.Handler;
import com.bitcamp.util.BreadCrumb;

public class ServerApp {

  // 메인 메뉴 목록 준비
  private String[] menus = {"게시판", "회원"};
  private int port;
  ArrayList<Handler> handlers = new ArrayList<>();

  public static void main(String[] args) {
    try {
      ServerApp app = new ServerApp(8888);
      app.execute();

    } catch (Exception e) {
      System.out.println("서버 실행 오류!");
    }
  }

  public ServerApp(int port) throws Exception {
    this.port = port;

    // Dao가 사용할 Connection 객체 준비
    Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb","study","1111");

    // DAO 객체를 준비한다.
    BoardDao boardDao = new MariaDBBoardDao(con);
    MemberDao memberDao = new MariaDBMemberDao(con);

    handlers.add(new BoardHandler(boardDao));
    handlers.add(new MemberHandler(memberDao));
  }

  public void execute() {
    try (ServerSocket serverSocket = new ServerSocket(this.port)) {
      System.out.println("서버 실행 중...");

      while (true) {
        new Thread(new ServiceProcessor(serverSocket.accept())).start(); 
        System.out.println("클라이언트 접속!");
      } // 반복
      //      System.out.println("서버 종료!");
    } catch (Exception e) {
      System.out.println("서버 실행 중 오류 발생!");
      e.printStackTrace();
    }
  }


  /*
  public static void main2(String[] args) {

      System.out.println("[게시글 관리 클라이언트]");
      System.out.println();

      welcome();

      // "메인" 메뉴의 이름을 스택에 등록한다.
      breadcrumbMenu.push("메인");
      System.out.println();


      loop: while (true) {

        // 메인 메뉴 출력
        printTitle();
        printMenus(menus);
        System.out.println();

        try { // 특정 명령어에 대한 예외처리 try

          // 메뉴에 진입할 때 breadcrumb 메뉴바에 그 메뉴를 등록한다.
          breadcrumbMenu.push(menus[mainMenuNo - 1]);

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

  static void welcome(DataOutputStream out) throws Exception {
    try (StringWriter strOut = new StringWriter();
        PrintWriter tempOut = new PrintWriter(strOut);) {
      tempOut.println("[게시판 애플리케이션]");
      tempOut.println();
      tempOut.println("환영합니다!");
      tempOut.println();
      out.writeUTF(strOut.toString());
    }
  }

  static void error (DataOutputStream out, Exception e) {
    try (StringWriter strOut = new StringWriter();
        PrintWriter tempOut = new PrintWriter(strOut);) {
      tempOut.printf("실행 오류 : %s\n", e.getMessage());
      tempOut.println();
      out.writeUTF(strOut.toString());
    } catch (Exception e2) {
      e2.printStackTrace();
    }
  }


  void printMainMenus(DataOutputStream out) throws Exception {
    try (StringWriter strOut = new StringWriter();
        PrintWriter tempOut = new PrintWriter(strOut);) {

      tempOut.println(BreadCrumb.getBreadCrumbOfCrrentThread().toString());

      for (int i = 0; i < menus.length; i++) {
        tempOut.printf("  %d: %s\n", i + 1, menus[i]);
      }
      // 메뉴 번호 입력을 요구하는 문장 출력
      tempOut.printf("\n 메뉴를 선택하세요[1..%d](quit: 종료)", menus.length);
      out.writeUTF(strOut.toString());
    }
  }

  void processMainMenu(DataInputStream in, DataOutputStream out, String request) {
    try {
      int menuNo = Integer.parseInt(request);
      if (menuNo < 1 || menuNo > menus.length) {
        throw new Exception ("메뉴 번호가 옳지 않습니다.");
      }

      BreadCrumb breadcrumb = BreadCrumb.getBreadCrumbOfCrrentThread();
      breadcrumb.put(menus[menuNo - 1]);

      handlers.get(menuNo - 1).execute(in, out);

      breadcrumb.pickUp();

      // 하위 메뉴에서 빠져나오면 현재의 경로를 출력한다.
      out.writeUTF(breadcrumb.toString());

    } catch (Exception e) {
      error(out, e);
    }
  }

  private class ServiceProcessor implements Runnable {

    Socket socket;

    public ServiceProcessor(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try (Socket s = this.socket;
          DataOutputStream out = new DataOutputStream(s.getOutputStream());
          DataInputStream in = new DataInputStream(s.getInputStream())) { // socket도 run() 실행이 끝나면 auto close() 하려고 변수에 담는 거임.

        // 접속한 클라이언트의 이동 경로를 보관할 breadcrumb 객체 준비
        BreadCrumb breadcrumb = new BreadCrumb(); // 현재 스레드 보관소에 저장된다.
        breadcrumb.put("메인");

        // 클라이언트에게 환영 메시지를 보낸다.
        welcome(out);

        while (true) {
          // 클라이언트가 보낸 요청 정보를 읽는다.
          String request = in.readUTF();

          if (request.equals("quit")) {
            break;

          } else if (request.equals("menu")) {
            printMainMenus(out);

          } else {
            processMainMenu(in, out, request);
          }
        }
        System.out.println("클라이언트에게 접속 종료!");

      } catch (Exception e) {
        System.out.println("클라이언트와 통신 중 오류 발생!");
        e.printStackTrace();
      }
    }
  }
}
