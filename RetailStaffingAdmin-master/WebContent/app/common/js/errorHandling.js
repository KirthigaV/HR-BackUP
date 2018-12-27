/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: errorHandling.js
 * Application: Retail Staffing Admin
 *
 */
function errorHandling(errorResultList) {
    // Application Objects
    this.APPLICATION_OBJ_STORE = "OBJ-0001";
    this.APPLICATION_OBJ_CALENDAR = "OBJ-0002";
    this.APPLICATION_OBJ_INTERVIEW = "OBJ-0003";
    //Object errors
    this.APPLICATION_OBJ_ERRORS = [ {
        objectCode : this.APPLICATION_OBJ_STORE,
        errorDesc : "Store Service"
    }, {
        objectCode : this.APPLICATION_OBJ_CALENDAR,
        errorDesc : "Calendar Service"
    }, {
        objectCode : this.APPLICATION_OBJ_INTERVIEW,
        errorDesc : "Interview Service"
    } ];
    // Error Types
    // ERR-0001 will contain a componentIdentifier of type INPUT_ERROR
    this.ERROR_INPUT_VALIDATION = "ERR-0001";
    // ERR-0002 will contain a componentIdentifier of type APPLICATION_OBJECT_X
    this.ERROR_DATABASE_EXCEPTION = "ERR-0002";
    // ERR-0003 will contain a componentIdentifier of type Duplicate Exception
    this.ERROR_DUPLICATE_HIRING_EVENT_NAME_EXCEPTION = "ERR-0003";

    /*
     * Set Errorcode and description Input Errors
     * 1.Invalid version number
     * 2.Invalid STore number
     * 3.Invalid inputs
     * 4.Invalid XML input
     * 5.Invalid Eventname exist
     */
    this.INPUT_ERRORS = [ {
        errorCode : "INP-0001",
        errorDesc : "Invalid Version Number"
    }, {
        errorCode : "INP-0002",
        errorDesc : "Invalid Store Number"
    }, {
        errorCode : "INP-0003",
        errorDesc : "Invalid Calendar Id"
    }, {
        errorCode : "INP-0004",
        errorDesc : "Invalid Begin Date"
    }, {
        errorCode : "INP-0005",
        errorDesc : "Invalid End Date"
    }, {
        errorCode : "INP-0006",
        errorDesc : "Invalid Date"
    }, {
        errorCode : "INP-0007",
        errorDesc : "Invalid Begin Time"
    }, {
        errorCode : "INP-0008",
        errorDesc : "Invalid End Time"
    }, {
        errorCode : "INP-0009",
        errorDesc : "Invalid Number of Interviewers"
    }, {
        errorCode : "INP-0010",
        errorDesc : "Invalid Number of Recurring Weeks"
    }, {
        errorCode : "INP-0011",
        errorDesc : "Invalid Interview Slot"
    }, {
        errorCode : "INP-0012",
        errorDesc : "Invalid Calendar Name"
    }, {
        errorCode : "INP-0013",
        errorDesc : "Invalid Calendar Type"
    }, {
        errorCode : "INP-0112",
        errorDesc : "Invalid XML Input"
    }, {
        errorCode : "INP-0119",
        errorDesc : "Invalid Hiring Event ID Input"
    }, {
        errorCode : "INP-0121",
        errorDesc : "Hiring Event Name already exists."
    } ];

    // errorResultList will should only contain 1 entry, but needs to be an
    // ArrayCollection in order to read the XML nodes
    var errorMessage = "";
    var index = 0;
    //Database error
    if (errorResultList[0].errorType === this.ERROR_DATABASE_EXCEPTION) {
        var applicationObjDesc = "";
        for (index = 0; index < this.APPLICATION_OBJ_ERRORS.length; index++) {
            if (errorResultList[0].componentIdentifier === this.APPLICATION_OBJ_ERRORS[index].objectCode) {
                applicationObjDesc = this.APPLICATION_OBJ_ERRORS[index].errorDesc;
                break;
            }
        }
        errorMessage = "Database Error Type: " + errorResultList[0].errorType
                + " Application Object: " + applicationObjDesc;
    }
    //Input Validation
    else if (errorResultList[0].errorType === this.ERROR_INPUT_VALIDATION
            || errorResultList[0].errorType === this.ERROR_DUPLICATE_HIRING_EVENT_NAME_EXCEPTION) {
        var validationDesc = "Unknown Validation Error";
        for (index = 0; index < this.INPUT_ERRORS.length; index++) {
            if (errorResultList[0].componentIdentifier === this.INPUT_ERRORS[index].errorCode) {
                validationDesc = this.INPUT_ERRORS[index].errorDesc;
                break;
            }
        }
        errorMessage = "Validation Error Type: " + errorResultList[0].errorType
                + " Validation Error Desc: " + validationDesc;
    }

    return errorMessage;
};