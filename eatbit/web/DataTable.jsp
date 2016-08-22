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
        <div class="container contenitori-blocco" id="ristoranti-container" style="margin-top: 10px;">
            <div class="col-sm-12" style="text-align: center;">
                <label >Filtra per cucina/prezzo:</label>
            </div>
            <div class="col-sm-6">            
                <select id="scelta_Cucina" aria-controls="tabella-ristoranti" class="form-control input-sm" style="margin-bottom: 10px;">
                        <option value="">Cucina</option>                        
                        <option value="AMERICANA">AMERICANA</option>
                        <option value="ASIATICA">ASIATICA</option>
                        <option value="AFRICANA">AFRICANA</option>
                        <option value="CINESE">CINESE</option>
                        <option value="JAPONESE">JAPONESE</option>
                        <option value="GIAPPONESE">GIAPPONESE</option>
                        <option value="SUSHI">SUSHI</option>
                        <option value="INDIANA">INDIANA</option>
                        <option value="ITALIANA">ITALIANA</option>
                        <option value="PIZZA">PIZZA</option>
                        <option value="FRANCESE">FRANCESE</option>
                        <option value="SPAGNOLA">SPAGNOLA</option>
                        <option value="PESCE">PESCE</option>
                        <option value="CARNE">CARNE</option>
                        <option value="FAST-FOOD">FAST-FOOD</option>
                        <option value="VEGERATIANA">VEGERATIANA</option>
                    </select> 
            </div>
            <div class="col-sm-6"> 
                <select id="scelta_Prezzo" class="form-control input-sm" style="margin-bottom: 10px;">
                        <option value="">Prezzo</option>
                        <option value="MOLTO BASSO">Molto Basso</option>
                        <option value="BASSO">Basso</option>
                        <option value="MEDIO">Medio</option>
                        <option value="ALTO">Alto</option>
                        <option value="MOLTO ALTO">Molto Alto</option>
                    </select> 
            </div>
            <table id="tabella-ristoranti" class="display" cellspacing="0" width="100%"  >
                <thead>
                    <tr >
                        <th class="col-sm-2 sorting bg-primary"> </th>
                        <th class="col-sm-2 sorting bg-primary">Nome Ristorante</th>
                        <th class="col-sm-2 sorting bg-primary">Voto</th>
                        <th class="col-sm-2 sorting bg-primary">Tipi di cucina</th>
                        <th class="col-sm-2 sorting bg-primary">Num Recensioni</th>
                        <th class="col-sm-2 sorting bg-primary"></th>
                        <th class="col-sm-2 sorting bg-primary">Prezzo</th>
                    </tr>
                </thead>
                <tfoot></tfoot>
                <tbody >                    
                    <c:forEach var="i" items="${list}">
                        <tr >
                            <!-- Prima colonna-->
                            <td class="col-sm-2 sorting_1 " style="padding-right: 0px; padding-left: 0px;" >  
                                <div class=" container-left">
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
                            </td>
                            <!-- Seconda colonna-->
                            <td class="col-sm-2 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-center">  
                                    <div class="container-fluid">
                                        <div class="container-writer">
                                            <h3 class="container-title"><c:out value="${i.getRestaurant().getName()}"/></h3>                                            
                                            <p><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> <c:out value="${i.getCoordinate().getAddress()}"></c:out>, <c:out value="${i.getCoordinate().getCity()}"></c:out>, <c:out value="${i.getCoordinate().getState()}"></c:out> </p>                                                        
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- Terza colonna-->
                            <td class="col-sm-2 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-center">  
                                    <div class="container-fluid">
                                        <div class="container-value">
                                            
                                            <c:forEach var="j" begin="1" end="5"> <!-- Quante stelle ha il ristorante-->
                                                <c:choose>
                                                    <c:when test="${i.getRestaurant().getGlobal_value()>=j}">
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
                            </td>
                            <!-- Quarta colonna-->
                            <td class="col-sm-2 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-center">  
                                    <div class="container-fluid">
                                        <div class="container-writer">                                            
                                            <c:forEach var="k" items="${i.getCuisines()}">
                                                    <span class="label label-info"><c:out value="${k}"/></span>
                                            </c:forEach>                                            
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- Quinta colonna-->
                            <td class=" col-sm-2 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-center">  
                                    <div class="container-fluid">
                                        <div class="container-value">
                                            <p><c:out value="${i.getRestaurant().getReviews_counter()}"></c:out></p>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- Sesta colonna-->
                            <td class="col-sm-2 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-right">  
                                    <div class="container-fluid">
                                        <div class="container-value">
                                            <button type="button" class="btn btn-success"><a href="../eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${i.getRestaurant().getId()}"></c:out>"><span class="glyphicon glyphicon-eye-open"></span> Visita</a></button>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- Settima colonna-->
                            <td class="col-sm-2 sorting_1" style="padding-right: 0px; padding-left: 0px;">
                                <p><c:out value="${i.getPriceRange().getName()}"></c:out></p>
                            </td>
                        </tr>
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
