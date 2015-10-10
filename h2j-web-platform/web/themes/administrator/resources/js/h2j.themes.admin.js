//$(document).ready(function() {
//    var hideWidth = '-290px'; //width that will be hidden
//    var collapsibleEl = $('.UIPossitionMoreMenu'); //collapsible element
//    var buttonEl = $(".UIHeaderIconpanel"); //button inside element
//    $(buttonEl).html($("#UIMinPanelContent").html()); //change text of button
//    collapsibleEl.css({'margin-left': hideWidth}); //on page load we'll move and hide part of elements
//    $("#ContentPanelId").html($("#UISmallContent").html());
//    $(buttonEl).click(function()
//    {
//        var curwidth = $(this).parent().offset(); //get offset value of the element
//        if (curwidth.left >= 0) //compare margin-left value
//        {
//            //animate margin-left value to -490px
//            collapsibleEl.animate({marginLeft: hideWidth}, 300);
//            $(this).html("<div class='UILestIcon UIFloatRight'><span></span></div><div class='UIClearRight'><span></span></div>"); //change text of button
//            $("#ContentPanelId").html($("#UISmallContent").html());
//        } else {
//            //animate margin-left value 0px
//            collapsibleEl.animate({marginLeft: "0"}, 300);
//            $(this).html("<div class='UIThanIcon UIFloatRight'><span></span></div><div class='UIClearRight'><span></span></div>"); //change text of button
//
//            $("#ContentPanelId").html($("#UIFullContent").html());
//
//        }
//    });
//});
$(document).ready(function() {
    var menu = new MoreActionMenu();
});
var MoreActionMenu = function(menuPanelId, smallContentId, fullContentId, hideWidth) {
    var menuPanel = $("#" + menuPanelId);
    var header = $(".UIHeaderIconpanel");
    var smallContent = $("#" + smallContentId);
    var fullContent = $("#" + fullContentId);
    menuPanel.css({'margin-left': hideWidth}); //on page load we'll move and hide part of elements
    $(header).click(function() {
        var curwidth = $(this).parent().offset(); //get offset value of the element
        if (curwidth.left >= 0) //compare margin-left value
        {
            menuPanel.animate({marginLeft: hideWidth}, 300);
            fullContent.css("display", "none");
            smallContent.css("display", "block");
        } else {
            menuPanel.animate({marginLeft: "0"}, 300);
            fullContent.css("display", "block");
            smallContent.css("display", "none");

        }
    });
}