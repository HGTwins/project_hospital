package edu.pnu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.pnu.domain.board.Board;
import edu.pnu.persistence.board.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepo;
	
	// 전체 불러 오기
	public List<Board> getBoards() {
		return boardRepo.findAll();
	}
	
	// 특정 게시물 불러 오기
	public Board getBoard(Long seq) {
		return boardRepo.findById(seq).get();
	}
	
	// Member 별명으로 불러 오기
	public List<Board> getBoardByAlias(String alias) {
		return boardRepo.findByMemberAlias(alias);
	}
	
	// Member id로 불러 오기
	public List<Board> getBoardByUsername(String username) {
		return boardRepo.findByMemberUsername(username);
	}
}
