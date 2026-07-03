package kopo.poly2.service.impl;

import kopo.poly2.dto.NoticeDTO;
import kopo.poly2.mapper.iNoticeMapper;
import kopo.poly2.service.iNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements iNoticeService {

    // 생성자 주입을 통해 데이터베이스와 연결할 Mapper 객체를 가져옵니다.
    private final iNoticeMapper noticeMapper;

    @Override
    public List<NoticeDTO> getNoticeList() throws Exception {
        log.info("{}.getNoticeList Start!", this.getClass().getName());
        return noticeMapper.getNoticeList();
    }

    /**
     * 공지사항 상세보기
     * 조회수 증가 로직이 함께 수행되므로 DB 값 변경에 따른 트랜잭션 처리가 필요합니다.
     */
    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception {
        log.info("{}.getNoticeInfo Start!", this.getClass().getName());

        // 상세보기 요청일 때만 조회수 증가 (수정 페이지 요청 시에는 증가하지 않음)
        if (type) {
            log.info("Update Read Count");
            noticeMapper.updateNoticeReadCnt(pDTO);
        }

        return noticeMapper.getNoticeInfo(pDTO);
    }

    @Transactional
    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info("{}.insertNoticeInfo Start!", this.getClass().getName());
        noticeMapper.insertNoticeInfo(pDTO);
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info("{}.updateNoticeInfo Start!", this.getClass().getName());
        noticeMapper.updateNoticeInfo(pDTO);
    }

    @Transactional
    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info("{}.deleteNoticeInfo Start!", this.getClass().getName());
        noticeMapper.deleteNoticeInfo(pDTO);
    }
}