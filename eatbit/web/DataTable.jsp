<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        
 	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

	<title>Eatbit | Search</title>
        
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
        <div class="container contenitori-blocco" id="ristoranti-container" style="margin-top: 10px">
            <table id="tabella-ristoranti" class="display" cellspacing="0" width="100%" >
                <thead>
                    <th></th>
                </thead>
                <tfoot></tfoot>
                <tbody>
                    <c:forEach var="i" items="${list}">
                        <tr>
                            <td>
                                <div class="container">
                                    <div class="container-fluid">
                                        <div class="row container-fluid">
                                            <div class="col-md-4 container-writer">
                                                <img src="img/restaurant-default.png" class="img-circle"/>
                                            </div>
                                            <div class="col-md-4 container-content">
                                                <h3 class="container-title"><c:out value="${i.getName()}"/></h3><!-- Nome ristorante-->
                                                <div class="row rating-stars">
                                                                <c:forEach var="j" begin="1" end="5"> <!-- Quante stelle ha il ristorante-->
                                                                    <c:choose>
                                                                        <c:when test="${i.getGlobal_value()>=j}">
                                                                            <img src="img/star-full.png"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img src="img/star-empty.png"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                </div>
                                                <p><c:out value="${i.getReviews_counter()}"></c:out> recensioni</p>
                                                <p><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> <c:out value="${i.getId()}"></c:out>, Ala (TN), Italia</p>
                                                <p><span class="glyphicon glyphicon-phone" aria-hidden="true"></span> 3492106738</p>
                                                <p><span class="glyphicon glyphicon-globe" aria-hidden="true"></span><a href="<c:out value="${i.getWeb_site_url()}"></c:out>" target="_blank"> Sito Web </a></p>                                         
                                            </div>
                                                <div class="col-md-15 container-content">
                                                    <div class="btn-visita">
                                                        <button type="button" class="btn btn-success"><a href="http://localhost:8080/eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${i.getId()}"></c:out>"> Visita</a></button>
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
                $('#tabella-ristoranti').DataTable( {
                    "pagingType": "full_numbers",
                    bFilter: true,
                    bInfo: false,
                    ordering:  false,
                    "language": {
                        "search":"Filtra per tipo di cucina:",
                        "lengthMenu": "Visualizza _MENU_ ristoranti per pagina",
                        "zeroRecords": "Non ci sono ristoranti",
                        "info": "Sto visualizzando _PAGE_ of _PAGES_",
                        "infoEmpty": "Non ci sono ristoranti",
                        "infoFiltered": "(Filtrato da _MAX_ totale ristoranti)",
                        "paginate": {
                            "previous": "Precedente",
                            "next": "Sucessivo",
                            "first":"Primo",
                            "last":"Ultimo"
                        }
                    },
                    "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "TUTTI"]]
                } );
            } );
	</script>
    </body>
</html>
