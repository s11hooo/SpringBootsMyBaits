package kopo.poly2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly2.dto.MsgDTO;
import kopo.poly2.dto.NoticeDTO;
import kopo.poly2.service.iNoticeService;
import kopo.poly2.utill.CmmUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 공지사항 게시판 컨트롤러
 * URL "/notice"로 시작되는 모든 요청을 처리합니다.
 */
@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {

    private final iNoticeService noticeService;

    /**
     * 공지사항 목록 화면 이동 및 데이터 조회
     */
    @GetMapping(value = "noticelist")
    public String noticeList(HttpSession session, ModelMap model) throws Exception {
        log.info("{}.noticelist Start!", this.getClass().getName());

        // 아직 로그인 기능이 구현되지 않았으므로 임시 세션 생성 (USER01 - 이협건)
        session.setAttribute("SESSION_USER_ID", "USER01");

        // 공지사항 리스트 조회 및 NPE(Null Pointer Exception) 예방 처리
        List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList())
                .orElseGet(ArrayList::new);

        // 조회 결과를 JSP에 전달하기 위해 모델 객체에 추가
        model.addAttribute("rList", rList);

        log.info("{}.noticelist End!", this.getClass().getName());

        // 실행될 JSP 파일 경로 반환 (webapp/WEB-INF/views/notice/noticeList.jsp)
        return "notice/noticelist";
    }

    /**
     * 공지사항 등록 화면 이동
     */
    @GetMapping(value = "noticereg")
    public String noticereg() throws Exception {
        log.info("{}.noticereg Start!", this.getClass().getName());
        log.info("{}.noticereg End!", this.getClass().getName());
        return "notice/noticereg";
    }

    /**
     * 공지사항 글 등록 실행 (Ajax 비동기 호출)
     */
    @ResponseBody
    @PostMapping(value = "noticeInsert")
    public MsgDTO noticeInsert(HttpServletRequest request, HttpSession session) {
        log.info("{}.noticeInsert Start!", this.getClass().getName());

        String msg = ""; // 클라이언트에 보낼 응답 메시지
        MsgDTO dto = null; // 결과 메시지 반환용 DTO

        try {
            // 세션 및 요청 파라미터에서 데이터 추출
            String userId = CmmUtill.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String title = CmmUtill.nvl(request.getParameter("title"));
            String noticeYn = CmmUtill.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtill.nvl(request.getParameter("contents"));

            // 전달받은 값 로그 출력 검증
            log.info("session user_id: {} / title: {} / noticeYn: {} / contents: {}", userId, title, noticeYn, contents);

            // DTO 객체 생성 및 세팅
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUserId(userId);
            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            // 게시글 저장 비즈니스 로직 호출
            noticeService.insertNoticeInfo(pDTO);
            msg = "등록되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다 : " + e.getMessage();
            log.info(e.toString());
        } finally {
            dto = new MsgDTO();
            dto.setMsg(msg);
            log.info("{}.noticeInsert End!", this.getClass().getName());
        }

        return dto;
    }

    /**
     * 공지사항 상세보기
     */
    @GetMapping(value = "noticeInfo")
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info("{}.noticeInfo Start!", this.getClass().getName());

        String nSeq = CmmUtill.nvl(request.getParameter("nSeq")); // 공지글 일련번호(PK)
        log.info("nSeq: {}", nSeq);

        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(nSeq);

        // getNoticeInfo 호출 시 type 파라미터를 true로 전달하여 조회수(Read Count) 증가 처리
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                .orElseGet(NoticeDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info("{}.noticeInfo End!", this.getClass().getName());
        return "notice/noticeInfo";
    }

    /**
     * 공지사항 수정 화면 이동 (기존 입력 내용 보여주기)
     */
    @GetMapping(value = "noticeEditInfo")
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info("{}.noticeEditInfo Start!", this.getClass().getName());

        String nSeq = CmmUtill.nvl(request.getParameter("nSeq"));
        log.info("nSeq: {}", nSeq);

        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(nSeq);

        // 수정 화면에서는 조회수가 늘어나면 안 되므로 type 파라미터를 false로 전달
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, false))
                .orElseGet(NoticeDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info("{}.noticeEditInfo End!", this.getClass().getName());
        return "notice/noticeEditInfo";
    }

    /**
     * 공지사항 글 수정 실행 (Ajax 비동기 호출)
     */
    @ResponseBody
    @PostMapping(value = "noticeUpdate")
    public MsgDTO noticeUpdate(HttpSession session, HttpServletRequest request) {
        log.info("{}.noticeUpdate Start!", this.getClass().getName());

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtill.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String nSeq = CmmUtill.nvl(request.getParameter("nSeq"));
            String title = CmmUtill.nvl(request.getParameter("title"));
            String noticeYn = CmmUtill.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtill.nvl(request.getParameter("contents"));

            log.info("userId: {} / nSeq: {} / title: {} / noticeYn: {} / contents: {}", userId, nSeq, title, noticeYn, contents);

            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUserId(userId);
            pDTO.setNoticeSeq(nSeq);
            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            // 게시글 수정 비즈니스 로직 호출
            noticeService.updateNoticeInfo(pDTO);
            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다 : " + e.getMessage();
            log.info(e.toString());
        } finally {
            dto = new MsgDTO();
            dto.setMsg(msg);
            log.info("{}.noticeUpdate End!", this.getClass().getName());
        }

        return dto;
    }

    /**
     * 공지사항 글 삭제 실행 (Ajax 비동기 호출)
     */
    @ResponseBody
    @PostMapping(value = "noticeDelete")
    public MsgDTO noticeDelete(HttpServletRequest request) {
        log.info("{}.noticeDelete Start!", this.getClass().getName());

        String msg = "";
        MsgDTO dto = null;

        try {
            String nSeq = CmmUtill.nvl(request.getParameter("nSeq"));
            log.info("nSeq: {}", nSeq);

            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setNoticeSeq(nSeq);

            // 게시글 삭제 비즈니스 로직 호출
            noticeService.deleteNoticeInfo(pDTO);
            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다 : " + e.getMessage();
            log.info(e.toString());
        } finally {
            dto = new MsgDTO();
            dto.setMsg(msg);
            log.info("{}.noticeDelete End!", this.getClass().getName());
        }

        return dto;
    }
}