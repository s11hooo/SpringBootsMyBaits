package kopo.poly2.utill;

public class EncryptstudyUtill {

    public static void main(String[] args) throws Exception {
        System.out.println("------------------------------------");
        System.out.println("<해시 암호화 알고리즘>");

        String planText = "암호화할 문자열";
        System.out.println("해시 암호화할 문자열 : " + planText);

        String hashEnc = EncryptUtill.encHashSHA256(planText);

        System.out.println("해시 암호화 결과 : " + hashEnc);
        System.out.println("------------------------------------");

        System.out.println("<AES-128 암호화 알고리즘>");
        System.out.println("AES-128 암호화할 문자열 : " + planText);

        String aesEnc = EncryptUtill.encAES128CBC(planText);

        System.out.println("AES-128 암호화 결과 : " + aesEnc);

        String aesDec = EncryptUtill.decAES128CBC(aesEnc);

        System.out.println("AES-128 복호화 결과 : " + aesDec);
        System.out.println("------------------------------------");
    }
}
