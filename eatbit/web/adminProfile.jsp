<%-- 
    Document   : userProfile
    Created on : May 21, 2016, 12:56:07 PM
    Author     : mario
--%>


<%@page language="java" session="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <title>Profile Page</title>
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        <link href="css/cssFooter.css" rel="stylesheet">
        <link href="css/profile.css" rel="stylesheet">
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>               
                
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        
    </head>
    <body>
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>
        
        <div class="container">
        
        <div class="col-md-3">
            <div class="row">
                <div class="col-sm-12">
                    <div class="thumbnail">
                        <img src="img/administrator.png" alt="administrator user">
                        <div class="caption">
                            <hr>
                            <h3><%= user.getNickname() %></h3>
                            <h4>Bentornato sulla tua pagina privata di <b>eatBit</b></h4>
                            <hr>
                            <p><b>Tuo Nome:</b>
                            <br>
                            <%= user.getName()%> <%= user.getSurname()%>  </p>
                            <p><b>Email:</b>
                            <br> <%= user.getEmail()%>  </p>
                            <p><b>Nuove Notifiche:</b>
                            <br><c:out value="${numberReview}"/> </p>


                            <div class="btn-group">
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" >
                                Altro <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Vedi tuoi Ristoranti</a></li>
                                    <li><a href="#">Modifica dati Profilo</a></li>

                                    <li class="divider"></li>
                                    <li><a href="#">Log Out</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>    
        </div>
                          
        <div class="col-md-9">
            <h2>Profilo Privato</h2>
            <ul class="nav nav-pills">
                <li class="active"><a data-toggle="tab" href="#menu1">Ristoranti in Attesa<span class="badge"><c:out value="${numberReview}"/></span></a></li>
                <li><a data-toggle="tab" href="#menu2">Risposte da Confermare<span class="badge"><c:out value="${numberReview}"/></span></a></li>
                <li><a data-toggle="tab" href="#menu3">Segnalati<span class="badge"> <c:out value="${numberNotification}"/> </span></a></li>
                <li><a data-toggle="tab" href="#menu4">Informazioni Profilo</a></li>
            </ul>

        <div class="tab-content">            
            <div id="menu1" class="tab-pane fade in active">
                <!--Reviews-->
                <br>
                <c:forEach items="${listReview}" var="review">
                    
                    <a href="#" class="list-group-item">
                      Data: <c:out value="${review.getDate_creation()}"/>
                      
                        <div class="panel panel-info">
                            <div class="panel-heading"> 
                                <c:out value="${review.getName()}"/>
                            </div>
                            <div class="panel-body">
                                <c:out value="${review.getDescription()}"/><br>
                                <h5>
                                    Valutazioni:<br>
                                    Globale - <c:out value="${review.getGlobal_value()}"/> Cibo - <c:out value="${review.getFood()}"/> Servizio - <c:out value="${review.getService()}"/> Atmosfera - <c:out value="${review.getAtmosphere()}"/>
                                </h5>
                            </div>
                      </div>
                    </a>

                    <br>

                </c:forEach>
              
            </div>
              
            <div id="home" class="tab-pane fade">
    
              <div class="list-group">
                  <br>
                  <!-- Notifications -->
                  <c:forEach items="${listNotification}" var="notification">
                      
                      <a href="#" class="list-group-item">
                        <h4 class="list-group-item-heading">
                            <c:out value="${notification.getDescription()}"/>
                            <br>
                        </h4>
                        <p class="list-group-item-text">
                            id = <c:out value="${notification.getId()}"/>
                        </p>
                      </a>
                      
                      <br>
                      
                  </c:forEach>
  
              </div>
            </div>
          </div>
        </div>
       </div>
    </body>
</html>
