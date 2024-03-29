<%@ page errorPage="error.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=ISO-8859-1" %> 
<%@ page import="java.io.*,java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
   Date dNow = new Date( );
   SimpleDateFormat fdate = new SimpleDateFormat ("E dd.MM.yyyy");
   SimpleDateFormat fd = new SimpleDateFormat ("u");
   SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
   pageContext.setAttribute("Today", fdate.format(dNow));
   pageContext.setAttribute("NowDay", fd.format(dNow));
   pageContext.setAttribute("NowTime", ft.format(dNow));
%>
<html lang="it">
    <head>
        <title>eatBit | <c:out value="${restaurant_context.getRestaurant().getName()}"/></title>

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

        <!-- Pnotify css -->
        <link href="css/pnotify.custom.min.css" rel="stylesheet">

        <!-- slider pro css -->
        <link href="css/slider-pro.css" rel="stylesheet">

        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">

        <!-- google font link -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        <link href='https://fonts.googleapis.com/css?family=Raleway:500' rel='stylesheet' type='text/css'>
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        <script type="text/javascript">
            var myLat=<c:out value="${restaurant_context.getCoordinate().getLatitude()}"/>;
            var myLon=<c:out value="${restaurant_context.getCoordinate().getLongitude()}"/>;
            var myRistoName="<c:out value="${restaurant_context.getRestaurant().getName()}"/>";
            var viciniLat=new Array();
            var viciniLon=new Array();
            var viciniName=new Array();
            var i=0;
            <c:forEach var="my_vicini" items="${vicini}">
                viciniLat[i]=<c:out value="${my_vicini.getLatitude()}"/>;
                viciniLon[i]=<c:out value="${my_vicini.getLongitude()}"/>;
                viciniName[i]="<c:out value="${my_vicini.getName()}"/>";
                i++;
            </c:forEach>
        </script>
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        
        <c:set var="req" value="${pageContext.request}" />
        <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
        
        <!-- Main Content -->
        <div class="container" id="header">
            <div class="row">
                <div class="col-xs-12 col-md-4" id="restaurant-name">
                    <h1><img src="img/restaurant/name.png"/> <c:out value="${restaurant_context.getRestaurant().getName()}"/></h1> 
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="informazioni">
                        <div class="row rating-stars">
                                <c:forEach var="i" begin="1" end="5">
                                    <c:choose>
                                        <c:when test="${restaurant_context.getRestaurant().getGlobal_value()>=i}">
                                            <img src="img/star-full.png"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="img/star-empty.png"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                        </div>
                        <p>(<c:out value="${restaurant_context.getRestaurant().getReviews_counter()}"/> recensioni)</p>
                        <p><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> <c:out value="${restaurant_context.getCoordinate().getAddress()}"/>, <c:out value="${restaurant_context.getCoordinate().getCity()}"/>, <c:out value="${restaurant_context.getCoordinate().getState()}"/></p>
                        <!--<p><span class="glyphicon glyphicon-phone" aria-hidden="true"></span> <c:out value="${restaurant_context.getRestaurant().getWeb_site_url()}"/></p>-->
                        <p><span class="glyphicon glyphicon-globe" aria-hidden="true"></span><a href="<c:out value="${restaurant_context.getRestaurant().getWeb_site_url()}"/>" target="_blank"> Sito Web</a></p>
                        <p id="tipi-di-cucina">
                            <c:forEach var="tipocucine" items="${restaurant_context.getCuisines()}">
                                <span class="label label-danger"><c:out value="${tipocucine}"/></span>
                            </c:forEach>
                        </p>
                    </div> 
                </div>
                <div class="col-xs-12 col-md-4">
                    <div id="informazioni-orario">
                        <p id="classifica"><span class="glyphicon glyphicon-sort" aria-hidden="true"></span> Classifica (per citt�): <c:out value="${restaurant_context.getCityPosition()}"/></p>
                        <p id="bold"><span class="glyphicon glyphicon-time" aria-hidden="true"></span> Oggi: </p>
                        <c:forEach var="ore" items="${restaurant_context.getHoursRanges()}">
                            <c:if test="${NowDay==ore.getDay()}">
                                  <p><c:out value="${ore.getFormattedStart_hour()}"/> - <c:out value="${ore.getFormattedEnd_hour()}"/></p>
                                  <c:choose>
                                      <c:when test="${ore.getStart_hour()<=NowTime && ore.getEnd_hour()>=NowTime}">
                                          <p id="info-apertura"><span class="label label-success">Ora Aperto</span></p>
                                      </c:when>
                                      <c:otherwise>
                                          <p id="info-apertura"><span class="label label-danger">Chiuso</span></p>
                                      </c:otherwise>
                                  </c:choose>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div> <!--End Container-->
        
          
        <!-------------Slider Start--------------->
        <div id="example3" class="slider-pro">
            <div class="sp-slides">
                <c:forEach var="photos" items="${restaurant_context.getPhotos()}">
                    <div class="sp-slide">
                        <img class="sp-image" src="css/images/blank.gif" 
                            data-src="<c:out value="${photos.getPath()}"/>" />

                        <p class="sp-layer sp-white sp-padding"
                            data-horizontal="50" data-vertical="50"
                            data-show-transition="left" data-show-delay="400">
                            <button  class="btn btn-large btn-block btn-danger btn-segnala" title="Segnala" onclick="segnalaPhoto(<c:out value="${photos.getId()}"/>)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                        </p>
                        <p class="sp-layer sp-black sp-padding"
                                data-horizontal="50" data-vertical="350"
                                data-show-transition="left" data-show-delay="600">
                                <c:out value="${photos.getName()}"/>: <c:out value="${photos.getDescription()}"/>
                        </p>
                    </div>
                </c:forEach>
            </div>

            <div class="sp-thumbnails">
                <c:forEach var="photos" items="${restaurant_context.getPhotos()}">
                    <img class="sp-thumbnail" src="<c:out value="${photos.getPath()}"/>"/>
                </c:forEach>
            </div>
        </div>
        <!-- #endregion Jssor Slider End -->
        <!-------------End Slider----------------->
        
        
        <!-------------Buttons Add Recensione, Upload Foto with collapse -------------------->
        <div class="container" id="button-container">
            <div class="row">
                <div class="col-md-12">
                    <p id="button-rec-foto">
                        <button type="button" class="btn btn-success btn-lg btn-config" 
                            <c:choose>
                                <c:when test="${sessionScope.user.getNickname()==null}">
                                    onclick="primaFaiLogin()"
                                 </c:when>
                                 <c:otherwise>
                                    data-toggle="collapse" data-target="#collapseAddRistoRate" aria-expanded="false" aria-controls="collapseAddRecensione"
                                </c:otherwise>
                            </c:choose>
                            ><span class="glyphicon glyphicon-star" aria-hidden="true"></span> Vota questo ristorante</button>
                        <button type="button" class="btn btn-success btn-lg btn-config" 
                            <c:choose>
                                <c:when test="${sessionScope.user.getNickname()==null}">
                                    onclick="primaFaiLogin()"
                                 </c:when>
                                 <c:otherwise>
                                    data-toggle="collapse" data-target="#collapseAddRecensione" aria-expanded="false" aria-controls="collapseAddRecensione"
                                </c:otherwise>
                            </c:choose>
                            ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Scrivi una recensione</button>
                        <button type="button" class="btn btn-success btn-lg btn-config" 
                                <c:choose>
                                    <c:when test="${sessionScope.user.getNickname()==null}">
                                        onclick="primaFaiLogin()"
                                    </c:when>
                                    <c:otherwise>
                                        data-toggle="collapse" data-target="#collapseAddFoto" aria-expanded="false" aria-controls="collapseAddFoto"
                                        data-toggle="collapse" data-target="#collapseAddRecensione" aria-expanded="false" aria-controls="collapseAddRecensione"
                                    </c:otherwise>
                                </c:choose>
                            ><span class="glyphicon glyphicon-camera" aria-hidden="true"></span> Aggiungi una foto</button>
                    </p>
                </div>
            </div>
            <div class="collapse" id="collapseAddRistoRate">
                <div class="well">
                    <div class="from-group">
                        <div class="row">
                            <div class="col-sm-12 col-md-3">
                                <label class="rating-lb"><span class="glyphicon glyphicon-star" aria-hidden="true"></span> Valutazione Globale: </label>
                            </div>
                            <div class="col-sm-12 col-md-9">
                                <div class="rating-bar" id="valutazioneGlobaleRistoBar" type="text"></div>
                                <input type="text" id="valutazioneGlobaleRistoValue" class="rating-value">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <p id="btn-pubblica"><button class="btn btn-primary" type="submit" onclick="addRistoVote(<c:out value="${restaurant_context.getRestaurant().getId()}"/>)"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span> Pubblica</button></p>
                    </div>
                </div>
            </div>
            <div class="collapse" id="collapseAddRecensione">
                <div class="well">
                    <form method="POST" id="add_review_form" action="${baseURL}/AddReviewServlet" enctype="multipart/form-data">
                        <input type="hidden" name="id_rest" id="id_rest" value="<c:out value="${restaurant_context.getRestaurant().getId()}"/>" >
                        <div class="from-group">
                            <div class="row">
                                <div class="col-sm-12 col-md-3">
                                    <label class="rating-lb"><span class="glyphicon glyphicon-star" aria-hidden="true"></span> Valutazione Globale: </label>
                                </div>
                                <div class="col-sm-12 col-md-9">
                                    <div class="rating-bar" id="valutazioneGlobaleBar" type="text"></div>
                                    <input type="text" name="global_value" id="valutazioneGlobaleValue" class="rating-value">
                                </div>
                            </div>
                        </div>
                        <div class="from-group">
                            <div class="row">
                                <div class="col-md-3">
                                    <label class="rating-lb"><span class="glyphicon glyphicon-apple" aria-hidden="true"></span> Cibo: </label>
                                </div>
                                <div class="col-md-9">
                                     <div class="rating-bar" id="ciboBar" type="text"></div>
                                    <input type="text" name="food" id="ciboValue" class="rating-value">
                                </div>
                            </div>
                        </div>
                        <div class="from-group">
                            <div class="row">
                                <div class="col-md-3">
                                    <label class="rating-lb"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Servizio: </label>
                                </div>
                                <div class="col-md-9">
                                     <div class="rating-bar" id="servizioBar" type="text"></div>
                                    <input type="text" name="service" id="servizioValue" class="rating-value">
                                </div>
                            </div>
                        </div>
                        <div class="from-group">
                            <div class="row">
                                <div class="col-md-3">
                                    <label class="rating-lb"><span class="glyphicon glyphicon-lamp" aria-hidden="true"></span> Atmosfera: </label>
                                </div>
                                <div class="col-md-9">
                                     <div class="rating-bar" id="atmosferaBar" type="text"></div>
                                    <input type="text" name="atmosphere" id="atmosferaValue" class="rating-value">
                                </div>
                            </div>
                        </div>
                        <div class="from-group">
                            <div class="row">
                                <div class="col-md-3">
                                    <label class="rating-lb"><span class="glyphicon glyphicon-usd" aria-hidden="true"></span> Prezzo: </label>
                                </div>
                                <div class="col-md-9">
                                     <div class="rating-bar" id="prezzoBar" type="text"></div>
                                    <input type="text" name="value_for_money" id="prezzoValue" class="rating-value">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <br/>
                            <p>
                                <label class="rating-lb" for="recensione-title">Titolo:</label>
                                <input id="recensione-title" name="name" type="text" maxlength="25">
                            </p>
                        </div>
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="comment">Recensione:</label>
                            <textarea class="form-control" rows="7" name="description" id="comment" maxlength="32000"></textarea>
                        </div>
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="AddRecensione-cerca-photo">Photo:</label>
                            <div class="input-group">
                                <label class="input-group-btn">
                                    <span class="btn btn-default">
                                        Cerca Photo&hellip; <input name="photo" name="foto" id="AddRecensione-cerca-photo" type="file" style="display: none;" accept="image/*" multiple>
                                    </span>
                                </label>
                                <input type="text" id="AddRecensione-cerca-photo-name" name="AddRecensione_cerca_photo_name" class="form-control" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="photo_description" maxlength="32000">Descrizione Photo:</label>
                            <textarea class="form-control" rows="3" name="photo_description" id="photo_description"></textarea>
                        </div>
                        <p id="btn-pubblica"><button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span> Pubblica</button></p>
                    </form>
                </div>
            </div>
            <div class="collapse" id="collapseAddFoto">
                <div class="well">
                    <form method="POST" action="${baseURL}/AddPhotoToRestaurantServlet" enctype="multipart/form-data" id="add_foto_form">
                        <div class="row">
                            <div class="col-md-12" id="AddFoto-FileInput">
                                <input type="hidden" name="id_rest" value="<c:out value="${restaurant_context.getRestaurant().getId()}"/>" >
                                <div class="input-group">
                                    <label class="input-group-btn">
                                        <span class="btn btn-default">
                                            Cerca Photo&hellip; <input name="photo" id="AddFoto-cerca-photo" type="file" style="display: none;" accept="image/*" multiple>
                                        </span>
                                    </label>
                                    <input type="text" id="AddFoto-cerca-photo-name" name="AddFoto_cerca_photo_name" class="form-control" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="photo_description" maxlength="32000">Descrizione Photo:</label>
                            <textarea class="form-control" rows="3" name="photo_description" id="photo_description"></textarea>
                        </div>
                        <div class="text-right" id="btn-upload">
                            <button type="submit" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Carica</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!------------------- Recensioni -------------------->
        <div class="container contenitori-blocco" id="recensioni-container">
            <h1><img src="img/restaurant/comments.png"/> Recensioni</h1>
            <hr/>
            <table id="tabella-recensioni" class="display" cellspacing="0" width="100%">
                <thead>
                    <th></th>
                </thead>
                <tfoot></tfoot>
                <tbody>
                    <c:forEach var="allComments" items="${restaurant_context.getReviewsContextsByNewest()}">
                        <tr>
                            <td>
                                <div class="comment">
                                    <div class="container-fluid">
                                        <div class="row container-fluid">
                                            <div class="col-md-2 comment-writer">
                                                <img  data-toggle="popover" data-placement="bottom"
                                                    data-content=" 
                                                    <p>Totale Recensioni: <c:out value="${allComments.getUser().getReviews_counter()}" /></p>
                                                    <p class='text-success'><span class='glyphicon glyphicon-thumbs-up' aria-hidden='true'></span> Totale Mi Piace: <c:out value="${allComments.getUser().getReviews_positive()}" /></p>
                                                    <p class='text-danger'><span class='glyphicon glyphicon-thumbs-down' aria-hidden='true'></span> Totale Non Mi Piace: <c:out value="${allComments.getUser().getReviews_negative()}" /></p>"
                                                src="<c:out value="${allComments.getUser().getAvatar_path()}" />" class="img-circle"/>
                                                <h5><c:out value="${allComments.getUser().getNickname()}" /></h5>
                                                <p class="comment-data">
                                                    <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                    <c:out value="${allComments.getReview().getDate_creation().getDate()}" />-<c:out value="${allComments.getReview().getDate_creation().getMonth()}" />-<c:out value="${allComments.getReview().getDate_creation().getYear()+1900}" /> <c:out value="${allComments.getReview().getDate_creation().getHours()}" />:<c:out value="${allComments.getReview().getDate_creation().getMinutes()}" />
                                                </p>
                                                <c:if test="${allComments.getPhoto()!=null}">
                                                    <a class="thumbnail" href="<c:out value="${allComments.getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${allComments.getPhoto().getPath()}" />">
                                                        <img src="<c:out value="${allComments.getPhoto().getPath()}" />">
                                                    </a>
                                                    <!--
                                                    <div class="text-center">
                                                        <a class="btn-segnala-photo-recensione popov" href="" id="<c:out value="${lastComment.getPhoto().getId()}"/>"  onclick="segnalaPhoto(this.id); return false;">Segnala Photo!</a>
                                                    </div>-->
                                                </c:if>
                                            </div>
                                            <div class="col-md-10 comment-content">
                                                <p>
                                                    <button type="button" class="btn btn-danger btn-segnala-review" id="<c:out value="${allComments.getReview().getId()}" />" title="Segnala Recensione" onclick="segnalaReview(this.id)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                                    <h3 class="comment-title"><c:out value="${allComments.getReview().getName()}" /></h3>
                                                </p>
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
                                                                            <p class="risposta-date"><c:out value="${allComments.getReply().getDate_creation().getDate()}" />-<c:out value="${allComments.getReply().getDate_creation().getMonth()}" />-<c:out value="${allComments.getReply().getDate_creation().getYear()+1900}" /> <c:out value="${allComments.getReply().getDate_creation().getHours()}" />:<c:out value="${allComments.getReply().getDate_creation().getMinutes()}" /></p>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                        <div class="row container-fluid">
                                                <div class="col-md-12 text-right">
                                                        <button type="button" class="btn btn-danger btn-mi-piace" 
                                                                <c:if test="${sessionScope.user.getNickname()==null}">
                                                                    disabled="disabled"
                                                                </c:if>
                                                                    onclick="miPiace(<c:out value="${allComments.getReview().getId()}"></c:out>,1)"
                                                                    id="btn-mipiace-<c:out value="${allComments.getReview().getId()}"></c:out>"
                                                                    ><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge"><c:out value="${allComments.getReview().getLikes()}" /></span></button>
                                                    <button type="button" class="btn btn-danger btn-non-mi-piace" 
                                                            <c:if test="${sessionScope.user.getNickname()==null}">
                                                                disabled="disabled"
                                                            </c:if>
                                                            onclick="nonMiPiace(<c:out value="${allComments.getReview().getId()}"></c:out>,0)"
                                                            id="btn-nonmipiace-<c:out value="${allComments.getReview().getId()}"></c:out>"
                                                            ><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge"><c:out value="${allComments.getReview().getDislikes()}" /></span></span></button>
                                                </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <!-------------------END Recensioni ----------------->

        <!------------------- Mappa Vicini -------------------->
        <div class="container contenitori-blocco" id="map-container">
            <h1><img src="img/restaurant/map.png"/> Vicino a <c:out value="${restaurant_context.getRestaurant().getName()}"/></h1>
            <hr/>
            <div id="map"></div>
        </div>
        <!-------------------END Mappa Vicini ----------------->

        <!------------------- Dettagli -------------------->
        <div class="container contenitori-blocco" id="dettagli-container">
            <h1><img src="img/restaurant/info.png"/> Dettagli</h1>
            <hr/>
            <div class="row" id="punteggi-riassunto">
                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                    <h4 class="dettagli-lable">Riassunto Punteggio:</h4>
                </div>
                <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                    <p>Cibo</p>
                    <p>Servizio</p>
                    <p>Atmosfera</p>
                    <p>Prezzo</p>
                </div>
                <div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
                    <div class="row rating-stars" id="cibo-stars">
                        <c:forEach var="i" begin="1" end="5">
                            <c:choose>
                                <c:when test="${voto_per_cibo>=i}">
                                    <img src="img/star-full.png"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="img/star-empty.png"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <div class="row rating-stars" id="servizio-stars">
                        <c:forEach var="i" begin="1" end="5">
                            <c:choose>
                                <c:when test="${voto_per_servizio>=i}">
                                    <img src="img/star-full.png"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="img/star-empty.png"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <div class="row rating-stars" id="atmosfera-stars">
                        <c:forEach var="i" begin="1" end="5">
                            <c:choose>
                                <c:when test="${voto_per_atmosfera>=i}">
                                    <img src="img/star-full.png"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="img/star-empty.png"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <div class="row rating-stars" id="prezzo-stars">
                        <c:forEach var="i" begin="1" end="5">
                            <c:choose>
                                <c:when test="${voto_per_prezzo>=i}">
                                    <img src="img/star-full.png"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="img/star-empty.png"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <hr/>
            <div class="row" id="cucina">
                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                    <h4 class="dettagli-lable">Descrizione:</h4>
                </div>
                <div class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
                    <p class="tipi-di-cucina">
                        <c:out value="${restaurant_context.getRestaurant().getDescription()}"/>
                    </p>
                </div>
            </div>
            <hr/>
            <div class="row" id="orario">
                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                    <h4 class="dettagli-lable">Orario:</h4>
                </div>
                <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3" id="giorni">
                    <p>Luned�</p><hr/>
                    <p>Marted�</p><hr/>
                    <p>Mercoled�</p><hr/>
                    <p>Gioved�</p><hr/>
                    <p>Venerd�</p><hr/>
                    <p>Sabato</p><hr/>
                    <p>Domenica</p><hr/>
                </div>
                <div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
                    <c:forEach var="ore" items="${restaurant_context.getHoursRanges()}">
                        <p><c:out value="${ore.getFormattedStart_hour()}"/> - <c:out value="${ore.getFormattedEnd_hour()}"/></p><hr/>
                    </c:forEach>
                </div>
            </div>
            <hr/>
            <div class="row" id="qr-code">
                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                    <h4 class="dettagli-lable">QR Code:</h4>
                </div>
                <div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
                    <img id="qr-code-img" src="<c:out value="${qr_url}"/>">
                </div>
            </div>
        </div>
        <!-------------------END Dettagli ----------------->

        <!--------------------Claim Restaurant ------------>
        <div class="container" id="button-container">
            <div class="row">
                <div class="col-md-12">
                    <p id="button-rec-foto">
                        <c:choose>
                            <c:when test="${sessionScope.user.getId()==restaurant_context.getOwner().getId() && sessionScope.user.getNickname()!=null}">
                                <a type="button" class="btn btn-primary btn-lg btn-config" href="${baseURL}/GetRestaurantInfoServlet?restaurant_id=<c:out value="${restaurant_context.getRestaurant().getId()}"/>"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Modifica Ristorante
                                </a> 
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn btn-danger btn-lg btn-config" 
                                    <c:choose>
                                        <c:when test="${sessionScope.user.getNickname()==null}">
                                            onclick="primaFaiLogin()"
                                        </c:when>
                                        <c:otherwise>
                                            data-toggle="collapse" data-target="#collapseAddClaim" aria-expanded="false" aria-controls="collapseAddClaim"
                                        </c:otherwise>
                                    </c:choose>
                                    ><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> Sono io il Proprietario!
                                </button>
                            </c:otherwise>
                        </c:choose>
                        <!-- button crea Ristorante -->
                        <c:choose>
                            <c:when test="${sessionScope.user.getNickname()==null}">
                                <button type="button" class="btn btn-primary btn-lg btn-config" onclick="primaFaiLogin()"><span class="glyphicon glyphicon-tag" aria-hidden="true"></span> Crea Un Ristorante
                                </button>
                            </c:when>
                            <c:otherwise>
                                <a type="button" class="btn btn-primary btn-lg btn-config" href="${baseURL}/restaurant_setup"><span class="glyphicon glyphicon-tag" aria-hidden="true"></span> Crea Un Ristorante
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
            <div class="collapse" id="collapseAddClaim">
                <div class="well">
                    <form id="claim_form">
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="claim_description" maxlength="32000">Causa Segnalazione:</label>
                            <textarea class="form-control" rows="3" name="claim_text" id="claim_description"></textarea>
                        </div>
                            <p id="btn-pubblica"><button class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span> Reclama</button></p>
                    </form>
                </div>
            </div>
        </div>

        <!-- end Main container -->
        
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