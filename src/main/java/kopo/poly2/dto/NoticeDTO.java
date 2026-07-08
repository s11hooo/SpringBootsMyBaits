package kopo.poly2.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * lombok를 통해 추가기 생성 메소드(getter)를 자동으로 생성해줌
 *
 * @Getter : 각 getter 함수를 생성하지 않아도 자동 생성
 * @Setter : 각 setter 함수를 생성하지 않아도 자동 생성
 */

@Getter
@Setter
public class NoticeDTO {

    private String noticeSeq;   // 기본키, 순번
    private String title;       // 제목
    private String noticeYn;    // 공지 여부
    private String contents;    // 내용
    private String userId;      // 작성자
    private String readCnt;     // 조회수
    private String regId;       // 등록자 아이디
    private String regDt;       // 등록일
    private String chgId;       // 수정자 아이디
    private String chgDt;       // 수정일
    private String userName;    // 등록자명

}