<%-- 
    Document   : userProfile
    Created on : May 21, 2016, 12:56:07 PM
    Author     : mario
--%>

<%@page import="database.contexts.ReviewContext"%>
<%@page import="database.ReviewNotification"%>
<%@page import="database.PhotoNotification" %>
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
        <link href="css/userProfile.css" rel="stylesheet">
        <link href="css/jquery-ui.css" rel="stylesheet">

        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
    </head>
    <body>
                
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>

        <div class="container">
        
            <div class="col-md-3">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="thumbnail restaurant">
                            <img src="${user.getAvatar_path()}" alt="normal user">
                            <div class="caption">
                              <hr>

                              <h3><%= user.getNickname() %></h3>
                              <h4>Bentornato sulla tua pagina privata di <b>eatBit</b></h4>
                              <hr>
                              <p><b>Nome:</b>
                                  <br> <%= user.getName()%> <%= user.getSurname()%>  </p>
                              <p><b>Email:</b>
                                  <br> <%= user.getEmail()%>  </p>
                              <p><b>Reviews:</b>
                                  <br><c:out value="${listReview.size()}"/> </p>
                              <p>
                                <form action="CreateRestaurant.jsp" method="get">
                                    <button class="btn btn-primary fixx" type="submit" role="button" onclick="">Carica un Ristorante</button>
                                </form>
                              <hr>
                            </div>
                        </div>
                    </div>
                </div>    
            </div>

            <div class="col-md-9">
                <h2>Risorse Personali</h2>
                <ul class="nav nav-pills restaurant">
                    <li class="active"><a data-toggle="tab" href="#menu1">Recensioni<span class="badge"><c:out value="${listReview.size()}"/></span></a></li>
                    <c:choose>                            
                        <c:when test="${user.getType() == 1}">
                            <li><a data-toggle="tab" href="#menu3">Notifiche<span class="badge"> <c:out value="${listPhotoNotification.size()+listReviewNotification.size()}"/> </span></a></li>
                        </c:when>
                    </c:choose>
                    <li><a data-toggle="tab" href="#menu2">Ristoranti<span class="badge"><c:out value="${listRestaurants.size()}"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu4">Modifica Profilo</a></li>
                </ul>

                <div class="tab-content">
                    
                    <!--MENU 1-->
                    <div id="menu1" class="tab-pane fade in active">
                        <!--Reviews-->
                        <br>
                        <c:choose>                            
                            <c:when test="${numberReview == 0}">
                                <div class="alert alert-info notice mmm" role="alert">
                                        <div class ="row">                                                                                        
                                           &nbsp; Non hai ancora nessuna recensione!
                                           <br>
                                           <br>
                                            &nbsp; Naviga e valuta i ristoranti di eatbit!
                                            <br>                                                                  
                                        </div>
                                    </div>
                            </c:when>
                            
                            <c:otherwise>
                                <c:forEach var="allComments" items="${listReview}">
                                    <div class="comment">
                                        <div class="container-fluid">
                                            <div class="row container-fluid">
                                                <div class="col-md-2 comment-writer">
                                                    <img src="img/avater/avater.png" class="img-circle"/>
                                                    <h5><c:out value="${allComments.getUser().getNickname()}" /></h5>
                                                    <p class="comment-data">
                                                        <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                        <c:out value="${allComments.getReview().getDate_creation()}" />
                                                    </p>
                                                    <c:if test="${allComments.getPhoto()!=null}">
                                                        <a class="thumbnail" href="<c:out value="${allComments.getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${allComments.getPhoto().getPath()}" />">
                                                            <img src="<c:out value="${allComments.getPhoto().getPath()}" />">
                                                        </a>
                                                        <%--
                                                        <div class="text-center">
                                                            <button type="button" class="btn btn-danger btn-segnala-photo-recensione popov" id="<c:out value="${allComments.getPhoto().getId()}"/>" title="Segnala Photo" onclick="segnalaPhoto(this.id)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                                        </div>
                                                        --%>
                                                    </c:if>
                                                </div>
                                                <div class="col-md-10 comment-content">
                                                    <p>
                                                        <button type="button" class="btn btn-danger btn-segnala-review" value="<c:out value="${allComments.getReview().getId()}"  />" disabled="disabled" title="Segnala Recensione" onclick="segnalaReview(this.id)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                                        <h3 class="comment-title"><c:out value="${allComments.getReview().getName()}" /></h3>                                                    
                                                    <div class="row rating-stars">
                                                        <c:forEach var="i" begin="1" end="5">
                                                            <c:choose>
                                                                <c:when test="${allComments.getReview().getGlobal_value()>=i}">
                                                                    <img src="img/star-full.png"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="img/star-empty.png"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </div>
                                                    
                                                    <p class="comment-text"><c:out value="${allComments.getReview().getDescription()}" /> </p>
                                                    
                                                    <c:if test="${allComments.getReply()!=null}">
                                                        <div class="container-fluid">
                                                            <div class="row">
                                                                <div class="col-md-2"></div>
                                                                <div class="col-md-10">
                                                                    <div class="container-fluid risposta-admin">
                                                                        <div class="row">
                                                                            <div class="col-md-2">
                                                                                <p class="lb"><label>Risposta:</label></p>
                                                                            </div>
                                                                            <div class="col-md-10">
                                                                                <p class="risposta-text"><c:out value="${allComments.getReply().getDescription()}" /></p>
                                                                                <p class="risposta-autore">Da: Admin</p>
                                                                                <p class="risposta-date"><c:out value="${allComments.getReply().getDate_creation()}" /></p>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    
                                                    <div class="container-fluid">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <div class="col-md-12">
                                                                    <button type="button" class="btn btn-danger btn-mi-piace"  disabled="disabled"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge"><c:out value="${allComments.getReview().getLikes()}" /></span></button>
                                                                    <button type="button" class="btn btn-danger btn-non-mi-piace" disabled="disabled"><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge"><c:out value="${allComments.getReview().getDislikes()}" /></span></button>
                                                                    <p class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span><a href="http://localhost:8080/eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${allComments.getReview().getId_restaurant()}" />"> <c:out value="${allComments.getRestaurantName()}" /></a></p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                            </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!--MENU 2-->
                    <div id="menu2" class="tab-pane fade">
                        
                        <c:choose>                            
                            <c:when test="${user.getType() == 0}">   
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Non sei ancora un utente Ristoratore!                                                
                                    </div>
                                    <div class="row">
                                        <div class ="col-md-10">
                                        </div>
                                        <div class ="col-md-2">
                                            <a href="CreateRestaurant.html"  class="btn btn-primary diventaRis">Carica un Ristorante!</a>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                                
                            <c:otherwise>                                
                                <c:forEach var="restaurant" items="${listRestaurants}">
                                    <div class="container-fluid restaurant">
                                        <div class="row container-fluid">
                                            <div class="col-md-4 restaurant-title">
                                                <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                                <h4><c:out value="${restaurant.getName()}" /></h4>
                                                <div class="row rating-stars">
                                                    <c:forEach var="i" begin="1" end="5">
                                                        <c:choose>
                                                            <c:when test="${restaurant.getGlobal_value()>=i}">
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
                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Descrizione: </span><span class="info-text"><c:out value="${restaurant.getDescription()}" /></span></p>
                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text"><c:out value="${restaurant.getReviews_counter()}" /></span></p>
                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-euro" aria-hidden="true"></span> Prezzo: </span><span class="info-text">21</span></p>
                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-apple" aria-hidden="true"></span> Cucina: </span>
                                                    <span class="label label-danger">Carne</span>
                                                    <span class="label label-danger">Pesce</span>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="row container-fluid">
                                            <!-- va qua url del ristorante -->                                            
                                            <div class="btn-visita">
                                                <form action="GetRestaurantContextForwardToJspServlet" method="GET">
                                                    <button class ="btn btn-success fixx" type="submit" value="${restaurant.getId()}" name="id_restaurant"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>Visita</button>            
                                                </form>
                                                
                                                <form action="GetRestaurantInfoServlet" method="POST">
                                                    <button class ="btn btn-success fixx" type="submit" value="${restaurant.getId()}" name="restaurant_id"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>Modifica</button>            
                                                </form>
                                            </div>                                            
                                        </div>
                                    </div>

                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        
                        
                    </div>

                    <!--MENU 3-->
                    <div id="menu3" class="tab-pane fade">
                        <c:choose>
                            <c:when test="${(listPhotoNotification.size()+listReviewNotification.size()) <= 0}">
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Nessuna nuova Notifica!                                                
                                    </div>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                <div class="col-md-6">
                                    <br>
                                    <!-- PhotoNotifications -->
                                    <h4>Nuove foto</h4>                           

                                    <c:forEach items="${listPhotoNotification}" var="photoNotification">
                                        <div class="alert alert-info notice notificaFoto" role="alert">
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp;<b><c:out value="${photoNotification.getUser().getName()}" /></b> ha caricato una foto su <b><c:out value="${photoNotification.getRestaurant_name()}" /></b>
                                                </a>
                                            </div>
                                            
                                            <div class="row">
                                                <div class ="col-md-10">
                                                    <div class="contenutoNotFoto">
                                                        <a class="thumbnail" href="<c:out value="${photoNotification.getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${photoNotification.getPhoto().getPath()}" />">
                                                            <img src="<c:out value="${photoNotification.getPhoto().getPath()}" />">
                                                        </a>                                                                                                                                                                                               
                                                    </div>
                                                </div>
                                                <div class ="col-md-2">                                                    
                                                </div>
                                            </div>
                                                    
                                            <div class="row">
                                                <div class ="col-md-10">
                                                    <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                    <c:out value="${photoNotification.getCreation()}"></c:out>
                                                </div>
                                                <div class ="col-md-2">   
                                                    <button  class="btn btn-primary diventaRis removePhotoNot" value="${photoNotification.getId()}">Non vedere più</button>
                                                </div>                                                        
                                            </div>
                                                    
                                            <div class="row">
                                                <div class ="col-md-10">
                                                </div>
                                                <div class ="col-md-2">   
                                                    <button  class="btn btn-primary diventaRis " value="${photoNotification.getId()}"  onclick="segnalaPhoto(${photoNotification.getPhoto().getId()})">Segnala subito</button>
                                                </div>                                                        
                                            </div>
                                        </div>
                                    </c:forEach>  
                                </div>                   

                                <div class="col-md-6">
                                    <br>
                                    <!-- ReviewNotifications -->
                                    <h4>Nuove recensioni</h4>                           

                                    <c:forEach items="${listReviewNotification}" var="reviewNotification">
                                        <div class="alert alert-info notice  not notificaRecensione" role="alert">
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp; Nuova recensione su <b><c:out value="${reviewNotification.getRestaurant_name()}" /></b>                                                   
                                                </a>                                                
                                            </div>
                                                
                                            <div class="row">
                                                <div class ="col-md-10">                                                    
                                                    <div class="panel panel-primary comm">
                                                        <div class="panel-heading">
                                                            <h3 class="panel-title"><c:out value="${reviewNotification.getUser().getName()}" /> ha commentato:</h3>
                                                        </div>
                                                        <div class="panel-body">
                                                            <c:out value="${reviewNotification.getReview().getDescription()}" />
                                                        </div>           
                                                    </div>
                                                </div>
                                                <div class ="col-md-2">                                                    
                                                </div>
                                            </div>    
                                                
                                            <div class="row">
                                                <div class ="col-md-10">
                                                    
                                                    <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                    <c:out value="${reviewNotification.getReview().getDate_creation()}"></c:out>
                                                    
                                                </div>
                                                <div class ="col-md-2">
                                                    <button  class=" right btn btn-primary diventaRis removeReviewNot" value="${reviewNotification.getId()}">Non vedere più</button>
                                                </div>
                                            </div>
                                                
                                            <div class="row">
                                                <div class ="col-md-10">
                                                </div>
                                                <div class ="col-md-2">
                                                    <button  class=" right btn btn-primary diventaRis rispRecensione" value="${reviewNotification.getId()}">Rispondi subito</button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>  
                                </div>      
                                
                                <div class="row">
                                        <div class="col-md-9">                                           
                                        </div>
                                        <div class="col-md-2">
                                            <a href="GetAllNotify" class="btn btn-primary">Vedi tutte le notifiche!</a>
                                        </div>
                                    <div class="col-md-2"></div>
                                </div>
                                
                            </c:otherwise>      
                        </c:choose>
                    </div>          
                    
                    <!--MENU 4-->
                    <div id="menu4" class="tab-pane fade">
                        
                        <div class="row">       
                            <FORM enctype='multipart/form-data' method='POST' action='ModifyProfileServlet'>
                            <ul class="list-group modifica restaurant">
                                
                                <li class="list-group-item">
                                    <div class='left'>
                                        Nome
                                    </div>
                                    <div class='right'>
                                        <input name="name" type="text" class="form-control" value="${user.getName()}" aria-describedby="basic-addon1">
                                    </div>
                                </li>
                                
                                <li class="list-group-item">
                                    <div class='left'>
                                        Cognome
                                    </div>
                                    <div class='right'>
                                        <input name="surname" type="text" class="form-control" value="${user.getSurname()}" aria-describedby="basic-addon1">
                                    </div>
                                </li>
                                
                                <li class="list-group-item">
                                    <div class='left'>
                                        Email
                                    </div>
                                    <div class='right'>
                                        <input type="text" class="form-control" disabled placeholder="${user.getEmail()}" aria-describedby="basic-addon1">
                                    </div>
                                </li>
                                
                                <li class="list-group-item">
                                    <div class='left'>
                                        Password
                                    </div>
                                    <div class='right'>
                                        <input type="text" class="form-control" disabled placeholder="*******" aria-describedby="basic-addon1">
                                    </div>
                                </li>
                                
                                <li class="list-group-item">
                                    <div class='left'>
                                        Cambia Avatar
                                        <div class="input-group">
                                            <label class="input-group-btn">
                                                <span class="btn btn-default">
                                                    Cerca File&hellip; <input name="avatar" type="file" style="display: none;" multiple>
                                                </span>
                                            </label>
                                            <input type="text" class="form-control" readonly>
                                        </div>
                                    </div>
                                </li>
                                
                                <li class="list-group-item">
                                    <div class="right">
                                        <p><button class="btn btn-primary fixx" type="submit" role="button">Salva Modifiche</button></p>
                                    </div>
                                    
                                </li>
                            </ul>   
                           </form>
                                    <form action="/" method="POST">
                                        <div class="right">
                                            <p><button class="btn btn-primary fixx cPwd" type="submit" role="button" onclick="">Cambia Password</button></p>
                                        </div>
                                    </form>
                        </div>
                                    
                        
                    </div>
                </div>
            </div>           
        </div>

         <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>

        <!--footer-->
        <%@include file="components/footer.html"%>
        
        <!-- Restaurant JS-->
        <script type="text/javascript" src="js/userProfile.js"></script>

        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
    </body>
</html>
