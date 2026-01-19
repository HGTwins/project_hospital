package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.board.Member;

public interface MemberRepository extends JpaRepository<Member, String>{
	public boolean existsByAlias(String alias);
}
