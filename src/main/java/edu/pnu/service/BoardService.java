package edu.pnu.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.pnu.domain.board.Board;
import edu.pnu.dto.BoardDto;
import edu.pnu.dto.BoardHospitalSearch;
import edu.pnu.dto.BoardMemberSearch;
import edu.pnu.persistence.BoardRepository;
import jakarta.transaction.Transactional;
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
	
	// Member id로 불러 오기
	public Page<BoardDto> findByMemberUsername(BoardMemberSearch bms) {
		Pageable pageable = PageRequest.of(bms.getPage(), bms.getSize());
		return boardRepo.findByMemberUsername(bms.getUsername(), pageable).map(BoardDto::from);
	}
	
	// hospital id로 불러 오기
	public Page<BoardDto> findByBasicInfoHospitalId(BoardHospitalSearch bhs) {
		Pageable pageable = PageRequest.of(bhs.getPage(), bhs.getSize());
		return boardRepo.findByBasicInfoHospitalId(bhs.getHospitalId(), pageable).map(BoardDto::from);
	}
	
	// 새 리뷰 저장하기
	@Transactional
	public void saveReview(BoardDto dto) {
		// 입력값 검증 (내용이 비어있는지 확인)
		if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
	        throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
	    }
		// 현재 로그인한 사용자의 이름(username) 가져오기
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName(); // JWT 토큰에서 추출된 username
	    
	    // DTO를 Entity로 변환 (작성자 정보 포함)
	    Board board = Board.builder()
	            .content(dto.getContent())
	            .hospitalId(dto.getHospitalId())
	            .username(username)
	            .createDate(new Date())
	            .build();
		
		boardRepo.save(board);
	}
	
	// 리뷰 수정하기
	@Transactional
	public void updateReview(Long seq, BoardDto dto) {
	    // 기존 리뷰 찾기
	    Board board = boardRepo.findById(seq)
	            .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다."));

	    // 권한 확인 (작성자 본인 또는 관리자)
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String currentUsername = auth.getName();
	    
	    boolean isAdmin = auth.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	    if (board.getUsername().equals(currentUsername) || isAdmin) {
	        // 내용 업데이트
	        board.setContent(dto.getContent());
	    } else {
	        throw new RuntimeException("수정 권한이 없습니다.");
	    }
	}
	
	// 리뷰 삭제하기
	@Transactional
	public void deleteReview(Long seq) {
	    // 삭제할 리뷰 존재 확인
	    Board board = boardRepo.findById(seq)
	            .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

	    // 현재 로그인한 사용자 정보 및 권한 가져오기
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String currentUsername = auth.getName();
	    
	    // 사용자의 권한(Role) 확인 (예: ROLE_ADMIN)
	    boolean isAdmin = auth.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	    // 권한 체크: 작성자 본인이거나 관리자인 경우에만 삭제 허용
	    if (board.getUsername().equals(currentUsername) || isAdmin) {
	        boardRepo.delete(board);
	    } else {
	        throw new RuntimeException("삭제 권한이 없습니다.");
	    }
	}
}
