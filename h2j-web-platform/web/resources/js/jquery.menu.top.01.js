$(document).ready(function() {
//    $('.H2jTopMenuStyle01').prepend('<div id="indicatorContainer"><div id="pIndicator"><div id="cIndicator"></div></div></div>');
    var activeElement = $('.H2jTopMenuStyle01>ul>li:first');

    $('.H2jTopMenuStyle01>ul>li').each(function() {
        if ($(this).hasClass('active')) {
            activeElement = $(this);
        }
    });


    var posLeft = activeElement.position().left;
    var elementWidth = activeElement.width();
    posLeft = posLeft + elementWidth / 2 - 6;
    if (activeElement.hasClass('has-sub')) {
        posLeft -= 6;
    }

    $('.H2jTopMenuStyle01 #pIndicator').css('left', posLeft);
    var element, leftPos, indicator = $('.H2jTopMenuStyle01 pIndicator');

    $(".H2jTopMenuStyle01>ul>li").hover(function() {
        element = $(this);
        var w = element.width();
        if ($(this).hasClass('has-sub'))
        {
            leftPos = element.position().left + w / 2 - 12;
        }
        else {
            leftPos = element.position().left + w / 2 - 6;
        }

        $('.H2jTopMenuStyle01 #pIndicator').css('left', leftPos);
    }
    , function() {
        $('.H2jTopMenuStyle01 #pIndicator').css('left', posLeft);
    });


    $('.H2jTopMenuStyle01>ul>.has-sub>ul').append('<div class="submenuArrow"></div>');
    $('.H2jTopMenuStyle01>ul').children('.has-sub').each(function() {
        var posLeftArrow = $(this).width();
        posLeftArrow /= 2;
        posLeftArrow -= 12;
        $(this).find('.submenuArrow').css('left', posLeftArrow);

    });

    $('.H2jTopMenuStyle01>ul').prepend('<li id="menu-button"><a>Menu</a></li>');
    $("#menu-button").click(function() {
        if ($(this).parent().hasClass('open')) {
            $(this).parent().removeClass('open');
        }
        else {
            $(this).parent().addClass('open');
        }
    });
});