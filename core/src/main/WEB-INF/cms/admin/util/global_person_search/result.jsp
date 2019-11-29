<%@ page contentType="text/html; charset=windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>

<html>
	<head>
		<title>Поиск назначений по районам</title>
		<meta name="Content-Type" content="<c:out value="${cmsApplication.contentType}"/>">
		<base href="<c:out value="${admRequest.baseUrl}"/>">
		<meta http-equiv="X-UA-TextLayoutMetrics" content="natural" />
		<link rel="stylesheet" href="admin/styleblue.css">
	</head>

	<body>
		<h3 align="center"></h3>
		<br>			
			<a tabindex="-1" href="<c:out value='${requestScope.protocol}'/>">
				Скачать протокол
			</a><br>
		<br>
		
		<c:if test="${not empty requestScope.error}">			
			<span style="color:red; font-size:12px; font-weight:bold" id="err">
				<c:out value="${requestScope.error}"/>
			</span><br>
		</c:if>	
		
		<c:forEach var="list" items="${requestScope.lists}">		
			<c:if test="${not empty list.title && not empty list.persons}">
				<h3 align="center"><c:out value="${list.title}"/></h3>
			</c:if>	
			
			<c:forEach var="person" items="${list.persons}" varStatus="row">		
				<b><c:out value="${row.count}"/>) ФИО:</b> <c:out value="${person.fio}"/><br>
				<b>Дата рождения:</b> <c:out value="${person.birthdate}"/><br>
				<b>Адрес регистрации:</b> <c:out value="${person.address}"/><br>
				<b>СНИЛС:</b> <c:out value="${person.snils}"/><br>
				<b>Район:</b> <c:out value="${person.borough}"/><br>
				<b>Документ удостоверяющий личность:</b> <c:out value="${person.passport}"/><br>
							
				<table border="1">
					<tr>
						<td><b>МСП</b></td>
						<td><b>ЛК</b></td>
						<td><b>Лицо, на основании данных ЛД которого сделано назначение</b></td>
						<td><b>Период предостав-ления МСП</b></td>
						<td><b>Статус назначения</b></td>
						<c:if test="${requestScope.displayPayments}">
							<td><b>Выплаты</b></td>
						</c:if>	
					</tr>					
					<c:forEach var="appoint" items="${person.appoints}">
						<tr>
							<td><c:out value="${appoint.msp}"/></td>
							<td><c:out value="${appoint.category}"/></td>
							<td><c:out value="${appoint.child}"/></td>
							<td>с <c:out value="${appoint.startDate}"/><br>по <c:out value="${appoint.endDate}"/></td>
							<td><c:out value="${appoint.status}"/></td>
							<c:if test="${requestScope.displayPayments}">
								<td><pre style="font-size:12px"><c:out value="${appoint.payments}"/></pre></td>
							</c:if>	
						</tr>			
					</c:forEach>
				</table>
				<br><br><br>
			</c:forEach>
		</c:forEach>
		<br>
	</body>
</html>
