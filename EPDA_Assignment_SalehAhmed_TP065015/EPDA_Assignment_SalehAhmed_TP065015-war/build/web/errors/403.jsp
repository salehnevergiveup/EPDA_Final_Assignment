<%-- 
    Document   : 403
    Created on : Jul 2, 2024, 8:24:54 PM
    Author     : saleh
--%>

<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>403 - Forbidden</title>
    <jsp:include page="../includes/style.jsp"/>
</head>
<body class="flex items-center justify-center h-screen bg-gray-100">
    <%
        out.println(
            new ContentCreator()
                .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg text-center", "")
                .header("403", 1, "text-5xl font-bold text-red-600 mb-4", "")
                .paragraph("Forbidden", "text-xl text-gray-700 mb-8", "")
                .action("Go to Homepage","/EPDA_Assignment_SalehAhmed_TP065015-war/profile/view.jsp", "user", "user", "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "")
                .end(ContentCreator.Wrapper.CONTAINER)
                .getContent()
        );
    %>
</body>
</html>
