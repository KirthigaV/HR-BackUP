/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: rsaServices.js
 * Application: Staffing Administration
 *
 * @param inputXml            XML/JSON containing the data which we passed from respective services.
 *                             The GET method has generated data.
 *                             The POST method has to get params and returns the success or failure
 *
 * @param version            Optional parameter, can be used later to change the request/response/logic that gets
 *                             applied.
 *                             Version 1 XML Input/Output,
 *                             version 2 JSON Input/Output
 *
 * @return                    Services return xml/json to the associated callbacks.
 *                             Success callbacks - returns proper data which needs.
 *                             Failure callbacks - returns failure if service get fails
 *                         
 */
/**
 * Method name: rsaServices
 * Service have POST and GET method
 * DataType: JSON
 * Content-Type: JSON
 * Jquery Ajax method has success and error methods
 */
/*Create a object for service*/
var RSASERVICES = new rsaServices();
function rsaServices() {
    /*
     * Service name: validateUserProfile
     * Post method
     * This service will validate the user
     * profile i.e roles
     * Parameters will be sent as null
     *
     **/
    this.validateUserProfile = function(url,userData,sucesscallbackFunction,failureCallBack){
         $.ajax({
             type : CONSTANTS.POST,
             url : url,
             cache : false,
             datatype : CONSTANTS.DATA_TYPE_JSON,
             //send json parameters to service
             data : userData,
             contentType : CONSTANTS.APPLICATION_JSON,
           //Success: return valid JSON data
             success : function(successJson) {
                 sucesscallbackFunction.fire(successJson);
             },
           //Error: Incase of failure should throw exception
             error : function(failureJson) {
                 failureCallBack.fire(failureJson);
             }
         });
    };
    /*
     * Method has post services to get the params
     * and send request to service.
     * Returns success or failure
     */
    this.POSTservices = function(url, data, callback,errorCallbackFunction, useStub, freezeScreen) {
        if (!useStub) {
            if (freezeScreen) {
                UTILITY.screenFreeze();
            }
            $.ajax({
                type : CONSTANTS.POST,
                data : data,
                /*
                 * The data to be sent to the post method is been sent here
                 */
                url : url,
                /*
                 * The url of the service call is being mentioned here
                 */
                cache : false,
                datatype : CONSTANTS.DATA_TYP_JSON,
                /* Specifying the datatype and the content type */
                contentType : CONSTANTS.APPLICATION_JSON,
                success : function(successJson) {
                    /*Calling the callback function*/
                    callback.fire(successJson);
                },
                error : function() {
                    var successJson = CONSTANTS.defaultServiceErrorMsg;
                    /*Calling the callback function*/
                    errorCallbackFunction.fire(successJson);
                }
            });
        }
    };
    this.getUserProfile = function(role, callback,errorCallbackFunction, useStub, freezeScreen,
            screenFreezeMessage) {
        var url = "/RetailStaffing/service/UserProfileService/getUserProfileDetails";
        /*Assigning the role as data to the get user profile service call*/
        var data = {
            "roles" : role
        };
        this.POSTservices(url, data, callback,errorCallbackFunction, useStub, freezeScreen,
                screenFreezeMessage);
    };
    /*
     * Service name: requisitionHiringEvents
     * Get method service and it returns
     * data to populate in UI
     *
     **/
    this.requisitionHiringEvents = function(sucesscallbackFunction, url,
            strNumber) {
        UTILITY.blockFullPage();
        //Jquery ajax service
        $.ajax({
            type : CONSTANTS.GET,
            //Service URL and param to get data for relevant store number
            url : url + "?version=2&storeNumber=" + strNumber,
            cache : false,
            //Param data type is JSON
            datatype : CONSTANTS.DATA_TYPE_JSON,
            //Return Data type is JSON
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success function to get valid data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: activeRequisitionsForStore
     * Get method service and it returns data to populate in UI
     *
     * */
    this.activeRequisitionsForStore = function(sucesscallbackFunction, url,
            strNumber) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            //Service URL and param to get data for relevant store number
            url : url + "?storeNumber=" + strNumber,
            cache : false,
            //Param data type is JSON
            datatype : CONSTANTS.DATA_TYPE_JSON,
            //Return Data type is JSON
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: createRequisitionCalendar
     * POST Method
     * parameters: Json data
     **/
    this.createRequisitionCalendar = function(sucesscallbackFunction, url, data) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.POST,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            //send json parameters to service
            data : data,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: updateCalendar
     * Get Method
     * Params passing with URL
     * */
    this.updateCalendar = function(sucesscallbackFunction, url, activeFlag,
            requisitionCalDes, lastupdateTimeStamp, calendarId) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?activeFlag=" + activeFlag + "&reqnCalDesc="
                    + requisitionCalDes + "&lastUpdateTimestamp="
                    + lastupdateTimeStamp + "&calendarId=" + calendarId,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: validStore
     * Get Method
     * Params passing with URL
     * Service will validate the valid store given by user
     * */
    this.validStore = function(sucesscallbackFunction, url, strNumber) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?storeNumber=" + strNumber,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });

    };
    /*
     * Service name: requisitionCalendars
     * Get Method
     * Params passing with URL
     * Service will return the requisitionCalendars data
     * */
    this.requisitionCalendars = function(sucesscallbackFunction, url, strNumber) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?version=2&storeNumber=" + strNumber,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });

    };
    /*
     * Service name: calendarSummaryGet
     * Get Method
     * Params passing with URL
     * Service will return the calendar summary details
     * */
    this.calendarSummaryGet = function(sucesscallbackFunction, url, beginDate,
            calId, endDate) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?beginDate=" + beginDate + "&calendarId=" + calId
                    + "&endDate=" + endDate,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });

    };
    /*
     * Service name: calendarDetailsGet
     * Get Method
     * Params passing with URL
     * Service will return the calendar day details
     * */
    this.calendarDetailsGet = function(sucesscallbackFunction, url, calId, date) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?calendarId=" + calId + "&date=" + date,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function(){
                 UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: addAvailabilityPost
     * POST Method
     * Parameters:JSON Data
     * Service will add the slot and return the slot affected details
     * */
    this.addAvailabilityPost = function(sucesscallbackFunction, url, data) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.POST,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            data : data,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });

    };
    /*
     * Service name: addAvailabilityPost
     * POST Method
     * Parameters:JSON data
     * Service will delete the slot and return slot affected details
     * */
    this.removeAvailabilityPost = function(sucesscallbackFunction, url, data) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.POST,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            data : data,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: addHiringEventStores
     * GET Method
     * Params will send with URL
     * Service will add the hiring event
     * */
    this.addHiringEventStores = function(callback, url, strNum) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?storeNumber=" + strNum,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: loadstates
     * GET Method
     * Params will send with URL
     *
     **/
    this.loadstates = function(callback, url) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: getHiringEventMgrData
     * GET Method
     * Params will send with URL
     * Service will validate the manager details given by user
     * */
    this.getHiringEventMgrData = function(callback, url, ldap) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?ldapId=" + ldap,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
          //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: checkDuplicateHiringEventName
     * GET Method
     * Params will send with URL
     * Service will validate the duplicate hiring event
     * */
    this.checkDuplicateHiringEventName = function(callback, url, hiringName) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?hiringEventName=" + hiringName,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: createHiringEvent
     * GET Method
     * Params will send with URL
     * Service return the valid hiring event
     * */
    this.createHiringEvent = function(callback, url, inputData) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.POST,
            data : "xml=" + JSON.stringify(inputData),
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: deleteHiringEvent
     * GET Method
     * Params will send with URL
     * Service used to delete the HiringEvent
     * */
    this.deleteHiringEvent = function(sucesscallbackFunction, url,
            completeDesc, reqCalDesc, requisitionCalendarId,
            lastUpdateTimestamp) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?reqnCalDesc=" + completeDesc
                    + "&lastUpdateTimestamp=" + lastUpdateTimestamp
                    + "&calendarId=" + requisitionCalendarId + "&activeFlag="
                    + false,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: editHiringEventDetail
     * GET Method
     * Params will send with URL
     * Service will validate the hiring event details which is edited
     * */
    this.editHiringEventDetail = function(sucesscallbackFunction, url,
            hiringEventId, calId) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?reqnCalId=" + calId + "&hireEventId=" + hiringEventId
                    + "&version=" + 1,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                sucesscallbackFunction.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: checkDuplicateHiringEventNameForedit
     * GET Method
     * Params will send with URL
     * Service will validate the duplicate hiring event details which is edited
     * */
    this.checkDuplicateHiringEventNameForedit = function(callback, url,
            hiringName, hiringeventId) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url + "?hireEventId=" + hiringeventId + "&hiringEventName="
                    + hiringName,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: deleteHiringEventParticipatingStore
     * POST Method
     * Parameters:JSON Data
     * Service will validate the participating store
     * */
    this.deleteHiringEventParticipatingStore = function(callback, url,
            deleterequest) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.POST,
            data : "data=" + JSON.stringify(deleterequest),
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
    /*
     * Service name: deleteHiringEventParticipatingStore
     * POST Method
     * Parameters:JSON data
     * Service will update the HiringEvent
     * */
    this.updateHiringEvent = function(callback, url, inputData) {
        UTILITY.blockFullPage();
        $.ajax({
            type : CONSTANTS.POST,
            data : "xml=" + JSON.stringify(inputData),
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
          //Success: return valid JSON data
            success : function(successJson) {
                UTILITY.unblockFullPage();
                callback.fire(successJson);
            },
            //Error: Incase of failure should throw exception
            error : function() {
                UTILITY.unblockFullPage();
            }
        });
    };
}
