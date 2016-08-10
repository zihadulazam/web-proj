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
                    <li class="active"><a data-toggle="tab" href="#menu1">Ristoranti in Attesa<span class="badge"><c:out value="10"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu2">Risposte da Confermare<span class="badge"><c:out value="10"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu3">Segnalati<span class="badge"> <c:out value="10"/> </span></a></li>
                    <li><a data-toggle="tab" href="#menu4">Informazioni Profilo</a></li>
                </ul>

                <div class="tab-content">     

                    <div id="menu1" class="tab-pane fade in active">

                        <!--RistorantiAttesa-->
                        <br>
                       
                            <c:forEach items="${ristorantiAttesa}" var="rA">
                                
                                
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        <a href="#">
                                            Nuovo Ristorante In attesa di essere confermato!
                                        </a>
                                    </div>
                                    <div class="row">
                                        <div class ="col-md-7">
                                        </div>
                                        <div class ="col-md-2">
                                            <button  class="btn btn-primary " value="${rA.getId()}">Accept</button>
                                        </div>
                                        <div class="col-md-1">

                                        </div>
                                        <div class ="col-md-2">
                                            <button  class="btn btn-primary resolveNotify" value="${rA.getId()}">Decline</button>
                                        </div>
                                    </div>
                                </div>
                                
                            </c:forEach>      
                        

                    </div>



                    <div id="menu2" class="tab-pane fade">
                        <div class="list-group">
                            <br>
                            Menu2
                        </div>
                    </div>
                    
                    <div id="menu3" class="tab-pane fade">
                        <div class="list-group">
                            <br>
                            Menu3
                        </div>
                    </div>
                    
                    <div id="menu4" class="tab-pane fade">
                        <div class="list-group">
                            <br>
                            Menu4
                        </div>
                    </div>

                </div>        
            </div>
        </div>
       
                
        <!--footer-->
        <%@include file="components/footer.html"%>
    </body>
</html>
