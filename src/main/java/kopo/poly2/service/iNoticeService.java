package kopo.poly2.service;

import kopo.poly2.dto.NoticeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface iNoticeService {
    List<NoticeDTO> getNoticeList() throws Exception;

    @Transactional
    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception;

    @Transactional
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;

    @Transactional
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    @Transactional
    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;
}
