package edu.pnu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.domain.board.Board;
import edu.pnu.dto.BoardHospitalSearch;
import edu.pnu.dto.BoardMemberSearch;
import edu.pnu.persistence.BoardRepository;
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
	
	// Member id로 불러 오기
	public Page<Board> findByMemberUsername(BoardMemberSearch bms) {
		Pageable pageable = PageRequest.of(bms.getPage(), bms.getSize());
		return boardRepo.findByMemberUsername(bms.getUsername(), pageable);
	}
	
	// hospital id로 불러 오기
	public Page<Board> findByBasicInfoHospitalId(BoardHospitalSearch bhs) {
		Pageable pageable = PageRequest.of(bhs.getPage(), bhs.getSize());
		return boardRepo.findByBasicInfoHospitalId(bhs.getHospitalId(), pageable);
	}
}
