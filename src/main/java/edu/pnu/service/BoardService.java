package edu.pnu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.dto.BoardDto;
import edu.pnu.dto.BoardHospitalSearch;
import edu.pnu.dto.BoardMemberSearch;
import edu.pnu.persistence.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepo;
	
	// 전체 불러 오기
	public Page<BoardDto> getBoards(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return boardRepo.findAll(pageable).map(BoardDto::from);
	}
	
	// 특정 게시물 불러 오기
	public BoardDto getBoard(Long seq) {
		return boardRepo.findById(seq).map(BoardDto::from).get();
	}
	
	// Member id로 불러 오기
	public Page<BoardDto> findByMemberUsername(BoardMemberSearch bms) {
		Pageable pageable = PageRequest.of(bms.getPage(), bms.getSize());
		return boardRepo.findByMemberUsername(bms.getUsername(), pageable).map(BoardDto::from);
	}
	
	public Page<BoardDto> findByBasicInfoHospitalId(BoardHospitalSearch bhs) {
		Pageable pageable = PageRequest.of(bhs.getPage(), bhs.getSize());
		return boardRepo.findByBasicInfoHospitalId(bhs.getHospitalId(), pageable).map(BoardDto::from);
	}
}
