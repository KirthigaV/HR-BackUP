/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: reusablecomponents.js
 * Application: Retail Staffing Admin
 *
 */
/*
 * Method is used to restrict the input value having
 * special characters
 */
(function($) {
   //Restrict the textbox input as per Regex pattern
    $.fn.restrictInputFeature = function() {
        $(this).keypress(function(e) {
            var temp = String.fromCharCode(e.which);
            var pattern = new RegExp($(e.currentTarget).attr("data-restrict"));
            if (!(pattern.test(temp))){
                return false;
            }
        });
    };
}(jQuery));
