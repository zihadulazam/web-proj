<%-- 
    Document   : userProfile
    Created on : May 21, 2016, 12:56:07 PM
    Author     : mario
--%>

<%@page import="database.Review"%>
<%@page import="database.Notification"%>
<%@page import="database.Restaurant"%>


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
        <link href="css/userProfile.css" rel="stylesheet">
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
    <body id="sfondo">
                
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>

        
        <div class="container">
        
        <div class="col-md-3">
            <div class="row">
                <div class="col-sm-12">
                  <div class="thumbnail">
                      <img src="img/user_default.png" alt="normal user">
                    <div class="caption">
                      <hr>
                      
                      <h3><%= user.getNickname() %></h3>
                      <h4>Bentornato sulla tua pagina privata di <b>eatBit</b></h4>
                      <hr>
                      <p><b>Tuo Nome:</b>
                          <br> <%= user.getName()%> <%= user.getSurname()%>  </p>
                      <p><b>Email:</b>
                          <br> <%= user.getEmail()%>  </p>
                      <p><b>Reviews:</b>
                          <br><c:out value="${numberReview}"/> </p>
                      
                      <p>
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
          <h2>Risorse Personali</h2>
          <ul class="nav nav-pills">
                <li class="active"><a data-toggle="tab" href="#menu1">Tuoi Commenti<span class="badge"><c:out value="${numberReview}"/></span></a></li>
                <li><a data-toggle="tab" href="#menu2">Ristoranti<span class="badge"><c:out value="${numberRestaurants}"/></span></a></li>
                <li><a data-toggle="tab" href="#menu3">Notifiche<span class="badge"> <c:out value="${numberNotification}"/> </span></a></li>
                <li><a data-toggle="tab" href="#menu4">Informazioni Profilo</a></li>
          </ul>

          <div class="tab-content">
              
            <div id="menu1" class="tab-pane fade in active">
                <!--Reviews-->
                <br>
                
                <c:forEach items="${listReview}" var="review">
                    <div class="comment">
                        <!--primo commento -->                                                
                        <div class="container-fluid">
                            <div class="row container-fluid">
                                <div class="col-md-2 comment-writer">
                                    <img src="img/avater/avater.png" class="img-circle"/>
                                    <h5>You</h5>
                                    <p class="comment-data">
                                        <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                       <c:out value="${review.getDate_creation()}"/> 
                                    </p>
                                </div>
                                <div class="col-md-10 comment-content">
                                    <h3 class="comment-title"><c:out value="${review.getName()}"/></h3>
                                    <div class="row rating-stars">
                                        <img src="img/star-full.png"/>
                                        <img src="img/star-full.png"/>
                                        <img src="img/star-full.png"/>
                                        <img src="img/star-empty.png"/>
                                        <img src="img/star-empty.png"/>
                                    </div>

                                    <p class="comment-text"><c:out value="${review.getDescription()}"/> </p>

                                    <div class="container-fluid">
                                        <div class="row">
                                            <div class="col-md-2"></div>
                                        </div>
                                    </div>

                                    <div class="container-fluid">
                                        <div class="row">
                                            <div class="col-md-6"> 
                                            </div>
                                            <div class="col-md-6">
                                                <h4 class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Ristorante: <a href="#">Nome del Ristorante</a></h4>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                                    
            </div>
              
            <div id="menu2" class="tab-pane fade">
 
                    <c:forEach var="restaurant" items="${listRestaurants}">
                        
                        <div class="container-fluid restaurant">
                            <div class="row container-fluid">
                                <div class="col-md-4 restaurant-title">
                                    <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                    <h4><c:out value="${restaurant.getName()}" /></h4>
                                    <div class="row rating-stars">
                                        <c:forEach var="i" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${restaurant.global_value>=i}">
                                                    <img src="img/star-full.png"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="img/star-empty.png"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="col-md-8 restaurant-body">
                                    <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text">Via pasina-51, Riva del Garda, 38066, Trento</span></p>
                                    <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text">256</span></p>
                                    <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-euro" aria-hidden="true"></span> Prezzo: </span><span class="info-text">21</span></p>
                                    <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-apple" aria-hidden="true"></span> Cucina: </span>
                                        <span class="label label-danger">Carne</span>
                                        <span class="label label-danger">Pesce</span>
                                    </p>
                                </div>
                            </div>
                            <div class="row container-fluid">
                                <!-- va qua url del ristorante -->
                                <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button></div>
                            </div>
                        </div>
                        
                    </c:forEach>
  
              
                      
            </div>
              
            <div id="menu3" class="tab-pane fade">
    
              <div class="list-group">
                  <br>
                  <!-- Notifications -->
                  <c:forEach items="${listNotification}" var="notification">
                      
                      <a href="#">
                        <div class="alert alert-info notice" role="alert">notifica
                            
                        </div>
                        
                      </a>
                      
                  </c:forEach>
  
              </div>                      
            </div>          
              
            <div id="menu4" class="tab-pane fade">
              <h3>Profile Informations</h3>
              <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.</p>
            </div>
          </div>
        </div>
            
        </div>
                
       <!--footer-->
       <%@include file="components/footer.html"%>

    </body>
</html>
