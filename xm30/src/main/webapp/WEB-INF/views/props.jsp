<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PRODUCT LIST</title>
</head>
<body>
<table border="1">
<tr>
	<th>Property Name</th><th>Property Value</th>
</tr>
<c:forEach items="${props }" var="prop">
	<tr>
		<td>${prop.name }</td><td>${prop.value }</td>
	</tr>
</c:forEach>
</table>
</body>
</html>