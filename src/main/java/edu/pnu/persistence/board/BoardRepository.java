package edu.pnu.persistence.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findByMemberAlias(String alias);
	List<Board> findByMemberUsername(String username);
}
