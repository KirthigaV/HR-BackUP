 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getCalendarsByStr.js
 * Application: Retail Staffing Admin
 *
 */
function getCalendarsByStr(response) {
    this.interviewScheduleCollection = [];
    this.calNameToUpperCase = function(obj) {
        return obj.requisitionCalendarDescription.toString().toUpperCase();
    };
    var resultList = [];
    var valid = response.status && response.ScheduleDescList && response.ScheduleDescList != null && response.ScheduleDescList.ScheduleDescriptionDetails;
    if (valid) {
        if (_.isArray(response.ScheduleDescList.ScheduleDescriptionDetails)) {
            resultList = response.ScheduleDescList.ScheduleDescriptionDetails;
        } else {
            resultList = [ response.ScheduleDescList.ScheduleDescriptionDetails ];
        }
        if (resultList != null && resultList.length > 0) {
            var firstline = {
                "requisitionCalendarDescription" : "Select Calendar..",
                "requisitionCalendarId" : "0",
                "type" : 0,
                "enabled" : true
            };
            this.interviewScheduleCollection.push(firstline);
            for (var m = 0; m < resultList.length; m++) {
                resultList[m].enabled = true;
            }
            this.interviewScheduleCollection = this.interviewScheduleCollection.concat(resultList);

            var grouped = _.groupBy(this.interviewScheduleCollection, "type");
            var sortedkeys = _.sortBy(_.keys(grouped), function(num) {
                return parseInt(num);
            });
            var calObjArr = [];
            for (var i = 0; i < sortedkeys.length; i++) {
                calObjArr = calObjArr.concat(_.sortBy(grouped[sortedkeys[i]], this.calNameToUpperCase.bind(this)));
            }
            this.interviewScheduleCollection = calObjArr;
        }
    }
    return this;
}