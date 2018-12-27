/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: tabNavigator.js
 * Application: Staffing Administration
 *
 * Used to display three tabs.
 */


/**
 * Method is used to display three tabs which is presented
 * in interview availability.
 * tab 1 - calendar summary
 * tab 2 - hiring event
 * tab 3 - Requisition
 * On click of every tabs the respective details
 * will be loaded
 */
function tabNavigator() {
    /*
     * this function is called when initialize is called
     * this is used to load
     * all htmls tab
     */
    this.initialize = function() {
        $("#tabs").tabs({
            create : function() {
                $("#tabs").css("display", "inline");
            }
        });
        $("#tabMessageBarLabel").html("Select a calendar below...");
        $('#tabs-1').load('app/RSAInterviewAvailability/view/Calendar.html');
        $('#tabs-2').load('app/RSAInterviewAvailability/view/HiringEvent.html');
        $('#tabs-3').load('app/RSAInterviewAvailability/view/Requisition.html');
    };
    /*
     * this method is called when Hiring Event Tab Selected,
     * Move application to
     * Hiring Event Summary Tab where initialize
     *  of hiring event tab
     */
    this.hiringEventTabClick = function() {
        $("#tabMessageBarLabel").html("Select a Hiring Event below...");
        $("#hiringList").attr("title",CONSTANTS.HIRINGTAB_TOOLTIP);
        hiringEvent.initialize();
    };
    /*
     * this method is called when Requisition Tab
     * selected where initialize of
     * requisition tab
     */
    this.requisitionTabClick = function() {
        $("#tabMessageBarLabel").html("Active Requisitions for your Location...");
        $("#requistionTab").attr("title",
                "All Active Requisitions For Location " + MODEL.StoreNumber);
        req.initialize();
    };
    /*
     * this method is called when Calendar Tab Selected,
     * Move application to
     * Calendar Summary Tab where initialize
     * of calendar tab
     */
    this.calendarTabClick = function() {
        $("#tabMessageBarLabel").html("Select a calendar below...");
        $("#calendarList").attr("title",CONSTANTS.CALENDARTAB_TOOLTIP);
        calendarTab.tabClick();

    };

};