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
var marker2;
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 45.889214, lng: 10.84307},
        zoom: 15,
        fullscreenControl: true,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR
        }
    });
    var myLatLng = {lat: 45.889214, lng: 10.84307};
    var myLatLng2 = {lat: 45.889620, lng: 10.84507};
    var image ='img/restaurant/map-pin.png';

    marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        title: 'Restaurant Name',
        icon: image
    });
    marker.addListener('click', function() {
        toggleBounce();
    });

    marker2 = new google.maps.Marker({
        position: myLatLng2,
        map: map,
        title: 'Restaurant Name',
        icon: image
    });
    marker2.addListener('click', function() {
        toggleBounce2();
    });
}

function toggleBounce() {
  if (marker.getAnimation() !== null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}
function toggleBounce2() {
  if (marker2.getAnimation() !== null) {
    marker2.setAnimation(null);
  } else {
    marker2.setAnimation(google.maps.Animation.BOUNCE);
  }
}


//segnala IMG
$(document).ready(function() {
    $(".btn-segnala").click(function(event) {
        var imgId=event.target.id;
        $.ajax(
        {
            url : "../eatbit/ReportPhotoServlet",
            type: "POST",
            data : {imgId:imgId,utente:'Io'},
            success:function(data, textStatus, jqXHR) 
            {
                //data: return data from server
                if(data == "1"){
                    //window.location.replace("/home");
                    // inserisci data nel nav bar
                   alert("Segnalato!!!");
                   location.reload();
                } 
                else{
                    alert("Errore!!!");
                    location.reload();
                }
            },
            error: function(jqXHR, textStatus, errorThrown) 
            {
                $('.error').addClass('alert alert-danger').html("Errore Server!! riprova più tardi"+textStatus);
                    shakeModal();      
            }
        });
    });
});