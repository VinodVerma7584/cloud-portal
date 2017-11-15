<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%--@elvariable id="virtualMachine" type="de.papke.cloud.portal.model.VirtualMachineModel"--%>

<jsp:include page="header.jsp" />

<div id="wrapper">

	<jsp:include page="navigation.jsp" />

	<div id="page-wrapper">
		<div class="row">
			<div class="col-lg-12">
			    <h4 class="page-header">Credentials Admin > <c:out value="${fn:toUpperCase(fn:substring(virtualMachine.cloudProvider, 0, 1))}${fn:toLowerCase(fn:substring(virtualMachine.cloudProvider, 1,fn:length(virtualMachine.cloudProvider)))}" /> > List</h4>
				<form id="main-form" method="get" action="<c:url value="/vm" />" target="output"
					role="form">
					<table id="datatable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Command</th>
                                <th>Success</th>
                                <th>Variables</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${virtualMachine.provisionLogList}" var="provisionLog">
                                <tr>
                                    <td><c:out value="${provisionLog.date}" /></td>
                                    <td><c:out value="${provisionLog.command}" /></td>
                                    <td><c:out value="${provisionLog.success}" /></td>
                                    <td>
                                    <c:forEach var="variable" items="${provisionLog.variableMap}">
                                        <p><c:out value="${variable.key}" /> = <c:out value="${variable.value}" /></p>
                                    </c:forEach>
                                    </td>
                                    <td>
                                        <c:if test="${provisionLog.command == 'apply'}">
                                            <button id="delete/action/${virtualMachine.cloudProvider}/${provisionLog.id}" type="submit" class="btn btn-danger btn-circle"><i class="fa fa-times"></i></button>
                                        </c:if>                                            
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
	                    <div class="modal-dialog">
	                        <div class="modal-content">
	                            <div class="modal-header">
	                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	                                <h4 class="modal-title" id="myModalLabel">Output</h4>
	                            </div>
	                            <div class="modal-body">
	                                <iframe name="output" id="output" frameborder="0" scrolling="yes"></iframe>
	                            </div>
	                            <div class="modal-footer">
	                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	                            </div>
	                        </div>
	                    </div>
                    </div>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="footer.jsp" />