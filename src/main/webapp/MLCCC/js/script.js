// Smooth scroll blocking
document.addEventListener( 'DOMContentLoaded', function() {
	if ( 'onwheel' in document ) {
		window.onwheel = function( event ) {
			if( typeof( this.RDSmoothScroll ) !== undefined ) {
				try { window.removeEventListener( 'DOMMouseScroll', this.RDSmoothScroll.prototype.onWheel ); } catch( error ) {}
				event.stopPropagation();
			}
		};
	} else if ( 'onmousewheel' in document ) {
		window.onmousewheel= function( event ) {
			if( typeof( this.RDSmoothScroll ) !== undefined ) {
				try { window.removeEventListener( 'onmousewheel', this.RDSmoothScroll.prototype.onWheel ); } catch( error ) {}
				event.stopPropagation();
			}
		};
	}

	try { $('body').unmousewheel(); } catch( error ) {}
});
function include(url) {
    document.write('<script src="' + url + '"></script>');
    return false;
}

/* cookie.JS
 ========================================================*/
include('js/jquery.cookie.js');


/* DEVICE.JS
 ========================================================*/
include('js/device.min.js');

/* Stick up menu
 ========================================================*/
include('js/tmstickup.js');
$(window).load(function () {
    if ($('html').hasClass('desktop')) {
        $('#stuck_container').TMStickUp({
        })
    }
});

/* Easing library
 ========================================================*/
include('js/jquery.easing.1.3.js');

/* ToTop
 ========================================================*/
include('js/jquery.ui.totop.js');
$(function () {
    $().UItoTop({ easingType: 'easeOutQuart' });
});


/* DEVICE.JS AND SMOOTH SCROLLIG
 ========================================================*/
include('js/jquery.mousewheel.min.js');
include('js/jquery.simplr.smoothscroll.min.js');
$(function () {
    if ($('html').hasClass('desktop')) {
        $.srSmoothscroll({
            step: 150,
            speed: 800
        });
    }
});

/* Copyright Year
 ========================================================*/
var currentYear = (new Date).getFullYear();
$(document).ready(function () {
    $("#copyright-year").text((new Date).getFullYear());
});


/* Superfish menu
 ========================================================*/
include('js/superfish.js');
include('js/jquery.mobilemenu.js');

/* Unveil
 ========================================================*/
include('js/jquery.unveil.js');
$(document).ready(function () {
    $('img').unveil(0, function () {
        $(this).load(function () {
            $(this).addClass("js-lazy-img");
        });
    });
});

/* Google Map
 ========================================================
$(window).load()
{
    if ($('#google-map').length > 0) {
        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(parseFloat(39.855893), parseFloat(-75.061802)),
            scrollwheel: false
        }
        new google.maps.Map(document.getElementById("google-map"), mapOptions);
    }
}

/* Orientation tablet fix
 ========================================================*/
$(function () {
    // IPad/IPhone
    var viewportmeta = document.querySelector && document.querySelector('meta[name="viewport"]'),
        ua = navigator.userAgent,

        gestureStart = function () {
            viewportmeta.content = "width=device-width, minimum-scale=0.25, maximum-scale=1.6, initial-scale=1.0";
        },

        scaleFix = function () {
            if (viewportmeta && /iPhone|iPad/.test(ua) && !/Opera Mini/.test(ua)) {
                viewportmeta.content = "width=device-width, minimum-scale=1.0, maximum-scale=1.0";
                document.addEventListener("gesturestart", gestureStart, false);
            }
        };

    scaleFix();
    // Menu Android
    if (window.orientation != undefined) {
        var regM = /ipod|ipad|iphone/gi,
            result = ua.match(regM);
        if (!result) {
            $('.sf-menu li').each(function () {
                if ($(">ul", this)[0]) {
                    $(">a", this).toggle(
                        function () {
                            return false;
                        },
                        function () {
                            window.location.href = $(this).attr("href");
                        }
                    );
                }
            })
        }
    }
});
var ua = navigator.userAgent.toLocaleLowerCase(),
    regV = /ipod|ipad|iphone/gi,
    result = ua.match(regV),
    userScale = "";
if (!result) {
    userScale = ",user-scalable=0"
}
document.write('<meta name="viewport" content="width=device-width,initial-scale=1.0' + userScale + '">')

$(document).ready(function () {

    var obj;

    if ((obj = $('#camera')).length > 0) {
        obj.camera({
            autoAdvance: true,
            height: '45.04643962848297%',
            minHeight: '200px',
            pagination: false,
            thumbnails: false,
            playPause: false,
            hover: false,
            loader: 'none',
            navigation: true,
            navigationHover: false,
            mobileNavHover: false,
            fx: 'simpleFade'
        })
    }

  if ((obj = $('a[data-type="lightbox"]')).length > 0) {
        obj.touchTouch();
    }

    if ((obj = $('#isotope')).length > 0) {
        obj.isotope({
            itemSelector: '.item',
            layoutMode: 'fitRows'
        });

        $('#filters').on('click', 'a', function () {
            var filterValue = $(this).attr('data-filter');

            if (filterValue == '*') {
                obj.isotope({ filter: filterValue });
            } else {
                obj.isotope({ filter: '.' + filterValue });
            }

            $('#filters').find('li').removeClass('active');
            $(this).parent().addClass('active');
            return false;
        });
    }

})

/* loginform========================================================*/
$(document).ready(function(){
	 $("#login").click(function () {
	 	$("#form").dialog();
	 });
});

/* news========================================================
$('.hit').data('count', 0);
$('.btn2').click(function(){
    $('.hit').html(function(){
        var $this = $(this),
        count = $this.data('count') + 1;
        $this.data('count', count);
        return count;
    });
});
$('.hit').data('count', 0);
$('.btn2').click(function(){
    $('.hit').html(function(){
        var $this = $(this),
        count = $this.data('count') + 1;
        $this.data('count', count);
        return count;
    });
});
localStorage.clicks = +(localStorage.clicks || 0) + 1;*/

/*  weekend program========================================================*/
$( function() {
	$( "#tabs" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
	$( "#tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
	$( "#tabs" ).tabs({
		event: "mouseover"
	});
$( "#tabs" ).tabs({ show: { effect: "blind", duration: 800 } });
} );
$( function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  } );

/* new slider*/
$(document).ready( function() {
    $('#myCarousel').carousel({
        interval:   4000
    });

    var clickEvent = false;
    $('#myCarousel').on('click', '.nav a', function() {
        clickEvent = true;
        $('.nav li').removeClass('active');
        $(this).parent().addClass('active');
    }).on('slid.bs.carousel', function(e) {
        if(!clickEvent) {
            var count = $('.nav').children().length -1;
            var current = $('.nav li.active');
            current.removeClass('active').next().addClass('active');
            var id = parseInt(current.data('slide-to'));
            if(count == id) {
                $('.nav li').first().addClass('active');
            }
        }
        clickEvent = false;
    });
});
