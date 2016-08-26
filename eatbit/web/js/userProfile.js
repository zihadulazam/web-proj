$(document).ready(function() {
    $(".cPwd").click(function(event) {
        alert("Segui il link che abbiamo inviato alla tua casella postale per modificare la password");
    });
    
    
    $(".removePhotoNot").click(function(event) {
        var notifyId = $(this).val();
        var element = $(this);
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
                    document.getElementById("SegnalaSubito").remove();
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