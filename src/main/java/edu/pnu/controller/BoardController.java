package edu.pnu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.board.Board;
import edu.pnu.service.BoardService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {
	private final BoardService boardService;

	@GetMapping("/review")
	public ResponseEntity<?> getBoards(Long id, String alias, String username) {
		Map<String, Object> ret= new HashMap<>();
		ret.put("key", "board");
		
		if(id != null) {
			List<Board> list= new ArrayList<>();
			list.add(boardService.getBoard(id));
			ret.put("jsondata", list);
		} else if(alias != null) {
			ret.put("jsondata", boardService.getBoardByAlias(alias));
		} else if(username != null) {
			ret.put("jsondata", boardService.getBoardByUsername(username));
		} else	{
			ret.put("jsondata", boardService.getBoards());
		}
		return ResponseEntity.ok(ret); 
	}
}