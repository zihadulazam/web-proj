<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        
 	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

	<title>Eatbit | Search</title>
        
        <!-- ico-->
        <link rel="icon" href="../eatbit/img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="../eatbit/img/favicon.ico" type="image/x-icon">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/datatable.css" rel="stylesheet">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
    </head>
    <body>
        
       <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        
        <!-- Main Content --> 
        <div class="container contenitori-blocco" id="ristoranti-container" style="margin-top: 10px; ">
            <div class="row">
                <div class="col-sm-3"> 
                  <input type="checkbox" name="cucina" value="ASIATICA" >ASIATICA
                  </div>
                  <div class="col-sm-3"> 
                  <input type="checkbox" name="cucina" value="CARNE" >CARNE
                  </div>
                  <div class="col-sm-3"> 
                  <input type="checkbox" name="cucina" value="CINESE" >CINESE
                  </div>
                  <div class="col-sm-3"> 
                  <input type="checkbox" name="cucina" value="FAST-FOOD" >FAST-FOOD
                  </div>
                </div>
            <div class="row">
                <div class="col-sm-3">         
                  <input type="checkbox" name="cucina" value="FRANCESE" >FRANCESE
                  </div>
                  <div class="col-sm-3">   
                  <input type="checkbox" name="cucina" value="GIAPPONESE" >GIAPPONESE
                  </div>
                  <div class="col-sm-3">   
                  <input type="checkbox" name="cucina" value="INDIANA" >INDIANA
                  </div>
                  <div class="col-sm-3">   
                  <input type="checkbox" name="cucina" value="ITALIANA" >ITALIANA
                  </div>
            </div>
            <div class="row" width="100%">
                <div class="col-sm-3">    
                  <input type="checkbox" name="cucina" value="PESCE" >PESCE
                </div>
                <div class="col-sm-3">   
                  <input type="checkbox" name="cucina" value="PIZZA" >PIZZA
                </div>
                <div class="col-sm-3">     
                  <input type="checkbox" name="cucina" value="SPAGNOLA" >SPAGNOLA
                </div>
                <div class="col-sm-3">     
                  <input type="checkbox" name="cucina" value="SUSHI" >VEGETARIANA
                </div>
            </div> 
            <div class="row">
                <div class="col-sm-8">     
                      <input type="radio" name="prezzo" value="BASSO" >BASSO
                      <input type="radio" name="prezzo" value="MEDIO" >MEDIO
                      <input type="radio" name="prezzo" value="ALTO" >ALTO
                      <input type="radio" name="prezzo" value="DI LUSSO" >DI LUSSO
                </div>
                <div class="col-sm-4">
                    
                    <button type="button" class="btn btn-success">FILTRA</button>
                    <button type="button" class="btn btn-info">RESET</button>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">     
                    <button type="button" class="btn btn-secondary">ORDINA PER PREZZO(BASSO A ALTO)</button>
                    <button type="button" class="btn btn-secondary">ORDINA PER PREZZO(ALTO A BASSO)</button>
                    <button type="button" class="btn btn-secondary">ORDINA PER MIGLIOR VOTO</button>
                    <button type="button" class="btn btn-secondary">ALFABETICO</button>
                </div>
            </div>
            <table id="tabella-ristoranti" class="col-sm-12" >
                <tbody >                    
                    <c:forEach var="i" items="${list}">
                        <tr >
                            <!-- Prima colonna-->
                            <td class="col-sm-3 sorting_1 " style="padding-right: 0px; padding-left: 0px;" >  
                                <div class=" container-left">
                                    <div class="row">
                                    <div class="container-fluid">
                                        <div class="container-writer">
                                            <c:choose>
                                                    <c:when test="${i.getPhotos().size()>0}">
                                                        <img src="<c:out value="${i.getPhotos().get(0).getPath()}"/>"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="img/restaurant-default.png" class="img-circle"/>
                                                    </c:otherwise>
                                                </c:choose>  
                                        </div>
                                        
                                    </div>
                                    </div>
                                </div>
                            </td>
                            <!-- Seconda colonna-->
                            <td class="col-sm-3 sorting_1" style="padding-right: 0px;padding-left: 0px;" >
                                <div class="container-center "align="left>  
                                    <div class="container-fluid">
                                        <div class="container-writer">
                                            <h4 class="container-title"><c:out value="${i.getRestaurant().getName()}"/></h4>                                            
                                            <h5>Recensioni:<c:out value="${i.getRestaurant().getReviews_counter()}"/></h5>
                                            <h5>Posizione:<c:out value="${i.getCityPosition()}"/></h5>
                                            <c:forEach var="j" begin="1" end="5"> <!-- Quante stelle ha il ristorante-->
                                                <c:choose>
                                                    <c:when test="${i.getRestaurant().getGlobal_value()>=j}">
                                                        <img src="img/star-full.png" style="max-width:20px; max-height:20px;border:0;"align="left"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="img/star-empty.png" style="max-width:20px; max-height:20px;border:0;"align="left"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>         
                                               
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- terza colonna-->
                            <td class="col-sm-4 sorting_1" style="padding-right: 0px;padding-left: 0px;" >
                                <div class="container-center "align="left>  
                                    <div class="container-fluid">
                                        <div class="container-writer">
                                            <h4><c:out value="${i.getCoordinate().getAddress()}"/></h4>
                                            <h4>Prezzo: <c:out value="${i.getPriceRange().getName()}"/></h4>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td class="col-sm-1 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-right">  
                                    <div class="container-fluid">
                                        <div class="container-writer">                                            
                                            <c:forEach var="k" items="${i.getCuisines()}">
                                                <p class="label-default" align="left"><c:out value="${k}"/></p>
                                            </c:forEach>                                            
                                        </div>
                                    </div>
                                </div>
                            </td>
                            
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- end Main container -->  
        
        <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
        <!-- Datatable -->    
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/responsive/2.1.0/js/dataTables.responsive.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/responsive/2.1.0/js/responsive.bootstrap.min.js">
	</script>
	
	<!--<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>-->
        
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {
                var tabella= $('#tabella-ristoranti').DataTable( {
                    "pagingType": "full_numbers",
                    bFilter: true,
                    bInfo: false,
                    ordering:true,
                    order: [[ 2, "desc" ]], 
                    scrollX:true,
                    scrollY:false,
                    scrollCollapse:false,
                    
                    "language": {
                        "search":"Filtra per nome del ristorante:",
                        "lengthMenu": "Visualizza _MENU_ ristoranti per pagina",
                        "zeroRecords": "Non ci sono ristoranti",
                        "info": "Stai visualizzando _PAGE_ di _PAGES_",
                        "infoEmpty": "Non ci sono ristoranti",
                        "infoFiltered": "(Filtrato da _MAX_ totale ristoranti)",
                        "paginate": {
                            "previous": "Precedente",
                            "next": "Sucessivo",
                            "first":"Primo",
                            "last":"Ultimo"
                        }
                    },
                    "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "TUTTI"]],
                    "columnDefs": [{"targets": [ 0 ], "searchable": false,"visible":true},
                                    {"targets": [ 1 ], "searchable": false},
                                    {"targets": [ 2 ], "searchable": false},
                                    {"targets": [ 3 ], "searchable": true},
                                    {"targets": [ 4 ], "searchable": false},
                                    {"targets": [ 5 ], "searchable": false},
                                    {"targets":[6],"searchable":true,"visible":false}]
                } );
                $('#scelta_Cucina').change(function() {                    
                    var val=$(this).val();
                    tabella
                       .columns( 3 )
                       .search(val)
                       .draw();
                   
                      });
                $('#scelta_Prezzo').change(function() {                    
                    var val=$(this).val();
                    tabella
                       .columns( 6 )
                       .search(val)
                       .draw();
                   
                      });
            } );
	</script>
    </body>
</html>
