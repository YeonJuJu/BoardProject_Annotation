<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <c:url var="root" value="${pageContext.request.contextPath }/" /> --%>
<c:url var="root" value="/" />
<script type="text/javascript">
  alert('로그인에 실패했습니다');
  location.href='${root}user/login?failure=true';
</script> 

