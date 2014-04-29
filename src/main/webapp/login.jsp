<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Altamira BPM - Login</title>
    </head>
    <body>
        <%
            if (session.isNew()) 
            {
                response.sendRedirect(request.getContextPath()+"/index.jsp");
                return;
            }
        %>
        <h1>Login Page</h1>
        
        <form name='f' action="<c:url value='${request.contextPath}/j_security_check' />" method="POST">

            <table>
                <tr>
                    <td>User:</td>
                    <td><input type='text' name='j_username' value=''>
                    </td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type='password' name='j_password' />
                    </td>
                </tr>
                <tr>
                    <td colspan='2'><input name="submit" type="submit"
                                           value="submit" />
                    </td>
                </tr>
                <tr>
                    <td colspan='2'><input name="reset" type="reset" />
                    </td>
                </tr>
            </table>

        </form>
    </body>
</html>
