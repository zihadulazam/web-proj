
function scroll_to_class(element_class, removed_height) {
	var scroll_to = $(element_class).offset().top - removed_height;
	if($(window).scrollTop() != scroll_to) {
		$('html, body').stop().animate({scrollTop: scroll_to}, 0);
	}
}

function bar_progress(progress_line_object, direction) {
	var number_of_steps = progress_line_object.data('number-of-steps');
	var now_value = progress_line_object.data('now-value');
	var new_value = 0;
	if(direction == 'right') {
		new_value = now_value + ( 100 / number_of_steps );
	}
	else if(direction == 'left') {
		new_value = now_value - ( 100 / number_of_steps );
	}
	progress_line_object.attr('style', 'width: ' + new_value + '%;').data('now-value', new_value);
}

jQuery(document).ready(function() {
	
    /*
        Fullscreen background
    */
    $.backstretch("assets/img/backgrounds/modifyRestBckg.jpg");
    
    $('#top-navbar-1').on('shown.bs.collapse', function(){
    	$.backstretch("resize");
    });
    $('#top-navbar-1').on('hidden.bs.collapse', function(){
    	$.backstretch("resize");
    });
    
    /*
        Form
    */
    $('.f1 fieldset:first').fadeIn('slow');
    
    $('.f1 input[type="text"], .f1 input[type="password"], .f1 textarea').on('focus', function() {
    	$(this).removeClass('input-error');
    });
    
    // next step
    $('.f1 .btn-next').on('click', function() {
    	var parent_fieldset = $(this).parents('fieldset');
    	var next_step = true;
    	// navigation steps / progress steps
    	var current_active_step = $(this).parents('.f1').find('.f1-step.active');
    	var progress_line = $(this).parents('.f1').find('.f1-progress-line');
    	
    	// fields validation
    	parent_fieldset.find('#name, #description, #web_site, #address, #prezzo_min, #prezzo_max, #Add-photo-name, #photo_description').each(function() {
    		if( $(this).val() == "" ) {
    			$(this).addClass('input-error');
    			next_step = false;
    		}
    		else {
    			$(this).removeClass('input-error');
    		}
    	});
    	// fields validation
    	
    	if( next_step ) {
    		parent_fieldset.fadeOut(400, function() {
    			// change icons
    			current_active_step.removeClass('active').addClass('activated').next().addClass('active');
    			// progress bar
    			bar_progress(progress_line, 'right');
    			// show next step
	    		$(this).next().fadeIn();
	    		// scroll window to beginning of the form
    			scroll_to_class( $('.f1'), 20 );
	    	});
    	}
    	
    });
    
    // previous step
    $('.f1 .btn-previous').on('click', function() {
    	// navigation steps / progress steps
    	var current_active_step = $(this).parents('.f1').find('.f1-step.active');
    	var progress_line = $(this).parents('.f1').find('.f1-progress-line');
    	
    	$(this).parents('fieldset').fadeOut(400, function() {
    		// change icons
    		current_active_step.removeClass('active').prev().removeClass('activated').addClass('active');
    		// progress bar
    		bar_progress(progress_line, 'left');
    		// show previous step
    		$(this).prev().fadeIn();
    		// scroll window to beginning of the form
			scroll_to_class( $('.f1'), 20 );
    	});
    });
    
    // submit
    $('.f1').on('submit', function(e) {
    	
    	// fields validation
    	$(this).find('input[type="text"], input[type="password"], textarea').each(function() {
    		if( $(this).val() == "" ) {
    			e.preventDefault();
    			$(this).addClass('input-error');
    		}
    		else {
    			$(this).removeClass('input-error');
    		}
    	});
        
       
    	// fields validation
    	
    });
    
    
});

$(document).ready(function(){
    $('#Add-photo').change(function(){
        $('#Add-photo-name').val($(this).val());
    });
});


