<%@ page contentType="text/html; charset=windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>

<html>
	<head>
		<title>Поиск назначений по районам</title>
		<meta name="Content-Type" content="<c:out value="${cmsApplication.contentType}"/>">
		<base href="<c:out value="${admRequest.baseUrl}"/>">
		<meta http-equiv="X-UA-TextLayoutMetrics" content="natural" />
		<link rel="stylesheet" href="admin/style.css">
	</head>

	<body class="editBG">
		<h3 align="center"></h3>
		<br>			
			<a tabindex="-1" href="<c:out value='${requestScope.protocol}'/>">
				Скачать протокол
			</a><br>
		<br>
		
		<c:if test="${not empty requestScope.error}">			
			<span style="color:red; font-size:12px; font-weight:bold" id="err">
				<c:out value="${requestScope.error}"/>
			</span><br><br>
		</c:if>	
		
		<c:forEach var="list" items="${requestScope.lists}">		
			<c:if test="${not empty list.title && not empty list.persons}">
				<h3 align="center"><c:out value="${list.title}"/></h3>
			</c:if>	
			
			<c:forEach var="person" items="${list.persons}" varStatus="row">		
				<font class="font"><b><c:out value="${row.count}"/>) ФИО:</b> <c:out value="${person.fio}"/></font><br>
				<b>Дата рождения:</b> <c:out value="${person.birthdate}"/><br>
				<b>Адрес регистрации:</b> <c:out value="${person.address}"/><br>
				<b>СНИЛС:</b> <c:out value="${person.snils}"/><br>
				<b>Район:</b> <c:out value="${person.borough}"/><br>
				<b>Документ удостоверяющий личность:</b> <c:out value="${person.passport}"/><br>
				<b>Статус ЛД:</b> <c:out value="${person.status}"/><br>
				<b>Дата снятия с учета:</b> <c:out value="${person.regOffDate}"/><br>
				<b>Причина снятия с учета:</b> <c:out value="${person.regOffReason}"/><br>

				<br>
				<b>Назначения</b>
				<table class="sort">
					<thead>
					<tr class="gridRow">
						<th><b>МСП</b></th>
						<th><b>ЛК</b></th>
						<th><b>Лицо, на основании данных ЛД которого сделано назначение</b></th>
						<th><b>Период предоставления МСП</b></th>
						<th><b>Статус назначения</b></th>
						<c:if test="${requestScope.displayPayments}">
							<th><b>Выплаты</b></th>
						</c:if>	
					</tr>	
					</thead>
					<tbody>
					<c:forEach var="appoint" items="${person.appoints}">
						<tr class="gridRow">
							<td><c:out value="${appoint.msp}"/></td>
							<td><c:out value="${appoint.category}"/></td>
							<td><c:out value="${appoint.child}"/></td>
							<td>
								<c:forEach var="period" items="${appoint.periods}">
									<nobr><c:out value="${period}"/></nobr><br>
								</c:forEach>
							</td>
							<td><c:out value="${appoint.status}"/></td>
							<c:if test="${requestScope.displayPayments}">
								<td>
								    <c:forEach var="payout" items="${appoint.payouts}">
								        <nobr><c:out value="${payout}"/></nobr><br>
                                    </c:forEach>
                                </td>
							</c:if>	
						</tr>			
					</c:forEach>
					</tbody>
				</table>				
							
				<c:if test="${requestScope.displayPetitions}">
					<br>
				        <b>Заявления</b>
					<table class="sort">
						<thead>
						<tr class="gridRow">
							<th><b>МСП</b></th>
							<th><b>ЛК</b></th>
							<th><b>Дата регистрации заявления</b></th>
							<th><b>Дата подготовки решения</b></th>
							<th><b>Статус заявления</b></th>
						</tr>	
						</thead>
						<tbody>
						<c:forEach var="petition" items="${person.petitions}">
							<tr class="gridRow">
								<td><c:out value="${petition.msp}"/></td>
								<td><c:out value="${petition.category}"/></td>
								<td><c:out value="${petition.regDate}"/></td>
								<td><c:out value="${petition.appointDate}"/></td>
								<td><c:out value="${petition.status}"/></td>
							</tr>			
						</c:forEach>
						</tbody>
					</table>
				</c:if>	
				<br><br><br>
			</c:forEach>
		</c:forEach>
		
	    <form 
			name="formMain" id="formMain" enctype="multipart/form-data" 
			action="<%=admRequest.getBaseUrl()%><c:out value="${data.actionCode}"/>.sx" 
			method="post"
		>
			<input type="hidden" name="cmd" value="resultCmd">
			<input type="hidden" name="objId" value="<%=admRequest.getParam("objId")%>">	
			
			<input type="hidden" name="yearOfStart" value='<%=admRequest.getParam("yearOfStart")%>'/>	
			<input type="hidden" name="monthOfStart" value='<%=admRequest.getParam("monthOfStart")%>'/>
			<input type="hidden" name="yearOfEnd" value='<%=admRequest.getParam("yearOfEnd")%>'/>
			<input type="hidden" name="monthOfEnd" value='<%=admRequest.getParam("monthOfEnd")%>'/>
			
			<input type="hidden" name="isAllMsp" value='<%=admRequest.getParam("isAllMsp")%>'/>
			<input type="hidden" name="data(mspList)" value='<%=admRequest.getParam("data(mspList)")%>'/>
			<input type="hidden" name="persons" value='<%=admRequest.getString("persons")%>'/>	
			<input type="hidden" name="fails" value='<%=admRequest.getString("fails")%>'/>	
			
			<c:if test="${requestScope.canCreateDoc}">
				<div>
					<div style="padding-right:10px; display: inline-flex; float:right;">
						<input type="submit" name="nextData" value="Сформировать документ" class="button_green" style="width:200px;"/>
					</div>
				</div>	
			</c:if>		
		</form>
		
	</body>
</html>
