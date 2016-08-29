<%-- 
    Document   : postReply
    Created on : Aug 26, 2016, 1:04:47 AM
    Author     : mario
--%>
<%@page import="database.contexts.ReviewContext"%>
<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
                
        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/userProfile.css" rel="stylesheet">
        <link href="css/jquery-ui.css" rel="stylesheet">

        <!-- single img Viewer css-->
        <!--questo riferimento forse va cancellato-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        
        <title>Rispondi al Commento</title>
    </head>
    <body>
        <!-- include navbar hear -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>
        
        <c:set var="req" value="${pageContext.request}" />
        <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
        
        
        <div class ="container">
            <br/>
     
            <div class="comment">
                <div class="container-fluid">
                    <div class="row container-fluid">
                        <div class="col-md-2 comment-writer">
                            <img src="${review.getUser().getAvatar_path()}" class="img-circle" alt="${review.getUser().getAvatar_path()}"/>
                            <h5><c:out value="${review.getUser().getNickname()}" /></h5>
                            <p class="comment-data">
                                <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                <c:out value="${review.getReview().getDate_creation().toLocaleString()}" />
                            </p>
                            <c:if test="${review.getPhoto()!=null}">
                                <a class="thumbnail" href="<c:out value="${review.getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${allComments.getPhoto().getPath()}" />">
                                    <img src="<c:out value="${review.getPhoto().getPath()}" />">
                                </a>

                                
                            </c:if>
                        </div>
                        <div class="col-md-10 comment-content">
                            <p>
                                <button type="button" class="btn btn-danger btn-segnala-review" value="<c:out value="${review.getReview().getId()}"  />" disabled="disabled" title="Segnala Recensione" onclick="segnalaReview(this.id)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                <h3 class="comment-title"><c:out value="${review.getReview().getName()}" /></h3>                                                    
                            <div class="row rating-stars">
                                <c:forEach var="i" begin="1" end="5">
                                    <c:choose>
                                        <c:when test="${review.getReview().getGlobal_value()>=i}">
                                            <img src="img/star-full.png"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="img/star-empty.png"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>

                            <p class="comment-text"><c:out value="${review.getReview().getDescription()}" /> </p>

                            
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-md-2"></div>
                                    <div class="col-md-10">
                                        <div class="container-fluid risposta-admin">
                                            <div class="row">
                                                <div class="col-md-2">
                                                    <p class="lb"><label>Inserisci Risposta:</label></p>
                                                </div>
                                                <div class="col-md-10">
                                                    <p class="risposta-text">
                                                    <form action="${baseURL}/AddReplyServlet" method="POST">
                                                        <textarea name="reply_text" style="color:black; text-align: left;" rows="4" cols="50">Scrivi qui il tuo messaggio</textarea>
                                                        <input type="hidden" name="id_review" value="${review.getReview().getId()}" />
                                                        <br>
                                                        <button  type="submit"  class="btn btn-primary go_reply">Pubblica Risposta</button>
                                                    </form>                                                    
                                                    <p class="risposta-autore">Da: Proprietario</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            

                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-12">
                                            <button type="button" class="btn btn-danger btn-mi-piace"  disabled="disabled"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge"><c:out value="${review.getReview().getLikes()}" /></span></button>
                                            <button type="button" class="btn btn-danger btn-non-mi-piace" disabled="disabled"><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge"><c:out value="${review.getReview().getDislikes()}" /></span></button>
                                            <p class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span><a href="${baseURL}/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${review.getReview().getId_restaurant()}" />"> <c:out value="${allComments.getRestaurantName()}" /></a></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    
   
            
        </div>
        
        <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>

        <!--footer-->
        <%@include file="components/footer.html"%>
        
        <!-- JS -->
        <script type="text/javascript" src="js/userProfile.js"></script>
        <script type="text/javascript" src="js/postReply.js"></script>
        <script type="text/javascript" src="js/index.js"></script>
        <script type="text/javascript" src="js/login-register.js"></script>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        
        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
        
    </body>
</html>
