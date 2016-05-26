<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- nav-bar -->
<div class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand navbar-extra-width" herf="#">
                <img alt="eatBit" class="eatbit-logo" src="img/logo.png">
            </a>
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand eatbit-brand" href="#">eatBit</a>
        </div>
        
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse">
            <%--<!-- login buttons & profile menu -->
            <!-- //se sarà loggato #not-logged{display: none}
                    //altrimenti #logged{display:none} -->--%>
                    
            <c:choose>
                <c:when test="${sessionScope.user_name == null}">
                    <ul class="nav navbar-nav navbar-right" id="not-logged">
                        <li><a class="navbar-link" href="" data-toggle="modal" href="javascript:void(0)" onclick="openRegisterModal();" >Registrati</a></li>
                        <li><button class="btn btn-default navbar-btn" data-toggle="modal" href="javascript:void(0)" onclick="openLoginModal();">Accedi</button></li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <!-- scheda profilo-->
                    <div class="nav navbar-nav navbar-right" id="logged">
                        <div class="dropdown">
                            <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                                <%= session.getAttribute("user_nickname")%>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                <li class="no-border">
                                    <div class="user-thumbnail thumbnail">
                                        <img src="<%=session.getAttribute("user_avatar")%>" alt="user foto">
                                        <div class="caption">
                                            <h5><%= session.getAttribute("user_name")%> <%= session.getAttribute("user_surname")%></h5>
                                            <a href="#">Profilo</a> 
                                        </div>
                                    </div>
                                </li>
                                <li><a href="#">Mio Ristoranti</a></li>
                                <li role="separator" class="divider"></li>
                                <li><a href="" onclick="logoutAjax();"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Logout</a></li>
                            </ul>
                        </div>
                    </div> 
                </c:otherwise>
            </c:choose>
            <!-- End login & profile button -->
            
        </div> 
        <!-- end collaps -->
    </div>
</div>
<!-- end nav bar -->