 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getHiringEventsByStr.js
 * Application: Retail Staffing Admin
 *
 */
function getHiringEventsByStr(response) {
    this.interviewScheduleCollection = [];
    var firstline = {};
    /*
     * This function converts calendar name to upper case
     *
     * @param obj
     * @return calname
     */
    this.calNameToUpperCase = function(obj) {
        return obj.requisitionCalendarDescription.toString().toUpperCase();
    };
    /*
     * This function sets interview schedule details of the requisition
     *
     * @param resultlist
     * @return N/A
     */
    this.storeInterviewSchedule = function(resultList) {
        if (!_.isNull(resultList) && resultList.length > 0) {
            firstline = {
                "requisitionCalendarDescription" : "Select Hiring Event..",
                "requisitionCalendarId" : "0",
                "type" : 0,
                "enabled" : true
            };

            this.interviewScheduleCollection.push(firstline);
            var today = new Date();
            for (var m = 0; m < resultList.length; m++) {

                var eventDate = new Date(resultList[m].hireEventBeginDate);
                if (eventDate > today) {
                    resultList[m].enabled = true;
                } else {
                    resultList[m].enabled = false;
                }
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
    };
    var resultList = [];

    // Fill Calendar Combo
    if (response.status && ("ScheduleDescList" in response) && !_.isNull(response.ScheduleDescList)) {
        if (response.ScheduleDescList && response.ScheduleDescList !== "") {
            if (response.ScheduleDescList.ScheduleDescriptionDetails) {
                resultList = UTILITY.checkAndStoreAsArray(response.ScheduleDescList.ScheduleDescriptionDetails);
                this.storeInterviewSchedule(resultList);
            }
        } else {
            firstline = {
                "requisitionCalendarDescription" : "Select Hiring Event..",
                "requisitionCalendarId" : "0",
                "type" : 0,
                "enabled" : true
            };
            this.interviewScheduleCollection.push(firstline);
        }
    }
    return this;
}