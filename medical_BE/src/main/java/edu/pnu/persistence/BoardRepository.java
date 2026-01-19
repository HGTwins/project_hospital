package edu.pnu.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	// 멤버 아이디로 게시물 검색
	Page<Board> findByMemberUsername(
			@Param("username") String username, Pageable pageable);
	
	// hospital 아이디별로 게시물 검색
	Page<Board> findByBasicInfoHospitalId(
			@Param("hospitalId") Long hospitalId, Pageable pageable);
}