$(document).ready(function(){
        var places;
        var location;
        var lat=" ";
        var lon=" ";
        var via;
        var num;
        var city;
        var country;
        var province;
	
    var autocomplete=new google.maps.places.Autocomplete(document.getElementById('address'));
	google.maps.event.addListener(autocomplete,'place_changed',function(){
		places=autocomplete.getPlace();
		location=""+places.formatted_address;
		lat=""+places.geometry.location.lat();
		lon=""+places.geometry.location.lng();
		via="Via ";
		num="";
		city="";
		country="";
		for (var i = 0; i < places.address_components.length; i++)
		{
			var addr=places.address_components[i];
			if(addr.types[0]=='route')
				via=addr.long_name;
			if(addr.types[0]=='street_number')
				num=addr.long_name;
			if(addr.types[0]=='locality')
				city=addr.long_name;
			if(addr.types[0]=='country')
				country=addr.long_name;
                        if(addr.types[0]=='administrative_area_level_2')
                                province = addr.long_name;

		}
		via+=" "+num;
        if(lat==" "|| lon==" ")
            alert("errore Indirizzo");
        else{
            alert(location+" "+lat+" "+lon+" "+via+" "+city+" "+province+" "+country);
            //passo le coordinate al server
            document.getElementById("location").value = location;
            document.getElementById("city").value = city;
            document.getElementById("province").value = province;
            document.getElementById("state").value = country;
            document.getElementById("latitude").value = lat;
            document.getElementById("longitude").value = lon;   
        }
	});


       
       
       $('select').change(function(){
           //prendo gli orari
           var LMH = document.getElementById("LunMatH").value;
           var LMM = document.getElementById("LunMatM").value;
           var LPH = document.getElementById("LunPomH").value;
           var LPM = document.getElementById("LunPomM").value;
           
           var MMH = document.getElementById("MarMatH").value;
           var MMM = document.getElementById("MarMatM").value;
           var MPH = document.getElementById("MarPomH").value;
           var MPM = document.getElementById("MarPomM").value;
           
           var MEMH = document.getElementById("MerMatH").value;
           var MEMM = document.getElementById("MerMatM").value;
           var MEPH = document.getElementById("MerPomH").value;
           var MEPM = document.getElementById("MerPomM").value;
           
           var GMH = document.getElementById("GioMatH").value;
           var GMM = document.getElementById("GioMatM").value;
           var GPH = document.getElementById("GioPomH").value;
           var GPM = document.getElementById("GioPomM").value;
           
           var VMH = document.getElementById("VenMatH").value;
           var VMM = document.getElementById("VenMatM").value;
           var VPH = document.getElementById("VenPomH").value;
           var VPM = document.getElementById("VenPomM").value;
           
           var SMH = document.getElementById("SabMatH").value;
           var SMM = document.getElementById("SabMatM").value;
           var SPH = document.getElementById("SabPomH").value;
           var SPM = document.getElementById("SabPomM").value;
           
           var DMH = document.getElementById("DomMatH").value;
           var DMM = document.getElementById("DomMatM").value;
           var DPH = document.getElementById("DomPomH").value;
           var DPM = document.getElementById("DomPomM").value;

           
           

           
           if ((LMH != "--") && (LMM != "--") && (LPH != "--") 
                   && (LPM != "--") && (MMH != "--") && (MMM != "--") 
                   && (MPH != "--") && (MPM != "--") && (MEMH != "--") 
                   && (MEMM != "--") && (MEPH != "--") && (MEPM != "--") 
                   && (GMH != "--") && (GMM != "--") && (GPH != "--") 
                   && (GPM != "--") && (VMH != "--") && (VMM != "--") 
                   && (VPH != "--") && (VPM != "--") && (SMH != "--") 
                   && (SMM != "--") && (SPH != "--") && (SPM != "--") 
                   && (DMH != "--") && (DMM != "--") && (DPH != "--") && (DPM != "--")) {
               
                          //formatto gli orari
                    var orarioLun = "1"+LMH+":"+LMM+LPH+":"+LPM;
                    var orarioMar = "2"+MMH+":"+MMM+MPH+":"+MPM;
                    var orarioMer = "3"+MEMH+":"+MEMM+MEPH+":"+MEPM;
                    var orarioGio = "4"+GMH+":"+GMM+GPH+":"+GPM;
                    var orarioVen = "5"+VMH+":"+VMM+VPH+":"+VPM;
                    var orarioSab = "6"+SMH+":"+SMM+SPH+":"+SPM;
                    var orarioDom = "7"+DMH+":"+DMM+DPH+":"+DPM;
               
               alert("Orari: " + orarioLun + " " + orarioMar + " " 
                   + orarioMer +" " + orarioGio + " " + orarioVen
                   + " " + orarioSab + " " + orarioDom );
           
                    document.getElementById("orarioL").value = orarioLun;
                     document.getElementById("orarioM").value = orarioMar;
                     document.getElementById("orarioMe").value = orarioMer;
                     document.getElementById("orarioG").value = orarioGio;
                     document.getElementById("orarioV").value = orarioVen;
                     document.getElementById("orarioS").value = orarioSab;   
                     document.getElementById("orarioD").value = orarioDom; 
                 }
           
       });
        
});
