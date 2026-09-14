<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공지사항 상세</title>
    <link rel="stylesheet" href="/css/notice.css">
    <script src="/js/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#btnEdit").on("click", function () {
                location.href = "/notice/noticeEditInfo?nSeq=${rDTO.noticeSeq}";
            });

            $("#btnDelete").on("click", function () {
                if (!confirm("삭제하시겠습니까?")) {
                    return false;
                }

                $.ajax({
                    url: "/notice/noticeDelete",
                    type: "POST",
                    data: {nSeq: "${rDTO.noticeSeq}"},
                    dataType: "json",
                    success: function (json) {
                        alert(json.msg);
                        if (json.msg === "삭제되었습니다.") {
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
    </script>
</head>
<body>

<div class="tbl_wrap">
    <h2>공지사항 상세</h2>

    <table class="tbl_row01">
        <caption>공지사항 상세</caption>
        <tr>
            <th>제목</th>
            <td>${rDTO.title}</td>
        </tr>
        <tr>
            <th>공지글 여부</th>
            <td>${rDTO.noticeYn}</td>
        </tr>
        <tr>
            <th>작성자</th>
            <td>${rDTO.userName}</td>
        </tr>
        <tr>
            <th>조회수</th>
            <td>${rDTO.readCnt}</td>
        </tr>
        <tr>
            <th>등록일</th>
            <td>${rDTO.regDt}</td>
        </tr>
        <tr>
            <th>내용</th>
            <td class="contents_view">${rDTO.contents}</td>
        </tr>
    </table>

    <div class="btn_wrap">
        <input type="button" id="btnEdit" value="수정">
        <input type="button" id="btnDelete" value="삭제">
        <input type="button" id="btnList" value="목록">
    </div>
</div>

</body>
</html>
