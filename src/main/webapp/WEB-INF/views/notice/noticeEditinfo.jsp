<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공지사항 수정</title>
    <link rel="stylesheet" href="/resources/css/notice.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        $(document).ready(function () {

            // 제목 바이트 카운터
            $("#title").on("keyup", function () {
                var byte = getByteLength($(this).val());
                $("#titleByte").text(byte + " / 200 byte");
            });

            // 내용 바이트 카운터
            $("#contents").on("keyup", function () {
                var byte = getByteLength($(this).val());
                $("#contentsByte").text(byte + " / 4000 byte");
            });

            // 수정 버튼 클릭
            $("#btnUpdate").on("click", function () {

                var title = $("#title").val();
                var contents = $("#contents").val();

                // 유효성 체크
                if (title.length === 0) {
                    alert("제목을 입력해주세요.");
                    $("#title").focus();
                    return false;
                }

                if (contents.length === 0) {
                    alert("내용을 입력해주세요.");
                    $("#contents").focus();
                    return false;
                }

                // 공지글 여부 라디오 버튼 체크
                var noticeYnChecked = false;
                var noticeYns = document.getElementsByName("noticeYn");
                for (var i = 0; i < noticeYns.length; i++) {
                    if (noticeYns[i].checked) {
                        noticeYnChecked = true;
                        break;
                    }
                }

                if (!noticeYnChecked) {
                    alert("공지글 여부를 선택해주세요.");
                    return false;
                }

                // Ajax를 통해 수정 Controller 호출
                // $("#f").serialize() : Form 태그 내 하위 요소 값들을 파라미터 형태로 변환
                $.ajax({
                    url: "/notice/noticeUpdate",
                    type: "POST",
                    data: $("#f").serialize(),
                    dataType: "json",
                    success: function (json) {
                        alert(json.msg);
                        if (json.msg === "수정되었습니다.") {
                            location.href = "/notice/noticeList";
                        }
                    },
                    error: function (xhr, status, error) {
                        alert("오류가 발생하였습니다.");
                        console.log(error);
                    }
                });
            });

            // 목록 버튼 클릭
            $("#btnList").on("click", function () {
                location.href = "/notice/noticeList";
            });

        });

        // 바이트 길이 계산 함수 (한글 3바이트)
        function getByteLength(str) {
            var byte = 0;
            for (var i = 0; i < str.length; i++) {
                byte += (str.charCodeAt(i) > 127) ? 3 : 1;
            }
            return byte;
        }
    </script>
</head>
<body>

<div class="tbl_wrap">

    <form id="f">
        <%-- 수정 시 PK값(noticeSeq)을 반드시 함께 전달해야 함 --%>
        <input type="hidden" name="nSeq" value="${rDTO.noticeSeq}">

        <table class="tbl_row01">
            <caption>공지사항 수정</caption>
            <tr>
                <th>제목</th>
                <td>
                    <%-- 기존에 입력된 값을 input에 출력 --%>
                    <input type="text" id="title" name="title" value="${rDTO.title}" maxlength="200">
                    <div class="byte_cnt"><span id="titleByte">0 / 200 byte</span></div>
                </td>
            </tr>
            <tr>
                <th>공지글 여부</th>
                <td>
                    <input type="radio" name="noticeYn" value="Y"
                           checked> 공지
                    &nbsp;&nbsp;
                    <input type="radio" name="noticeYn" value="N"
                           checked> 일반
                </td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
                    <%-- 기존에 입력된 내용을 textarea에 출력 --%>
                    <textarea id="contents" name="contents">${rDTO.contents}</textarea>
                    <div class="byte_cnt"><span id="contentsByte">0 / 4000 byte</span></div>
                </td>
            </tr>
        </table>
    </form>

    <div class="btn_wrap">
        <input type="button" id="btnUpdate" value="수정">
        <input type="button" id="btnList" value="목록">
    </div>

</div>

</body>
</html>
