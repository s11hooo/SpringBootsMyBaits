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

@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {

    private final iNoticeService noticeService;

    @GetMapping(value = {"noticeList", "NoticeList", "noticelist"})
    public String noticeList(HttpSession session, ModelMap model) throws Exception {
        log.info("{}.noticeList Start!", this.getClass().getName());

        session.setAttribute("SESSION_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList())
                .orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        log.info("{}.noticeList End!", this.getClass().getName());

        return "notice/noticeList";
    }

    @GetMapping(value = {"noticeReg", "NoticeReg", "noticereg"})
    public String noticeReg() throws Exception {
        log.info("{}.noticeReg Start!", this.getClass().getName());
        log.info("{}.noticeReg End!", this.getClass().getName());

        return "notice/noticeReg";
    }

    @ResponseBody
    @PostMapping(value = "noticeInsert")
    public MsgDTO noticeInsert(HttpServletRequest request, HttpSession session) {
        log.info("{}.noticeInsert Start!", this.getClass().getName());

        String msg;
        MsgDTO dto;

        try {
            String userId = CmmUtill.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String title = CmmUtill.nvl(request.getParameter("title"));
            String noticeYn = CmmUtill.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtill.nvl(request.getParameter("contents"));

            log.info("userId: {} / title: {} / noticeYn: {} / contents: {}", userId, title, noticeYn, contents);

            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUserId(userId);
            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            noticeService.insertNoticeInfo(pDTO);
            msg = "등록되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
        } finally {
            dto = new MsgDTO();
            log.info("{}.noticeInsert End!", this.getClass().getName());
        }

        dto.setMsg(msg);

        return dto;
    }

    @GetMapping(value = {"noticeInfo", "NoticeInfo"})
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info("{}.noticeInfo Start!", this.getClass().getName());

        String nSeq = CmmUtill.nvl(request.getParameter("nSeq"));
        log.info("nSeq: {}", nSeq);

        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(nSeq);

        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                .orElseGet(NoticeDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info("{}.noticeInfo End!", this.getClass().getName());

        return "notice/noticeInfo";
    }

    @GetMapping(value = {"noticeEditInfo", "NoticeEditInfo"})
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info("{}.noticeEditInfo Start!", this.getClass().getName());

        String nSeq = CmmUtill.nvl(request.getParameter("nSeq"));
        log.info("nSeq: {}", nSeq);

        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(nSeq);

        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, false))
                .orElseGet(NoticeDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info("{}.noticeEditInfo End!", this.getClass().getName());

        return "notice/noticeEditInfo";
    }

    @ResponseBody
    @PostMapping(value = "noticeUpdate")
    public MsgDTO noticeUpdate(HttpSession session, HttpServletRequest request) {
        log.info("{}.noticeUpdate Start!", this.getClass().getName());

        String msg;
        MsgDTO dto;

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

            noticeService.updateNoticeInfo(pDTO);
            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
        } finally {
            dto = new MsgDTO();
            log.info("{}.noticeUpdate End!", this.getClass().getName());
        }

        dto.setMsg(msg);

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "noticeDelete")
    public MsgDTO noticeDelete(HttpServletRequest request) {
        log.info("{}.noticeDelete Start!", this.getClass().getName());

        String msg;
        MsgDTO dto;

        try {
            String nSeq = CmmUtill.nvl(request.getParameter("nSeq"));
            log.info("nSeq: {}", nSeq);

            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setNoticeSeq(nSeq);

            noticeService.deleteNoticeInfo(pDTO);
            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
        } finally {
            dto = new MsgDTO();
            log.info("{}.noticeDelete End!", this.getClass().getName());
        }

        dto.setMsg(msg);

        return dto;
    }
}
