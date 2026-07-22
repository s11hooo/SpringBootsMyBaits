package kopo.poly2.service.impl;

import kopo.poly2.dto.MailDTO;
import kopo.poly2.dto.UserInfoDTO;
import kopo.poly2.mapper.iUserInfoMapper;
import kopo.poly2.service.iMailService;
import kopo.poly2.service.iUserInfoService;
import kopo.poly2.utill.CmmUtill;
import kopo.poly2.utill.DateUtill;
import kopo.poly2.utill.EncryptUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements iUserInfoService {

    // 회원관련 SQL 사용하기 위한 Mapper 가져오기
    private final iUserInfoMapper userInfoMapper;

    // 메일 발송을 위한 MailService 자바 객체 가져오기
    private final iMailService mailService;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info("{}.getUserIdExists Start!", this.getClass().getName());

        // DB 아이디가 존재하는지 SQL 쿼리 실행
        UserInfoDTO rDTO = userInfoMapper.getUserIdExists(pDTO);

        log.info("{}.getUserIdExists End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info("{}.emailAuth Start!", this.getClass().getName());

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getEmailExists(pDTO))
                .orElseGet(UserInfoDTO::new);

        log.info("rDTO : {}", rDTO);

        // 이메일 주소가 중복되지 않는다면.. 메일 발송
        if (CmmUtill.nvl(rDTO.getExistsYn()).equals("N")) {

            // 6자리 랜덤 숫자 생성하기
            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);

            log.info("authNumber : {}", authNumber);

            // 인증번호 발송 로직
            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 중복 확인 인증번호 발송 메일");
            dto.setContents("인증번호는 " + authNumber + " 입니다.");
            dto.setToMail(EncryptUtill.decAES128CBC(CmmUtill.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto); // 이메일 발송

            dto = null;

            rDTO.setAuthNumber(authNumber); // 인증번호를 결과값에 넣어주기
        }

        log.info("{}.emailAuth End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info("{}.insertUserInfo Start!", this.getClass().getName());

        // 회원가입 성공 : 1, 아이디 중복으로인한 가입 취소 : 2, 기타 에러 발생 : 0
        int res;

        // 회원가입
        int success = userInfoMapper.insertUserInfo(pDTO);

        // db에 데이터가 등록되었다면(회원가입 성공했다면....)
        if (success > 0) {

            res = 1;

            /*
             * ############################################################
             *                  메일 발송 로직 추가 시작!!
             * ############################################################
             */

            MailDTO mDTO = new MailDTO();

            // 회원정보화면에서 입력받은 이메일 변수(아직 암호화되어 넘어오기 때문에 복호화 수행함)
            mDTO.setToMail(EncryptUtill.decAES128CBC(CmmUtill.nvl(pDTO.getEmail())));

            mDTO.setTitle("회원가입을 축하드립니다."); // 제목

            // 메일 내용에 가입자 이름넣어서 내용 발송
            mDTO.setContents(CmmUtill.nvl(pDTO.getUserName()) + "님의 회원가입을 진심으로 축하드립니다.");

            // 회원 가입이 성공했기 때문에 메일을 발송함
            mailService.doSendMail(mDTO);

            /*
             * ############################################################
             *                  메일 발송 로직 추가 끝!!
             * ############################################################
             */

        } else {
            res = 0;
        }

        log.info("{}.insertUserInfo End!", this.getClass().getName());

        return res;
    }
    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {

        log.info("{}.getLogin Start!", this.getClass().getName());

        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getLogin(pDTO))
                .orElseGet(UserInfoDTO::new);

        if (!CmmUtill.nvl(rDTO.getUserId()).isEmpty()) {

            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(
                    EncryptUtill.decAES128CBC(CmmUtill.nvl(rDTO.getEmail()))
            );

            mDTO.setTitle("로그인 알림");

            mDTO.setContents(
                    DateUtill.getDateTime("yyyy.MM.dd hh:mm:ss")
                            + "에 "
                            + CmmUtill.nvl(rDTO.getUserName())
                            + "님이 로그인하였습니다."
            );

            mailService.doSendMail(mDTO);
        }

        log.info("{}.getLogin End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public UserInfoDTO searchUserIdOrPasswordProc(UserInfoDTO pDTO) throws Exception {
        log.info("{}.searchUserIdOrPasswordProc Start!", this.getClass().getName());

        UserInfoDTO rDTO = userInfoMapper.getUserId(pDTO);

        log.info("{}.searchUserIdOrPasswordProc End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public void newPasswordProc(UserInfoDTO pDTO) {

    }
}