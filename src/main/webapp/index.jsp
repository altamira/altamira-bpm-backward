<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Altamira BPM</title>
    </head>
    <body>
        <%
            String userId = request.getUserPrincipal().getName();
            if(userId==null)
            {
                %><p>Welcome Guest</p><%
            }
            else
            {
                %><p>Welcome </p><%
                out.print(userId);
            }
        %>
    </body>
</html>
