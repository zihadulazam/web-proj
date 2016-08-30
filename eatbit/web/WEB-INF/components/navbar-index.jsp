<%-- 
    Document   : navbar
    Created on : 27-mag-2016, 11.35.00
    Author     : zihadul
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="user" scope="session" class="database.User"/>
<!-- nav-bar -->
<div class="navbar navbar-default">
    <div class="container-fluid">
        
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand navbar-extra-width" herf="/eatbit/home">
                <img alt="eatBit" class="eatbit-logo" src="img/logo.png">
            </a>
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand eatbit-brand" href="../eatbit/home">eatBit</a>
        </div>
        
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse">
            <%--<!-- login buttons & profile menu -->
            <!-- //se sarà loggato #not-logged{display: none}
                    //altrimenti #logged{display:none} -->--%>
                    
            <c:choose>
                <c:when test="${sessionScope.user.getNickname()==null}">
                    <ul class="nav navbar-nav navbar-right" id="not-logged">
                        <li><a class="navbar-link" href="" data-toggle="modal" href="javascript:void(0)" onclick="openRegisterModal();" >Registrati</a></li>
                        <li><button class="btn btn-default navbar-btn" data-toggle="modal" href="javascript:void(0)" onclick="openLoginModal();">Accedi</button></li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <!-- scheda profilo-->
                    <ul class="nav navbar-nav navbar-right" id="logged">
                        <li class="dropdown">
                            <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                                <%= user.getNickname() %>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                <li class="no-border">
                                    <div class="user-thumbnail thumbnail">
                                        <img src="<%= user.getAvatar_path()%>" alt="user foto" class="img-circle">
                                        <div class="caption">
                                            <h5><%=user.getName()%> <%= user.getSurname() %></h5>
                                            <a href="/eatbit/ProfileServlet">Profilo</a> 
                                        </div>
                                    </div>
                                </li>
                                <li role="separator" class="divider"></li>
                                <li><a href="" onclick="logoutAjax();"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Logout</a></li>
                            </ul>
                        </li>
                        <c:if test="${sessionScope.user.getType()==1 && sessionScope.user.getNickname()!=null}">
                            <li id="notification">
                                <a class="btn btn-default" type="button" id="btn-notify" href="../eatbit/GetAllNotify">
                                    <span class="glyphicon glyphicon glyphicon-bell" aria-hidden="true"></span>
                                </a>
                                <span id="ntfy-badge" class="badge badge-notify">--</span>
                            </li>
                        </c:if>
                    </ul> 
                </c:otherwise>
            </c:choose>
            <!-- End login & profile button -->
            
        </div> 
        <!-- end collaps -->
    </div>
</div>
<!-- end nav bar -->
