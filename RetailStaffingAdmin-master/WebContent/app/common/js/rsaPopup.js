/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: rsaPopup.js
 * Application: Retail Staffing Admin
 *
 */
function rsaPopup() {
    var genricpopupHeight, genricpopupWidth;
    var okCallback, cancelCallback,alertOkCallback;
    /*
     * Display model popup
     * Set generic popup height and width
     */
    $("#genericPopup").on("show.bs.modal", function() {
        $("#appBody").addClass("blurBody");
        if (genricpopupHeight != null && genricpopupWidth != null) {
            $('#genericPopup .modal-dialog').css({
                "height" : genricpopupHeight,
                "width" : genricpopupWidth
            });
        }
        // Set height of the popup dynamically
        $(this).find('.modal-dialog').css({
            'margin-top' : function() {
                return -($('#genericPopup .modal-dialog').outerHeight() / 2);
            },
            'margin-left' : function() {
                return -($('#genericPopup .modal-dialog').outerWidth() / 2);
            }
        });
    });
    // Set background as blured as per flex
    $("#genericPopup").off("hide.bs.modal");
    $("#genericPopup").on("hide.bs.modal", function() {
        $("#appBody").removeClass("blurBody");
    });
    // On click of OK button hide the popup
    $('#genericPopup').off("click", "#okbutton");
    $('#genericPopup').on("click", "#okbutton", function() {
        $('#genericPopup').on('hidden.bs.modal', function() {
            okCallback();
        }.bind(this));
        $('#genericPopup').modal('hide');

    }.bind(this));
    // on click of cancel button trigger calback
    $('#genericPopup').off("click", "#cancelbutton");
    $('#genericPopup').on("click", "#cancelbutton", function() {
        $('#genericPopup').on('hidden.bs.modal', function() {
            cancelCallback();
        }.bind(this));
        $('#genericPopup').modal('hide');

    }.bind(this));
    // Set Popup as draggable as per flex
    $("#genericPopup").draggable({
        handle : ".modal-header"
    });
    // Display model popup as per flex
    $("#alertModalId").off("show.bs.modal");
    $("#alertModalId").on("show.bs.modal", function() {
        $("#appBody").addClass("blurBody");
        $('#alertModalId .modal-dialog').css({
            "height" : "110px",
            "width" : "300px"
        });
        // Set top and left margin to look alike flex screens
        $(this).find('.modal-dialog').css({
            'margin-top' : function() {
                return -($('#alertModalId .modal-dialog').outerHeight() / 2);
            },
            'margin-left' : function() {
                return -($('#alertModalId .modal-dialog').outerWidth() / 2);
            }
        });
    });
    $("#alertModalId").off("shown.bs.modal");
    $("#alertModalId").on("shown.bs.modal", function() {
        $("#alertModalId .alertmodalbtn").focus();
    });
    // Activate the background and remove blur effect
    $("#alertModalId").off("hide.bs.modal");
    $("#alertModalId").on("hide.bs.modal", function() {
        if($("#noassocalertModalId.in").length === 0){
            $("#appBody").removeClass("blurBody");
        }
    });
    $("#alertModalId").off("hidden.bs.modal");
    $("#alertModalId").on("hidden.bs.modal", function() {
        if($("#noassocalertModalId.in").length > 0){
            $("#noassocalertModalId .alertmodalbtn").focus();
        }
        if(alertOkCallback != null)
    	{
        	alertOkCallback();
    	}
    });
    // Set Alert Popup as draggable as per flex
    $("#alertModalId").draggable({
        handle : ".modal-header"
    });
    // Display model popup as per flex
    $("#noassocalertModalId").off("show.bs.modal");
    $("#noassocalertModalId").on("show.bs.modal", function() {
        $("#appBody").addClass("blurBody");
        $('#noassocalertModalId .modal-dialog').css({
            "height" : "110px",
            "width" : "300px"
        });
        // Set top and left margin to look alike flex screens
        $(this).find('.modal-dialog').css({
            'margin-top' : function() {
                return -($('#noassocalertModalId .modal-dialog').outerHeight() / 2);
            },
            'margin-left' : function() {
                return -($('#noassocalertModalId .modal-dialog').outerWidth() / 2);
            }
        });
    });
    $("#noassocalertModalId").off("shown.bs.modal");
    $("#noassocalertModalId").on("shown.bs.modal", function() {
        $("#noassocalertModalId .alertmodalbtn").focus();
    });
    // Activate the background and remove blur effect
    $("#noassocalertModalId").off("hide.bs.modal");
    $("#noassocalertModalId").on("hide.bs.modal", function() {
        if($("#alertModalId.in").length === 0){
            $("#appBody").removeClass("blurBody");
        }
    });
    $("#noassocalertModalId").off("hidden.bs.modal");
    $("#noassocalertModalId").on("hidden.bs.modal", function() {
        if($("#alertModalId.in").length > 0){
            $("#alertModalId .alertmodalbtn").focus();
        }
    });
    // Set Alert Popup as draggable as per flex
    $("#noassocalertModalId").draggable({
        handle : ".modal-header"
    });
    // Set background of the screen as per flex when popup comes
    this.alert = function(message,okClick) {
    	if (okClick != null) {
    		alertOkCallback = okClick;
        } else {
        	alertOkCallback = null;
        }
        $("#alertModalId").modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $("#alertModalId .alertModalBody").text(message);
    };
    // Set background of the screen as per flex when popup comes
    this.noassocAlert = function(message) {
        $("#noassocalertModalId").modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $("#noassocalertModalId .alertModalBody").text(message);
    };
    /*
     * Set height and width of the popup dynamically to look UI like flex
     * Set callback functions as well
     */
    this.show = function(okClick, CancelClick, height, width) {
        if (height !== undefined && width !== undefined) {
            genricpopupHeight = height;
            genricpopupWidth = width;
        } else {
            genricpopupHeight = null;
            genricpopupWidth = null;
        }
        if (okClick != null) {
            okCallback = okClick;
        } else {
            okCallback = null;
        }
        if (CancelClick != null) {
            cancelCallback = CancelClick;
        } else {
            cancelCallback = null;
        }

        $("#genericPopup").modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $('#genericPopup').off("hidden.bs.modal");

    };
    /*
     * This function is used to display warning message dynamically
     */
    this.warn = function(iMessage, okClick, CancelClick, iHeight, iWidth) {
        this.clearPopup();
        this.show(okClick, CancelClick, iHeight, iWidth);
        $(".genericPopupClass .modal-header #modalLabel").text("Warning");
        $(".genericPopupClass .modal-header #icon").attr("class", "warning-icon");
        $(".genericPopupClass .txtMessageOne").text(iMessage);
        $(".genericPopupClass .modal-header .close").hide();
    };
    /*
     * This function is used to display warning message for Unsaved data
     */
    this.warnUnsavedData = function(iMessage1, iMessage2, okClick, CancelClick, iHeight, iWidth) {
        this.warn(iMessage1, okClick, CancelClick, iHeight, iWidth);
        $(".genericPopupClass .txtMessageTwo").text(iMessage2);

    };
    /*
     * This function is used to display warning message for missing phone screen
     */
    this.warnMissingPhnSrc = function(iMessage1, iMessage2, okClick, CancelClick, iHeight, iWidth) {
        this.warnUnsavedData(iMessage1, iMessage2, okClick, CancelClick, iHeight, iWidth);
    };
    /*
     * This function is used to display the content in warning message similar
     * to flex
     */
    this.autoholdWarn = function(iMessage1, iMessage2, iMessage3_4, okClick, CancelClick, iHeight, iWidth) {
        var msgs = iMessage3_4.split("%_?");
        this.warnUnsavedData(iMessage1, iMessage2, okClick, CancelClick, iHeight, iWidth);
        $(".genericPopupClass .txtMessageThree").text(msgs[0]);
        $(".genericPopupClass .txtMessageFour").text(msgs[1]);
    };
    /*
     * This function is used to display common functionality on click of cancel
     * button in popup
     */
    this.commonPopup = function(iMessage, okClick, iHeight, iWidth) {
        this.warn(iMessage, okClick, null, iHeight, iWidth);
        $(".genericPopupClass #cancelbutton").hide();

    };
    /*
     * Reset the control values in popup
     */
    this.clearPopup = function() {
        $(".genericPopupClass .modal-header #modalLabel").text("");
        $(".genericPopupClass .modal-header #icon").removeAttr("class");
        $(".genericPopupClass .txtMessageOne").text("");
        $(".genericPopupClass .txtMessageTwo").text("");
        $(".genericPopupClass .txtMessageThree").text("");
        $(".genericPopupClass .txtMessageFour").text("");
        $(".genericPopupClass .modal-header .close").show();
        $(".genericPopupClass #cancelbutton").show();
        $('#genericPopup .modal-dialog').removeAttr("style");
    };
}