<%-- 
    Document   : userProfile
    Created on : May 21, 2016, 12:56:07 PM
    Author     : mario
--%>

<%@page import="database.Notification"%>
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
        
                
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        
    </head>
    <body>
        <jsp:useBean id="user" scope="session" class="database.User"/>
        
        <h1>------------------------------------------Barra--------------------------------------</h1>
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
                          <br><%= user.getReviews_counter() %> </p>
                      
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
          <h2>Private Resources</h2>
          <ul class="nav nav-pills">
                <li class="active"><a data-toggle="tab" href="#home">Notifications<span class="badge"><%= session.getAttribute("numberNotification")%></span></a></li>
                <li><a data-toggle="tab" href="#menu1">My Reviews<span class="badge"><%= session.getAttribute("numberReview")%></span></a></li>
                <li><a data-toggle="tab" href="#menu2">Profile Info</a></li>
          </ul>

          <div class="tab-content">
            <div id="home" class="tab-pane fade in active">
             
              
              <h3>Notifications</h3>
              
              <div class="list-group">
                  
           
              
              
                
                <a href="#" class="list-group-item">
                  <h4 class="list-group-item-heading">List group item heading</h4>
                  <p class="list-group-item-text">Donec id elit non mi porta gravida at eget metus. Maecenas sed diam eget risus varius blandit.</p>
                </a>
                <a href="#" class="list-group-item">
                  <h4 class="list-group-item-heading">List group item heading</h4>
                  <p class="list-group-item-text">Donec id elit non mi porta gravida at eget metus. Maecenas sed diam eget risus varius blandit.</p>
                </a>
              </div>
              
              
            </div>
            <div id="menu1" class="tab-pane fade">
              <h3>Reviews</h3>
              <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
            </div>
            <div id="menu2" class="tab-pane fade">
              <h3>Profile Informations</h3>
              <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.</p>
            </div>
          </div>
        </div>
            
        </div>

    </body>
</html>
