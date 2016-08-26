<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        
 	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

	<title>Eatbit | Search</title>
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/datatable.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">

        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        <script>
            
        </script>
    </head>
    <body>
        
       <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        
        <!-- Main Content --> 
        <c:choose>
            <c:when test="${list.isEmpty()}">
                <div class="container" id="main" >
                <div id="last-update">
                    <div id="info-box">
                        <h1 align="justify">
                            <strong>Non sono stati trovati risultati per la tua ricerca!</strong> Sembra che non ci siano ristoranti nel posto che stavi cercando
                                o con quel nome o cucina, ti invitiamo a riprovare.<br>
                                Ricordati che nella <strong><a href="home">Home</a></strong> è possibile trovare i migliori ristoranti per voto o numero di recensioni, se sei in cerca di ispirazione.<br>
                                Suggerimenti di cucine:
                                <ul>
                                  <li>asiatica</li>
                                  <li>carne</li>
                                  <li>cinese</li>
                                  <li>fast-food</li>
                                  <li>francese</li>
                                  <li>giapponese</li>
                                  <li>indiana</li>
                                  <li>italiana</li>
                                  <li>pesce</li>
                                  <li>pizza</li>
                                  <li>spagnola</li>
                                  <li>vegetariana</li>
                                </ul> 
                           </h1>
                            <h2 align="justify">
                    </div>
                </div>
            </div>
            </c:when>
        <c:otherwise>
        <div class="container contenitori-blocco">
            <div class="row">
                <div class="col-md-12 text-right">
                        <button type="button" class="btn btn-default btn-config" data-toggle="collapse" data-target="#collapseAddClaim" aria-expanded="false" aria-controls="collapseAddClaim"><span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span> Filtri
                        </button>
                </div>
            </div>
            <div class="collapse" id="collapseAddClaim">
                <div class="well">
                    <h4>Tipo di cucina:</h4>
                    <div class="row">
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="ASIATICA"> Asiatica
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="CARNE" > Carne
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="CINESE" > Cinese
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="FAST-FOOD" > Fast-food
                        </div>
                        </div>
                    <div class="row">
                        <div class="col-sm-3 col-md-3 col-xs-6">          
                        <input type="checkbox" name="cucina" value="FRANCESE" > Francese
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="GIAPPONESE" > Giapponese
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="INDIANA" > Indiana
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="ITALIANA" > Italiana
                        </div>
                    </div>
                    <div class="row" width="100%">
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="PESCE" > Pesce
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="PIZZA" > Pizza
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="SPAGNOLA" > Spagnola
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6"> 
                        <input type="checkbox" name="cucina" value="SUSHI" > Vegetariana
                        </div>
                    </div>
                    <hr/>
                    <h4>Prezzo:</h4>
                    <div class="row">
                        <div class="col-sm-6 col-md-8 col-xs-12"> 
                            <input id="prezzo" type="radio" name="prezzo" value="BASSO" > Basso
                            <input id="prezzo" type="radio" name="prezzo" value="MEDIO" > Medio
                            <input id="prezzo" type="radio" name="prezzo" value="ALTO" > Alto
                            <input id="prezzo" type="radio" name="prezzo" value="DI LUSSO" > Di Lusso
                        </div>
                        <div class="col-sm-6 col-md-4 col-xs-12 text-center"> 
                            
                            <button id="filtra" type="button" class="btn btn-success btn-ordina-per"><span class="glyphicon glyphicon-filter" aria-hidden="true"></span> Filtra</button>
                            <button id="reset" type="button" class="btn btn-info btn-ordina-per"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span> Reset</button>
                        </div>
                    </div>
                </div>
            </div>
            <h4>Ordina per:</h4>
             <div id="ordina-per">
                 <div class="row">
                     <div class="col-sm-8 col-md-8 col-xs-12 text-center btn-ordina-per">
                         <div class="row">
                            <div class="col-sm-6 col-md-3 col-xs-12 text-center btn-ordina-per">     
                                <button id="ORDINAASC" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-sort-by-attributes" aria-hidden="true"></span> Prezzo Crescente</button>
                            </div>
                            <div class="col-sm-6 col-md-3 col-xs-12 text-center btn-ordina-per">     
                                <button id="ORDINADESC" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-sort-by-attributes-alt" aria-hidden="true"></span> Prezzo Derescente</button>
                            </div>
                            <div class="col-sm-6 col-md-3 col-xs-12 text-center btn-ordina-per">     
                                <button id="ORDERVOTE" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-star" aria-hidden="true"></span> Miglior Voto</button>
                            </div>
                            <div class="col-sm-6 col-md-3 col-xs-12 text-center btn-ordina-per">     
                                <button id="ALPHA" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-sort-by-alphabet" aria-hidden="true"></span> Alfabetico</button>
                            </div>
                        </div>
                     </div>
                     <div class="col-sm-4 col-md-4 col-xs-12 text-center btn-ordina-per"></div>
                 </div>
             </div>
            <table id="tabella-ristoranti" class="col-sm-12" >
                <tbody >                    
                    <c:forEach var="i" items="${list}">
                        <tr >
                            <!-- Prima colonna-->
                            <td class="col-md-3 col-sm-3 col-xs-12 text-center sorting_1 comment" style="padding-right: 0px; padding-left: 0px;" >
                                <c:choose>
                                    <c:when test="${i.getPhotos().size()>0}">
                                        <img class="prof-img" src="<c:out value="${i.getPhotos().get(0).getPath()}"/>"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img class="prof-img" src="img/restaurant-default.png" class="img-circle"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <!-- Seconda colonna-->
                            <td class="col-md-3 col-sm-3 col-xs-12 text-center sorting_1 comment" style="padding-right: 0px;padding-left: 0px;" >
                                <a class="link-nome" href="../eatbit/GetRestaurantContextForwardToJspServlet?id_restaurant=<c:out value="${i.getRestaurant().getId()}"/>"><h4 class="container-title"><c:out value="${i.getRestaurant().getName()}"/></h4></a>                                            
                                <h5><strong>Recensioni: </strong><c:out value="${i.getRestaurant().getReviews_counter()}"/></h5>
                                <h5><strong>Posizione: </strong><c:out value="${i.getCityPosition()}"/></h5>
                                <div class="rating-stars text-center">
                                    <c:forEach var="j" begin="1" end="5"> <!-- Quante stelle ha il ristorante-->
                                        <c:choose>
                                            <c:when test="${i.getRestaurant().getGlobal_value()>=j}">
                                                <img src="img/star-full.png" style="max-width:20px; max-height:20px;border:0;"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="img/star-empty.png" style="max-width:20px; max-height:20px;border:0;"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div> 
                            </td>
                            <!-- terza colonna-->
                            <td class="col-md-3 col-sm-3 col-xs-12 text-center sorting_1 comment" style="padding-right: 0px;padding-left: 0px;" >
                                    <h5><c:out value="${i.getCoordinate().getAddress()}"/></h5>
                                    <h5><strong>Prezzo: </strong><c:out value="${i.getPriceRange().getName()}"/></h5>
                            </td>
                            <!-- quarta colonna-->
                            <td class="col-md-3 col-sm-3 col-xs-12 text-center sorting_1 comment" style="padding-right: 0px;padding-left: 0px;">                                          
                                    <div class="cucine">                                            
                                        <c:forEach var="k" items="${i.getCuisines()}">
                                            <p><span class="label label-info"><c:out value="${k}"/></span></p>
                                        </c:forEach>
                                    </div>
                            </td>
                            <!-- QUINTA, contiene NOME prezzo per ricerca-->
                            <td class="col-sm-1 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-right">  
                                    <div class="container-fluid">
                                        <div class="container-writer">  
                                            <c:out value="${i.getPriceRange().getName()}"/>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- SESTA, contiene valore prezzo per ricerca-->
                            <td class="col-sm-1 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-right">  
                                    <div class="container-fluid">
                                        <div class="container-writer">  
                                            <c:out value="${i.getPriceRange().getMax()}"/>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- SETTIMA, contiene VOTO GLOBALE per ricerca-->
                            <td class="col-sm-1 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-right">  
                                    <div class="container-fluid">
                                        <div class="container-writer">  
                                            <c:out value="${i.getRestaurant().getGlobal_value()}"/>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <!-- OTTAVA, contiene NOME per ricerca-->
                            <td class="col-sm-1 sorting_1" style="padding-right: 0px;padding-left: 0px;">
                                <div class="container-right">  
                                    <div class="container-fluid">
                                        <div class="container-writer">  
                                            <c:out value="${i.getRestaurant().getName().toUpperCase()}"/>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
           </c:otherwise>
        </c:choose> 
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
                var tabella= $('#tabella-ristoranti').dataTable( {
                    "pagingType": "full_numbers",
                    bFilter: true,
                    bInfo: false,
                    ordering:true,
                    order: [[ 2, "desc" ]], 
                    scrollX:false,
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
                                    {"targets": [ 2 ], "searchable": false
                <%
String ua=request.getHeader("User-Agent").toLowerCase();
if(ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {

%>,"visible":false <%}%>                 
                
                
                },
                                    {"targets": [ 3 ], "searchable": true},
                                    {"targets": [ 4 ], "searchable": true,"visible":false},
                                    {"targets": [ 5 ], "searchable": true,"visible":false},
                                    {"targets": [ 6 ], "searchable": true,"visible":false},
                                    {"targets": [ 7 ], "searchable": true,"visible":false}]
                } );
                
                $('#filtra').click(function(){
                var ele = document.getElementsByName("cucina");
                var prezzo = $('input[name=prezzo]:checked').val();
                var cucine = '';
                 for(var i=0;i<ele.length;i++)
                          if(ele[i].checked)
                              cucine = (cucine!=='') ? cucine+'|'+ele[i].value : ele[i].value;
                 tabella.fnFilter(cucine, 3, true, false, true, true);
                 if (!(typeof prezzo === "undefined"))
                     tabella.fnFilter(prezzo,4,true,false,true,true);
           });
                $('#ORDINAASC').click(function(){
                    tabella.fnSort([ [5,'asc']] );
                });  
                
                $('#ORDINADESC').click(function(){
                    tabella.fnSort([ [5,'desc']] );
                });  
                
                $('#ORDERVOTE').click(function(){
                    tabella.fnSort([ [6,'desc']] );
                });
                
                 $('#ALPHA').click(function(){
                    tabella.fnSort([ [7,'asc']] );
                });
                $('#reset').click(function(){
                    $('input:checkbox').removeAttr('checked');
                    $('input[name=prezzo]').attr('checked',false);
                    tabella.fnFilter("",3);
                    tabella.fnFilter("",4);
                });  
                
                
} );
                
            
                
	</script>
    </body>
</html>
