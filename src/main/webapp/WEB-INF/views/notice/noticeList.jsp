<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%@ page import="kopo.poly2.dto.NoticeDTO" %>
<%@ page import="kopo.poly2.utill.CmmUtill" %>
<%
    List<NoticeDTO> rList = Optional.ofNullable((List<NoticeDTO>) request.getAttribute("rList"))
            .orElseGet(java.util.ArrayList::new);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>공지사항 리스트</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#btnReg").on("click", function () {
                location.href = "/notice/noticeReg";
            });
        });
    </script>
</head>
<body>
<h2>공지사항 리스트</h2>
<hr/>
<br/>

<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">순번</div>
            <div class="divTableHead">제목</div>
            <div class="divTableHead">공지글 여부</div>
            <div class="divTableHead">조회수</div>
            <div class="divTableHead">등록자</div>
            <div class="divTableHead">등록일</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (int i = 0; i < rList.size(); i++) {
                NoticeDTO rDTO = rList.get(i);
                if (rDTO == null) {
                    rDTO = new NoticeDTO();
                }
        %>
        <div class="divTableRow">
            <div class="divTableCell"><%=i + 1%></div>
            <div class="divTableCell">
                <a href="/notice/noticeInfo?nSeq=<%=CmmUtill.nvl(rDTO.getNoticeSeq())%>">
                    <%=CmmUtill.nvl(rDTO.getTitle())%>
                </a>
            </div>
            <div class="divTableCell"><%=CmmUtill.nvl(rDTO.getNoticeYn())%></div>
            <div class="divTableCell"><%=CmmUtill.nvl(rDTO.getReadCnt())%></div>
            <div class="divTableCell"><%=CmmUtill.nvl(rDTO.getUserName())%></div>
            <div class="divTableCell"><%=CmmUtill.nvl(rDTO.getRegDt())%></div>
        </div>
        <%
            }
        %>
    </div>
</div>
<br/>
<button id="btnReg" type="button">등록</button>
</body>
</html>
