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
                    <c:forEach var="i" begin="1" end="7">
                        <tr>
                            <td>
                                <div class="container">
                                    <div class="container-fluid">
                                        <div class="row container-fluid">
                                            <div class="col-md-4 container-writer">
                                                <img src="img/restaurant-default.png" class="img-circle"/>
                                            </div>
                                            <div class="col-md-4 container-content">
                                                <h3 class="container-title">Titolo Ristorante <c:out value="${i}"/></h3>
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
                                            </div>
                                                <div class="col-md-15 container-content">
                                                    <div class="btn-visita">
                                                        <button type="button" class="btn btn-success"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Visita</button>
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
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">
        
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/responsive/2.1.0/js/dataTables.responsive.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/responsive/2.1.0/js/responsive.bootstrap.min.js">
	</script>
	
	<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
        
        <script type="text/javascript" language="javascript" class="init">
              $(document).ready(function() {
                $('#tabella-ristoranti').DataTable( {
                    "pagingType": "full_numbers",
                    bFilter: false,
                    bInfo: false,
                    ordering:  false,
                    "language": {
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
