 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getHiringEventDetails.js
 * Application: Retail Staffing Admin
 *
 */
function getHiringEventDetails(response) {
    this.returnedErrorMessage = "";
    if (response.error) {
        var errorResultList = [];
        if (_.isArray(response.error)) {
            errorResultList = response.error;
        } else {
            errorResultList = [ response.error ];
        }
        this.returnedErrorMessage = new errorHandling(errorResultList);
    } else if (response.hiringEventResponse && response.hiringEventResponse.hiringEventDetail) {
        this.hiringEventDetails = {};

        this.hiringEventDetails.hireEventId = response.hiringEventResponse.hiringEventDetail.hireEventId;
        this.hiringEventDetails.hireEventLocationDescription = response.hiringEventResponse.hiringEventDetail.hireEventLocationDescription;
        this.hiringEventDetails.hireEventAddressText = response.hiringEventResponse.hiringEventDetail.hireEventAddressText;
        this.hiringEventDetails.hireEventCityName = response.hiringEventResponse.hiringEventDetail.hireEventCityName;
        this.hiringEventDetails.hireEventStateCode = response.hiringEventResponse.hiringEventDetail.hireEventStateCode;
        this.hiringEventDetails.hireEventZipCodeCode = response.hiringEventResponse.hiringEventDetail.hireEventZipCodeCode;
        this.hiringEventDetails.hireEventBeginDate = UTILITY.convertSqlDateFormatToStdDateFormat(response.hiringEventResponse.hiringEventDetail.hireEventBeginDate);
        this.hiringEventDetails.hireEventEndDate = UTILITY.convertSqlDateFormatToStdDateFormat(response.hiringEventResponse.hiringEventDetail.hireEventEndDate);
        this.hiringEventDetails.hireEventTypeIndicator = response.hiringEventResponse.hiringEventDetail.hireEventTypeIndicator;

        // Set the Event Type Text based on hireEventTypeIndicator value
        if (this.hiringEventDetails.hireEventTypeIndicator === "SSE") {
            this.hiringEventDetails.eventTypeText = "Single Store Event";
        } else if (this.hiringEventDetails.hireEventTypeIndicator === "MSE") {
            this.hiringEventDetails.eventTypeText = "Multi Store Event";
        } else if (this.hiringEventDetails.hireEventTypeIndicator === "OSE") {
            this.hiringEventDetails.eventTypeText = "Off-Site Event";
        }
        // Hiring Event Manager Details
        if (response.hiringEventResponse.hasOwnProperty("hiringEventMgrData")) {
            this.hiringEventDetails.emgrHumanResourcesAssociateId = response.hiringEventResponse.hiringEventMgrData.emgrHumanResourcesAssociateId;
            this.hiringEventDetails.name = response.hiringEventResponse.hiringEventMgrData.name;
            this.hiringEventDetails.phone = response.hiringEventResponse.hiringEventMgrData.phone;
            this.hiringEventDetails.title = response.hiringEventResponse.hiringEventMgrData.title;
            this.hiringEventDetails.email = response.hiringEventResponse.hiringEventMgrData.email;
        }
    }
    return this;
}