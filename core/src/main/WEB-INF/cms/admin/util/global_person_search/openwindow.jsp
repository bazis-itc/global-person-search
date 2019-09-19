<%@ page contentType="text/html; charset=windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>
<html>
  <head>
    <title>Поиск назначений по районам</title>
    <base href="<%=admRequest.getBaseUrl()%>"/>
    <meta http-equiv="X-UA-TextLayoutMetrics" content="natural" />
    <meta name="Content-Type" content="<c:out value="${cmsApplication.contentType}"/>">
    <link rel="stylesheet" href="admin/styleblue.css">
  </head>

  <body marginheight="10" marginwidth="10" leftmargin="10" topmargin="10" style="background-color:f6f6f6">
  <script type="text/javascript">
    window.open("<%=admRequest.getBaseUrl()%><c:out value="${data.actionCode}"/>.sx?cmd=openWindowCmd"
		+ "&designCode=<c:out value='${data.designCode}'/>" 
		+ "&objId=<c:out value="${data.objId}"/>"
		,"_blank"
		,"status=yes,toolbar=no,scrollbars=yes,menubar=no,location=no,resizable=no,channelmode=no");
  </script>
  </body>
</html>
