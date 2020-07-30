<%@ page contentType="text/html; charset=windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp" %>

<html>
<head>
  <title>Поиск назначений по районам</title>
  <meta name="Content-Type" content="<c:out value="${cmsApplication.contentType}"/>">
  <base href="<c:out value="${admRequest.baseUrl}"/>">
  <script language="javascript" src="admin/scripts/images.js"></script>
  <script language="javascript" src="admin/scripts/object.js"></script>
  <script language="javascript" src="admin/scripts/resize.js"></script>
  <link rel="stylesheet" href="admin/style.css">
</head>
<body marginheight="10" marginwidth="10" leftmargin="10" topmargin="10" onload="try {resize();} catch (e) {}">
<table cellpadding=0 cellspacing=0 width="100%" class="editBG">
  <tr>
    <td width="100%" colspan="2" align="center" valign="top">
      <table cellpadding=5 cellspacing=0 width="95%" border=0 class=list>
        <tr>
          <td colspan=2><span style="color:blue"><b>Документ сформирован</b></span></td>
        </tr>
      </table>
      <br>
      <div align=right style="padding-right:10px;">
        <input type="button" name="nextData" value="Выход" class="button_green" onClick="getFalse(event);window.close();"/>
      </div>
    </td>
  </tr>
</table>
</body>
</html>
