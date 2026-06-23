<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Tự động chuyển hướng người dùng từ / sang /home
    response.sendRedirect(request.getContextPath() + "/home");
%>
