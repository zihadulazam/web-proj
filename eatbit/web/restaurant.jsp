<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="it">
    <head>
        <title>eatBit | <c:out value="${restaurant_context.getRestaurant().getName()}"/></title>
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
        
        <!-- Main Content -->
        <div class="container">
            <div class="row" id="header">
                <div class="col-xs-12 col-md-12"  >
                    
                    <hr/> 
                </div>
            </div>
            
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
                        <p id="classifica"><span class="glyphicon glyphicon-sort" aria-hidden="true"></span> Classifica (per città): <c:out value="${restaurant_context.getCityPosition()}"/></p>
                        <p id="bold"><span class="glyphicon glyphicon-time" aria-hidden="true"></span> Oggi</p>
                        <p>11:00-15:00</p>
                        <p>18:00-00:00</p>
                        <p id="info-apertura"><span class="label label-success">Ora Aperto</span></p>
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
                            <button  class="btn btn-large btn-block btn-danger btn-segnala" id="<c:out value="${photos.getId()}"/>" title="Segnala" onclick="segnalaPhoto(this.id)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
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
                        <button type="button" class="btn btn-success btn-lg btn-config" data-toggle="collapse" data-target="#collapseAddRistoRate" aria-expanded="false" aria-controls="collapseAddRecensione"><span class="glyphicon glyphicon-star" aria-hidden="true"></span> Vota questo ristorante</button>
                        <button type="button" class="btn btn-success btn-lg btn-config" data-toggle="collapse" data-target="#collapseAddRecensione" aria-expanded="false" aria-controls="collapseAddRecensione"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Scrivi una recensione</button>
                        <button type="button" class="btn btn-success btn-lg btn-config" data-toggle="collapse" data-target="#collapseAddFoto" aria-expanded="false" aria-controls="collapseAddFoto"><span class="glyphicon glyphicon-camera" aria-hidden="true"></span> Aggiungi una foto</button>
                    </p>
                </div>
            </div>
            <div class="collapse" id="collapseAddRistoRate">
                <div class="well">
                    <form>
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
                            <p id="btn-pubblica"><button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span> Pubblica</button></p>
                        </div>
                    </form>
                </div>
            </div>
            <div class="collapse" id="collapseAddRecensione">
                <div class="well">
                    <form>
                        <div class="from-group">
                            <div class="row">
                                <div class="col-sm-12 col-md-3">
                                    <label class="rating-lb"><span class="glyphicon glyphicon-star" aria-hidden="true"></span> Valutazione Globale: </label>
                                </div>
                                <div class="col-sm-12 col-md-9">
                                    <div class="rating-bar" id="valutazioneGlobaleBar" type="text"></div>
                                    <input type="text" id="valutazioneGlobaleValue" class="rating-value">
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
                                    <input type="text" id="ciboValue" class="rating-value">
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
                                    <input type="text" id="servizioValue" class="rating-value">
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
                                    <input type="text" id="atmosferaValue" class="rating-value">
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
                                    <input type="text" id="prezzoValue" class="rating-value">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <br/>
                            <p>
                                <label class="rating-lb" for="comment">Titolo:</label>
                                <input id="recensione-title" type="text">
                            </p>
                        </div>
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="comment">Recensione:</label>
                            <textarea class="form-control" rows="5" id="comment"></textarea>
                        </div>
                        <div class="form-group">
                            <br/>
                            <label class="rating-lb" for="comment">Photo:</label>
                            <div class="input-group">
                                <label class="input-group-btn">
                                    <span class="btn btn-default">
                                        Cerca Photo&hellip; <input name="photo" id="AddRecensione-cerca-photo" type="file" style="display: none;" accept="image/*" multiple>
                                    </span>
                                </label>
                                <input type="text" id="AddRecensione-cerca-photo-name" class="form-control" readonly>
                            </div>
                            <p id="btn-pubblica"><button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span> Pubblica</button></p>
                        </div>
                    </form>
                </div>
            </div>
            <div class="collapse" id="collapseAddFoto">
                <div class="well">
                    <form>
                        <div class="row">
                            <div class="col-md-10" id="AddFoto-FileInput">
                                <div class="input-group">
                                    <label class="input-group-btn">
                                        <span class="btn btn-default">
                                            Cerca Photo&hellip; <input name="photo" id="AddFoto-cerca-photo" type="file" style="display: none;" accept="image/*" multiple>
                                        </span>
                                    </label>
                                    <input type="text" id="AddFoto-cerca-photo-name" class="form-control" readonly>
                                </div>
                            </div>
                            <div class="col-md-2" id="btn-upload">
                                <button type="submit" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Carica</button>
                            </div>
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
                    <c:forEach var="i" begin="1" end="7">
                        <tr>
                            <td>
                                <div class="comment">
                                    <div class="container-fluid">
                                        <div class="row container-fluid">
                                            <div class="col-md-2 comment-writer">
                                                <img src="img/avater/avater.png" class="img-circle"/>
                                                <h5>Nickname</h5>
                                                <p class="comment-data">
                                                    <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                    10 Nov 2015 10:30
                                                </p>
                                                <a class="thumbnail" href="img/02.jpg" data-lightbox="example-<c:out value="${i}"/>">
                                                    <img src="img/02.jpg">
                                                </a>
                                                <div class="text-center">
                                                    <button type="button" class="btn btn-danger btn-segnala-photo-recensione popov" title="Segnala Photo"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                                </div>
                                            </div>
                                            <div class="col-md-10 comment-content">
                                                <p>
                                                    <button type="button" class="btn btn-danger btn-segnala-review" title="Segnala Recensione"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                                    <h3 class="comment-title">Titolo Commento <c:out value="${i}"/></h3>
                                                </p>
                                                <div class="row rating-stars">
                                                    <img src="img/star-full.png"/>
                                                    <img src="img/star-full.png"/>
                                                    <img src="img/star-full.png"/>
                                                    <img src="img/star-empty.png"/>
                                                    <img src="img/star-empty.png"/>
                                                </div>
                                                
                                                <p class="comment-text">Lorem Ipsum è un testo segnaposto utilizzato nel settore della tipografia e della stampa. Lorem Ipsum è considerato il testo segnaposto standard sin dal sedicesimo secolo, quando un anonimo tipografo prese una cassetta di caratteri e li assemblò per preparare un testo campione. </p>
                                                
                                                <div class="container-fluid">
                                                    <div class="row">
                                                        <div class="col-md-2">
                                                        </div>
                                                        <div class="col-md-10">
                                                            <div class="container-fluid risposta-admin">
                                                                <div class="row">
                                                                    <div class="col-md-2">
                                                                        <p class="lb"><label>Risposta:</label></p>
                                                                    </div>
                                                                    <div class="col-md-10">
                                                                        <p class="risposta-text">"Lorem Ipsum è un testo segnaposto utilizzato nel settore della tipografia e della stampa. Lorem Ipsum è considerato il testo segnaposto standard sin dal sedicesimo secolo, quando un anonimo tipografo prese una cassetta di caratteri e li assemblò per preparare un testo campione."</p>
                                                                        <p class="risposta-autore">Da: Admin</p>
                                                                        <p class="risposta-date">15 Nov 2015 16:31</p>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <div class="container-fluid">
                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <button type="button" class="btn btn-danger btn-mi-piace"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge">4</span></button>
                                                            <button type="button" class="btn btn-danger btn-non-mi-piace"><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge">1</span></button>
                                                        </div>
                                                    </div>
                                                </div>
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
            <h1><img src="img/restaurant/map.png"/> Vicino a NOME_RISTORANTE</h1>
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
                                <c:when test="${restaurant_context.getRestaurant().getGlobal_value()>=i}">
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
                                <c:when test="${restaurant_context.getRestaurant().getGlobal_value()>=i}">
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
                                <c:when test="${restaurant_context.getRestaurant().getGlobal_value()>=i}">
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
                                <c:when test="${restaurant_context.getRestaurant().getGlobal_value()>=i}">
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
                    <h4 class="dettagli-lable">Cucina:</h4>
                </div>
                <div class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
                    <p class="tipi-di-cucina">
                        <c:forEach var="tipocucine" items="${restaurant_context.getCuisines()}">
                            <span class="label label-primary"><c:out value="${tipocucine}"/></span>
                        </c:forEach>
                    </p>
                </div>
            </div>
            <hr/>
            <div class="row" id="orario">
                <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                    <h4 class="dettagli-lable">Orario:</h4>
                </div>
                <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3" id="giorni">
                    <p>Lunedì</p><hr/>
                    <p>Martedì</p><hr/>
                    <p>Mercoledì</p><hr/>
                    <p>Giovedì</p><hr/>
                    <p>Venerdì</p><hr/>
                    <p>Sabato</p><hr/>
                    <p>Domenica</p>
                </div>
                <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                    <c:forEach var="ore" items="${restaurant_context.getHoursRanges()}">
                        <p><c:out value="${ore.getStart_hour()}"/> - <c:out value="${ore.getEnd_hour()}"/></p><hr/>
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