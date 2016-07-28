<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="it">
    <head>
        <title>eatBit | Ristorante</title>
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
                <div class="col-xs-12 col-md-12"  id="restaurant-name">
                    <h1><img src="img/restaurant/name.png"/> Nome Ristorante</h1> 
                    <hr/> 
                </div>
            </div>
            
            <div class="row">
                <div class="col-xs-12 col-md-4">
                    <p id="restaurant-profile-pic-p"><img src="img/restaurant-default2.jpg" alt="immagine profilo" class="img-thumbnail" id="restaurant-profile-pic"/></p>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="informazioni">
                        <div class="row rating-stars">
                                <img src="img/star-full.png"/>
                                <img src="img/star-full.png"/>
                                <img src="img/star-full.png"/>
                                <img src="img/star-empty.png"/>
                                <img src="img/star-empty.png"/>
                        </div>
                        <p>(299 recensioni)</p>
                        <p><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Via Mario 51, Ala (TN), Italia</p>
                        <p><span class="glyphicon glyphicon-phone" aria-hidden="true"></span> 3492106738</p>
                        <p><span class="glyphicon glyphicon-globe" aria-hidden="true"></span><a href="#" target="_blank"> Sito Web</a></p>
                        <p id="tipi-di-cucina">
                            <span class="label label-primary">Carne</span>
                            <span class="label label-primary">Pesce</span>
                        </p>
                    </div> 
                </div>
                <div class="col-xs-12 col-md-4">
                    <div id="informazioni-orario">
                        <p id="classifica"><span class="glyphicon glyphicon-sort" aria-hidden="true"></span> Classifica (per città): 21</p>
                        <p id="bold"><span class="glyphicon glyphicon-time" aria-hidden="true"></span> Oggi</p>
                        <p>11:00-15:00</p>
                        <p>18:00-00:00</p>
                        <p id="info-apertura"><span class="label label-success">Ora Aperto</span></p>
                    </div>
                </div>
            </div>
        </div> <!--End Container-->
        
          
        <!-------------Slider Start--------------->
        <div id="jssor_1" style="position: relative; margin: 0 auto; top: 0px; left: 0px; width: 1150px; height: 480px; overflow: hidden; visibility: hidden; background-color: #24262e;">
            <!-- Loading Screen -->
            <div data-u="loading" style="position: absolute; top: 0px; left: 0px;">
                <div style="filter: alpha(opacity=70); opacity: 0.7; position: absolute; display: block; top: 0px; left: 0px; width: 100%; height: 100%;"></div>
                <div style="position:absolute;display:block;background:url('img/loading.gif') no-repeat center center;top:0px;left:0px;width:100%;height:100%;"></div>
            </div>
            <div data-u="slides" style="cursor: default; position: relative; top: 0px; left: 240px; width: 920px; height: 480px; overflow: hidden;">
                <div data-p="150.00" style="display: none;">
                    <img data-u="image" src="img/01.jpg" />
                    <img data-u="thumb" src="img/01.jpg" />
                </div>
                <div data-p="150.00" style="display: none;">
                    <img data-u="image" src="img/02.jpg" />
                    <img data-u="thumb" src="img/02.jpg" />
                </div>
            </div>
            <!-- Thumbnail Navigator -->
            <div data-u="thumbnavigator" class="jssort01-99-66" style="position:absolute;left:0px;top:0px;width:240px;height:480px;" data-autocenter="2">
                <!-- Thumbnail Item Skin Begin -->
                <div data-u="slides" style="cursor: default;">
                    <div data-u="prototype" class="p">
                        <div class="w">
                            <div data-u="thumbnailtemplate" class="t"></div>
                        </div>
                        <div class="c"></div>
                    </div>
                </div>
                <!-- Thumbnail Item Skin End -->
            </div>
            <!-- Arrow Navigator -->
            <span data-u="arrowleft" class="jssora05l" style="top:158px;left:248px;width:40px;height:40px;" data-autocenter="2"></span>
            <span data-u="arrowright" class="jssora05r" style="top:158px;right:8px;width:40px;height:40px;" data-autocenter="2"></span>
        </div>

        <!-- #endregion Jssor Slider End -->
        <!-------------End Slider----------------->
        
        
        <!-------------Buttons Add Recensione, Upload Foto with collapse -------------------->
        <div class="container" id="button-container">
            <div class="row">
                <div class="col-md-12">
                    <p id="button-rec-foto">
                        <button type="button" class="btn btn-success btn-lg btn-config" data-toggle="collapse" data-target="#collapseAddRecensione" aria-expanded="false" aria-controls="collapseAddRecensione"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Scrivi una recensione</button>
                        <button type="button" class="btn btn-success btn-lg btn-config" data-toggle="collapse" data-target="#collapseAddFoto" aria-expanded="false" aria-controls="collapseAddFoto"><span class="glyphicon glyphicon-camera" aria-hidden="true"></span> Aggiungi una foto</button>
                    </p>
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
                            <label class="rating-lb" for="comment">Recensione:</label>
                            <textarea class="form-control" rows="5" id="comment"></textarea>
                            <p id="btn-pubblica"><button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span> Pubblica</button></p>
                        </div>
                    </form>
                </div>
            </div>
            <div class="collapse" id="collapseAddFoto">
                <div class="well">
                    <form>
                        <div class="row">
                            <div class="col-md-10">
                                <input type="file">
                            </div>
                            <div class="col-md-2">
                                <button id="btn-upload" type="submit" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Carica</button>
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
                                            </div>
                                            <div class="col-md-10 comment-content">
                                                <h3 class="comment-title">Titolo Commento <c:out value="${i}"/></h3>
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
                                                        <div class="col-md-2"></div>
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
                                                        <div class="col-md-6">
                                                            <button type="button" class="btn btn-default btn-mi-piace"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge">4</span></button>
                                                            <button type="button" class="btn btn-danger btn-non-mi-piace"><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge">1</span></button>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <h4 class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Ristorante: <a href="#">Nome del Ristorante</a></h4>
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
            
        </div>
        <!-------------------END Dettagli ----------------->

        <!-- end Main container -->
        
        <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
        <!-- slider JS -->
        <script type="text/javascript" src="js/jssor.slider.mini.js"></script>
        <script type="text/javascript" src="js/slider-config-js.js"></script>
        
        <!-- Datatable js-->
        <script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
        
        <!-- Google Map JS CDN-->
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5PvbD12gNax9Avkf-qes0_Y-_oB90b-o&callback=initMap"async defer></script>
       
        <!-- Restaurant JS-->
        <script type="text/javascript" src="js/restaurant.js"></script>

    </body>
</html>