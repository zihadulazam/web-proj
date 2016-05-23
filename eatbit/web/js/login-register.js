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
    /*   Remove this comments when moving to server
    $.post( "/login", function( data ) {
            if(data == 1){
                window.location.replace("/home");            
            } else {
                 shakeModal(); 
            }
        });
    */

/*   Simulate error message from the server   */
     shakeModal();
}

function shakeModal(){
    $('#loginModal .modal-dialog').addClass('shake');
             $('.error').addClass('alert alert-danger').html("Email/Password combinazione invalido !!!");
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
                            equalTo: "#password"
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
                        password:{
                            required: '*Campo obligatorio',
                            minlength:'eve avere almeno 5 caratteri!!'
                        },
                        password_confirmation:{
                            required: '*Campo obligatorio',
                            equalTo: 'Le password non corrispondono!!'
                        }
                    }
                });
            }
        }
        
        //dopo il document load
        $(D).ready(function($) {
            JQUERY4U.UTIL.setupFormValidation();
        });

})(jQuery, window, document);

