

$(document).ready(function() {
    $(".go_reply").click(function(event) {
        var id_review = document.getElementById("id_review").value;
        var description = document.getElementById("reply_text").value;
        alert(id_review + " " + description);
        
        
        $.ajax(
        {
            url : "../eatbit/AddReplyServlet",
            type: "POST",
            data : {id_review:id_review,description:description},
            success:function(dati)  
            {
                //data: return data from server
                if (dati == "1"){
                    //window.location.replace("/home");
                    alert("Risposta pubblicata!!!  Codice: "+dati);  
                }else if(dati == "0"){
                    alert("Errore Server!!! - Exception thrown!!  Codice: "+dati);        
                }else if(dati == "-1"){
                    alert("Manca la descrizione!!!!  Codice: "+dati);        
                }else if(dati == "-2"){
                    alert("Non ti é permesso rispondere a questo commento!!!   Codice: "+dati);        
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    
    
});