<%@ page contentType="text/html; charset=Windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="sx.admin.AdmApplication" %>

<html>
<head>
    <title>Ввод параметров</title>
    <meta name="Content-Type" content="<c:out value="${admApplication.contentType}"/>">
    <base href="<c:out value="${admRequest.baseUrl}"/>">
    <script type="text/javascript" src="<c:out value="${admRequest.baseUrl}"/>admin/scripts/jquery-3.1.1.min.js"></script>
	<link rel="icon" href="<c:out value="${admRequest.baseUrl}"/>/favicon.ico" type="image/x-icon">
	<link rel="stylesheet" href="admin/style.css">
    
	<script>
		String.prototype.replaceAll = function(search, replacement) {
			var target = this;
			return target.replace(new RegExp(search, 'g'), replacement);
		};
		$(document).ready(function() {
			checkIsAllMsp();
			$(".msp-names").each(function(i,el){
				selUnselMsp(el);
			});
			$(".msp-names").bind("click", function() {
				selUnselMsp(this);
			});
		});
		function checkIsAllMsp(){
			if ($("#isAllMsp").prop("checked")) {
				/*скрываем блок с МСП*/
				$("#mspBlock").hide();
				diselAllMsp();
			} else {
				$("#mspBlock").show();
				/*показываем блок с МСП*/
			}
		}
		function diselAllMsp() {
			$(".msp-names").each(function(index, elem) {
				$(elem).prop("checked",false);
			})
			$("#selMsp").val("");
		}
		function selUnselMsp (th) {
			var selMsp = $("#selMsp").val();
			var thisVal = $(th).val();
			if ($(th).prop("checked")) {
				if (selMsp.indexOf(thisVal) < 0) {
					/*добавляем выбранное*/
					$("#selMsp").val(selMsp+thisVal+",");
				}
			} else {
				if (selMsp.indexOf($(th).val()) >= 0) {					
					$("#selMsp").val(selMsp.replaceAll(thisVal+",",""));
				}
			}
		}
		function checkValues (th) {
			var isAllMsp = $("#isAllMsp").prop("checked"),
			selMsp = $("#selMsp").val(),
			ms = $("[name='monthOfStart']").val(),
			ys = $("[name='yearOfStart']").val(),
			me = $("[name='monthOfEnd']").val(),
			ye = $("[name='yearOfEnd']").val();			
			if (ms == "" || $.trim(ys) == "" || me == "" || $.trim(ye) == "") {
				alert("Параметры периода обязательны для заполнения.");
				return false;
			}
			if ($.trim(ys).length != 4 || $.trim(ye).length != 4) {
				alert("Год должен быть 4х значным.");
				return false;
			}
			if (ye*1 < ys*1 || (ye*1 == ys*1 && me*1 < ms*1)) {
				alert("Период С не может быть позже периода ПО.");
				return false;
			}
			if (!isAllMsp && $.trim(selMsp) == "") {
				alert("Выберите МСП для запроса, либо запросите по всем МСП.");
				return false;
			}			
			th.submit();
		}
		function checkVals() {
			var message;
			var extended = <c:out value="${requestScope.extended}"/>;
			if (!extended) message = null;
			else if (document.getElementById('surname').value == "") 
				message = "Не указана фамилия";
			else if (document.getElementById('name').value == "") 
				message = "Не указано имя";
			else if (document.getElementById('data(birthdate)').value == "") 
				message = "Не указана дата рождения";
			if (message) alert(message);
			popupMsgOn(self, 'Идет поиск', 'Подождите...');
			return message == null;
		}
	</script>
	
	<script type="text/javascript" src="admin/scripts/images.js"></script>
	<script type="text/javascript" src="admin/scripts/object.js"></script>
	<script type="text/javascript" src="admin/scripts/editobject.js"></script>
	<script type="text/javascript" src="admin/scripts/resize.js"></script>
	<script type="text/javascript" src="admin/scripts/cal2.js"></script>
	<script type="text/javascript" src="admin/scripts/autoMask.js"></script> 
	<script type="text/javascript" src="admin/scripts/hint.js"></script>
	<script type="text/javascript" src="admin/scripts/cscroll.js"></script>
	<script type="text/javascript" src="admin/scripts/controls/core/prototype.js"></script>
	<script type="text/javascript" src="admin/scripts/tablekit/tablekit.js"></script>
	<script type="text/javascript" src="admin/scripts/js.js"></script>
	<script type="text/javascript" src="admin/scripts/list.js"></script>
