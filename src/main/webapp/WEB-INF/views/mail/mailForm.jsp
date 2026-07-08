<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>메일 작성하기</title>

    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>

    <script type="text/javascript">

        // HTML 로딩 완료 후 실행
        $(document).ready(function () {

            // 메일 발송 버튼 클릭
            $("#btnSend").on("click", function () {

                // Ajax 호출
                $.ajax({
                    url: "/mail/sendMail",
                    type: "post", // 전송 방식
                    dataType: "JSON", // 결과는 JSON으로 받기
                    data: $("#ff").serialize(), // form 데이터 전송
                    success: function (json) {
                        alert(json.msg); // 결과 메시지 출력
                    }
                });

            });

        });

    </script>

</head>

<body>

<h2>메일 작성하기</h2>
<hr/>
<br/>

<form id="ff">

    <div class="divTable minimalistBlack">

        <div class="divTableBody">

            <div class="divTableRow">
                <div class="divTableCell">받는사람</div>
                <div class="divTableCell">
                    <input type="text" name="toMail" maxlength="100" style="width:95%"/>
                </div>
            </div>

            <div class="divTableRow">
                <div class="divTableCell">메일제목</div>
                <div class="divTableCell">
                    <input type="text" name="title" maxlength="100" style="width:95%"/>
                </div>
            </div>

            <div class="divTableRow">
                <div class="divTableCell">메일내용</div>
                <div class="divTableCell">
                    <textarea name="contents" style="width:95%; height:400px"></textarea>
                </div>
            </div>

        </div>

    </div>

    <div>
        <button id="btnSend" type="button">메일 발송</button>
        <button type="reset">다시 작성</button>
    </div>

</form>

</body>
</html>