/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $(".acceptRestaurantAttempt").click(function(event) {
        var id_restaurant = $(this).val();
        var element = $(this).parent().parent();
        var id_user = $(this).prev().val();
        $.ajax(
        {
            url : "../eatbit/AcceptRestaurantRequestByAdminServlet",
            type: "POST",
            data : {id_restaurant:id_restaurant,id_user:id_user},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    new PNotify({
                        title: 'Accettazione',
                        text: 'Accettazione andata a BUON FINE !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                    new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a Confermare il ristorante Chiamata fallita!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });            
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });      
            }
                    });
    });
    
    
    $(".declineRestaurantAttempt").click(function(event) {
        var id_restaurant = $(this).val();
        var element = $(this).parent().parent();
        var id_user =  $("#valore").val();
        $.ajax(
        {
            url : "../eatbit/DenyRestaurantRequestByAdminServlet",
            type: "POST",
            data : {id_restaurant:id_restaurant,id_user:id_user},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    new PNotify({
                        title: 'DECLINED',
                        text: 'Richiesta DECLINATA !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                    new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a DECLINARE il ristorante - Chiamata fallita!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });               
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });   
            }
                    });
    });
    
     $(".acceptReportedPhoto").click(function(event) {
        var id_photo = document.getElementById("AcceptReportPhoto").value;
        var element1 = document.getElementById("DeclineReportPhoto");
        var element2 = document.getElementById("AcceptReportPhoto");
        
        $.ajax(
        {
            url : "../eatbit/RemovePhotoByAdminServlet",
            type: "POST",
            data : {id_photo:id_photo},
            success:function(dati)  
            {   
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element1.remove();
                    element2.remove();
                    document.getElementById("DeclineReportPhoto").remove();
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Foto RIMOSSA con Successo !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                    new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a RIMUOVERE la FOTO - Chiamata fallita!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });             
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });      
            }
                    });
    });
    
         $(".declineReportedPhoto").click(function(event) {
        var id_photo = document.getElementById("DeclineReportPhoto").value;
        var element1 = document.getElementById("DeclineReportPhoto");
        var element2 = document.getElementById("AcceptReportPhoto");
        //alert("id_photo = " + id_photo);
        $.ajax(
        {
            url : "../eatbit/UnreportPhotoByAdminServlet",
            type: "POST",
            data : {id_photo:id_photo},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element1.remove();
                    element2.remove();
                    document.getElementById("AcceptReportPhoto").remove();
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Foto tolta dall elenco delle FOTO SEGNALATE !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                    new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a DECLINARE la Segnalazione - Chiamata fallita!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });   
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });    
            }
                    });
    });
    
         $(".acceptReportedReview").click(function(event) {
        var id_review = document.getElementById("AcceptReportedReview").value;
        var element = document.getElementById("AcceptReportedReview");
        
        $.ajax(
        {
            url : "../eatbit/RemoveReviewByAdminServlet",
            type: "POST",
            data : {id_review:id_review},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    document.getElementById("DeclineReportedReview").remove();
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Review RIMOSSA con Successo !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                    new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a RIMUOVERE la REVIEW - Chiamata fallita!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });           
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });      
            }
                    });
    });
    
         $(".declineReportedReview").click(function(event) {
        var id_review = document.getElementById("DeclineReportedReview").value;
        var element = document.getElementById("DeclineReportedReview");
        
        $.ajax(
        {
            url : "../eatbit/UnreportReviewByAdminServlet",
            type: "POST",
            data : {id_review:id_review},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    document.getElementById("AcceptReportedReview").remove();
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Review tolta dall elenco delle FOTO SEGNALATE !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                     new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a DECLINARE la Segnalazione - Chiamata fallita!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });          
                }
            },
            error: function() 
            {
               new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });       
            }
                    });
    });
    
    $(".acceptReply").click(function(event) {
        var id_reply = $(this).val();
        var element = $(this).parent().parent();
        
        $.ajax(
        {
            url : "../eatbit/AcceptReplyByAdminServlet",
            type: "POST",
            data : {id_reply:id_reply},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    new PNotify({
                        title: 'AccettazioneReply',
                        text: 'La REPLY é stata aggiunta in risposta alla recensione !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                else{
                     new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti ad Accettare la Reply!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });           
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });       
            }
                    });
    });
    
    $(".declineReply").click(function(event) {
        var id_reply = $(this).val();
        var element = $(this).parent().parent();
        
        $.ajax(
        {
            url : "../eatbit/DenyReplyByAdminServlet",
            type: "POST",
            data : {id_reply:id_reply},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    new PNotify({
                        title: 'DeclineReply',
                        text: 'La REPLY é stata RIMOSSA con successo !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    }); 
                }
                else{
                    new PNotify({
                        title: 'Errore',
                        text: 'Ci dispiace ma non siamo riusciti a RIMUOVERE la Reply!!!  Codice = '+dati ,
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });            
                }
            },
            error: function() 
            {
                new PNotify({
                        title: 'ErroreServer',
                        text: 'Errore Server - Ci scusiamo per il disagio!',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });      
            }
                    });
    });
    
     });