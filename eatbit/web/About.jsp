<%-- 
    Document   : About
    Created on : 16-ago-2016, 17.28.20
    Author     : Andrea
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*,java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html lang="it">
<head>
        <title>eatBit | About Page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- jquery ui -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- Datatable Css-->
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css" rel='stylesheet' type='text/css'>
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/restaurant.css" rel="stylesheet">

        <!-- slider pro css -->
        <link href="css/slider-pro.css" rel="stylesheet">

        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">

        <!-- google font link -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        <link href='https://fonts.googleapis.com/css?family=Raleway:500' rel='stylesheet' type='text/css'>
        
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        <h1>
            About EatBit <br> EatBit è il portale di ristoranti più piccolo al mondo, probabilmente. 
            Permette agli utenti e a chi ha fame di cercare fra una manciata di ristoranti che potrebbero non esistere.
            Il marchio eatBit rappresenta la più piccola community di mangiatori al mondo, con meno di 20 utenti registrati, e il suo
            sito è attivo solo in italia.
        </h1>
         <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
        <!-- slider JS -->
        <script type="text/javascript" src="js/jquery.sliderPro.min.js"></script>
        <script type="text/javascript" src="js/slider-config-js.js"></script>
        
        <!-- Datatable js-->
        <script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
        
        <!-- Google Map JS CDN-->
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5PvbD12gNax9Avkf-qes0_Y-_oB90b-o&callback=initMap"async defer></script>
       
        <!-- Restaurant JS-->
        <script type="text/javascript" src="js/restaurant.js"></script>

        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
    </body>
</html>
