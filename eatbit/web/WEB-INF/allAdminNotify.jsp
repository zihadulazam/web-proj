<%-- 
    Document   : allAdmin
    Created on : Aug 21, 2016, 6:20:15 PM
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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <title>All Notify</title>
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        <link href="css/cssFooter.css" rel="stylesheet">
        <link href="css/userProfile.css" rel="stylesheet">
        <link href="css/adminProfile.css" rel="stylesheet">
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>  
                <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    </head>
    <body>
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>
        
        <div class="container">
                <h2>Profilo Privato</h2>
                <ul class="nav nav-pills restaurant">
                    <li class="active"><a data-toggle="tab" href="#menu1">Ristoranti in Attesa<span class="badge"><c:out value="${ristorantiAttesa.size()}"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu2">Risposte da Confermare<span class="badge"><c:out value="${risposteConfermare.size()}"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu3">Segnalati<span class="badge"> <c:out value="${listPhotoNotification.size()+listReviewNotification.size()}"/> </span></a></li>
                    
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
                                                        &nbsp; é in attesa di conferma dopo la <b>creazione</b> del ristorente <b><c:out value="${rA.getRestaurant().getName()}" /></b>
                                                    </c:when>
                                                    <c:when test="${rA.getIsClaim() == 1}">
                                                        &nbsp; L'utente <b><c:out value="${rA.getUser().getName()}" /></b> 
                                                        &nbsp; <b>reclama</b> il ristorante <b><c:out value="${rA.getRestaurant().getName()}" /></b>
                                                    </c:when>
                                                    <c:otherwise>
                                                        &nbsp; L'utente <b><c:out value="${rA.getUser().getName()}" /></b> 
                                                        &nbsp; <b>ha creato e reclamato</b> il ristorante <b><c:out value="${rA.getRestaurant().getName()}" /></b>
                                                    </c:otherwise>
                                                </c:choose>                                        
                                            </a>
                                            <br>
                                            <hr>
                                             
                                            <blockquote class="blockquote">
                                                <p><c:out value="${rA.getUsertextclaim()}"/></p>
                                                
                                              </blockquote>
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
                            <c:when test="${(listPhotoNotification.size()+reviewSegnalate.size()) <= 0}">
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Nessuna nuova Notifica!                                                
                                    </div>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                
                                <div class ="row">
                                    
                                    <c:choose>
                                        <c:when test="${ (listPhotoNotification.size() > listReviewNotification.size()) && (listPhotoNotification.size() >= 1) }">
                                            <c:set var="indice_max_notifiche" value="${listPhotoNotification.size()-1}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${listReviewNotification.size() >= 1}">
                                                <c:set var="indice_max_notifiche" value="${listReviewNotification.size()-1}" />
                                            </c:if>
                                            <c:if test="${ (listReviewNotification.size() < 1) && (listPhotoNotification.size() < 1) }">
                                                <c:set var="indice_max_notifiche" value="${0}" />
                                            </c:if>                                                
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    <c:forEach begin="0" end="${indice_max_notifiche}" var="i">
                                        <c:if test="${ (i <= listPhotoNotification.size()-1) && (i>0)}">
                                            
                                            <div class="alert alert-info notice notificaFoto" role="alert">
                                                    <div class ="row">
                                                        <a href="#">
                                                            &nbsp;<span>Segnalazione Foto</span>
                                                        </a>
                                                    </div>

                                                    <div class="row">
                                                        <div class ="col-md-10">
                                                            <div class="contenutoNotFoto">
                                                                <a class="thumbnail" style="display: -moz-box;" href="<c:out value="${listPhotoNotification.get(i).getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${listPhotoNotification.get(i).getPhoto().getPath()}" />">
                                                                    <img src="<c:out value="${listPhotoNotification.get(i).getPhoto().getPath()}" />">
                                                                </a>                                                                                                                                                                                               
                                                            </div>
                                                        </div>
                                                        <div class ="col-md-2">                                                    
                                                        </div>
                                                    </div>

                                                </div>
                                            
                                        </c:if>
                                        
                                        <c:if test="${ (i <= listReviewNotification.size()-1) && (i>0)}">
                                        
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
                                                </div>
                                            </div>
                                            
                                        </c:if>                                                                                
                                    </c:forEach>
                                    
                                </div>
                            </c:otherwise>      
                        </c:choose>
                        </div>
                    </div>
                    
                   

                </div> 
        </div>
              
                           
                                            
         <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
                
        <!--footer-->
        <%@include file="components/footer.html"%>
                        
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        
        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
        
        <!-- Admin JS-->
        <script type="text/javascript" src="js/adminProfile.js"></script>
        <script type="text/javascript" src="js/index.js"></script>
                    
    </body>
</html>
