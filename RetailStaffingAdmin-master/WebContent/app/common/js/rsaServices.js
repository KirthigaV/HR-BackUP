/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: rsaServices.js
 * Application: Retail Staffing Admin
 *
 */
var RSASERVICES = new rsaServices();
function rsaServices() {
/*
 * This method is used to get generic URL for RSA service
 */
    this.getRSAUrl = function(methodName) {
        return CONSTANTS.RSA_SERVICE_MAIN_PATH + CONSTANTS.RSA_SERVICE_MID_PATH
                + methodName;
    };
/*
 * This method is used to get Interview Service  URL
 */
    this.getInterviewServiceURL = function(methodName) {
        return CONSTANTS.RSA_SERVICE_MAIN_PATH
                + CONSTANTS.INTERVIEW_SERVICE_MID_PATH + methodName;
    };
/*
 * This method is used to get Hiring Event Service URL
 */
    this.getHiringEventServiceURL = function(methodName) {
        return CONSTANTS.RSA_SERVICE_MAIN_PATH
                + CONSTANTS.HIRING_SERVICE_MID_PATH + methodName;
    };
    /*
     * This method is used to resend Queweb Email POST service call
     */
    this.resendQueWebEmail = function(requisitionNumber, url,
            sucesscallbackFunction, errorcallbackFunction) {
        var formData = {
            "inputData" : requisitionNumber
        };
        $.ajax({

            type : CONSTANTS.POST,
            url : url,
            cache : false,
            crossDomain : true,
            data : formData,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            success : function(successJson) {
                //Success
                sucesscallbackFunction.fire(successJson);
            },
            error : function() {
                //Error
                errorcallbackFunction.fire(errorThrown);
            }
        });

    };
    /*
     * This method is used to do POST AJAX calls
     */
    this.callMethodServicePOST = function(data, url, sucesscallbackFunction,
            errorcallbackFunction, stubdata) {
        var isStub = false;
        if (!isStub) {
            $.ajax({
                type : CONSTANTS.POST,
                url : url,
                cache : false,
                datatype : CONSTANTS.DATA_TYPE_JSON,
                data : data,
                contentType : CONSTANTS.APPLICATION_JSON,
                success : function(successJson) {
                    //Success
                    sucesscallbackFunction.fire(successJson);

                },
                error : function() {
                    //Error handle
                    errorcallbackFunction.fire(errorThrown);
                }
            });
        } else {
            //Success for stub
            sucesscallbackFunction.fire(stubdata);
        }

    };
    /*
     * This method is used to call POST AJAX URL encoded calls
     */
    this.callQPMethodPOST = function(data, url, sucesscallbackFunction,
            errorcallbackFunction) {
        this.blockFullPage();
        return $.ajax({
            type : CONSTANTS.POST,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            data : data,
            contentType : CONSTANTS.FORM_URLENCODED,
            success : function(successJson) {
                //Success
                //this.unblockFullPage();
                sucesscallbackFunction(successJson);

            }.bind(this),
            error : function(errorThrown) {
                //Error
                //this.unblockFullPage();
                errorcallbackFunction(errorThrown);
            }.bind(this)
        });
    };
    /*
     * This method is used to call POST AJAX calls
     */
    this.callMethodPOSTNoBlock = function(data, url, sucesscallbackFunction,
            errorcallbackFunction) { 
        $.ajax({
            type : CONSTANTS.POST,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            data : data,
            contentType : CONSTANTS.APPLICATION_JSON,
            success : function(successJson) {
            	//Success                     
                sucesscallbackFunction(successJson);
            }.bind(this),
            error : function(errorThrown) {
                //Error
                errorcallbackFunction(errorThrown);
            }.bind(this)
        });
    };
    /*
     * This method is used to call POST AJAX calls
     */
    this.callMethodPOST = function(data, url, sucesscallbackFunction,
            errorcallbackFunction) {  	
    	        this.blockFullPage();    	
        $.ajax({
            type : CONSTANTS.POST,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            data : data,
            contentType : CONSTANTS.APPLICATION_JSON,
            success : function(successJson) {
            	//Success
                this.unblockFullPage();            
                sucesscallbackFunction(successJson);

            }.bind(this),
            error : function(errorThrown) {
                //Error            	
                this.unblockFullPage();            	
                errorcallbackFunction(errorThrown);
            }.bind(this)
        });
    };
    /*
     * This method is used to call GET AJAX calls
     */
    this.callMethodGET = function(url, sucesscallbackFunction,
            errorcallbackFunction) {
        this.blockFullPage();
        $.ajax({
            type : CONSTANTS.GET,
            url : url,
            cache : false,
            datatype : CONSTANTS.DATA_TYPE_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            success : function(successJson) {
                //Success
                this.unblockFullPage();
                sucesscallbackFunction(successJson);

            }.bind(this),
            error : function(errorThrown) {
                //Error
                this.unblockFullPage();
                errorcallbackFunction(errorThrown);
            }.bind(this)
        });
    };
    /*
     * This method is used to call POST AJAX calls and supports Stub as well
     */
    this.POSTservices = function(url, data, callback, useStub, freezeScreen) {
        if (!useStub) {
            if (freezeScreen) {
                UTILITY.screenFreeze();
            }
            $.ajax({
                type : "POST",
                data : data,
                url : url,
                cache : false,
                datatype : CONSTANTS.DATA_TYP_JSON,
                contentType : CONSTANTS.APPLICATION_JSON,
                success : function(successJson) {
                    //Success
                    callback.fire(successJson);
                },
                error : function() {
                    //Error
                    $.unblockUI();
                }
            });
        } else {
            //Success JSON Stub
            callback.fire(JSON_STUB.partnerViewPartnerTypeList);
        }
    };
    /*
     * This method is used to get Summary details
     */
    this.getSummaryResults = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/getSummaryResults";
        var data1 = "data=" + data;
        this.POSTservices(url, data1, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get Candidate details
     */
    this.getcandidateDetails = function(callback, data) {
        var data1 = data;
        var url = "/RetailStaffing/service/RetailStaffingService/getCandidateDetails";
        this.POSTservices(url, data1, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get Review Phone screen details
     */
    this.getRevPhnScrnDetails = function(callback, data) {
        var data1 = {
            data : data
        };
        var url = "/RetailStaffing/service/RetailStaffingService/getRevPhnScrnDetails";
        this.POSTservices(url, data1, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to save Review Phone screen Summary details
     */
    this.saveRevPhnScrnDtl = function(callback, data) {
        var data1 = "data=" + data;
        var url = "/RetailStaffing/service/RetailStaffingService/saveRevPhnScrnDtl";
        this.POSTservices(url, data1, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get userprofile details
     */
    this.getUserProfileDetailss = function(role, callback, useStub,
            freezeScreen) {

        var url = "/RetailStaffing/service/UserProfileService/getUserProfileDetails";
        var data = {
            "roles" : role
        };
        var data1 = "data=" + data;
        this.POSTservices(url, data1, callback, useStub, freezeScreen);
    };
    /*
     * This method is used to get locale information
     */
    this.getLocaleDetails = function(callback) {
        var url = "/RetailStaffing/service/RetailStaffingService/getLocaleDetails";
        this.GETservices(url, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get search details
     */
    this.getSearchDetails = function(callback, data, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "inputData=" + JSON.stringify(data),
                    url : "/RetailStaffing/service/RetailStaffingService/getSearchDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    this.GETservices = function(url, callback, useStub, freezeScreen) {
        if (!useStub) {
            if (freezeScreen) {
                UTILITY.screenFreeze();
            }
            $.ajax({
                type : "GET",
                url : url,
                cache : false,
                datatype : CONSTANTS.DATA_TYP_JSON,
                contentType : CONSTANTS.APPLICATION_JSON,
                success : function(successJson) {
                    //Success
                    callback.fire(successJson);
                },
                error : function() {
                    //Error
                    $.unblockUI();
                }
            });
        } else {
            callback.fire(JSON_STUB.partnerViewPartnerTypeList);
        }
    };
    /*
     * This function is used to get valid store
     */
    this.validStore = function(callback) {
        callback.fire(JSON_STUB.validStore);

    };
    /*
     * This function is used to get requisition calendar details
     */
    this.requisitionCalendars = function(callback) {
        callback.fire(JSON_STUB.calendarResult);
    };
    /*
     * This function is used to get requisition details
     */
    this.getRequisitionDetails = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this.getRSAUrl(CONSTANTS.GET_REQ_DTL_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to getStore Hiring event list
     */
    this.getStoreHiringEventList = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodGET(this
                .getInterviewServiceURL(CONSTANTS.GET_STR_HIRING_EVENTS_PATH)
                + "?storeNumber=" + data, sucesscallbackFunction,
                errorcallbackFunction);
    };
    /*
     * This function is used to get Store calender list
     */
    this.getStoreCalendarList = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodGET(this
                .getInterviewServiceURL(CONSTANTS.GET_STR_CALENDARS_PATH)
                + "?storeNumber=" + data, sucesscallbackFunction,
                errorcallbackFunction);
    };
    /*
     * This function is used to get Hiring event details
     */
    this.getHiringEventDetails = function(data, callback) {
        $.ajax({
            type : "GET",
            url : this.getHiringEventServiceURL(CONSTANTS.GET_HIRING_EVENT_DETAILS_PATH)+ "?hireEventId="+ data,
            cache : false,
            datatype : CONSTANTS.DATA_TYP_JSON,
            contentType : CONSTANTS.APPLICATION_JSON,
            success : function(successJson) {
                //Success
                callback.fire(successJson);
            },
            error : function() {
                //Error
                $.unblockUI();
            }
        });
    };
    /*
     * This function is used to get Hiring event details
     */
    this.getHiringEventDetailsEvtID = function(data, sucesscallbackFunction, errorcallbackFunction) {
        this
                .callMethodGET(
                        this
                                .getHiringEventServiceURL(CONSTANTS.GET_HIRING_EVENT_DETAILS_PATH)
                                + "?hireEventId="
                                + data,
                        sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to save requisition screen details
     */
    this.saveReqScrnDtls = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this.getRSAUrl(CONSTANTS.SAVE_REQ_DTL_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to create phone screen details
     */
    this.createPhoneScr = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data,
                this.getRSAUrl(CONSTANTS.CREATE_PHN_SCR_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to get candidate count
     */
    this.getCandidateCount = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this.getRSAUrl(CONSTANTS.GET_CAND_COUNT),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to getStaffing Associate Pool details
     */
    this.getRetailStaffingViewAssociatePool = function(data,
            sucesscallbackFunction, errorcallbackFunction) {
        return this.callQPMethodPOST(data,
                "/QualifiedPool/service/qualifiedpool/associates?version=2",
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to getStaffing Applicant pool details
     */
    this.getRetailStaffingViewApplicantPool = function(data,
            sucesscallbackFunction, errorcallbackFunction) {
        return this.callQPMethodPOST(data,
                "/QualifiedPool/service/qualifiedpool/applicants?version=2",
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to mark applicant as considered
     */
    this.markApplicantsAsConsideredInQPEvent = function(data,
            sucesscallbackFunction, errorcallbackFunction) {
        this.callMethodPOSTNoBlock(data, this
                .getRSAUrl(CONSTANTS.MARK_APPLICANTS_AS_CONSIDERED_IN_QP_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This function is used to attach applicant to requisition
     */
    this.retailStaffingAttachApplToReq = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this
                .getRSAUrl(CONSTANTS.ATTACH_APPL_TO_REQ_EVENT_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to load candidate details
     */
    this.loadCandidateDetails = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this
                .getRSAUrl(CONSTANTS.VIEW_APPL_PROFILE_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to load rejected details
     */
    this.loadRejectDetails = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodGET(this.getRSAUrl(CONSTANTS.GET_REJECT_METHD),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to create rejected details
     */
    this.createRejectDetails = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOSTNoBlock(data, this
                .getRSAUrl(CONSTANTS.CREATE_RJT_DETAILS_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to get phone screen details
     */
    this.getPhoneScreenDetailsPopUp = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this
                .getRSAUrl(CONSTANTS.GET_PHN_SCRN_DETAILS_POPUP_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to  get user profile details
     */
    this.getUserProfile = function(callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + JSON.stringify(saveDetails),
                    url : "/RetailStaffing/service/UserProfileService/getUserProfileDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to create phone screen details
     */
    this.createPhoneScr = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data,
                this.getRSAUrl(CONSTANTS.CREATE_PHN_SCR_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to save requisition screen details
     */
    this.saveReqScrnDtls = function(data, sucesscallbackFunction,
            errorcallbackFunction) {
        this.callMethodPOST(data, this.getRSAUrl(CONSTANTS.SAVE_REQ_DTL_PATH),
                sucesscallbackFunction, errorcallbackFunction);
    };
    /*
     * This method is used to load RetailStaffing Request screen
     */
    this.loadRetailStaffingRequestScreen = function(callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/RetailStaffing/service/RetailStaffingService/loadReqRequestForm",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get department details by store
     */
    this.getDeptsByStr = function(callback, strNumber, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + strNumber,
                    url : "/RetailStaffing/service/RetailStaffingService/getDeptDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get store calendars
     */
    this.getStoreCalendars = function(callback, strNum, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/RetailStaffing/service/InterviewService/requisitionCalendars?storeNumber="
                            + strNum,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get job title store by department
     */
    this.getJobTtlByStrByDept = function(callback, inputData, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + JSON.stringify(inputData),
                    url : "/RetailStaffing/service/RetailStaffingService/getJobTtlDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get phonescreen details
     */
    this.getPhnScreenDetails = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/getPhoneScreenDetails";
        var data1 = "data=" + data;
        this.POSTservices(url, data1, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to update phone screen details
     */
    this.updatePhnScreenDetails = function(inputData, callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : inputData,
                    url : "/RetailStaffing/service/RetailStaffingService/submitITIDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get hiring event calender details
     */
    this.getHiringEventCalendars = function(callback, strNum, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/RetailStaffing/service/InterviewService/requisitionHiringEvents?storeNumber="
                            + strNum,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get interview Screen Details
     */
    this.getIntervwScreenDetails = function(inputData, callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + inputData,
                    url : "/RetailStaffing/service/RetailStaffingService/getInterviewDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to add hiring event for store
     */
    this.addHiringEventStores = function(callback, strNum, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/StaffingForms/service/StoreService/getStoreDetails?storeNumber="
                            + strNum,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to check if Hiring event name already exist
     */
    this.checkDuplicateHiringEventName = function(callback, hiringName,
            freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/StaffingForms/service/HiringEventService/checkDuplicateHiringEventName?hiringEventName="
                            + hiringName,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to create Hiring Event
     */
    this.createHiringEvent = function(callback, inputData, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : inputData,
                    url : "/StaffingForms/service/HiringEventService/createHiringEvent",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get Hiring event store details
     */
    this.getHiringEventStoreDetails = function(callback, strNum, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/StaffingForms/service/StoreService/getStoreDetails?storeNumber="
                            + strNum,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //SUccess
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get Hiring event manager details
     */
    this.getHiringEventMgrData = function(callback, ldap, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/RetailStaffing/service/HiringEventService/getHiringEventMgrData?ldapId="
                            + ldap,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to create requistion calendar
     */
    this.createRequisitionCalendar = function(callback, storeNum, calName,
            reqNbr, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "name=" + calName + "&storeNumber=" + storeNum
                            + "&type=" + 10,
                    url : "/RetailStaffing/service/InterviewService/createRequisitionCalendar",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to submit requisition screen details
     */
    this.submitReqScrnDtls = function(callback, saveDetails, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : saveDetails,
                    url : "/RetailStaffing/service/RetailStaffingService/submitReqScrnDtls",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get interview results load screen
     */
    this.getInterviewResultsLoadScreen = function(callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/RetailStaffing/service/RetailStaffingService/getInterviewResultsLoadScreen",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Sucess
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get interviewresults request by store
     */
    this.getInterviewResultsReqsByStr = function(strNum, callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + strNum,
                    url : "/RetailStaffing/service/RetailStaffingService/getInterviewResultsReqsByStr",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get interview results by candidates
     */
    this.getInterviewResultsCandidatesByReq = function(reqNbr, callback,
            freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + reqNbr,
                    url : "/RetailStaffing/service/RetailStaffingService/getInterviewResultsCandidatesByReq",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get Interview results by candidate
     */
    this.getInterviewResultsCandidateIntvwDtls = function(inputData, callback,
            freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + JSON.stringify(inputData),
                    url : "/RetailStaffing/service/RetailStaffingService/getInterviewResultsCandidateIntvwDtls",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to update interview results by candidate
     */
    this.updateInterviewResultsCandidateIntvwDtls = function(inputData,
            callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : inputData,
                    url : "/RetailStaffing/service/RetailStaffingService/updateInterviewResultsCandidateIntvwDtls",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get available interview times
     */
    this.getAvailableInterviewTimes = function(myReqCalId, intrvDuration,
            rscSchFlag, callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.GET,
                    url : "/RetailStaffing/service/RetailStaffingService/getAvailableInterviewTimes?reqCalId="
                            + myReqCalId
                            + "&rscSchdFlg="
                            + rscSchFlag
                            + "&interviewDuration=" + intrvDuration,
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Sucess
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to submit interview screen details
     */
    this.submitInterviewScrnDetails = function(inputData, callback,
            freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : inputData,
                    url : "/RetailStaffing/service/RetailStaffingService/submitInterviewScrnDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    /*
     * This method is used to get store details
     */
    this.getStoreDetails = function(storeNo, inputData, callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + storeNo + "&interviewDate="
                            + JSON.stringify(inputData),
                    url : "/RetailStaffing/service/RetailStaffingService/getStoreDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }

                });
    };
    /*
     * This method is used to get store phone screen details
     */
    this.getStoreDetailsPhnScrn = function(storeNo, callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + storeNo,
                    url : "/RetailStaffing/service/RetailStaffingService/getStoreDetails",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }

                });
    };
    /*
     * This method is used to block page as per flex UI
     */
    this.blockFullPage = function() {
        $("body").block({
            message : null,
            overlayCSS : {
                backgroundColor : "rgb(139,139,139)",
                opacity : 0.6,
                cursor : "wait"
            },
            ignoreIfBlocked : false
        });
    };
    /*
     * This method is used to unblock page as per flex UI
     */
    this.unblockFullPage = function() {
        $("body").unblock();
    };
    /*
     * This method is used to get store driver license exempt
     */
    this.getStoresDriverLicenseExempt = function (callback)
    {
        var url = "/RetailStaffing/service/RetailStaffingService/getStoresDriverLicenseExempt";
        this.GETservices(url,callback,false,true,"Please Wait...");
    };
    /*
     * This method is used to check QP pilot status
     */
    this.checkQpPilotStatus = function (callback)
    {
        var url = "/RetailStaffing/service/RetailStaffingService/checkQpPilotStatus";
        this.GETservices(url,callback,false,true,"Please Wait...");
    };
    this.getAllInterviewStatuses = function(callback, inputData,
            freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : "data=" + inputData,
                    url : "/RetailStaffing/service/RetailStaffingService/getAllInterviewStatuses",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
    
    /*
     * This method is used to submit the Background Check Consent/Check by candidate
     */
    this.submitBackgroundConsentByCandidate = function(inputData,
            callback, freezeScreen) {
        if (freezeScreen) {
            UTILITY.screenFreeze();
        }
        $
                .ajax({
                    type : CONSTANTS.POST,
                    data : inputData,
                    url : "/RetailStaffing/service/RetailStaffingService/submitBackgroundConsentByCandidate",
                    cache : false,
                    datatype : CONSTANTS.DATA_TYPE_JSON,
                    contentType : CONSTANTS.APPLICATION_JSON,
                    success : function(successJson) {
                        //Success
                        callback.fire(successJson);
                    },
                    error : function() {
                        //Error
                        $.unblockUI();
                    }
                });
    };
}
