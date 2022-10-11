package com.bitcamp.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.bitcamp.board.service.BoardService;

@Controller // 페이지 컨트롤러에 붙이는 애노테이션
//- 애노테이션을 붙일 때 객체 이름을 명ㅎ시하면 그 이름으로 저장한다.
//- 프론트 컨트롤러는 페이지 컨트롤러를 찾을 때 이 이름으로 찾을 것이다.
public class BoardListController {

  BoardService boardService;

  public BoardListController(BoardService boardService) {
    System.out.println("BoardListController() 호출됨!");
    this.boardService = boardService;
  }

  @GetMapping("/board/list") // 요청이 들어 왔을 때 호출될 메소드에 붙이는 애노테이션
  public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    req.setAttribute("boards", boardService.list());
    return "/board/list.jsp";
  }
}
