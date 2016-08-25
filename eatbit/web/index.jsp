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

         <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
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
                        <form id="search-form" role="form" method="get" action="/eatbit/PopulateTable">
                            <div class="jumbo-textbox-container form-group horizontally-centered">
                                <div class="checkbox text-right">
                                        <span class="glyphicon glyphicon-screenshot" aria-hidden="true"></span><label><input type="checkbox" id="vicino_a_me">Vicino a me</label>
                                        <div class="alert alert-danger text-center" id="error-msg" role="alert" style="display: none;"><span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span></div>
                                        <div class="alert alert-success text-center" id="success-msg" role="alert" style="display: none;"><span class="glyphicon glyphicon-ok-sign" aria-hidden="true"></span></div>
                                </div>
                                <input type="hidden" id="lat" name="latitude">
                                <input type="hidden" id="lon" name="longitude">
                                <input type="text" class="form-control" id="locationRisto" name="luogo" placeholder="Dove?">
                                <input type="text" class="sapce-top form-control typeahead" id="nomeRisto" name="name" placeholder="Nome del Ristorante / Tipo di Cucina">
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
                                        <c:forEach var="lastComment" items="${last5Reviews}">
                                            <div class="comment">
                                                <div class="container-fluid">
                                                    <div class="row container-fluid">
                                                        <div class="col-md-2 comment-writer">
                                                            <img src="img/avatar/avatar.png" class="img-circle"/>
                                                            <h5><c:out value="${lastComment.getUser().getNickname()}" /></h5>
                                                            <p class="comment-data">
                                                                <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                                <c:out value="${lastComment.getReview().getDate_creation()}" />
                                                            </p>
                                                            <c:if test="${lastComment.getPhoto()!=null}">
                                                                <a class="thumbnail" href="<c:out value="${lastComment.getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${lastComment.getPhoto().getPath()}" />">
                                                                    <img src="<c:out value="${lastComment.getPhoto().getPath()}" />">
                                                                </a>
                                                                <div class="text-center">
                                                                    <a class="btn-segnala-photo-recensione popov" href="" id="<c:out value="${lastComment.getPhoto().getId()}"/>"  onclick="segnalaPhoto(this.id); return false;">Segnala Photo!</a>
                                                                </div>
                                                            </c:if>
                                                        </div>
                                                        <div class="col-md-10 comment-content">
                                                            <p>
                                                                <button type="button" class="btn btn-danger btn-segnala-review" id="<c:out value="${lastComment.getReview().getId()}" />" title="Segnala Recensione" onclick="segnalaReview(this.id)"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></button>
                                                                <h3 class="comment-title"><c:out value="${lastComment.getReview().getName()}" /></h3>
                                                            </p>
                                                            <div class="row rating-stars">
                                                                <c:forEach var="i" begin="1" end="5">
                                                                    <c:choose>
                                                                        <c:when test="${lastComment.getReview().getGlobal_value()>=i}">
                                                                            <img src="img/star-full.png"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img src="img/star-empty.png"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </div>

                                                            <p class="comment-text"><c:out value="${lastComment.getReview().getDescription()}" /> </p>
                                                            
                                                            <c:if test="${lastComment.getReply()!=null}">
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
                                                                                        <p class="risposta-text"><c:out value="${lastComment.getReply().getDescription()}" /></p>
                                                                                        <p class="risposta-autore">Da: Admin</p>
                                                                                        <p class="risposta-date"><c:out value="${lastComment.getReply().getDate_creation()}" /></p>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </c:if>
                                                            <div class="container-fluid">
                                                                <div class="row">
                                                                    <div class="col-md-12">
                                                                        <button type="button" class="btn btn-danger btn-mi-piace" 
                                                                                    <c:if test="${sessionScope.user.getNickname()==null}">
                                                                                        disabled="disabled"
                                                                                    </c:if>
                                                                                        onclick="miPiace(<c:out value="${lastComment.getReview().getId()}"></c:out>,1)"
                                                                                        id="btn-mipiace-<c:out value="${lastComment.getReview().getId()}"></c:out>"
                                                                                        ><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Mi Piace <span class="badge"><c:out value="${lastComment.getReview().getLikes()}" /></span></button>
                                                                        <button type="button" class="btn btn-danger btn-non-mi-piace" 
                                                                                <c:if test="${sessionScope.user.getNickname()==null}">
                                                                                    disabled="disabled"
                                                                                </c:if>
                                                                                onclick="nonMiPiace(<c:out value="${lastComment.getReview().getId()}"></c:out>,0)"
                                                                                id="btn-nonmipiace-<c:out value="${lastComment.getReview().getId()}"></c:out>"
                                                                                ><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Non Mi Piace <span class="badge"><c:out value="${lastComment.getReview().getDislikes()}" /></span></span></button>
                                                                        <p class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span><a href="http://localhost:8080/eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${lastComment.getReview().getId_restaurant()}" />"> <c:out value="${lastComment.getRestaurantName()}" /></a></p>
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
                                    <div class="col-md-12 update">
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
                                                                <h4><c:out value="${topRatedRisto.getRestaurant().getName()}" /></h4>
                                                                <div class="row rating-stars">
                                                                    <c:forEach var="i" begin="1" end="5">
                                                                        <c:choose>
                                                                            <c:when test="${topRatedRisto.getRestaurant().getGlobal_value()>=i}">
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
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text"><c:out value="${topRatedRisto.getCoordinate().getAddress()}" />, <c:out value="${topRatedRisto.getCoordinate().getCity()}" />, <c:out value="${topRatedRisto.getCoordinate().getState()}" /></span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text"><c:out value="${topRatedRisto.getRestaurant().getReviews_counter()}" /></span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-euro" aria-hidden="true"></span> Prezzo: </span><span class="info-text"><c:out value="${topRatedRisto.getPriceRange().getMin()}"/>€ - <c:out value="${topRatedRisto.getPriceRange().getMax()}" />€</span></p>
                                                                <p class="info-row cucin"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-apple" aria-hidden="true"></span> Cucina: </span>
                                                                    <c:forEach var="tipocucine" items="${topRatedRisto.getCuisines()}">
                                                                        <span class="label label-danger tipo-cucine cucin"><c:out value="${tipocucine}"/></span>
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </div>
                                                        <div class="row container-fluid">
                                                            <!-- va qua url del ristorante -->
                                                            <div class="btn-visita"><a type="button" class="btn btn-primary" href="http://localhost:8080/eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${topRatedRisto.getRestaurant().getId()}"/>"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</a></div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row" >
                                <!-- top 5 per recensioni -->
                                <div class="col-md-12 update">
                                    <div id="update-right">
                                        <div class="update-head-img horizontally-centered">
                                            <h2>Top 5 per # recensioni</h2>
                                        </div>
                                        <div class="top-by-reviews update-body">
                                                <!-- primo elemento -->
                                                <c:forEach var="topReviewRisto" items="${top5RestByReviews}">
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/restaurant-default.png" class="r-img img-circle"/>
                                                                <h4><c:out value="${topReviewRisto.getRestaurant().getName()}" /></h4>
                                                                <div class="row rating-stars">
                                                                    <c:forEach var="i" begin="1" end="5">
                                                                        <c:choose>
                                                                            <c:when test="${topReviewRisto.getRestaurant().getGlobal_value()>=i}">
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
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Indirizzo: </span><span class="info-text"><c:out value="${topReviewRisto.getCoordinate().getAddress()}" />, <c:out value="${topReviewRisto.getCoordinate().getCity()}" />, <c:out value="${topReviewRisto.getCoordinate().getState()}" /></span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-edit" aria-hidden="true"></span> Numero Recensioni: </span><span class="info-text"><c:out value="${topReviewRisto.getRestaurant().getReviews_counter()}" /></span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-euro" aria-hidden="true"></span> Prezzo: </span><span class="info-text"><c:out value="${topReviewRisto.getPriceRange().getMin()}" />€ - <c:out value="${topReviewRisto.getPriceRange().getMax()}"/>€</span></p>
                                                                <p class="info-row"><span class="info-lable"><span class="glyphicon glyphicon glyphicon-apple" aria-hidden="true"></span> Cucina: </span>
                                                                    <c:forEach var="tipocucine" items="${topReviewRisto.getCuisines()}">
                                                                        <span class="label label-danger tipo-cucine"><c:out value="${tipocucine}"/></span>
                                                                    </c:forEach>
                                                                </p>
                                                            </div>
                                                        </div>
                                                        <div class="row container-fluid">
                                                            <!-- va qua url del ristorante -->
                                                            <div class="btn-visita"><a type="button" class="btn btn-success" href="http://localhost:8080/eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${topReviewRisto.getRestaurant().getId()}"/>"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</a></div>
                                                        </div>
                                                    </div> <!-- fine primo elemento -->
                                                </c:forEach>
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
        
        <!-- index.js -->
        <script src="js/index.js"></script>
        
        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
    </body>
</html>
