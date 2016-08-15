<%-- 
    Document   : CreateRestaurant
    Created on : 14-ago-2016, 19.13.22
    Author     : Andrea
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
        <title>eatBit | Inserisci nuovo ristorante</title>
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
        
    </head>
    <body>
        <!-- nav-bar -->
        <div class="navbar navbar-default navbar-fixed-top">
            <div class="container-fluid">
                
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <a class="navbar-brand navbar-extra-width" href="#">
                        <img class="eatbit-logo" src="img/eatbit.png">
                    </a>

                </div>
            </div>
        </div>
        <div class="jumbotron">
            <div class="container-fluid">
                <div class="horizontally-centered">
                    <div class="jumbo-text-container">
        
            <div class="input-thumbnail thumbnail">
                <form role="form">
                            <div class="jumbo-textbox-container form-group horizontally-centered">
                                    <br>
                                    <div class="form-group">
                                        <label for="restaurant">Nome del ristorante:</label>
                                        <input id="name" class="form-control" type="text" placeholder="Nome ristorante" name="restaurant">
                                    </div>
                                    <div class="form-group">
                                        <label for="restaurant">Descrizione:</label> <br>
                                        <textarea rows="4" cols="95" style="color:black"> </textarea>
                                    </div>
                                    <div class="form-group">
                                        <label for="city">Sito web:</label>                                          
                                        <input id="website" class="form-control" type="text" placeholder="Sito Web" name="website">
                                    </div>
                                    <div class="form-group">
                                        <label for="city">Città:</label>                                          
                                        <input id="surname" class="form-control" type="text" placeholder="Città" name="city">
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="province">Provincia:</label> 
                                        <input id="nickname" class="form-control" type="text" placeholder="Provincia" name="province">
                                    </div>
           
                                    <div class="form-group">
                                        <label for="address">Indirizzo:</label> 
                                        <input id="email" class="form-control" type="text" placeholder="Indirizzo" name="address">
                                    </div>
                                    <div class="form-group">
                                    <label for="image">Inserisci immagini:</label>
                                    <input type="file" onchange="previewFile()"><br>
                                    </div>
                                    <div class="form-group">
                                    <label for="image">Tipo di cucina:</label>
                                    <fieldset>
                                    Cinese <input type="checkbox" name="chinese" value="chinese" style="width: 50%">
                                    Thailandese <input type="checkbox" name="thai" value="thai">
                                    <br>
                                    Italiano <input type="checkbox" name="italian" value="italian" style="width: 50%">
                                    Messicano <input type="checkbox" name="mexican" value="mexican">
                                    <br>
                                    Brasiliano <input type="checkbox" name="brazilian" value="brazilian" style="width: 50%">
                                    Giapponese <input type="checkbox" name="japanese" value="japanese">
                                    </fieldset>
                                    </div>
                                    <div class="form-group">
                                    <label for="image">Range di prezzo:</label>
                                    <center><table>
                           
                                        <tr><td>
                                                <input id="fromprice" class="form-control" type="text" placeholder="Da" name="fromprice" style=""> </td>
                                            <td><br></td>
                                            <td><input id="toprice" class="form-control" type="text" placeholder="A" name="toprice"> </td>
                                    </tr>
                                        
                                        </table></center>
                                    </div>
                                    <label for="image">Orari di apertura:</label>
                                    <center>
                                    <table>
                                        
                                            <tr><td><label for="image">Lunedì:</label></td></tr>
                                        <tr><td>
                                                <input id="fromlun" class="form-control" type="text" placeholder="Dalle" name="fromlun"> </td><td><br></td>
                                            <td><input id="tolun" class="form-control" type="text" placeholder="Alle" name="tolun"> </td>
                                    </tr>
                                    <tr><td><br><label for="image">Martedì:</label></td></tr>
                                    <tr><td>
                                        
                                                <input id="frommar" class="form-control" type="text" placeholder="Dalle" name="frommar"> </td><td><br></td>
                                            <td><input id="tomar" class="form-control" type="text" placeholder="Alle" name="tomar" </td>
                                    </tr>
                                    <tr><td><br><label for="image">Mercoledì:</label></td></tr>
                                    <tr><td>
                                            <input id="frommer" class="form-control" type="text" placeholder="Dalle" name="frommer"> </td><td><br></td>
                                            <td><input id="tomer" class="form-control" type="text" placeholder="Alle" name="tomer"> </td>
                                    </tr>
                                    <tr><td><br><label for="image">Giovedì:</label></td></tr>
                                    <tr><td>
                                                <input id="fromgiove" class="form-control" type="text" placeholder="Dalle" name="fromgiove"> </td><td><br></td>
                                            <td><input id="togiove" class="form-control" type="text" placeholder="Alle" name="togiove"> </td>
                                    </tr>
                                    <tr><td><br><label for="image">Venerdì:</label></td></tr>
                                    <tr><td>
                                                <input id="fromvene" class="form-control" type="text" placeholder="Dalle" name="fromvene"> </td><td><br></td>
                                            <td><input id="tovene" class="form-control" type="text" placeholder="Alle" name="tovene"> </td>
                                    </tr>
                                    <tr><td><br><label for="image">Sabato:</label></td></tr>
                                    <tr><td>
                                                <input id="fromsaba" class="form-control" type="text" placeholder="Dalle" name="fromsaba"> </td><td><br></td>
                                            <td><input id="tosaba" class="form-control" type="text" placeholder="Alle" name="tosaba"> </td>
                                    </tr>
                                    <tr><td><br><label for="image">Domenica:</label></td></tr>
                                    <tr><td>
                                                <input id="fromdome" class="form-control" type="text" placeholder="Dalle" name="fromdome"> </td><td><br></td>
                                            <td><input id="todome" class="form-control" type="text" placeholder="Alle" name="todome"> </td>
                                    </tr>    
                                    </table></center>                                    
                                    <br>
                                    <div class="form-group">
                                    <fieldset>
                                    <input type="checkbox" name="owner" value="owner"> Sono il proprietario
                                    </fieldset>
                                    </div>
                                    </div>
                                    <button class="btn btn-default btn-register" type="submit" name="commit" aria-label="Left Align">
                                        <span class="glyphicon glyphicon glyphicon glyphicon-check" aria-hidden="true"></span>
                                        Registra ristorante
                                    </button>
            </div></div></div></div></div></form></div></div></div></div>
</body>
</html>
