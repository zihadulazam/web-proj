$(document).ready(function() {
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
                if(dati = "1"){
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
                if(dati = "1"){
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