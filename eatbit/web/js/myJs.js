
$(function() {
    $( "#nomeRisto" ).autocomplete({
        max:1,
        source:function(request,response){
            $.ajax({
                type:"GET",
                url:"../eatbit/NameAutocompleteServlet",
                data:{keys:request.term},
                success: function(data) {
                    response($.map( data, function( item ) {
                        return {
                            value: item
                        }
                    }));
                },
                dataType: "json"//set to JSON  
            });   
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';}
        //else { Return your label here }
        }
    });
    $( "#locationRisto" ).autocomplete({
        max:1,
        source:function(request,response){
            $.ajax({
                type:"GET",
                url:"../eatbit/LocationAutoCompleteServlet",
                data:{keys:request.term},
                success: function(data) {
                    response($.map( data, function( item ) {
                        return {
                            value: item
                        }
                    }));
                },
                dataType: "json"//set to JSON  
            });   
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';}
        //else { Return your label here }
        }
    }); 
  });
  
  
//segnala Review
function segnalaReview(reviewId){
    $.ajax(
        {
            url : "../eatbit/ReportReviewServlet",
            type: "GET",
            data : {id_review:reviewId},
            success:function(data, textStatus, jqXHR) 
            {
                if(data == "1")
                    alert("Grazie per la segnalazione :)");
                    
            },
            error: function(jqXHR, textStatus, errorThrown) 
            {
                alert("Mi dispiace, segnalazione non è disponibile");
            }
        });
}

function segnalaPhoto(photoId){
    $.ajax(
    {
        url : "../eatbit/ReportPhotoServlet",
        type: "GET",
        data : {id_photo:photoId},
        success:function(data, textStatus, jqXHR) 
        {
            if(data == "1")
                alert("Grazie per la segnalazione :)");
                
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            alert("Mi dispiace, segnalazione non è disponibile");
        }
    });
}
  
  
  