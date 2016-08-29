
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
        <title>eatBit | Crea nuovo ristorante</title>
        
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        
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
        <%@include file="components/navbar-index.jsp"%>

        <!-- page container -->
        <!-- Top content -->
        <div class="top-content">
            <div class="container">
                
                <div class="row">
                    <div class="col-sm-8 col-sm-offset-2 text">
                        <h1><strong>Aggiungi</strong> Un Ristorante</h1>
                        <div class="description">
                       	    <p>
                                Dopo <strong>l'approvazione</strong> il tuo ristorante verra' aggiunto nel nostro sito!
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 col-lg-8 col-lg-offset-2 form-box">
                    	<form role="form" action="${baseURL}/AddRestaurantServlet" method="post" class="f1" enctype="multipart/form-data">

                    		<h3>Crea Ristorante</h3>
                    		<p>Compila il modulo con i dati <strong>reali</strong> del ristorante!</p>
                    		<div class="f1-steps">
                    			<div class="f1-progress">
                    			    <div class="f1-progress-line" data-now-value="16.66" data-number-of-steps="3" style="width: 16.66%;"></div>
                    			</div>
                    			<div class="f1-step active">
                    				<div class="f1-step-icon"><i class="fa fa-university"></i></div>
                    				<p>Dati</p>
                    			</div>
                    			<div class="f1-step">
                    				<div class="f1-step-icon"><i class="fa fa-cutlery"></i></div>
                    				<p>Tipi di Cucina, Prezzo e Photo</p>
                    			</div>
                    		    <div class="f1-step">
                    				<div class="f1-step-icon"><i class="fa fa-clock-o"></i></div>
                    				<p>Orario d'appertura</p>
                    			</div>
                    		</div>
                    		
                    		<fieldset>
                    		    <h4>Informazioni sul vostro ristorante:</h4>
                    			<div class="form-group">
                    			    <label class="sr-only" for="name">Nome</label>
                                    <input type="text" name="name" placeholder="Nome..." class="form-control" id="name">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="description">Descrizione</label>
                                    <textarea name="description" placeholder="Descrizione..." 
                                    	                 class="form-control" id="description"></textarea>
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="web_site">Sito Web</label>
                                    <div class="input-group">
                                        <input type="text" name="url" placeholder="Sito Web..." class="form-control" id="web_site">
                                        <span class="input-group-addon"><span class="fa fa-globe" aria-hidden="true"></span></span>
                                    </div>
                                    
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="address">Indirizzo</label>
                                    <div class="input-group">
                                        <input type="text"  placeholder="Indririzzo..." class="form-control" id="address">
                                        <span class="input-group-addon"><span class="fa fa-map-marker" aria-hidden="true"></span></span>
                                    </div>
                                    <label id="lblresult"></label>
                                </div>
                                <input type="hidden" id="location" name="address" value=" ">
                                <input type="hidden" id="city" name="city" value=" ">
                                <input type="hidden" id="province" name="province" value=" ">
                                <input type="hidden" id="state" name="state" value=" ">
                                <input name="longitude" type="hidden" id="longitude" value=" ">
                                <input name="latitude" type="hidden" id="latitude" value=" ">
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-next">Successivo</button>
                                </div>
                            </fieldset>

                            <fieldset>
                                <h4>Tipi di cucina, Prezzo e Photo:</h4>
                                <div class="form-group">
                                    <h5>Cucina: <span class="text-primary" id="num-cucine">(Massimo 3)</span></h5>
                                    <div class="row">
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <c:forEach var="cucine" items="${cuisines}" begin="0" end="6">
                                                <input type="checkbox" class="my-cuisine" name="cuisine" value="<c:out value="${cucine}"/>"/> <c:out value="${cucine}"/>
                                                <br />
                                            </c:forEach>
                                         </div>
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <c:forEach var="cucine" items="${cuisines}" begin="7" end="13">
                                                <input type="checkbox" class="my-cuisine" name="cuisine" value="<c:out value="${cucine}"/>"/> <c:out value="${cucine}"/>
                                                <br />
                                            </c:forEach>
                                         </div>
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                             <c:forEach var="cucine" items="${cuisines}" begin="14" end="20">
                                                <input type="checkbox" class="my-cuisine" name="cuisine" value="<c:out value="${cucine}"/>"/> <c:out value="${cucine}"/>
                                                <br />
                                            </c:forEach>
                                         </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <h5>Fascia Prezzo:</h5>
                                    <div class="input-group">
                                        <select class="form-control" name="min" id="prezzo_min">
                                            <option>Prezzo Min</option>
                                            <c:forEach var="i" begin="1" end="1000">
                                                <option><c:out value="${i}"/></option>
                                            </c:forEach>
                                        </select>
                                        <span class="input-group-addon">Euro</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                         <select class="form-control" name="max" id="prezzo_max">
                                            <option>Prezzo Max</option>
                                            <c:forEach var="i" begin="1" end="1000">
                                                <option><c:out value="${i}"/></option>
                                            </c:forEach>
                                        </select>
                                        <span class="input-group-addon">Euro</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <h5>Photo:</h5>
                                    <div class="input-group">
                                        <label class="input-group-btn">
                                            <span class="btn btn-default" id="btn-camera">
                                                Cerca Photo&hellip; <input name="photo" id="Add-photo" type="file" style="display: none;" accept="image/*" multiple>
                                            </span>
                                        </label>
                                        <input type="text" id="Add-photo-name" class="form-control" readonly>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <h5>Descrizione Photo:</h5>
                                    <label class="sr-only" for="photo_description">Descrizione Photo:</label>
                                    <textarea name="photo_description" placeholder="Descrizione..." 
                                    	                 class="form-control" id="photo_description"></textarea>
                                </div>
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-previous">Precedente</button>
                                    <button type="button" class="btn btn-next" id="btn-next-sec" disabled="disabled">Successivo</button>
                                </div>
                            </fieldset>

                            <fieldset>
                                <h4>Orario d'apertura:</h4>
                                <div class="row">
                                    <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3 text-center">
                                        <h5>Giorno</h5>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center">
                                        <h5>Dalle</h5>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1 text-center"></div>
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
                                            <select class="form-control" id="LunMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="LunMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="LunPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="LunPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
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
                                            <select class="form-control" id="MarMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="MarMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="MarPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="MarPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
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
                                            <select class="form-control" id="MerMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="MerMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="MerPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="MerPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
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
                                            <select class="form-control" id="GioMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="GioMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="GioPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="GioPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
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
                                            <select class="form-control" id="VenMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="VenMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="VenPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="VenPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
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
                                            <select class="form-control" id="SabMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="SabMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="SabPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="SabPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
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
                                            <select class="form-control" id="DomMatH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="DomMatM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-0 col-sm-0 col-md-1 col-lg-1"></div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="DomPomH">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="23">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                                        <div class="input-group">
                                            <select class="form-control" id="DomPomM">
                                                <option value="--">--</option>
                                                <option value="00">00</option>
                                                <option value="01">01</option>
                                                <option value="02">02</option>
                                                <option value="03">03</option>
                                                <option value="04">04</option>
                                                <option value="05">05</option>
                                                <option value="06">06</option>
                                                <option value="07">07</option>
                                                <option value="08">08</option>
                                                <option value="09">09</option>
                                                <c:forEach var="i" begin="10" end="59">
                                                    <option><c:out value="${i}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <br/>
                                <div class="input-group">
                                    <input type="checkbox" name="claim" id="claim-checkbox"> Sono io il proprietario
                                </div>
                                <div class="form-group">
                    			    <label class="sr-only" for="name">Testo Reclamo</label>
                                    <input type="text" name="text_claim" placeholder="Testo Reclamo..." class="form-control" id="testo_claim">
                                </div>
                                <br/>
                                <br/>
                                <input type="hidden" id="orarioL" name="hour">
                                <input type="hidden" id="orarioM" name="hour">
                                <input type="hidden" id="orarioMe" name="hour">
                                <input type="hidden" id="orarioG" name="hour">
                                <input type="hidden" id="orarioV" name="hour">
                                <input type="hidden" id="orarioS" name="hour">
                                <input type="hidden" id="orarioD" name="hour">

                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-previous">Precedente</button>
                                    <button type="submit" class="btn btn-submit" id="btn-fine" disabled="disabled">Fine</button>
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
        <script src="assets/js/retina-1.1.0.min.js"></script>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5PvbD12gNax9Avkf-qes0_Y-_oB90b-o&libraries=places"></script>
        <script src="js/createRestaurant.js"></script>
</body>
</html>
