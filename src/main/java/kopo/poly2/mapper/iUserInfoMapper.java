package kopo.poly2.mapper;

import kopo.poly2.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface iUserInfoMapper {

    // 회원 가입하기(회원정보 등록하기)
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    // 회원 가입 전 아이디 중복체크하기(DB조회하기)
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    // 회원 가입 전 이메일 중복체크하기(DB조회하기)
    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserId(UserInfoDTO pDTO) throws Exception;

    int updatePassword(UserInfoDTO pDTO) throws Exception;
}