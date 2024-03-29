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
                        text: 'Accettazione andata a BUON FINE !',
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
                        text: 'Ci dispiace ma non siamo riusciti a Confermare il ristorante Chiamata fallita!  Codice = '+dati ,
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
                        text: 'Richiesta DECLINATA !',
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
                        text: 'Ci dispiace ma non siamo riusciti a DECLINARE il ristorante - Chiamata fallita!  Codice = '+dati ,
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
        var id_photo = $(this).val();
        var element = $(this).parent();
       
        
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
                    element.remove();
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Foto RIMOSSA con Successo !',
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
                        text: 'Ci dispiace ma non siamo riusciti a RIMUOVERE la FOTO - Chiamata fallita!  Codice = '+dati ,
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
        var id_photo = $(this).val();
        var element1 = $(this).parent();
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
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Foto RIAMESSA nel Sistema !',
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
                        text: 'Ci dispiace ma non siamo riusciti a DECLINARE la Segnalazione - Chiamata fallita!  Codice = '+dati ,
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
        var id_review = $(this).val();
        var element =$(this).parent();
        
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
                        text: 'Ci dispiace ma non siamo riusciti a RIMUOVERE la REVIEW - Chiamata fallita!  Codice = '+dati ,
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
        var id_review = $(this).val();
        var element = $(this).parent();
        
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
                    new PNotify({
                        title: 'Rimozione',
                        text: 'Review tolta dall elenco delle FOTO SEGNALATE !',
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
                        text: 'Ci dispiace ma non siamo riusciti a DECLINARE la Segnalazione - Chiamata fallita!  Codice = '+dati ,
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
                        title: 'Reply',
                        text: 'Risposta aggiunta con successo !',
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
                        text: 'Ci dispiace ma non siamo riusciti ad Accettare la Reply!  Codice = '+dati ,
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
                        text: 'La REPLY é stata RIMOSSA con successo !',
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
                        text: 'Ci dispiace ma non siamo riusciti a RIMUOVERE la Reply!  Codice = '+dati ,
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
    
    /*
    $(".cPwd").click(function(event) {
        
        formdata=$('#pswForm').serializeArray();
        $.ajax(
        {
            url : "../eatbit/SendPswVerificationEmailServlet?",
            type: "POST",
            data : formdata,
            success:function(dati)  
            {
                //data: return data from server
                if(dati == "1"){
                    //window.location.replace("/home");
                    new PNotify({
                        title: 'Cambio Password',
                        text: 'Segui il link che abbiamo inviato alla tua casella postale per cambiare la password!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                    
                }
                else{
                    new PNotify({
                        title: 'Cambio Password Fallito ',
                        text: 'Ci dispiace ma ce qualche problema con i tuoi dati. Controlla se sei Loggato',
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
                        title: 'Cambio Password Fallito ',
                        text: 'Ci dispiace ma ce qualche problema con il server centrale cercheremo di rimediare al piu presto',
                        type: 'error',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });     
            }
                    });
    });
    
    */
     });