$(document).ready(function() {
    
    $(".cPwd").click(function(event) {
        
        var id_user = $(this).val();
        
        $.ajax(
        {
            url : "../eatbit/SendPswVerificationEmailServlet",
            type: "POST",
            data : {id_user:id_user},
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
    
    
    $(".removePhotoNot").click(function(event) {
        var notifyId = $(this).val();
        var element = $(this).parent();
        $.ajax(
        {
            url : "../eatbit/RemovePhotoNotificationFromUserServlet",
            type: "POST",
            data : {notifyId:notifyId},
            success:function(dati)  
            {
                //data: return data from server
                if(dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                    
                }
                else{
                    alert("Chiamata fallita!!!");            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });

    $(".removeReviewNot").click(function(event) {
        var notifyId = $(this).val();
        var element = $(this);
        $.ajax(
        {
            url : "../eatbit/RemoveReviewNotificationFromUserServlet",
            type: "POST",
            data : {notifyId:notifyId},
            success:function(dati)  
            {
                //data: return data from server
                if(dati == "1"){
                    //window.location.replace("/home");
                    element.remove();
                }
                else{
                    alert("Chiamata fallita!!!");            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    

});