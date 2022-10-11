package com.bitcamp.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.bitcamp.board.domain.AttachedFile;
import com.bitcamp.board.domain.Board;
import com.bitcamp.board.domain.Member;
import com.bitcamp.board.service.BoardService;

@Controller // 페이지 컨트롤러에 붙이는 애노테이션
public class BoardFileDeleteController {

  BoardService boardService;

  public BoardFileDeleteController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping("/board/fileDelete") // 요청이 들어 왔을 때 호출될 메소드에 붙이는 애노테이션
  public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int no = Integer.parseInt(request.getParameter("no")); // 여기서 받는 번호는 삭제할 첨부파일 번호 

    // 첨부파일 정보를 가져온다.
    AttachedFile attachedFile = boardService.getAttachedFile(no);

    // 게시글의 작성자가 로그인 사용자인지 검사한다.
    Member loginMember = (Member) request.getSession().getAttribute("loginMember");
    Board board = boardService.get(attachedFile.getBoardNo());

    if (board.getWriter().getNo() != loginMember.getNo()) {
      throw new Exception("게시글 작성자가 아닙니다.");
    }

    if (!boardService.deleteAttachedFile(no)) {
      throw new Exception("게시글 첨부파일 삭제할 수 없습니다.");
    } 

    return "redirect:detail?no=" + board.getNo();

  }
}
