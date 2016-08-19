
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

(function($,W,D)
    {
        var JQUERY4U = {};

        JQUERY4U.UTIL =
        {
            setupFormValidation: function()
            {
                //set error action
               $.validator.setDefaults({
                    errorElement: 'div',
                    errorClass: 'help-block',
                    highlight: function(element) {
                    $(element)
                        .closest('.form-group')
                        .addClass('has-error');
                    },
                    unhighlight: function(element) {
                    $(element)
                        .closest('.form-group')
                        .removeClass('has-error');
                    },
                    errorPlacement: function (error, element) {
                        return true;
                    }
                });

                // search validation
                $("#search-form").validate({
                    rules:{
                        luogo:{
                             required:function(element) {
                                    return $('#nomeRisto').val() == '';
                            }
                        },
                        name:{
                             required:function(element) {
                                    return $('#locationRisto').val() == '';
                            }
                        }
                    },
                    messages:{
                        luogo:'*Campo obligatorio',
                        name:'*Campo obligatorio'
                    },
                    submitHandler: function(form) {
                        form.submit();
                    }
                });
            }
        }
        
        //dopo il document load
        $(D).ready(function($) {
            JQUERY4U.UTIL.setupFormValidation();
        });

})(jQuery, window, document);

//chiama login modal se non è loggato
function primaFaiLogin(){
    showLoginForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
    $('.error').addClass('alert alert-danger').html("Prima devi fare il Login");
                shakeModal();
}