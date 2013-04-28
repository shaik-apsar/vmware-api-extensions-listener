<%@page isErrorPage="true"%>
<html>
<head>
    <title>VMware Cloud director API Extensions Listener Exception Handling</title>
</head>
<body>

<h2><font color="red">VMware Cloud director API Extensions Listener Exception Handling</font></h2>

<h3><%=exception.fillInStackTrace() %></h3>
<br>

<h2>Root Cause:&nbsp;&nbsp;<font color="red"><%=exception.getCause().getCause().getMessage()%></font></h2>



</body>
</html>