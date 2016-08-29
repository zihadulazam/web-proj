//PNotify opz
PNotify.prototype.options.styling = "bootstrap3";
var stack_modal = {"dir1": "down", "dir2": "right", "push": "top", "modal": true, "overlay_close": true};
//autocomplete
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
                if(data == "1"){
                     new PNotify({
                        title: 'Ok',
                        text: 'Grazie per la segnalazione !!',
                        type: 'success',
                        mobile: {
                            swipe_dismiss: true,
                            styling: true
                        }
                    });
                }
                    
            },
            error: function(jqXHR, textStatus, errorThrown) 
            {
                new PNotify({
                    title: 'Mi dispiace',
                    text: 'segnalazione non è disponibile !!',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
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
            new PNotify({
                    title: 'Ok',
                    text: 'Grazie per la segnalazione !',
                    type: 'success',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
                
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            new PNotify({
                    title: 'Mi dispiace',
                    text: 'segnalazione non disponibile !',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
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
                new PNotify({
                    title: 'Mi dispiace',
                    text: 'Hai già votato questa recensione oppure stai votando una delle tue recensioni !',
                    type: 'notice',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
            }
            if(data=="-1"){
                new PNotify({
                    title: 'Mi dispiace',
                    text: 'Votazione al momento non è disponibile !',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
            }
            if(data=="0"){
                new PNotify({
                    title: 'Mi dispiace',
                    text: 'Votazione al momento non è disponibile !',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
            }
                
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            new PNotify({
                    title: 'Mi dispiace',
                    text: 'Votazione al momento non è disponibile !',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
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
                new PNotify({
                    title: 'Mi dispiace',
                    text: 'Hai già votato questa recensione oppure stai votando una delle tue recensioni !',
                    type: 'notice',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
            }
            if(data=="-1"){
                new PNotify({
                    title: 'Mi dispiace',
                    text: 'Votazione al momento non è disponibile !!',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
            }
                
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            new PNotify({
                    title: 'Mi dispiace',
                    text: 'Votazione al momento non è disponibile !',
                    type: 'error',
                    mobile: {
                        swipe_dismiss: true,
                        styling: true
                    }
                });
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
                        latitude:{
                            required: function(element) {
                                    if($('#nomeRisto').val() == '' && $('#locationRisto').val() == '')
                                        return true;
                                    else 
                                        return false;
                            }
                        },
                        luogo:{
                             required:function(element) {
                                    if($('#nomeRisto').val() == '' && $("#lat").val() == '')
                                        return true;
                                    else 
                                        return false;
                            }
                        },
                        name:{
                             required:function(element) {
                                    if($('#locationRisto').val() == '' && $("#lat").val() == '')
                                        return true;
                                    else 
                                        return false;
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

                // footer: invia email validation
                $("#send-email-form").validate({
                    rules:{
                        email:{
                            required:true,
                            email:true
                        },
                        text:'required'
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

$('[data-toggle=popover]').popover({
    html: true
}).mouseover(function() {
    $(this).popover('show');
});

$('[data-toggle=popover]').mouseout(function() {
    $(this).popover('hide');
});

jQuery(document).ready(function() {
  caricaNumNotifica();
});
function caricaNumNotifica(){
    if($('#btn-notify').length!=0){ 
        $.ajax(
        {
            url : "../eatbit/NotificationCount",
            type: "GET",
            success:function(data, textStatus, jqXHR) 
            {
                 if(parseInt(data)>0){
                     $('#ntfy-badge').html(data);
                     $("#ntfy-badge").css("display", "block");
                 }
                 else{
                     $("#ntfy-badge").css("display", "none");
                 }
            }
        });
    }
}


