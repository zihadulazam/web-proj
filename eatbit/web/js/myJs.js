
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
  
  
  //rate slider config
  jQuery(document).ready(function ($) {
    $("#valutazioneGlobaleBar").slider({
        min:1,
        max:5,
        animate:"slow",
        value:1,
        slide: function( event, ui ) {
                  $("#valutazioneGlobaleValue").val(ui.value);
        }
    });
    $("#valutazioneGlobaleValue").val($("#valutazioneGlobaleBar").slider("value"));
    
    $("#ciboBar").slider({
        min:1,
        max:5,
        animate:"slow",
        value:1,
        slide: function( event, ui ) {
                  $("#ciboValue").val(ui.value);
        }
    });
    $("#ciboValue").val($("#ciboBar").slider("value"));
    
    $("#servizioBar").slider({
        min:1,
        max:5,
        animate:"slow",
        value:1,
        slide: function( event, ui ) {
                  $("#servizioValue").val(ui.value);
        }
    });
    $("#servizioValue").val($("#servizioBar").slider("value"));
    
     $("#atmosferaBar").slider({
        min:1,
        max:5,
        animate:"slow",
        value:1,
        slide: function( event, ui ) {
                  $("#atmosferaValue").val(ui.value);
        }
    });
    $("#atmosferaValue").val($("#atmosferaBar").slider("value"));
    
    $("#prezzoBar").slider({
        min:1,
        max:5,
        animate:"slow",
        value:1,
        slide: function( event, ui ) {
                  $("#prezzoValue").val(ui.value);
        }
    });
    $("#prezzoValue").val($("#prezzoBar").slider("value"));
  });