</head>
<body onload="try {resize();} catch (e) {}">
    <form name="formMain" id="formMain" 
		  enctype="multipart/form-data" 
		  action="<%=admRequest.getBaseUrl()%><c:out value="${data.actionCode}"/>.sx" 
		  method="post" onSubmit="return checkVals();" class="editBG">
		<input type="hidden" name="cmd" value="paramsCmd">
		<input type="hidden" name="objId" value="<%=admRequest.getObjIdList().get(0).toString()%>">

		<input type="hidden" name="data(mspList)" id="id_mspList" value="" ischanged="false">
		<input type="hidden" name="delobj(mspList)" id="del_id_mspList" value="">
				
		<c:if test="${requestScope.extended}">		
		<br>
		<table>
			<tr>
				<td><font class="font"><b>Фамилия:</b></font></td>
				<td class="noBottom"><input id="surname" name="surname" value=""/></td>
			</tr>     
			<tr>
				<td><font class="font"><b>Имя:</b></font></td>
				<td class="noBottom"><input id="name" name="name" value=""/></td>
			</tr>  
			<tr>
				<td><font class="font"><b>Отчество:</b></font></td>
				<td class="noBottom"><input name="patronymic" value=""/></td>
			</tr>     
			<tr>
				<td><font class="font"><b>Дата рождения:</b></font></td>
				<td>
					<input type="hidden" name="birthdate" id="data(birthdate)" value="">
					<input style="width:80px;" size="10" maxlength="10" type="text" onKeyPress="setChanged()"
						 name="date(birthdate)"
						 id="cal_birthdate"
						 onBlur="setDatetoHidden(this,'birthdate','Не правильный формат даты документа');"
						 onfocus="initMask(this);" mask="xx.xx.xxxx">
					<script>
						addCalendar("Выберите дату", "birthdate", null, "Не правильный формат даты поля");
					</script>
					<span id="buttons_birthdate">
						<button class="lnk" title="Выбрать" tabindex="-1" onClick="showCal('birthdate',null,event);return false;">
							<img src="admin/images/view_up.gif" onMouseOver="newImg(this,view_down)" onMouseOut="newImg(this,view_up)"/>
						</button>
						<button class="lnk" title="Удалить" tabindex="-1" onClick="clearDateField('birthdate');return getFalse();">
							<img src="admin/images/delete_src_up.gif" onMouseOver="newImg(this,delete_src_down)"
								onMouseOut="newImg(this,delete_src_up)"/>
						</button>
					</span>
				</td>
			</tr> 
			<tr>
				<td><font class="font"><b>СНИЛС:</b></font></td>
				<td><input name="snils" value=""/></td>
			</tr>                 
		</table>
		</c:if>
		
		<br>
		<font class="font"><b>Период для запроса информации:</b></font>
		<table class="param-content">
			<tr>
				<td><b></b></td>
				<td><b>Месяц</b></td>
				<td><b>Год</b></td>
			</tr>
			<tr>
                <td><b>С</b></td>
                <td class="noBottom">
                    <select name="monthOfStart">
						<option value="1" selected="selected">Январь</option>
						<option value="2">Февраль</option>
						<option value="3">Март</option>
						<option value="4">Апрель</option>
						<option value="5">Май</option>
						<option value="6">Июнь</option>
						<option value="7">Июль</option>
						<option value="8">Август</option>
						<option value="9">Сентябрь</option>
						<option value="10">Октябрь</option>
						<option value="11">Ноябрь</option>
						<option value="12">Декабрь</option>
                    </select> 
                </td>
                <td class="noBottom">
                    <input name="yearOfStart" value="2019">
                </td>                                    
            </tr>
            <tr>
                <td><b>ПО</b></td>
                <td class="noBottom">
                    <select name="monthOfEnd">
						<option value="1" selected="selected">Январь</option>
						<option value="2">Февраль</option>
						<option value="3">Март</option>
						<option value="4">Апрель</option>
						<option value="5">Май</option>
						<option value="6">Июнь</option>
						<option value="7">Июль</option>
						<option value="8">Август</option>
						<option value="9">Сентябрь</option>
						<option value="10">Октябрь</option>
						<option value="11">Ноябрь</option>
						<option value="12">Декабрь</option>
                    </select> 
                </td>
                <td class="noBottom">
                    <input name="yearOfEnd" value="2020">
                </td>                                    
            </tr>                        
        </table>
		<br>
		<table>
			<tr>
				<td><font class="font"><b>Запросить по всем МСП:</b></font></td>
				<td><input id="isAllMsp" name="isAllMsp" onClick="checkIsAllMsp()" type="checkbox" checked="checked"></td>
			</tr>                        
        </table>	
		<br>
		<div id="mspBlock">
			<font class="font"><b>Выбор МСП для запроса информации:</b></font>
			<select 
				size="10" multiple="" tabindex="1" style="width:300px; " 
				name="title_mspList" id="title_mspList"
				onchange="setChanged()">        
			</select>
			<button type="button" class="lnk" title="Выбрать" tabindex="-1" action="choose" 
				onclick="getNewObj('mspList', '', 'pprServ', true, true, true, 'admin/select', null, null, null, 'false'); return getFalse(event);">
				<img src="admin/images/view_up.gif" class="ico" onmouseover="newImg(this,view_down)" onmouseout="newImg(this,view_up)">
			</button>
		</div>
				
        <div align=right style="padding-right:10px;">
          <input type="submit" value="Дальше" class="button_green"/>
        </div>
	</form>
	<!--<div style="padding-right:10px; display: inline-flex; float:right;">
		<input type="button" value="Закрыть" class="button_blue" id="winCloseButton" onClick="self.close(); return getFalse(event);"/>
	</div>-->
</body>
</html>