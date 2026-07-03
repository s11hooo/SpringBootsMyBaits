package kopo.poly2.mapper;

import kopo.poly2.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface iNoticeMapper {

    // 공지사항 리스트 조회
    List<NoticeDTO> getNoticeList() throws Exception;

    // 공지사항 글 등록
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;

    // 공지사항 상세보기
    NoticeDTO getNoticeInfo(NoticeDTO pDTO) throws Exception;

    // 공지사항 조회수 업데이트
    void updateNoticeReadCnt(NoticeDTO pDTO) throws Exception;

    // 공지사항 글 수정
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    // 공지사항 글 삭제
    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;
}