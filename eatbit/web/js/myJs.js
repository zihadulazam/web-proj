
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
  
//mi piace review

function miPiace(reviewId,likeType){
    var btnMiPiaceId="#btn-mipiace-"+reviewId;
    var btnNonMiPiaceId="#btn-nonmipiace-"+reviewId
    $.ajax(
    {
        url : "../eatbit/UserLikeDislikeReviewServlet",
        type: "GET",
        data : {id_review:reviewId, like_type:likeType},
        success:function(data, textStatus, jqXHR) 
        {
            if(data == "1"){
                var newLikeNum=parseInt($(btnMiPiaceId).find(".badge").text());
                newLikeNum++;
                $(btnMiPiaceId).find(".badge").text(newLikeNum);
                $(btnMiPiaceId).prop("disabled",true);
            }
            if(data == "2"){
                var newLikeNum=parseInt($(btnMiPiaceId).find(".badge").text());
                newLikeNum++;
                $(btnMiPiaceId).find(".badge").text(newLikeNum);
                $(btnMiPiaceId).prop("disabled",true);
                var newUnLikeNum=parseInt($(btnNonMiPiaceId).find(".badge").text());
                newUnLikeNum--;
                $(btnNonMiPiaceId).find(".badge").text(newUnLikeNum);
                $(btnNonMiPiaceId).prop("disabled",false);
            }
            if(data=="-2")
            {
                $(btnMiPiaceId).prop("disabled",true);
                $(btnNonMiPiaceId).prop("disabled",true);
                alert("Non puoi votare i tuoi review !!");
            }
            if(data=="-1")
                alert("Mi dispiace, votazione al momento non è disponibile");
                
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            alert("Mi dispiace, votazione al momento non è disponibile");
        }
    });
}

function nonMiPiace(reviewId,likeType){
    var btnMiPiaceId="#btn-mipiace-"+reviewId;
    var btnNonMiPiaceId="#btn-nonmipiace-"+reviewId
    $.ajax(
    {
        url : "../eatbit/UserLikeDislikeReviewServlet",
        type: "GET",
        data : {id_review:reviewId, like_type:likeType},
        success:function(data, textStatus, jqXHR) 
        {
            data=-2;
            if(data == "1"){
                var newUnLikeNum=parseInt($(btnNonMiPiaceId).find(".badge").text());
                newUnLikeNum++;
                $(btnNonMiPiaceId).find(".badge").text(newUnLikeNum);
                $(btnNonMiPiaceId).prop("disabled",true);
            }
            if(data == "2"){
                var newUnLikeNum=parseInt($(btnNonMiPiaceId).find(".badge").text());
                newUnLikeNum++;
                $(btnNonMiPiaceId).find(".badge").text(newUnLikeNum);
                $(btnNonMiPiaceId).prop("disabled",true);
                var newLikeNum=parseInt($(btnMiPiaceId).find(".badge").text());
                newLikeNum--;
                $(btnMiPiaceId).find(".badge").text(newLikeNum);
                $(btnMiPiaceId).prop("disabled",false);
            }
            if(data=="-2")
            {
                $(btnMiPiaceId).prop("disabled",true);
                $(btnNonMiPiaceId).prop("disabled",true);
                alert("Non puoi votare i tuoi review !!");
            }
            if(data=="-1")
                alert("Mi dispiace, votazione al momento non è disponibile");
                
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            alert("Mi dispiace, votazione al momento non è disponibile");
        }
    });
}    
  