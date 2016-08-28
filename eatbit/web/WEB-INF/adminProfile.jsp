<%-- 
    Document   : userProfile
    Created on : May 21, 2016, 12:56:07 PM
    Author     : mario
--%>


<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>

<%@page import="database.contexts.AttemptContext"%>
<%@page import="database.contexts.ReplyContext"%>
<%@page import="database.contexts.PhotoContext"%>
<%@page import="database.contexts.ReviewContext"%>
<%@page import="database.Restaurant"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
    <head>       
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <title>Admin Page</title>
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/cssFooter.css" rel="stylesheet">
        <link href="css/userProfile.css" rel="stylesheet">
        <link href="css/adminProfile.css" rel="stylesheet">
        <link href="css/jquery-ui.css" rel="stylesheet">
        <!-- Pnotify css -->
       <link href="css/pnotify.custom.min.css" rel="stylesheet">
       
        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        
    </head>
    <body>
        <c:set var="req" value="${pageContext.request}" />
         <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />     
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>
        
        <div class="container">
        
            <div class="col-md-3">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="thumbnail restaurant">
                            <div class="AvatarContainer">
                                <img class="Image" src="${user.getAvatar_path()}" alt="normal user">
                            </div>
                            <div class="caption" style="word-wrap:break-word;">
                                <hr>
                                <h3><%= user.getNickname() %></h3>
                                <h4>Bentornato sulla tua pagina privata di <b>eatBit</b></h4>
                                <hr>
                                <p><b>Tuo Nome:</b>
                                <br>
                                <%= user.getName()%> <%= user.getSurname()%>  </p>
                                <p><b>Email:</b>
                                <br> <%= user.getEmail()%>  </p>


                                <div class="btn-group">
                                    
                                    <form action="${baseURL}/GetAllAdmin" method="POST">
                                            <button class="btn btn-primary " type="submit" role="button">Vedi tutte le notifiche</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>    
            </div>

            <div class="col-md-9">
                
                <h2>Profilo Privato</h2>
                <ul class="nav nav-pills restaurant">
                    <li class="active"><a data-toggle="tab" href="#menu1">Ristoranti in Attesa<span class="badge"><c:out value="${ristorantiAttesa.size()}"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu2">Risposte da Confermare<span class="badge"><c:out value="${risposteConfermare.size()}"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu3">Segnalati<span class="badge"> <c:out value="${listPhotoNotification.size()+listReviewNotification.size()}"/> </span></a></li>
                    <li><a data-toggle="tab" href="#menu4">Informazioni Profilo</a></li>
                </ul>

                <div class="tab-content">     

                    <div id="menu1" class="tab-pane fade in active">
                        <br>
                        <c:choose>
                            <c:when test="${ristorantiAttesa.size() == 0}">
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Nessun ristorante in attesa di conferma!                                                
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <!--RistorantiAttesa-->
                                <br>
                                <c:forEach items="${ristorantiAttesa}" var="rA">

                                    <div class="alert alert-info notice restaurant" role="alert">
                                        <div class ="row">
                                            <a href="#">
                                                <c:choose>
                                                    <c:when test="${rA.getIsClaim() == 0}">
                                                        &nbsp; L'utente <b><c:out value="${rA.getUser().getName()}" /></b> 
                                                        &nbsp;é in attesa di conferma dopo la <b>creazione</b> del ristorente <b><c:out value="${rA.getRestaurant().getName()}" /></b>
                                                    </c:when>
                                                    <c:when test="${rA.getIsClaim() == 1}">
                                                        &nbsp; L'utente <b><c:out value="${rA.getUser().getName()}" /></b> 
                                                        &nbsp;<b>reclama</b> il ristorante <b><c:out value="${rA.getRestaurant().getName()}" /></b>
                                                    </c:when>
                                                    <c:otherwise>
                                                        &nbsp; L'utente <b><c:out value="${rA.getUser().getName()}" /></b> 
                                                        &nbsp;<b>ha creato e reclamato</b> il ristorante <b><c:out value="${rA.getRestaurant().getName()}" /></b>
                                                    </c:otherwise>
                                                </c:choose>                                        
                                            </a>
                                            <br>
                                            <hr>
                                             
                                            <blockquote class="blockquote">
                                                <p><c:out value="${rA.getUsertextclaim()}"/></p>
                                                
                                              </blockquote>
                                        </div>
                                        <div class="row">
                                            <div class ="col-md-8">
                                            </div>
                                            <div class ="col-md-2">
                                                <input id="valore" class="hidden" value="${rA.getUser().getId()}"/> 
                                                <button  class="btn btn-primary acceptRestaurantAttempt" value="${rA.getRestaurant().getId()}">Accept</button>
                                            </div>

                                            <div class ="col-md-2">
                                                <button  class="btn btn-primary declineRestaurantAttempt" value="${rA.getRestaurant().getId()}">Decline</button>
                                            </div>
                                        </div>
                                    </div>

                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>



                    <div id="menu2" class="tab-pane fade">
                        <div class="list-group">
                            <br>
                            <c:choose>
                                <c:when test="${risposteConfermare.size() == 0}">
                                    <div class="alert alert-info notice restaurant" role="alert">
                                        <div class ="row">
                                            &nbsp; Nessuna Reply in attesa di accettazione!                                                
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${risposteConfermare}" var="RC">
                                        <div class="alert alert-info notice  notificaRecensione" style="border-radius:0px;" role="alert">
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp; <span>Reply in attesa di accettazione!</span>                                                   
                                                </a>                                                
                                            </div>
                                            
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp;&nbsp;&nbsp; <span>Review:</span>                                                   
                                                </a>                                                
                                            </div>

                                            <div class="row">
                                                
                                                <div class ="col-md-12">                                                    
                                                    <div class="panel panel-primary comm">
                                                        <div class="panel-heading">
                                                            <h3 class="panel-title"><c:out value="${RC.getUser().getName()}" /> ha commentato:</h3>
                                                        </div>
                                                        <div class="panel-body">
                                                            <c:out value="${RC.getReview().getDescription()}" />
                                                        </div>
                                                        <div class ="row dx">
                                                            <div class="col-md-12">
                                                                &nbsp;
                                                                <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                                <c:out value="${RC.getReview().getDate_creation().toLocaleString()}"></c:out>
                                                            </div>
                                                            
                                                        </div>
                                                    </div>
                                                        
                                                </div>
                                                
                                            </div>    
                                            
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp;&nbsp;&nbsp; <span>Reply:</span>                                                   
                                                </a>                                                
                                            </div>
                                                            
                                            <div class="row">
                                                <div class ="col-md-12">                                                    
                                                    <div class="panel panel-primary comm">
                                                        <div class="panel-heading">
                                                            <h3 class="panel-title">Il proprietario ha risposto:</h3>
                                                        </div>
                                                        <div class="panel-body">
                                                            <c:out value="${RC.getReply().getDescription()}" />
                                                        </div>   
                                                        <div class ="row dx">
                                                            <div class="col-md-12">
                                                                &nbsp;
                                                                <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                                <c:out value="${RC.getReply().getDate_creation().toLocaleString()}"></c:out>
                                                            </div>
                                                            
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>                                                    

                                            <div class="row">
                                                <div class ="col-md-8">
                                                </div>
                                                <div class ="col-md-2">
                                                    <div class="row">
                                                        <button  class=" right btn btn-primary fisso acceptReply " value="${RC.getReply().getId()}">Accept</button>
                                                        <button  class=" right btn btn-primary fisso declineReply" value="${RC.getReply().getId()}">Decline</button>
                                                    </div>
                                                </div>
                                                <div class="col-md-2">
                                                    
                                                </div>
                                            </div>

                                        </div>
                                    </c:forEach>  
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    
                    <div id="menu3" class="tab-pane fade">
                        <div class="list-group">
                            <br>
                            <c:choose>
                            <c:when test="${(listPhotoNotification.size()+listReviewNotification.size()) <= 0}">
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Nessuna nuova Notifica!                                                
                                    </div>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                <div class ="row">
                                    <c:if test="${ (listPhotoNotification.size()-1) >= 0}">
                                        <c:forEach begin="0" end="${(listPhotoNotification.size()-1)}" var="i">
                                        
                                            <div class="alert alert-info notice notificaFoto" role="alert">
                                                    <div class ="row">
                                                        <a href="#">
                                                            &nbsp;<span>Segnalazione Foto</span>
                                                        </a>
                                                    </div>

                                                    <div class="row">
                                                        <div class ="col-md-10">
                                                            <div class="contenutoNotFoto">
                                                                <a class="thumbnail" href="<c:out value="${listPhotoNotification.get(i).getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${listPhotoNotification.get(i).getPhoto().getPath()}" />">
                                                                    <img src="<c:out value="${listPhotoNotification.get(i).getPhoto().getPath()}" />">
                                                                </a>                                                                                                                                                                                               
                                                            </div>
                                                        </div>
                                                        <div class ="col-md-2">                                                    
                                                        </div>
                                                    </div>

                                                    <div class="row">
                                                        <div class ="col-md-9">
                                                        </div>
                                                        <div class ="col-md-2">   
                                                            <div class="row">
                                                                <button class="btn btn-primary fisso acceptReportedPhoto" value="${listPhotoNotification.get(i).getPhoto().getId()}">Accept</button>
                                                                <button  class="btn btn-primary fisso declineReportedPhoto " value="${listPhotoNotification.get(i).getPhoto().getId()}" >Decline</button>
                                                            </div>
                                                            </div>       
                                                        <div class="col-md-1">                                                    
                                                        </div>
                                                    </div>

                                                </div>
                                        </c:forEach>
                                        </c:if>
                                        
                                    <c:if test="${ (listReviewNotification.size()-1) >= 0}">
                                            <c:forEach begin="0" end="${(listReviewNotification.size()-1)}" var="i">
                                                <div class="alert alert-info notice  notificaRecensione" role="alert">
                                                    <div class ="row">
                                                        <a href="#">
                                                            &nbsp; <span>Segnalazione Review</span>                                                   
                                                        </a>                                                
                                                    </div>

                                                    <div class="row">
                                                        <div class ="col-md-10">                                                    
                                                            <div class="panel panel-primary comm">
                                                                <div class="panel-heading">
                                                                    <h3 class="panel-title"><c:out value="${listReviewNotification.get(i).getUser().getName()}" /> ha commentato:</h3>
                                                                </div>
                                                                <div class="panel-body">
                                                                    <c:out value="${listReviewNotification.get(i).getReview().getDescription()}" />
                                                                </div>           
                                                            </div>
                                                        </div>
                                                        <div class ="col-md-2">                                                    
                                                        </div>
                                                    </div>    

                                                    <div class="row">
                                                        <div class ="col-md-9">

                                                            <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                            <c:out value="${listReviewNotification.get(i).getReview().getDate_creation().toLocaleString()}"></c:out>

                                                        </div>
                                                        <div class ="col-md-2">
                                                            <div class="row">
                                                                <button class="right btn btn-primary fisso acceptReportedReview" value="${listReviewNotification.get(i).getReview().getId()}"/>Accept</button>
                                                                <button class=" right btn btn-primary fisso declineReportedReview" value="${listReviewNotification.get(i).getReview().getId()}"/>Decline</button>
                                                            </div>
                                                            </div>
                                                        <div class="col-md-1">                                                    
                                                        </div>
                                                    </div>

                                                </div>
                                            </c:forEach>
                                        </c:if>                                                                                
                                    
                                    
                                </div>
                            </c:otherwise>      
                        </c:choose>
                        </div>
                    </div>
                    
                    <!--MENU 4-->
                    <div id="menu4" class="tab-pane fade">
                        
                        <div class="row">       
                            <FORM enctype='multipart/form-data' method='POST' action='${baseURL}/ModifyProfileServlet'>
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
                                                     <input name="avatar" type="file" multiple>
                                                </span>
                                            </label>
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
                                  <form id="pswForm" method="POST">
                                        <input type="hidden" name="id_user" value="${user.id}"/>
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
        
        <!-- Admin JS-->
        <script type="text/javascript" src="js/adminProfile.js"></script>
        <script type="text/javascript" src="js/index.js"></script>
        
        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
        
        
    </body>
</html>
