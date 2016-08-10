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
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
                
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

        <script>
            $(document).ready(function() {
                $(".resolveNotify").click(function(event) {
                    var notifyId = $(this).val();
                    var element = $(this);
                    $.ajax(
                    {
                        url : "../eatbit/RemovePhotoNotificationFromUserServlet",
                        type: "POST",
                        data : {notifyId:notifyId},
                        success:function(data)  
                        {
                            //data: return data from server
                            if(data == "OK"){
                                //window.location.replace("/home");
                                element.remove();
                            }
                            else{
                                alert("Chiamata fallita!!!");            
                            }
                        },
                       error: function() 
                        {
                            alert("Errore Server!!!");     
                        }
                             });
                });
             });
        </script>
        
                <script>
            $(document).ready(function() {
                $(".resolveNotify2").click(function(event) {
                    var notifyId = $(this).val();
                    var element = $(this);
                    $.ajax(
                    {
                        url : "../eatbit/RemoveReviewNotificationFromUserServlet",
                        type: "POST",
                        data : {notifyId:notifyId},
                        success:function(data)  
                        {
                            //data: return data from server
                            if(data == "OK"){
                                //window.location.replace("/home");
                                element.remove();
                            }
                            else{
                                alert("Chiamata fallita!!!");            
                            }
                        },
                       error: function() 
                        {
                            alert("Errore Server!!!");     
                        }
                             });
                });
             });
        </script>

        
    </head>
    <body>
                
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>
        
        <input type="hidden" name="viewid" value="test.jsp">
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
                                  <br><c:out value="${numberReview}"/> </p>
                              <hr>
                            </div>
                        </div>
                    </div>
                </div>    
            </div>

            <div class="col-md-9">
                <h2>Risorse Personali</h2>
                <ul class="nav nav-pills restaurant">
                    <li class="active"><a data-toggle="tab" href="#menu1">Recensioni<span class="badge"><c:out value="${numberReview}"/></span></a></li>
                    <c:choose>                            
                        <c:when test="${user.getType() == 1}">
                            <li><a data-toggle="tab" href="#menu3">Notifiche<span class="badge"> <c:out value="${numberListPhotoNotification}"/> </span></a></li>
                        </c:when>
                    </c:choose>
                    <li><a data-toggle="tab" href="#menu2">Ristoranti<span class="badge"><c:out value="${numberRestaurants}"/></span></a></li>
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
                                            <a href="#">
                                               
                                               &nbsp; Non hai ancora nessuna recensione!
                                               <br>
                                               <br>
                                                &nbsp; Naviga e valuta i ristoranti di eatbit!
                                                <br>                          
                                            </a>
                                        </div>
                                    </div>
                            </c:when>
                            
                            <c:otherwise>
                                <c:forEach var="reviewContext" items="${listReview}">
                                    <div class="comment">
                                        <!--primo commento -->                                                
                                        <div class="container-fluid">
                                            <div class="row container-fluid">
                                                <div class="col-md-2 comment-writer">
                                                    <img src="img/avater/avater.png" class="img-circle"/>
                                                    <h5>You</h5>
                                                    <p class="comment-data">
                                                        <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                        <c:out value="${reviewContext.getReview().getDate_creation()}"></c:out>
                                                    </p>
                                                </div>
                                                <div class="col-md-10 comment-content">
                                                    <h3 class="comment-title"><c:out value="${reviewContext.getReview().getName()}"></c:out></h3>
                                                    <div class="row rating-stars">
                                                        <c:forEach var="i" begin="1" end="5">
                                                            <c:choose>
                                                                <c:when test="${reviewContext.getReview().getGlobal_value()>=i}">
                                                                    <img src="img/star-full.png"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="img/star-empty.png"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </div>

                                                    <p class="comment-text"><c:out value="${reviewContext.getReview().getDescription()}"></c:out> </p>

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
                                                                <h4 class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Ristorante: <a href="#"><c:out value="${reviewContext.getRestaurantName()}"></c:out></a></h4>
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
                                                <a href="#">
                                                   &nbsp; Non sei ancora un utente Ristoratore! 
                                                </a>
                                            </div>
                                            <div class="row">
                                                <div class ="col-md-10">
                                                </div>
                                                <div class ="col-md-2">
                                                    <button  class="btn btn-primary diventaRis">Carica un Ristorante!</button>
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
                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button></div>
                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-pencil" aria-hidden="true"></span> Modifica</button></div>
                                        </div>
                                    </div>

                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        
                        
                    </div>

                    <!--MENU 3-->
                    <div id="menu3" class="tab-pane fade">                                                                    

                                
                            <div class="col-md-6">
                                <br>
                                <!-- PhotoNotifications -->
                                <h4>Nuove foto caricate sui tuoi ristoranti</h4>                           

                                <c:forEach items="${listPhotoNotification}" var="photoNotification">
                                    <div class="alert alert-info notice  not" role="alert">
                                        <div class ="row">
                                            <a href="#">
                                               &nbsp; Hanno caricato una nuova foto sul tuo ristorante!
                                            </a>
                                        </div>
                                        <div class="row">
                                            <div class ="col-md-10">
                                            </div>
                                            <div class ="col-md-2">
                                                <button  class="btn btn-primary diventaRis" value="${photoNotification.getId()}">Non vedere più!</button>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>      

                            </div>                   

                            <div class="col-md-6">
                                <br>
                                <!-- ReviewNotifications -->
                                <h4>Nuove recensioni sui tuoi ristoranti</h4>                          

                                <c:forEach items="${listPhotoNotification}" var="photoNotification">
                                    <div class="alert alert-info notice restaurant not" role="alert">
                                        <div class ="row">
                                            <a href="#">
                                               &nbsp; Hanno caricato una nuova foto sul tuo ristorante!
                                            </a>
                                        </div>
                                        <div class="row">
                                            <div class ="col-md-10">
                                            </div>
                                            <div class ="col-md-2">
                                                <button  class="btn btn-primary diventaRis" value="${photoNotification.getId()}">Non vedere più!</button>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>  
                            </div>
                                
                        

                            

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
                                        <input name="name" type="text" class="form-control" placeholder="${user.getName()}" aria-describedby="basic-addon1">
                                    </div>
                                </li>
                                
                                <li class="list-group-item">
                                    <div class='left'>
                                        Cognome
                                    </div>
                                    <div class='right'>
                                        <input name="surname" type="text" class="form-control" placeholder="${user.getSurname()}" aria-describedby="basic-addon1">
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
                                    <div class="right">
                                        <p><button class="btn btn-primary fixx" type="submit" role="button" onclick="">Cambia Password</button></p>
                                    </div>
                                </li>
                            </ul>   
                           </form>
                        </div>
                                    
                        
                    </div>
                </div>
            </div>           
        </div>
        
       <!--footer-->
       <%@include file="components/footer.html"%>

    </body>
</html>
