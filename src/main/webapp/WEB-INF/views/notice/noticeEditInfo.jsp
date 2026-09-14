<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공지사항 수정</title>
    <link rel="stylesheet" href="/css/notice.css">
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#titleByte").text(getByteLength($("#title").val()) + " / 200 byte");
            $("#contentsByte").text(getByteLength($("#contents").val()) + " / 4000 byte");

            $("#title").on("keyup", function () {
                $("#titleByte").text(getByteLength($(this).val()) + " / 200 byte");
            });

            $("#contents").on("keyup", function () {
                $("#contentsByte").text(getByteLength($(this).val()) + " / 4000 byte");
            });

            $("#btnUpdate").on("click", function () {
                if ($("#title").val().length === 0) {
                    alert("제목을 입력하세요.");
                    $("#title").focus();
                    return false;
                }

                if ($("#contents").val().length === 0) {
                    alert("내용을 입력하세요.");
                    $("#contents").focus();
                    return false;
                }

                $.ajax({
                    url: "/notice/noticeUpdate",
                    type: "POST",
                    data: $("#f").serialize(),
                    dataType: "json",
                    success: function (json) {
                        alert(json.msg);
                        if (json.msg === "수정되었습니다.") {
                            location.href = "/notice/noticeInfo?nSeq=${rDTO.noticeSeq}";
                        }
                    },
                    error: function () {
                        alert("오류가 발생했습니다.");
                    }
                });
            });

            $("#btnList").on("click", function () {
                location.href = "/notice/noticeList";
            });
        });

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
    <h2>공지사항 수정</h2>

    <form id="f">
        <input type="hidden" name="nSeq" value="${rDTO.noticeSeq}">

        <table class="tbl_row01">
            <caption>공지사항 수정</caption>
            <tr>
                <th>제목</th>
                <td>
                    <input type="text" id="title" name="title" value="${rDTO.title}" maxlength="200">
                    <div class="byte_cnt"><span id="titleByte">0 / 200 byte</span></div>
                </td>
            </tr>
            <tr>
                <th>공지글 여부</th>
                <td>
                    <label>
                        <input type="radio" name="noticeYn" value="Y" <c:if test="${rDTO.noticeYn eq 'Y'}">checked</c:if>>
                        공지
                    </label>
                    <label>
                        <input type="radio" name="noticeYn" value="N" <c:if test="${rDTO.noticeYn ne 'Y'}">checked</c:if>>
                        일반
                    </label>
                </td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
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
