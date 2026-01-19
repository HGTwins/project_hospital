/*
package edu.pnu;

import java.util.Date;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.pnu.domain.board.Board;
import edu.pnu.domain.board.Member;
import edu.pnu.domain.board.Role;
import edu.pnu.persistence.BoardRepository;
import edu.pnu.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberInit implements ApplicationRunner {

	private final MemberRepository memberRepo;
	private final BoardRepository boardRepo;
	private final PasswordEncoder encoder  = new BCryptPasswordEncoder();

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Member m1 = Member.builder().username("member").password(encoder.encode("abcd")).alias("사용자")
				.role(Role.ROLE_MEMBER).enabled(true).build();

		memberRepo.save(m1);

		Member m2 = Member.builder().username("admin").password(encoder.encode("abcd")).alias("최고관리자")
				.role(Role.ROLE_ADMIN).enabled(true).build();
		
		memberRepo.save(m2);

		for (int i = 0; i < 10; i++) {
			boardRepo.save(Board.builder().title("제목 " + i).content("내용 " + i).createDate(new Date())
					.member((i % 2) == 1 ? m1 : m2).cnt(0L).build());
		}
	}
}
*/
