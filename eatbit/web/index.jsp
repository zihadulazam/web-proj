<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="it">
    <head>
        <title>eatBit | Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-index.jsp"%>
        
        <!-- Main Content -->
            
            <!-- search jumbotorn --> 
            <div class="jumbotron">
                <div class="container-fluid horizontally-centered">
                    <div class="jumbo-text-container"> 
                        <h1 class="name"><strong>eatBit</strong></h1>
                        <div class="motto horizontally-centered">
                            <p>Trova il ristorante più <strong>adatto</strong> a te..</p> 
                        </div>
                    </div>
                    <div class="input-thumbnail thumbnail">
                        <form role="form">
                            <div class="jumbo-textbox-container form-group horizontally-centered">
                                <input type="text" class="form-control" id="locationRisto" placeholder="Dove?">
                                <input type="text" class="sapce-top form-control typeahead" id="nomeRisto" placeholder="Nome del ristorante">
                            </div>
                            <div class="jumbo-button-container">
                                <button class="btn btn-default btn-lg btn-block" type="submit" aria-label="Left Align">
                                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span> Cerca</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- end search jumbotorn -->
            
            <!-- body after jumbotron -->
            
            <div class="container" id="main">
                <div id="last-update">
                    <div id="info-box">
                                <h1>Ultime aggiornamenti</h1>
                                <p class="lead">Ultime recensioni, Top 5 ristoranti e Ultimi ristoranti inserite</p>
                    </div>
                    <div id="updates">
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-md-12 update">
                                    <div class="update-head-img horizontally-centered">
                                        <img src="img/index/comment.png"/>
                                    </div>
                                    <div class="update-body">
                                        <c:forEach var="lastComments" items="${last5Reviews}">
                                            <div class="comment">
                                                <div class="container-fluid">
                                                    <div class="row container-fluid">
                                                        <div class="col-md-2 comment-writer">
                                                            <img src="img/avater/avater.png" class="img-circle"/>
                                                            <h5>NickName</h5>
                                                            <p class="comment-data">
                                                                <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                                10 Nov 2015 10:30
                                                            </p>
                                                        </div>
                                                        <div class="col-md-10 comment-content">
                                                            <h3 class="comment-title"><c:out value="${lastComments.name}" /></h3>
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
                                                                        <button type="button" class="btn btn-danger btn-mi-piace" disabled="disabled"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge">4</span></button>
                                                                        <button type="button" class="btn btn-danger btn-non-mi-piace" disabled="disabled"><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge">1</span></button>
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
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                            <!-- top 5 & top review -->
                            <div class="row" >
                                <!-- top 5 restaurant -->
                                <div id="second-update-row">
                                    <div class="col-md-6 update">
                                        <div id="update-left">
                                            <div class="update-head-img horizontally-centered">
                                                <h2>Top 5 per voto</h2>
                                            </div>
                                            <div class="top-by-rates update-body">
                                                <!-- primo elemento -->
                                                <c:forEach var="topRatedRisto" items="${top5RestByValue}">
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                                                <h4><c:out value="${topRatedRisto.name}" /></h4>
                                                                <div class="row rating-stars">
                                                                    <c:forEach var="i" begin="1" end="5">
                                                                        <c:choose>
                                                                            <c:when test="${topRatedRisto.global_value>=i}">
                                                                                <img src="img/star-full.png"/>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <img src="img/star-empty.png"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:forEach>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-8 restaurant-body">
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text">Via pasina-51, Riva del Garda, 38066, Trento</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text">256</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-euro" aria-hidden="true"></span> Prezzo: </span><span class="info-text">21</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-apple" aria-hidden="true"></span> Cucina: </span>
                                                                    <span class="label label-danger">Carne</span>
                                                                    <span class="label label-danger">Pesce</span>
                                                                </p>
                                                            </div>
                                                        </div>
                                                        <div class="row container-fluid">
                                                            <!-- va qua url del ristorante -->
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- top 5 per recensioni -->
                                <div class="col-md-6 update">
                                    <div id="update-right">
                                        <div class="update-head-img horizontally-centered">
                                            <h2>Top 5 per # recensioni</h2>
                                        </div>
                                        <div class="top-by-reviews update-body">
                                                <!-- primo elemento -->
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                                                <h4>Nome del Ristorante</h4>
                                                                <div class="row rating-stars">
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-empty.png"/>
                                                                        <img src="img/star-empty.png"/>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-8 restaurant-body">
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text">Via pasina-51, Riva del Garda, 38066, Trento</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text">256</span></p>
                                                            </div>
                                                        </div>
                                                        <div class="row container-fluid">
                                                            <!-- va qua url del ristorante -->
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div> <!-- fine primo elemento -->
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                                                <h4>Nome del Ristorante</h4>
                                                                <div class="row rating-stars">
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-empty.png"/>
                                                                        <img src="img/star-empty.png"/>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-8 restaurant-body">
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text">Via pasina-51, Riva del Garda, 38066, Trento</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text">256</span></p>
                                                            </div>
                                                        </div>
                                                        <div class="row container-fluid">
                                                            <!-- va qua url del ristorante -->
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div> 
                                                    
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                                                <h4>Nome del Ristorante</h4>
                                                                <div class="row rating-stars">
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-full.png"/>
                                                                        <img src="img/star-empty.png"/>
                                                                        <img src="img/star-empty.png"/>
                                                                </div>
                                                            </div>
                                                            <div class="col-md-8 restaurant-body">
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text">Via pasina-51, Riva del Garda, 38066, Trento</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text">256</span></p>
                                                            </div>
                                                        </div>
                                                        <div class="row container-fluid">
                                                            <!-- va qua url del ristorante -->
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div> 
                                        </div>
                                    </div>
                                </div>  
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <!-- end Main container -->
        
        <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
    
    </body>
</html>
