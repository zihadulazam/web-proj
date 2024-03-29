
  //vicino a me checkbox
  jQuery(document).ready(function ($) {
      $("#vicino_a_me").change(function() {
            if(this.checked) {
                $("#locationRisto").val('');
                getLocation();
            }
            else{
                $("#locationRisto").attr("disabled", false);
                $("#locationRisto").fadeIn('slow');
                $("#error-msg").css("display", "none");
                $("#success-msg").css("display", "none");
                $("#lat").val('');
                $("#lon").val('');
            }
        });
  });

  //geolocalizzazione
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition,error);
    } else {
        $("#error-msg").text("Geolocation non supportato da questo browser.");
        $("#success-msg").css("display", "none");
        $("#error-msg").css("display", "block");
        $("#vicino_a_me").attr("disabled", true);
        $("#vicino_a_me").prop('checked', false);
    }
}

function showPosition(position) {
    $("#success-msg").text("Rilevato la tua posizione Attuale");
    $("#lat").val(position.coords.latitude);
    $("#lon").val(position.coords.longitude);
    $("#error-msg").css("display", "none"); 
    $("#locationRisto").attr("disabled", true);
    $("#locationRisto").fadeOut(600,function(){$("#success-msg").css("display", "block");});
}
function error(err){
    if(err.code == 1) {
        $("#error-msg").text("L'utente non ha autorizzato la geolocalizzazione");
        $("#success-msg").css("display", "none");
        $("#error-msg").css("display", "block");
    }
    else if(err.code == 2) 
    {
        $("#error-msg").text("Posizione non disponibile");
        $("#success-msg").css("display", "none");
        $("#error-msg").css("display", "block");
    } 
    else if(err.code == 3) 
    {
        $("#error-msg").text("Timeout");
        $("#success-msg").css("display", "none");
        $("#error-msg").css("display", "block");
    } else {
        var errormsg="ERRORE:" + err.message;
        $("#error-msg").text(errormsg);
        $("#success-msg").css("display", "none");
        $("#error-msg").css("display", "block");       
    }
    $("#vicino_a_me").attr("disabled", true);
    $("#vicino_a_me").prop('checked', false);
}