<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공지사항 목록</title>
    <link rel="stylesheet" href="/css/notice.css">
</head>
<body>

<div class="tbl_wrap">
    <h2>공지사항 목록</h2>

    <div class="btn_write">
        <a href="/notice/noticeReg">글쓰기</a>
    </div>

    <table class="tbl_head01">
        <caption>공지사항 목록</caption>
        <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>등록일</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="dto" items="${rList}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td class="left">
                    <a href="/notice/noticeInfo?nSeq=${dto.noticeSeq}">
                        <c:if test="${dto.noticeYn eq 'Y'}">
                            <span class="notice_yn">[공지]</span>
                        </c:if>
                        ${dto.title}
                    </a>
                </td>
                <td>${dto.userName}</td>
                <td>${dto.readCnt}</td>
                <td>${dto.regDt}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty rList}">
            <tr>
                <td colspan="5">등록된 공지사항이 없습니다.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

</body>
</html>
