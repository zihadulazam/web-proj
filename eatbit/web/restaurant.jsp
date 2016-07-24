<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="it">
    <head>
        <title>eatBit | Ristorante</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/restaurant.css" rel="stylesheet">
        
        <!-- google font link -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        <link href='https://fonts.googleapis.com/css?family=Nunito:700' rel='stylesheet' type='text/css'>
        
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        
        <!-- Main Content -->
        <div class="container">
            <div class="row" id="header">
                <div class="col-xs-12 col-md-4">
                    <p id="restaurant-profile-pic-p"><img src="img/restaurant-default2.jpg" alt="immagine profilo" class="img-thumbnail" id="restaurant-profile-pic"/></p>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="informazioni">
                        <h1 id="restaurant-name">Nome Ristorante</h1> 
                        <hr/>
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
            
        </div>
          
          
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
        
        <!-- end Main container -->
        
        <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
        <!-- slider JS -->
        <script type="text/javascript" src="js/jssor.slider.mini.js"></script>
        <script type="text/javascript" src="js/slider-config-js.js"></script>
    </body>
</html>