package kopo.poly2.service;

import kopo.poly2.dto.MailDTO;

public interface iMailService {

    // 메일 발송
    int doSendMail(MailDTO pDTO);
}