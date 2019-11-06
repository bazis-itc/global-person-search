<%@ page contentType="text/html; charset=windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>

<html>
	<head>
		<title>Ошибка</title>
		<meta name="Content-Type" content="<c:out value="${cmsApplication.contentType}"/>">
		<base href="<c:out value="${admRequest.baseUrl}"/>">
		<meta http-equiv="X-UA-TextLayoutMetrics" content="natural" />
		<link rel="stylesheet" href="admin/styleblue.css">
		  <script>
			function showSt() {
				var st = document.getElementById("st");
				if (st) {
					if (st.style.display == "none" || st.style.display == "NONE") {
						st.style.display = "block";
					} else {
						st.style.display = "none";
					}			
				}
			}
		  </script>
	</head>

	<body>
		<span style="color:red; font-size:12px; font-weight:bold" id="err"><c:out value="${requestScope.error}"/></span>
		<br><br>
		<a onClick="showSt()"><u>Отобразить Stacktrace</u></a>
		<br>
		<span style="display:none; text-align: left; font-size:12px" id="st">
			<pre><c:out value="${requestScope.stacktrace}"/></pre>
		</span>				
	</body>
</html>