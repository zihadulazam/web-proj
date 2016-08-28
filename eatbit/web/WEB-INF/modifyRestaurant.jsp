
<%@page import="database.contexts.RestaurantContext"%>
<%@page import="database.PriceRange"%>

<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
        <title>eatBit | Modifica ristorante</title>
        <meta charset="ISO-8859-1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        <link type="text/css" href="css/bootstrap-timepicker.min.css" />
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
         <link href="css/modifyRestaurant.css" rel="stylesheet">
        
        <!-- google font link -->
        <!-- header font -->
         <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        <!-- footer font -->
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">

        <!-- creat Restaurant liks -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link rel="stylesheet" href="assets/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="css/form-elements.css">
        <link rel="stylesheet" href="css/createRestaurant.css">

        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        
    </head>
    <body>
        <c:set var="req" value="${pageContext.request}" />
        <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        <!-- page container -->
        <!-- Top content -->
        <div class="top-content">
            <div class="container">
                
                <div class="row">
                    <div class="col-sm-8 col-sm-offset-2 text">
                        <h1><strong>Modifica il tuo ristorante</strong> <c:out value="${restaurant.getRestaurant().getName()}" /></h1>
                        <h3>Proprietario: <strong><c:out value="${restaurant.getOwner().getName()}" /></strong></h3>
                        <div class="description">
                       	    <p>
                                Dopo <strong>l'approvazione</strong> i tuoi dati verranno aggiornati!
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3 form-box">
                    	<form id="formModifica" action="${baseURL}/ModifyRestaurantServlet" method="post" class="f1">
                                
                    		<p>Compila il modulo con i dati <strong>reali</strong> del ristorante!</p>
                    		
                    		<fieldset>
                    		    <h4>Informazioni sul vostro ristorante:</h4>
                    			<div class="form-group">
                    			    <h5>Nome:</h5>
                                    <input type="text" name="name" value="${restaurant.getRestaurant().getName()}" class="form-control" id="name">
                                </div>
           
                                <div class="form-group">
                                    <h5>Descrizione</h5>
                                    <input type="text" name="description" placeholder="Descrizione" value="${restaurant.getRestaurant().getDescription()}" class="form-control" id="web_site">
                                </div>
                                <div class="form-group">
                                    <h5>Sito Web:</h5>
                                    <div class="input-group">
                                        <input type="text" name="web_site" placeholder="Sito Web..." value="${restaurant.getRestaurant().getWeb_site_url()}" class="form-control" id="web_site">
                                        <span class="input-group-addon"><span class="fa fa-globe" aria-hidden="true"></span></span>
                                    </div>
                                    
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="address">Indirizzo</label>
                                    <div class="input-group">
                                        <input type="text" name="indirizzo" placeholder="Indririzzo..." class="form-control" id="address">
                                        <span class="input-group-addon"><span class="fa fa-map-marker" aria-hidden="true"></span></span>
                                    </div>
                                    <label id="lblresult"></label>
                                </div>
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-next">Successivo</button>
                                </div>
                            </fieldset>

                            <fieldset>
                                <h4>Tipi di cucina e Prezzo:</h4>
                                <div class="form-group">
                                    <h5>Cucina:</h5>
                                    <div class="row">
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="checkbox" name="cuisine" value="Asiatica"/> Asiatica
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Cinese"/> Cinese
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Giapponese"/> Japonese
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Indiana"/> Indiana
                                         </div>
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="checkbox" name="cuisine" value="Italiana"/> Italiana
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Pizza"/> Pizza
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Francese"/> Francese
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Spagnola"/> Spagnola
                                         </div>
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="checkbox" name="cuisine" value="Carne"/> Carne
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Fast-Food"/> Fast-Food
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Vegetariana"/> Vegetariana
                                            <br />
                                            <input type="checkbox" name="cuisine" value="Pesce"/> Pesce
                                         </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <h5>Fascia Prezzo:</h5>
                                    <div class="input-group">
                                        <select class="form-control" name="min" class="form-control" id="prezzo_min">
                                            <option>Prezzo Min</option>
                                            <c:forEach var="i" begin="1" end="1000">
                                                <option><c:out value="${i}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                         <select class="form-control" name="max" class="form-control" id="prezzo_max">
                                            <option>Prezzo Max</option>
                                            <c:forEach var="i" begin="1" end="1000">
                                                <option><c:out value="${i}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-previous">Precedente</button>
                                    <button type="button" class="btn btn-next">Successivo</button>
                                </div>
                            </fieldset>
                            
                                              
                                        
                            <fieldset>
                                <h4>Orario d'apertura (Quasi Finito):</h4>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3 text-center">
                                        <h5>Giorno</h5>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center">
                                        <h5>Dalle</h5>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-center"></div>
                                    <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center">
                                        <h5>Alle</h5>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Lunedì:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="LunMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="LunMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="LunPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="LunPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Martedì:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MarMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MarMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MarPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MarPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Mercoledì:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MerMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                            
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MerMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MerPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="MerPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Giovedì:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="GioMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="GioMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="GioPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="GioPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Venerdì:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="VenMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="VenMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="VenPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="VenPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Sabato:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="SabMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="SabMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="SabPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="SabPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                                        <h5>Domenica:</h5>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="DomMatH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="DomMatM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="DomPomH">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select name="hour" class="form-control" id="DomPomM">
                                                <option>--</option>
                                                <option>00</option>
                                                <option>01</option>
                                                <option>02</option>
                                                <option>03</option>
                                                <option>04</option>
                                                <option>05</option>
                                                <option>06</option>
                                                <option>07</option>
                                                <option>08</option>
                                                <option>09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <br/>
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-previous">Precedente</button>
                                    <button type ="submit" id="invia" class="btn btn-submit">Fine</button>
                                    
                                    <input name="address" type="hidden" id="location" />
                                    <input name="city" type="hidden" id="city" />
                                    <input name="province" type="hidden" id="province" />
                                    <input name="state" type="hidden" id="state" />
                                    <input name="longitude" type="hidden" id="longitude" />
                                    <input name="latitude" type="hidden" id="latitude" />
                                    <input name="id_restaurant" type="hidden" value="${restaurant.getRestaurant().getId()}" />
                                    <input name="orarioL" type="hidden" id="orarioL" />
                                    <input name="orarioM" type="hidden" id="orarioM" />
                                    <input name="orarioMe" type="hidden" id="orarioMe" />
                                    <input name="orarioG" type="hidden" id="orarioG" />
                                    <input name="orarioV" type="hidden" id="orarioV" />
                                    <input name="orarioS" type="hidden" id="orarioS" />
                                    <input name="orarioD" type="hidden" id="orarioD" />
                                </div>
                            </fieldset>
                    	
                    	</form>
                    </div>
                </div>
                    
            </div>
        </div>


         <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>

        <!-- Javascript -->
        <script src="assets/js/jquery.backstretch.min.js"></script>
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
  
        <script src="assets/js/retina-1.1.0.min.js"></script>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5PvbD12gNax9Avkf-qes0_Y-_oB90b-o&libraries=places"></script>
        <script src="js/createRestaurant.js"></script>
</body>
</html>

