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
                
                
                // registration validation
                $("#reset-form").validate({
                    rules:{
                        password:{
                            required: true,
                            strongPassword: true
                        },
                        passwordCheck:{
                            required:true,
                            equalTo: "#password"
                        }
                    },
                    messages:{
                        passwordCheck:{
                            required: '*Campo obligatorio',
                            equalTo: 'Le password non corrispondono!!'
                        }
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