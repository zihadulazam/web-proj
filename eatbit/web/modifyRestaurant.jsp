
<%@page import="database.Restaurant"%>
<%@page import="database.PriceRange"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
        <title>eatBit | Modifica ristorante</title>
        <meta charset="UTF-8">
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

        <!-- Favicon and touch icons -->
        <link rel="shortcut icon" href="assets/ico/favicon.png">
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">
        
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>

        <!-- page container -->
        <!-- Top content -->
        <div class="top-content">
            <div class="container">
                
                <div class="row">
                    <div class="col-sm-8 col-sm-offset-2 text">
                        <h1><strong>Modifica il tuo ristorante</strong> <c:out value="${restaurant.getName()}" /></h1>
                        <div class="description">
                       	    <p>
                                Dopo <strong>l'approvazione</strong> i tuoi dati verranno aggiornati!
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3 form-box">
                    	<form action="${pageContext.request.contextPath}/ModifyRestaurantServlet" method="post" class="f1">

                    		<p>Compila il modulo con i dati <strong>reali</strong> del ristorante!</p>
                    		<div class="f1-steps">
                    			<div class="f1-progress">
                    			    <div class="f1-progress-line" data-now-value="16.66" data-number-of-steps="4" style="width: 26.66%;"></div>
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
                    			    <h5>Nome:</h5>
                                    <input type="text" name="name" value="${restaurant.getName()}" class="form-control" id="name">
                                </div>
                                <div class="form-group">
                                    <h5>Descrizione</h5>
                                    <textarea name="description" placeholder="Descrizione..." 
                                    	                 class="form-control" id="description"><c:out  value="${restaurant.getDescription()}" /></textarea>
                                </div>
                                <div class="form-group">
                                    <h5>Sito Web:</h5>
                                    <div class="input-group">
                                        <input type="text" name="web_site" placeholder="Sito Web..." value="${restaurant.getWeb_site_url()}" class="form-control" id="web_site">
                                        <span class="input-group-addon"><span class="fa fa-globe" aria-hidden="true"></span></span>
                                    </div>
                                    
                                </div>
                                <div class="form-group">
                                    <h5>Indirizzo:</h5>
                                    <div class="input-group">
                                        <input type="text" value="" name="address" placeholder="Indririzzo..." class="form-control" id="address">
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
                                            <input type="checkbox" name="americana" value="Americana"/> Americana
                                            <br />
                                            <input type="checkbox" name="asiatica" value="Asiatica"/> Asiatica
                                            <br />
                                            <input type="checkbox" name="africana" value="Africana"/> Africana
                                            <br />
                                            <input type="checkbox" name="cinese" value="Cinese"/> Cinese
                                            <br />
                                            <input type="checkbox" name="japonese" value="Japonese"/> Japonese
                                            <br />
                                            <input type="checkbox" name="sushi" value="Sushi"/> Sushi
                                         </div>
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="checkbox" name="CK" value="Indiana"/> Indiana
                                            <br />
                                            <input type="checkbox" name="CK" value="Italiana"/> Italiana
                                            <br />
                                            <input type="checkbox" name="CK" value="Pizza"/> Pizza
                                            <br />
                                            <input type="checkbox" name="CK" value="Francese"/> Francese
                                            <br />
                                            <input type="checkbox" name="CK" value="Spagnola"/> Spagnola
                                            <br />
                                            <input type="checkbox" name="CK" value="Pesce"/> Pesce
                                         </div>
                                         <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                            <input type="checkbox" name="CK" value="Carne"/> Carne
                                            <br />
                                            <input type="checkbox" name="CK" value="Messicana"/> Messicana
                                            <br />
                                            <input type="checkbox" name="CK" value="Fast-Food"/> Fast-Food
                                            <br />
                                            <input type="checkbox" name="CK" value="Vegetariana"/> Vegetariana
                                         </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <h5>Fascia Prezzo:</h5>
                                    <div class="input-group">
                                        <input value="${priceRange.getMin()}" type="text" name="prezzo_min" placeholder="Prezzo Min..." class="form-control" id="prezzo_min">
                                        <span class="input-group-addon">€</span>
                                    </div>
                                    <div class="input-group">
                                        <input value = "${priceRange.getMax()}" type="text" name="prezzo_max" placeholder="Prezzo Max..." class="form-control" id="prezzo_max">
                                        <span class="input-group-addon">€</span>
                                    </div>
                                </div>
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-previous">Precedente</button>
                                    <button type="button" class="btn btn-next">Successivo</button>
                                </div>
                            </fieldset>
                                        
                            <fieldset>
                                <h4>Orario d'apertura:</h4>
                                <div class="form-group">
                                    <label class="sr-only" for="f1-facebook">Ora inizio</label>
                                    <input type="text" name="oraInizio" placeholder="Ora Inizio (Es: 10:00)" class="f1-facebook form-control" id="f1-facebook">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="oraFine">Ora Fine</label>
                                    <input type="text" name="f1-twitter" placeholder="Ora Inizio (Es: 22:00)" class="f1-twitter form-control" id="f1-twitter">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="f1-google-plus">Giorni della settimana</label>
                                    <input type="text" name="giorni" placeholder="Giorni della settimana (Es: 1,2,3,4,5,6,7)" class="f1-google-plus form-control" id="f1-google-plus">
                                </div>
                                <div class="f1-buttons">
                                    <button type="button" class="btn btn-previous">Precedente</button>
                                    <button type="submit" class="btn btn-submit">Fine</button>
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

