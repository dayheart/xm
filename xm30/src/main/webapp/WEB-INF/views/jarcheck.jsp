<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello JSPD Cloud Home Page</title>
</head>
<body onLoad="document.form1.reqButton.focus();">

<br><hr align=center><br>
[Example]<br>
Document Builder Factory - org.apache.xerces.jaxp.DocumentBuilderFactoryImpl<br>
SAX Parser Factory - org.apache.xerces.jaxp.SAXParserFactoryImpl<br>
Transformer Factory - org.apache.xalan.processor.TransformerFactoryImpl<br>
<br>
(ex) javax.servlet.http.HttpServlet<br>

<form action="jarcheck" name=form1>
<input type=text name="reqName" value="${reqName }">
<input type=submit name=reqButton value="FIND">
</form>
<br>
[Search Result]
<p>${result }</p>
</body>
</html>