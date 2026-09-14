<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공지사항 등록</title>
    <link rel="stylesheet" href="/css/notice.css">
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#title").on("keyup", function () {
                $("#titleByte").text(getByteLength($(this).val()) + " / 200 byte");
            });

            $("#contents").on("keyup", function () {
                $("#contentsByte").text(getByteLength($(this).val()) + " / 4000 byte");
            });

            $("#btnSend").on("click", function () {
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
                    url: "/notice/noticeInsert",
                    type: "POST",
                    data: $("#f").serialize(),
                    dataType: "json",
                    success: function (json) {
                        alert(json.msg);
                        if (json.msg === "등록되었습니다.") {
                            location.href = "/notice/noticeList";
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
    <h2>공지사항 등록</h2>

    <form id="f">
        <table class="tbl_row01">
            <caption>공지사항 등록</caption>
            <tr>
                <th>제목</th>
                <td>
                    <input type="text" id="title" name="title" maxlength="200">
                    <div class="byte_cnt"><span id="titleByte">0 / 200 byte</span></div>
                </td>
            </tr>
            <tr>
                <th>공지글 여부</th>
                <td>
                    <label><input type="radio" name="noticeYn" value="Y"> 공지</label>
                    <label><input type="radio" name="noticeYn" value="N" checked> 일반</label>
                </td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
                    <textarea id="contents" name="contents"></textarea>
                    <div class="byte_cnt"><span id="contentsByte">0 / 4000 byte</span></div>
                </td>
            </tr>
        </table>
    </form>

    <div class="btn_wrap">
        <input type="button" id="btnSend" value="등록">
        <input type="button" id="btnList" value="목록">
    </div>
</div>

</body>
</html>
