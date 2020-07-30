<%@ page contentType="text/html; charset=Windows-1251" %>
<%@ include file="/WEB-INF/cms/admin/common.jsp"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="sx.admin.AdmApplication" %>

<html>
<head>
    <title>���� ����������</title>
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
				/*�������� ���� � ���*/
				$("#mspBlock").hide();
				diselAllMsp();
			} else {
				$("#mspBlock").show();
				/*���������� ���� � ���*/
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
					/*��������� ���������*/
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
				alert("��������� ������� ����������� ��� ����������.");
				return false;
			}
			if ($.trim(ys).length != 4 || $.trim(ye).length != 4) {
				alert("��� ������ ���� 4� �������.");
				return false;
			}
			if (ye*1 < ys*1 || (ye*1 == ys*1 && me*1 < ms*1)) {
				alert("������ � �� ����� ���� ����� ������� ��.");
				return false;
			}
			if (!isAllMsp && $.trim(selMsp) == "") {
				alert("�������� ��� ��� �������, ���� ��������� �� ���� ���.");
				return false;
			}			
			th.submit();
		}
		function checkVals() {
			var message;
			var extended = <c:out value="${requestScope.extended}"/>;
			if (!extended) message = null;
			else if (document.getElementById('surname').value == "") 
				message = "�� ������� �������";
			else if (document.getElementById('name').value == "") 
				message = "�� ������� ���";
			else if (document.getElementById('data(birthdate)').value == "") 
				message = "�� ������� ���� ��������";
			if (message) alert(message);
			popupMsgOn(self, '���� �����', '���������...');
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
				<td><font class="font"><b>�������:</b></font></td>
				<td class="noBottom"><input id="surname" name="surname" value=""/></td>
			</tr>     
			<tr>
				<td><font class="font"><b>���:</b></font></td>
				<td class="noBottom"><input id="name" name="name" value=""/></td>
			</tr>  
			<tr>
				<td><font class="font"><b>��������:</b></font></td>
				<td class="noBottom"><input name="patronymic" value=""/></td>
			</tr>     
			<tr>
				<td><font class="font"><b>���� ��������:</b></font></td>
				<td>
					<input type="hidden" name="birthdate" id="data(birthdate)" value="">
					<input style="width:80px;" size="10" maxlength="10" type="text" onKeyPress="setChanged()"
						 name="date(birthdate)"
						 id="cal_birthdate"
						 onBlur="setDatetoHidden(this,'birthdate','�� ���������� ������ ���� ���������');"
						 onfocus="initMask(this);" mask="xx.xx.xxxx">
					<script>
						addCalendar("�������� ����", "birthdate", null, "�� ���������� ������ ���� ����");
					</script>
					<span id="buttons_birthdate">
						<button class="lnk" title="�������" tabindex="-1" onClick="showCal('birthdate',null,event);return false;">
							<img src="admin/images/view_up.gif" onMouseOver="newImg(this,view_down)" onMouseOut="newImg(this,view_up)"/>
						</button>
						<button class="lnk" title="�������" tabindex="-1" onClick="clearDateField('birthdate');return getFalse();">
							<img src="admin/images/delete_src_up.gif" onMouseOver="newImg(this,delete_src_down)"
								onMouseOut="newImg(this,delete_src_up)"/>
						</button>
					</span>
				</td>
			</tr> 
			<tr>
				<td><font class="font"><b>�����:</b></font></td>
				<td><input name="snils" value=""/></td>
			</tr>                 
		</table>
		</c:if>
		
		<br>
		<font class="font"><b>������ ��� ������� ����������:</b></font>
		<table class="param-content">
			<tr>
				<td><b></b></td>
				<td><b>�����</b></td>
				<td><b>���</b></td>
			</tr>
			<tr>
                <td><b>�</b></td>
                <td class="noBottom">
                    <select name="monthOfStart">
						<option value="1" selected="selected">������</option>
						<option value="2">�������</option>
						<option value="3">����</option>
						<option value="4">������</option>
						<option value="5">���</option>
						<option value="6">����</option>
						<option value="7">����</option>
						<option value="8">������</option>
						<option value="9">��������</option>
						<option value="10">�������</option>
						<option value="11">������</option>
						<option value="12">�������</option>
                    </select> 
                </td>
                <td class="noBottom">
                    <input name="yearOfStart" value="2019">
                </td>                                    
            </tr>
            <tr>
                <td><b>��</b></td>
                <td class="noBottom">
                    <select name="monthOfEnd">
						<option value="1" selected="selected">������</option>
						<option value="2">�������</option>
						<option value="3">����</option>
						<option value="4">������</option>
						<option value="5">���</option>
						<option value="6">����</option>
						<option value="7">����</option>
						<option value="8">������</option>
						<option value="9">��������</option>
						<option value="10">�������</option>
						<option value="11">������</option>
						<option value="12">�������</option>
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
				<td><font class="font"><b>��������� �� ���� ���:</b></font></td>
				<td><input id="isAllMsp" name="isAllMsp" onClick="checkIsAllMsp()" type="checkbox" checked="checked"></td>
			</tr>                        
        </table>	
		<br>
		<div id="mspBlock">
			<font class="font"><b>����� ��� ��� ������� ����������:</b></font>
			<select 
				size="10" multiple="" tabindex="1" style="width:300px; " 
				name="title_mspList" id="title_mspList"
				onchange="setChanged()">        
			</select>
			<button type="button" class="lnk" title="�������" tabindex="-1" action="choose" 
				onclick="getNewObj('mspList', '', 'pprServ', true, true, true, 'admin/select', null, null, null, 'false'); return getFalse(event);">
				<img src="admin/images/view_up.gif" class="ico" onmouseover="newImg(this,view_down)" onmouseout="newImg(this,view_up)">
			</button>
		</div>
				
        <div align=right style="padding-right:10px;">
          <input type="submit" value="������" class="button_green"/>
        </div>
	</form>
	<!--<div style="padding-right:10px; display: inline-flex; float:right;">
		<input type="button" value="�������" class="button_blue" id="winCloseButton" onClick="self.close(); return getFalse(event);"/>
	</div>-->
</body>
</html>