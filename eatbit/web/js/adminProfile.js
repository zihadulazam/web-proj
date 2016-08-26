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
                }
                else{
                    alert("Chiamata fallita!!!  "+dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
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
                }
                else{
                    alert("Chiamata fallita!!!  "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    
     $(".acceptReportedPhoto").click(function(event) {
        var id_photo = $(this).val();
        var element = $(this);
        
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
                    document.getElementById("DeclineReportPhoto").remove();
                }
                else{
                    alert("Chiamata fallita!!!  Codice = "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    
         $(".declineReportedPhoto").click(function(event) {
        var id_photo = $(this).val();
        var element = $(this);
        
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
                    element.remove();
                    document.getElementById("AcceptReportPhoto").remove();
                }
                else{
                    alert("Chiamata fallita!!!  Codice = "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    
         $(".acceptReportedReview").click(function(event) {
        var id_review = document.getElementById("AcceptReportedReview").value;
        var element = $(this);
        
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
                }
                else{
                    alert("Chiamata fallita!!!  Codice = "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    
         $(".declineReportedReview").click(function(event) {
        var id_review = document.getElementById("DeclineReportedReview").value;
        var element = $(this);
        
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
                }
                else{
                    alert("Chiamata fallita!!!  Codice = "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
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
                    alert("Andata a buon Fine!!!  Codice = "+ dati);   
                }
                else{
                    alert("Chiamata fallita!!!  Codice = "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
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
                    alert("Andata a buon Fine!!!  Codice = "+ dati); 
                }
                else{
                    alert("Chiamata fallita!!!  Codice = "+ dati);            
                }
            },
            error: function() 
            {
                alert("Errore Server!!!");     
            }
                    });
    });
    
     });