//rate slider config
  jQuery(document).ready(function ($) {
    $("#valutazioneGlobaleRistoBar").slider({
        min:1,
        max:5,
        animate:"slow",
        value:1,
        slide: function( event, ui ) {
                  $("#valutazioneGlobaleRistoValue").val(ui.value);
        }
    });
    $("#valutazioneGlobaleRistoValue").val($("#valutazioneGlobaleRistoBar").slider("value"));

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
  
  //upload inputs
  $(document).ready(function(){
    $('#AddRecensione-cerca-photo').change(function(){
        $('#AddRecensione-cerca-photo-name').val($(this).val());
    });
    $('#AddFoto-cerca-photo').change(function(){
        $('#AddFoto-cerca-photo-name').val($(this).val());
    });
  });
  
  // Data Table JS
  $(document).ready(function() {
    $('#tabella-recensioni').DataTable( {
        "pagingType": "full_numbers",
        bFilter: false,
        bInfo: false,
        ordering:  false,
        "language": {
            "lengthMenu": "Visualizza _MENU_ recensioni per pagina",
            "zeroRecords": "Non ci sono recensioni",
            "info": "Sto visualizzando _PAGE_ of _PAGES_",
            "infoEmpty": "Non ci sono recensioni disponibili",
            "infoFiltered": "(Filtrato da _MAX_ totale recensioni)",
            "paginate": {
                "previous": "Precedente",
                "next": "Sucessivo",
                "first":"Primo",
                "last":"Ultimo"
            }
        },
        "lengthMenu": [[5, 10, 20, -1], [5, 10, 20, "TUTTI"]]
    } );
} );


//google map JS
var map;
var marker;
var marker2=new Array();
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: myLat, lng: myLon},
        zoom: 15,
        fullscreenControl: true,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR
        }
    });
    var myLatLng = {lat: myLat, lng: myLon};
    var image1 ='img/restaurant/map-pin-risto.png';
    var image2 ='img/restaurant/map-pin.png';

    marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        title: myRistoName,
        icon: image1
    });

    var infowindow = new google.maps.InfoWindow();
    var marker, i;
    i=0;
    google.maps.event.addListener(marker, 'click', (function(marker) {
        return function() {
          infowindow.setContent(myRistoName);
          infowindow.open(map, marker);
        }
      })(marker));

    for (i = 1; i < viciniLat.length; i++) {  
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(viciniLat[i], viciniLon[i]),
        map: map,
        icon: image2
      });

      google.maps.event.addListener(marker, 'click', (function(marker, i) {
        return function() {
          infowindow.setContent(viciniName[i]);
          infowindow.open(map, marker);
        }
      })(marker, i));
    }
}



//add restaurant vote
function addRistoVote(restaurantId){
    var vote=$('#valutazioneGlobaleRistoValue').val();
     $.ajax(
    {
        url : "../eatbit/UserVoteServlet",
        type: "GET",
        data : {id_restaurant:restaurantId, vote:vote},
        success:function(data, textStatus, jqXHR) 
        {
            if(data == "1"){
                alert("Grazie per il suo voto preziozo");
                location.reload();
            }
            if(data=="-2")
                alert("Mi dispiace, Non puoi votare il tuo ristorante !!");
            if(data=="-1" || data=="0")
                alert("Mi dispiace, votazione al momento non è disponibile");
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            alert("Mi dispiace, votazione al momento non è disponibile");
        }
    });
}

//add restaurant vote
function claimRisto(){
    var restaurantId=$('#id_rest').val();
    var text=$('#claim_description').val();
     $.ajax(
    {
        url : "../eatbit/ClaimRestaurant",
        type: "POST",
        data : {id_rest:restaurantId, text_claim:text},
        success:function(data, textStatus, jqXHR) 
        {
            if(data == "1"){
                alert("Abbiamo ricevuto la sua segnalazione, a breve riceverà un'email con l'esito.");
                location.reload();
            }
            if(data=="0")
                alert("Mi dispiace, segnalazione al momento non è disponibile !!");
            if(data=="-1")
                alert("Mi dispiace, Mancano i Parametri");
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            alert("Mi dispiace, segnalazione al momento non è disponibile");
        }
    });
}

//controllo claim_Risto
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

                // Claim validation
                $("#claim_form").validate({
                    rules:{
                         claim_text:'required'
                    },
                    messages:{
                        claim_text:'*Campo obligatorio'
                    },
                    submitHandler: function() {
                        claimRisto();
                    }
                });

                // Review validation
                $("#add_review_form").validate({
                    rules:{
                         name:'required',
                         description:'required',
                         AddRecensione_cerca_photo_name:'required',
                         photo_description:'required'
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