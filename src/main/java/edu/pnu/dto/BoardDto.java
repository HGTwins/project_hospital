package edu.pnu.dto;

import java.util.Date;

import edu.pnu.domain.board.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {

    private Long seq;
    private String content;
    private Date createDate;
    private Long cnt;
    
    // BasicInfo 관련 필드
    private Long hospitalId;
    
    // Member 관련 필드
    private String username;

    public BoardDto(Board b) {
        this.seq = b.getSeq();
        this.content = b.getContent();
        this.createDate = b.getCreateDate();
        this.cnt = b.getCnt();
        this.username = b.getMember().getUsername();
        this.hospitalId = b.getBasicInfo().getHospitalId();
    }
    
    public static BoardDto from(Board entity) {
    	return new BoardDto(entity);
    }
}
