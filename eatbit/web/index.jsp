<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="it">
    <head>
        <title>eatBit | Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
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
                <div class="container-fluid">
                    <div class="horizontally-centered">
                        <div class="jumbo-text-container"> 
                            <h1 class="name"><strong>eatBit</strong></h1>
                            <div class="motto horizontally-centered">
                                <p>Trova il ristorante più <strong>adatto</strong> a te..</p> 
                            </div>
                        </div>
                        <div class="input-thumbnail thumbnail">
                            <form role="form">
                                <div class="jumbo-textbox-container form-group horizontally-centered">
                                    <input type="text" class="form-control" placeholder="Dove?">
                                    <input type="text" class="sapce-top form-control" placeholder="Nome del ristorante">
                                </div>
                                <div class="jumbo-button-container">
                                    <button class="btn btn-danger btn-lg btn-block" type="submit" aria-label="Left Align">
                                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> Cerca</button>
                                </div>
                            </form>
                        </div>
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
                                                        <h3 class="comment-title">Titolo Commento</h3>
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
                                                                <div class="col-md-4">
                                                                    <button type="button" class="btn btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Utile</button>
                                                                </div>
                                                                <div class="col-md-8">
                                                                     <h4 class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Ristorante: <a href="#">Nome del Ristorante</a></h4>
                                                                </div> 
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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
                                                        <h3 class="comment-title">Titolo Commento</h3>
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
                                                                <div class="col-md-4">
                                                                    <button type="button" class="btn btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Utile</button>
                                                                </div>
                                                                <div class="col-md-8">
                                                                     <h4 class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Ristorante: <a href="#">Nome del Ristorante</a></h4>
                                                                </div> 
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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
                                                        <h3 class="comment-title">Titolo Commento</h3>
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
                                                                <div class="col-md-4">
                                                                    <button type="button" class="btn btn-info"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Utile</button>
                                                                </div>
                                                                <div class="col-md-8">
                                                                     <h4 class="comment-nome-ristorante"><span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span> Ristorante: <a href="#">Nome del Ristorante</a></h4>
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
                            <!-- top 5 & top review -->
                            <div calss="row" >
                                <!-- top 5 restaurant -->
                                <div id="second-update-row">
                                    <div class="col-md-6 update">
                                        <div id="update-left">
                                            <div class="update-head-img horizontally-centered">
                                                <h2>Top 5 per voto</h2>
                                            </div>
                                            <div class="top-by-rates update-body">
                                                <!-- primo elemento -->
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/avater/avater.png" class="r-img img-circle"/>
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
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div> <!-- fine primo elemento -->
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/avater/avater.png" class="r-img img-circle"/>
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
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div> 
                                                    
                                                    <div class="container-fluid restaurant">
                                                        <div class="row container-fluid">
                                                            <div class="col-md-4 restaurant-title">
                                                                <img src="img/avater/avater.png" class="r-img img-circle"/>
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
                                                            <div class="btn-visita"><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button></div>
                                                        </div>
                                                    </div> 
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
                                                                <img src="img/avater/avater.png" class="r-img img-circle"/>
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
                                                                <img src="img/avater/avater.png" class="r-img img-circle"/>
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
                                                                <img src="img/avater/avater.png" class="r-img img-circle"/>
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
        
        
        <!-- scripts -->
    
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="js/jquery-1.12.4.min.js"></script>
    
        <!-- js (Bootstrap) -->
        <script src="js/bootstrap.min.js"></script>
        
        <!-- login modal js -->
        <script src="js/login-register.js"></script>
        
        <!-- hosted by Microsoft Ajax CDN -->
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    </body>
</html>
