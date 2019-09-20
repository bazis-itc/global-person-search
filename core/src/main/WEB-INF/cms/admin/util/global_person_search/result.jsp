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
				<c:out value="${requestScope.error}"/>
			</span>
		</c:if>	
		
		<c:forEach var="person" items="${requestScope.completely}" varStatus="row">		
			<b><c:out value="${row.count}"/>) ���:</b> <c:out value="${person.fio}"/><br>
			<b>���� ��������:</b> <c:out value="${person.birthdate}"/><br>
			<b>����� �����������:</b> <c:out value="${person.address}"/><br>
			<b>�����:</b> <c:out value="${person.snils}"/><br>
			<b>�����:</b> <c:out value="${person.borough}"/><br>
						
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
						<td><c:out value="${appoint.msp}"/></td>
						<td><c:out value="${appoint.category}"/></td>
						<td><c:out value="${appoint.child}"/></td>
						<td>� <c:out value="${appoint.startDate}"/><br>�� <c:out value="${appoint.endDate}"/></td>
						<td><c:out value="${appoint.status}"/></td>
					</tr>			
				</c:forEach>
			</table>
			<br><br><br>
		</c:forEach>
		
		<c:if test="${not empty requestScope.partially}">
			<h3 align="center">������� ���������� ������ �� �����</h3>
		</c:if>	
		
		<c:forEach var="person" items="${requestScope.partially}" varStatus="row">		
			<b><c:out value="${row.count}"/>) ���:</b> <c:out value="${person.fio}"/><br>
			<b>���� ��������:</b> <c:out value="${person.birthdate}"/><br>
			<b>����� �����������:</b> <c:out value="${person.address}"/><br>
			<b>�����:</b> <c:out value="${person.snils}"/><br>
			<b>�����:</b> <c:out value="${person.borough}"/><br>
						
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
						<td><c:out value="${appoint.msp}"/></td>
						<td><c:out value="${appoint.category}"/></td>
						<td><c:out value="${appoint.child}"/></td>
						<td>� <c:out value="${appoint.startDate}"/><br>�� <c:out value="${appoint.endDate}"/></td>
						<td><c:out value="${appoint.status}"/></td>
					</tr>			
				</c:forEach>
			</table>
			<br><br><br>
		</c:forEach>
		<br>
	</body>
</html>
