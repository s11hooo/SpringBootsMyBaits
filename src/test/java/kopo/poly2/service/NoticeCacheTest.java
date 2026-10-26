package kopo.poly2.service;

import kopo.poly2.config.CacheConfig;
import kopo.poly2.dto.NoticeDTO;
import kopo.poly2.mapper.iNoticeMapper;
import kopo.poly2.service.impl.NoticeService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NoticeCacheTest {
    @Configuration
    static class TestConfig {
        @Bean FakeMapper mapper() { return new FakeMapper(); }
        @Bean NoticeService service(FakeMapper mapper) { return new NoticeService(mapper); }
    }
    static class FakeMapper implements iNoticeMapper {
        int listCalls, detailCalls, readCountCalls, writes;
        boolean fail;
        List<NoticeDTO> rows = List.of();
        public List<NoticeDTO> getNoticeList() { listCalls++; return rows; }
        public NoticeDTO getNoticeInfo(NoticeDTO dto) { detailCalls++; return dto; }
        public void updateNoticeReadCnt(NoticeDTO dto) { readCountCalls++; }
        public void insertNoticeInfo(NoticeDTO dto) { write(); rows = List.of(dto); }
        public void updateNoticeInfo(NoticeDTO dto) { write(); rows = List.of(dto); }
        public void deleteNoticeInfo(NoticeDTO dto) { write(); rows = List.of(); }
        private void write() { if (fail) throw new IllegalStateException("failed"); writes++; }
    }
    AnnotationConfigApplicationContext context;
    iNoticeService service;
    FakeMapper mapper;
    @BeforeEach void setUp() {
        context = new AnnotationConfigApplicationContext(CacheConfig.class, TestConfig.class);
        service = context.getBean(iNoticeService.class);
        mapper = context.getBean(FakeMapper.class);
    }
    @AfterEach void close() { context.close(); }
    NoticeDTO notice(String title) { NoticeDTO dto = new NoticeDTO(); dto.setTitle(title); return dto; }

    @Test void repeatedListReadsCallMapperOnlyOnce() throws Exception {
        mapper.rows = List.of(notice("Original"));
        assertEquals("Original", service.getNoticeList().get(0).getTitle());
        assertEquals("Original", service.getNoticeList().get(0).getTitle());
        assertEquals(1, mapper.listCalls);
    }
    @ParameterizedTest @ValueSource(strings = {"insert", "update", "delete"})
    void writesInvalidateList(String operation) throws Exception {
        mapper.rows = List.of(notice("Before"));
        assertEquals("Before", service.getNoticeList().get(0).getTitle());
        NoticeDTO after = notice("After");
        switch (operation) {
            case "insert" -> service.insertNoticeInfo(after);
            case "update" -> service.updateNoticeInfo(after);
            case "delete" -> service.deleteNoticeInfo(after);
        }
        List<NoticeDTO> result = service.getNoticeList();
        if (operation.equals("delete")) assertTrue(result.isEmpty());
        else assertEquals("After", result.get(0).getTitle());
        assertEquals(1, mapper.writes);
        assertEquals(2, mapper.listCalls);
    }
    @Test void detailsAreNotCached() throws Exception {
        NoticeDTO dto = notice("Detail");
        service.getNoticeInfo(dto, true);
        service.getNoticeInfo(dto, true);
        assertEquals(2, mapper.detailCalls);
        assertEquals(2, mapper.readCountCalls);
    }
    @Test void failedWriteKeepsCache() throws Exception {
        service.getNoticeList();
        mapper.fail = true;
        assertThrows(IllegalStateException.class, () -> service.updateNoticeInfo(notice("After")));
        service.getNoticeList();
        assertEquals(1, mapper.listCalls);
    }
}