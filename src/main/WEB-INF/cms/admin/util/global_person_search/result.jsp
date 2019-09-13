<%@ page contentType="text/html; charset=windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>

<html>
	<head>
		<title>����� ���������� �� �������</title>
		<meta name="Content-Type" content="<c:out value="${cmsApplication.contentType}"/>">
		<base href="<c:out value="${admRequest.baseUrl}"/>">
		<meta http-equiv="X-UA-TextLayoutMetrics" content="natural" />
		<link rel="stylesheet" href="admin/styleblue.css">
	</head>

	<body>
		<h3 align="center"></h3>
		<br>
		<c:if test="${empty requestScope.error}">			
			<a tabindex="-1" href="<c:out value='${requestScope.protocol}'/>">
				������� ��������
			</a><br>
		</c:if>	
		<br>
		
		<c:if test="${not empty requestScope.error}">			
			<span style="color:red; font-size:12px; font-weight:bold" id="err">
				${requestScope.error}
			</span>
		</c:if>	
		
		<c:forEach var="person" items="${requestScope.completely}" varStatus="row">		
			<b>${row.count}) ���:</b> ${person.fio}<br>
			<b>���� ��������:</b> ${person.birthdate}<br>
			<b>����� �����������:</b> ${person.address}<br>
			<b>�����:</b> ${person.snils}<br>
			<b>�����:</b> ${person.borough}<br>
						
			<table border="1">
				<tr>
					<td><b>���</b></td>
					<td><b>��</b></td>
					<td><b>����, �� ��������� ������ �� �������� ������� ����������</b></td>
					<td><b>������ ���������-����� ���</b></td>
					<td><b>������ ����������</b></td>
				</tr>					
				<c:forEach var="appoint" items="${person.appoints}">
					<tr>
						<td>${appoint.msp}</td>
						<td>${appoint.category}</td>
						<td>${appoint.child}</td>
						<td>� ${appoint.startDate}<br>�� ${appoint.endDate}</td>
						<td>${appoint.status}</td>
					</tr>			
				</c:forEach>
			</table>
			<br><br><br>
		</c:forEach>
		
		<c:if test="${not empty requestScope.partially}">
			<h3 align="center">������� ���������� ������ �� �����</h3>
		</c:if>	
		
		<c:forEach var="person" items="${requestScope.partially}" varStatus="row">		
			<b>${row.count}) ���:</b> ${person.fio}<br>
			<b>���� ��������:</b> ${person.birthdate}<br>
			<b>����� �����������:</b> ${person.address}<br>
			<b>�����:</b> ${person.snils}<br>
			<b>�����:</b> ${person.borough}<br>
						
			<table border="1">
				<tr>
					<td><b>���</b></td>
					<td><b>��</b></td>
					<td><b>����, �� ��������� ������ �� �������� ������� ����������</b></td>
					<td><b>������ ���������-����� ���</b></td>
					<td><b>������ ����������</b></td>
				</tr>					
				<c:forEach var="appoint" items="${person.appoints}">
					<tr>
						<td>${appoint.msp}</td>
						<td>${appoint.category}</td>
						<td>${appoint.child}</td>
						<td>� ${appoint.startDate}<br>�� ${appoint.endDate}</td>
						<td>${appoint.status}</td>
					</tr>			
				</c:forEach>
			</table>
			<br><br><br>
		</c:forEach>
		<br>
	</body>
</html>
