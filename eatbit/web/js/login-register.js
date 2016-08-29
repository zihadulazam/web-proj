/*
 *
 * login-register modal
 * Autor: Creative Tim
 * Web-autor: creative.tim
 * Web script: http://creative-tim.com
 * 
 */
function showRegisterForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('Registrazione');
    });
    $('.error').removeClass('alert-success');   
    $('.error').removeClass('alert alert-danger').html('');
       
}
function showLoginForm(){
    $('#loginModal .registerBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        
        $('.modal-title').html('Login');
    });
     $('.error').removeClass('alert-success');       
     $('.error').removeClass('alert alert-danger').html(''); 
}

function openLoginModal(){
    showLoginForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
}
function openRegisterModal(){
    showRegisterForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
    
}

function loginAjax(){
    formdata=$('#login-form').serializeArray();
    $.ajax(
    {
        url : "../eatbit/LoginServlet",
        type: "POST",
        data : formdata,
        success:function(data, textStatus, jqXHR) 
        {
            //data: return data from server
            if(data == "1"){
                //window.location.replace("/home");
                // inserisci data nel nav bar
                $('#loginModal').modal('hide');
                location.reload();
            } 
            else{
                $('.error').addClass('alert alert-danger').html("Email/Password non valido!!");
                shakeModal();
            }
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            $('.error').addClass('alert alert-danger').html("Errore Server!! riprova più tardi "+textStatus);
                shakeModal();      
        }
    });
}
function logoutAjax(){
    $.ajax(
    {
        url : "../eatbit/LogoutServlet",
        type: "GET",
        success:function(data, textStatus, jqXHR) 
        {
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            $('.error').addClass('alert alert-danger').html("Errore Server! riprova più tardi");
            shakeModal();      
        }
    });
}
function regAjax(){
    rformdata=$('#register-form').serializeArray();
    $.ajax(
    {
        url : "../eatbit/RegisterUserServlet",
        type: "POST",
        data : rformdata,
        success:function(data, textStatus, jqXHR) 
        {
            //data: return data from server
            if(data == "1"){
                //se reg andato a buon fine
                showLoginForm();
                $('.error').addClass('alert alert-success').html("Congratulazioni !! adesso sei un nostro utente. Conferma l'e-mail ed <strong>Accedi</strong> per entrare nel tuo profilo.");     
            } 
            else{
                if(data=="-2")
                {
                    $('.error').addClass('alert alert-danger').html("Errore: esiste già un profilo con questo indirizzo e-mail");     
                }
                else{
                    if(data=="-3")
                    {
                        $('.error').addClass('alert alert-danger').html("Errore: esiste già un profilo con questo nickname");    
                    }
                    else{
                        $('.error').addClass('alert alert-danger').html("Errore: Riprova");     
                    }
                }
                shakeModal();
            }
        },
        error: function(jqXHR, textStatus, errorThrown) 
        {
            $('.error').addClass('alert alert-danger').html("Errore Server!! riprova più tardi");
            shakeModal();      
        }
    });
}

function shakeModal(){
    $('#loginModal .modal-dialog').addClass('shake');
             //$('.error').addClass('alert alert-danger').html("Email/Password combinazione invalido !!!");
             $('input[type="password"]').val('');
             setTimeout( function(){ 
                $('#loginModal .modal-dialog').removeClass('shake'); 
    }, 1000 ); 
}


// verifica i input, se non è valido genera errore
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
                        if (element.prop('type') === 'checkbox') {
                            error.insertAfter(element.parent());
                        } else {
                            error.insertAfter(element);
                        }
                    }
                });
                
                //custom rules
                $.validator.addMethod('strongPassword', function(value, element) {
                    return this.optional(element) 
                    || value.length >= 6
                    && /[a-z]/i.test(value);
                }, 'La tua password deve avere almeno 6 caratteri e deve contenere almeno un numero!!');
                
                //login validation
               $("#login-form").validate({
                    rules:{
                        emailorNickname:'required',
                        password:"required"
                    },
                    messages:{
                        emailorNickname:'*Campo obligatorio',
                        password:'*Campo obligatorio'
                    },
                    submitHandler: function(form) {
                        loginAjax();
                    }
                });
                
                // registration validation
                $("#register-form").validate({
                    rules:{
                        name:'required',
                        surname:"required",
                        nickname:"required",
                        email:{
                            required:true,
                            email:true
                        },
                        password:{
                            required: true,
                            strongPassword: true
                        },
                        password_confirmation:{
                            required:true,
                            equalTo: "#regPassword"
                        }
                    },
                    messages:{
                        name:'*Campo obligatorio',
                        surname:'*Campo obligatorio',
                        nickname:'*Campo obligatorio',
                        email:{
                            required: '*Campo obligatorio',
                            email:'Per favore inserisci un indirizzo email valido'
                        },
                        regPassword:{
                            required: '*Campo obligatorio'
                        },
                        password_confirmation:{
                            required: '*Campo obligatorio',
                            equalTo: 'Le password non corrispondono!!'
                        }
                    },
                    submitHandler: function(form) {
                        regAjax();
                    }
                });
            }
        }
        
        //dopo il document load
        $(D).ready(function($) {
            JQUERY4U.UTIL.setupFormValidation();
        });

})(jQuery, window, document);

 jQuery(document).ready(function ($) {
     $("#privacy_accept").change(function() {
            if(this.checked) {
                 $("#btn-reg").attr("disabled", false);
            }
            else{
                $("#btn-reg").attr("disabled", true);
            }
        });
 });

 function recuperPsw(){
     var emailRP=$('#emailorNickname').val();
     if(emailRP==""){
         $('.error').addClass('alert alert-danger').html("Devi inserire Email");
            shakeModal();
     }
     else{
          $.ajax(
            {
                url : "../eatbit/SendForgottenPswEmailServlet",
                type: "POST",
                data : {email:emailRP},
                success:function(data, textStatus, jqXHR) 
                {
                    if(data == "1"){
                        new PNotify({
                            title: 'Ok',
                            text: 'Abbiamo inviato un\'email dove puoi recuperare la tua password. Conltrolla!!',
                            type: 'success',
                            mobile: {
                                swipe_dismiss: true,
                                styling: true
                            }
                        });
                        location.reload();
                    }
                    if(data=="0"){
                        new PNotify({
                            title: 'Mi dispiace',
                            text: 'Recupero password al momento non è disponibile !!',
                            type: 'error',
                            mobile: {
                                swipe_dismiss: true,
                                styling: true
                            }
                        });
                    }
                    if(data=="-1"){
                        new PNotify({
                            title: 'Mi dispiace',
                            text: 'Mancano i Parametri !!',
                            type: 'error',
                            mobile: {
                                swipe_dismiss: true,
                                styling: true
                            }
                        });
                    }
                    if(data=="-2"){
                        new PNotify({
                            title: 'Attenzione',
                            text: 'Questa email non corrisponde a nessun utente registrato !!',
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
                            text: 'Recupero password al momento non è disponibile !!',
                            type: 'error',
                            mobile: {
                                swipe_dismiss: true,
                                styling: true
                            }
                        });
                }
            });
     }
 }

