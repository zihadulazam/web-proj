
<%@page import="database.ReviewNotification"%>
<%@page import="database.PhotoNotification" %>

<html lang="en">
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <title>Notifiche</title>
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
                
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/userProfile.css" rel="stylesheet">
        <link href="css/jquery-ui.css" rel="stylesheet">

        <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
    </head>
    <body>
        <!-- nav-bar -->
        <!--BARRA-->
        <%@include file="components/navbar-second.jsp"%>
        
        <div class="container">
            <div class="row">
                <h2>Tutte le Notifiche</h2>
                <p>Qui puoi visualizzare tutte le notifiche ancora disponibili</p>
            </div>    

                <ul class="nav nav-pills restaurant">
                    <li class="active"><a data-toggle="tab" href="#menu1">Foto<span class="badge"><c:out value="${listPhotoNotification.size()}"/></span></a></li>
                    <li><a data-toggle="tab" href="#menu2">Recensioni<span class="badge"><c:out value="${listReviewNotification.size()}"/></span></a></li>
                </ul>

                <div class="tab-content">
                    
                    <!--MENU 1-->
                    <div id="menu1" class="tab-pane fade in active">
                        <!--Foto-->
                        <br>
                         <c:choose>
                            <c:when test="${(listPhotoNotification.size()) <= 0}">
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Nessuna Notifica!                                                
                                    </div>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                <div class="row">                                                              

                                    <c:forEach items="${listPhotoNotification}" var="photoNotification">
                                        <div class="alert alert-info notice notificaFoto" role="alert">
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp;<b><c:out value="${photoNotification.getUser().getName()}" /></b> ha caricato una foto su <b><c:out value="${photoNotification.getRestaurant_name()}" /></b>
                                                </a>
                                            </div>
                                            
                                            <div class="row">
                                                <div class ="col-md-10">
                                                    <div class=" contenutoNotFoto">
                                                        <a class="thumbnail autoX" href="<c:out value="${photoNotification.getPhoto().getPath()}" />" data-lightbox="example-<c:out value="${photoNotification.getPhoto().getPath()}" />">
                                                            <img src="<c:out value="${photoNotification.getPhoto().getPath()}" />">
                                                        </a>                                                                                                                                                                                               
                                                    </div>
                                                </div>
                                                <div class ="col-md-2">                                                    
                                                </div>
                                            </div>
                                                    
                                            <div class="row">
                                                <div class ="col-md-10">
                                                    <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                    <c:out value="${photoNotification.getCreation()}"></c:out>
                                                </div>
                                                <div class ="col-md-2">   
                                                    <button  class="btn btn-primary diventaRis removePhotoNot" value="${photoNotification.getId()}">Non vedere più</button>
                                                </div>                                                        
                                            </div>
                                                    
                                            <div class="row">
                                                <div class ="col-md-10">
                                                </div>
                                                <div class ="col-md-2">   
                                                    <button  class="btn btn-primary diventaRis " value="${photoNotification.getId()}"  onclick="segnalaPhoto(${photoNotification.getPhoto().getId()})">Segnala subito</button>
                                                </div>                                                        
                                            </div>
                                        </div>
                                    </c:forEach>  
                                </div>    
                            </c:otherwise>
                        </c:choose>                          
                            
                    </div>

                    <!--MENU 2-->
                    <div id="menu2" class="tab-pane fade">
                        <!--Revisioni-->
                        <br>
                        
                        <c:choose>
                            <c:when test="${(listReviewNotification.size()) <= 0}">
                                <div class="alert alert-info notice restaurant" role="alert">
                                    <div class ="row">
                                        &nbsp; Nessuna Recensione!                                                
                                    </div>
                                </div>
                            </c:when>
                            
                            <c:otherwise>                                
                                <div class="row">                                                             

                                    <c:forEach items="${listReviewNotification}" var="reviewNotification">
                                        <div class="alert alert-info notice  not notificaRecensione" role="alert">
                                            <div class ="row">
                                                <a href="#">
                                                    &nbsp; Nuova recensione su <b><c:out value="${reviewNotification.getRestaurant_name()}" /></b>                                                   
                                                </a>                                                
                                            </div>
                                                
                                            <div class="row">
                                                <div class ="col-md-10">                                                    
                                                    <div class="panel panel-primary comm">
                                                        <div class="panel-heading">
                                                            <h3 class="panel-title"><c:out value="${reviewNotification.getUser().getName()}" /> ha commentato:</h3>
                                                        </div>
                                                        <div class="panel-body">
                                                            <c:out value="${reviewNotification.getReview().getDescription()}" />
                                                        </div>           
                                                    </div>
                                                </div>
                                                <div class ="col-md-2">                                                    
                                                </div>
                                            </div>    
                                                
                                            <div class="row">
                                                <div class ="col-md-10">
                                                    
                                                    <span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
                                                    <c:out value="${reviewNotification.getReview().getDate_creation()}"></c:out>
                                                    
                                                </div>
                                                <div class ="col-md-2">
                                                    <button  class=" right btn btn-primary diventaRis removeReviewNot" value="${reviewNotification.getId()}">Non vedere più</button>
                                                </div>
                                            </div>
                                                
                                            <div class="row">
                                                <div class ="col-md-10">
                                                </div>
                                                <div class ="col-md-2">
                                                    <button  class=" right btn btn-primary diventaRis rispRecensione" value="${reviewNotification.getId()}">Rispondi subito</button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>  
                                </div>                     
                                
                            </c:otherwise>      
                        </c:choose>
                    </div>
          
            </div>
        </div>
        
        <!-- included modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>

        <!--footer-->
        <%@include file="components/footer.html"%>
        
        <!-- UserProfile JS-->
        <script type="text/javascript" src="js/userProfile.js"></script>

        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
</body>
</html>