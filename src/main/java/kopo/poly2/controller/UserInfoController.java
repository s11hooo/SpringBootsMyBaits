package kopo.poly2.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly2.dto.MsgDTO;
import kopo.poly2.dto.UserInfoDTO;
import kopo.poly2.service.iUserInfoService;
import kopo.poly2.utill.CmmUtill;
import kopo.poly2.utill.EncryptUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    private final iUserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info("{}.user/userRegForm", this.getClass().getName());

        return "/user/userRegForm";
    }
    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info("{}.getUserIdExists Start!", this.getClass().getName());

        String userId = CmmUtill.nvl(request.getParameter("userId"));

        log.info("userId : {}", userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO))
                .orElseGet(UserInfoDTO::new);

        log.info("{}.getUserIdExists End!", this.getClass().getName());

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {

        log.info("{}.getEmailExists Start!", this.getClass().getName());

        String email = CmmUtill.nvl(request.getParameter("email"));

        log.info("email : {}", email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtill.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO))
                .orElseGet(UserInfoDTO::new);

        log.info("{}.getEmailExists End!", this.getClass().getName());

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) {

        log.info("{}.insertUserInfo start!", this.getClass().getName());

        int res = 0;
        String msg = "";
        MsgDTO dto;
        UserInfoDTO pDTO;

        try {

            String userId = CmmUtill.nvl(request.getParameter("userId"));
            String userName = CmmUtill.nvl(request.getParameter("userName"));
            String password = CmmUtill.nvl(request.getParameter("password"));
            String email = CmmUtill.nvl(request.getParameter("email"));
            String addr1 = CmmUtill.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtill.nvl(request.getParameter("addr2"));

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);
            pDTO.setPassword(EncryptUtill.encHashSHA256(password));
            pDTO.setEmail(EncryptUtill.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e;
            log.info(e.toString());

        } finally {

            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info("{}.insertUserInfo End!", this.getClass().getName());
        }

        return dto;
    }
}