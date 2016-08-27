var tmp=5;
jQuery(document).ready(function ($) {
    setInterval(printSec, 1000);
});

function printSec(){
    tmp--;
    if(tmp<0){
        redirect();
    }
    else{
        $('#sec').html(tmp);
    }
}
function redirect(){
    window.location.href='../eatbit/home';
}

