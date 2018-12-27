var applicantProfileObj = new applicantProfile();
var reqDet = new requisitionDetails();
var CONSTANTS = new constants();
var retailStaffingObj = new retailStaffing();

QUnit.module("applicantProfile", {

	setup : function(assert) {
		fixture = document.getElementById('qunit-fixture');
    var modalPop = document.createElement('div');
    var modalBody = document.createElement('div');
    var qpapplGrid = document.createElement('div');
    var qpassocGrid = document.createElement('div');
    modalPop.setAttribute('id','alertModalId');
    modalBody.setAttribute('class','alertModalBody');
    qpapplGrid.setAttribute('id','qpapplGrid');
    qpassocGrid.setAttribute('id','qpassocGrid');
    modalPop.appendChild(modalBody);
    fixture.appendChild(modalPop);
    fixture.appendChild(qpapplGrid);
    fixture.appendChild(qpassocGrid);
    CONSTANTS.retailStaffingObj = retailStaffing;
	},

	teardown : function(assert) {
		$.unblockUI();
    $('#alertModalId').hide();
	}
});

/* Defining the static options that needs to be displayed in the screen */

QUnit.test("initialize", function(assert){
	var expected = true;
	var actual = false;
  var secondCall = false;
  var thirdCall = false;
	$.ajax = function(options) {
		var responseOne = {
			"Response":{
        "rejComboRes":{  
         "rejectionReason":[  
            {  
               "employmentPositionCandidateRejectReasonCode":0,
               "lastUpdateSystemUserId":"GXN5764                 ",
               "lastUpdateTimestamp":"2013-01-03 13:14:14.803427",
               "displayReasonCode":"Viewed    ",
               "shortReasonDescription":"Viewed Not Selected ",
               "reasonDescription":"Viewed Not Selected"
            }
         ]
        }
      }
		};
    var responseTwo = {
    		"Response" : {  
    			   "status":"SUCCESS",
    			   "PersonalInfoList":{  
    			      "PersonalInfo":{  
    			         "applType":"AS",
    			         "fullName":"ESPARSA,LOUIS F",
    			         "phoneNum":"404-433-8211",
    			         "address1":"2455 PACES FERRY RD",
    			         "address2":"APT#10206",
    			         "cityStateZip":"SMYRNA, GA 30080",
    			         "applID":113948055,
    			         "candRefNbr":194960
    			      }
    			   },
    			   "AssociateInfoList":{  
    			      "AssociateInfo":{  
    			         "currStore":9090,
    			         "currDept":873,
    			         "currTitle":"SSDBAD",
    			         "currStatus":"FS",
    			         "hireDate":"2010-09-20"
    			      }
    			   },
    			   "AssociateReviewList":{  
    			      "ReviewResults":[  
    			         {  
    			            "date":"2012-05-01",
    			            "results":4
    			         }
    			      ]
    			   },
    			   "AssociatePrePosList":{  
    			      "PreviousPosition":[  
    			         {  
    			            "dept":873,
    			            "position":"SSDBAD",
    			            "dateFrom":"2012-04-16",
    			            "dateTo":"2099-12-31"
    			         }
    			      ]
    			   },
    			   "EducationList":{  
    			      "Education":[  
    			         {  
    			            "schoolName":"CAMBRIDGE",
    			            "education":"MASTERS",
    			            "graduate":"Y"
    			         }
    			      ]
    			   },
    			   "WorkHistoryList":{  
    			      "WorkHistory":[  
    			         {  
    			            "company":"MAN U",
    			            "position":"CASHIER",
    			            "location":"",
    			            "supervisor":"",
    			            "comDateFrom":"2009-01-01",
    			            "comDateTo":"2010-01-01",
    			            "payAtLeaving":"0.00",
    			            "supervisorTitle":"",
    			            "reasonLeaving":""
    			         }
    			      ]
    			   },
    			   "LanguageList":{  
    			      "LanguageSet":{  
    			         "language":"AS,ENGLISH,"
    			      }
    			   },
    			   "SkillList":{  
    			      "SkillSet":[  
    			         {  
    			            "skill":"Carpentry"
    			         }
    			      ]
    			   },
    			   "PhnScreenHistoryList":{  
    			      "PhnScreenHistory":{  
    			         "requisitionNum":100063616,
    			         "storeNum":1301,
    			         "deptNum":"091",
    			         "job":"HOFASC",
    			         "phnScreenStatus":"PHN SCREEN COMPLETED",
    			         "status":"INTERVIEW SCHEDULED",
    			         "phnScreenType":"DETAILED",
    			         "lastUpdate":"2014-08-04"
    			      }
    			   },
    			   "CandidateHistoryList":{  
    			      "CandidateHistory":{  
    			         "reqNumber":150166570,
    			         "store":1301,
    			         "dept":"091",
    			         "title":"OFFICE ASSOCIATE",
    			         "intvDisposition":"",
    			         "offerStatus":"",
    			         "offerResults":""
    			      }
    			   },
    			   "ApplicationHistoryList":{  
    			      "ApplicationHistory":[  
    			         {  
    			            "store":"0117",
    			            "lastUpdatedS":"2013-08-09 13:45:28.744"
    			         }
    			      ]
    			   },
    			   "ApplicationHistoryList2":{  
    			      "ApplicationHistory2":[  
    			         {  
    			            "position":"DPTSUP",
    			            "dept":"021",
    			            "lastUpdatedP":"2014-06-20 13:59:41.050682"
    			         }
    			      ]
    			   }
    			}
    };
    var responseThree = {
      "Response":{  
         "status":"SUCCESS"
      }
    }; 
    
	options.callOneSuccess = function(data) {
          CONSTANTS.retailStaffingObj.applicantObj = {
            id : '12345',
            type : 'test',
            applDate : '06/12/2015'
          };
          applicantProfileObj.loadRejectDetailsSuccess(data);
    };
    options.callTwoSuccess = function(data) {
          reqDet.model.LoadREQDGDltls = [{reqNbr:"54321"}];
          applicantProfileObj.loadCandidateDetailsSuccess(data);
    };
    options.callThreeSuccess = function(data) {
          applicantProfileObj.model.rjctSaveCheck = false;
          applicantProfileObj.createRejectDetailsSuccess(data);
    };
            
    if (!secondCall && !thirdCall){
      secondCall = true;
      options.callOneSuccess(responseOne);
    } 
    else if (secondCall && !thirdCall){
      thirdCall = true;
      options.callTwoSuccess(responseTwo);
    }
    else {
      options.callThreeSuccess(responseThree);
    }
        
    };

	applicantProfileObj.initializePage();
  if ($("#reviewResultsGrid .r0").text() === "2012-05-01"
      && $("#prevPositionGrid .even .r0").text() === "873"
      && $("#phnScrHistGrid .r1").text() === "1301"
      && $("#candStatHistGrid .r3").text() === "OFFICE ASSOCIATE"
      && $("#appHistGrid1 .r0").text() === "0117"
      && $("#appHistGrid2 .r0").text() === "DPTSUP"
      && $("#alertModalId .alertModalBody").text() === 'Details Saved Successfully'
    ) {
    actual = true;
  }
	assert.equal(actual,expected,"Initialize Success");
});

QUnit.test("initialize", function(assert){
  var expected = true;
  var actual = false;
  var secondCall = false;
  var thirdCall = false;
  $.ajax = function(options) {
    var responseOne = {
      "Response":{
        "rejComboRes":{  
         "rejectionReason":[  
            {  
               "employmentPositionCandidateRejectReasonCode":0,
               "lastUpdateSystemUserId":"GXN5764                 ",
               "lastUpdateTimestamp":"2013-01-03 13:14:14.803427",
               "displayReasonCode":"Viewed    ",
               "shortReasonDescription":"Viewed Not Selected ",
               "reasonDescription":"Viewed Not Selected"
            }
         ]
        }
      }
    };
    var responseTwo = {
        "Response" : {  
             "status":"SUCCESS",
             "PersonalInfoList":{  
                "PersonalInfo":{  
                   "applType":"AS",
                   "fullName":"ESPARSA,LOUIS F",
                   "phoneNum":"404-433-8211",
                   "address1":"2455 PACES FERRY RD",
                   "address2":"APT#10206",
                   "cityStateZip":"SMYRNA, GA 30080",
                   "applID":113948055,
                   "candRefNbr":194960
                }
             },
             "AssociateInfoList":{  
                "AssociateInfo":{  
                   "currStore":9090,
                   "currDept":873,
                   "currTitle":"SSDBAD",
                   "currStatus":"FS",
                   "hireDate":"2010-09-20"
                }
             },
             "AssociateReviewList":{  
                "ReviewResults":[  
                   {  
                      "date":"2012-05-01",
                      "results":4
                   }
                ]
             },
             "AssociatePrePosList":{  
                "PreviousPosition":[  
                   {  
                      "dept":873,
                      "position":"SSDBAD",
                      "dateFrom":"2012-04-16",
                      "dateTo":"2099-12-31"
                   }
                ]
             },
             "EducationList":{  
                "Education":[  
                   {  
                      "schoolName":"CAMBRIDGE",
                      "education":"MASTERS",
                      "graduate":"Y"
                   }
                ]
             },
             "WorkHistoryList":{  
                "WorkHistory":[  
                   {  
                      "company":"MAN U",
                      "position":"CASHIER",
                      "location":"",
                      "supervisor":"",
                      "comDateFrom":"2009-01-01",
                      "comDateTo":"2010-01-01",
                      "payAtLeaving":"0.00",
                      "supervisorTitle":"",
                      "reasonLeaving":""
                   }
                ]
             },
             "LanguageList":{  
                "LanguageSet":{  
                   "language":"AS,ENGLISH,"
                }
             },
             "SkillList":{  
                "SkillSet":[  
                   {  
                      "skill":"Carpentry"
                   }
                ]
             },
             "PhnScreenHistoryList":{  
                "PhnScreenHistory":{  
                   "requisitionNum":100063616,
                   "storeNum":1301,
                   "deptNum":"091",
                   "job":"HOFASC",
                   "phnScreenStatus":"PHN SCREEN COMPLETED",
                   "status":"INTERVIEW SCHEDULED",
                   "phnScreenType":"DETAILED",
                   "lastUpdate":"2014-08-04"
                }
             },
             "CandidateHistoryList":{  
                "CandidateHistory":{  
                   "reqNumber":150166570,
                   "store":1301,
                   "dept":"091",
                   "title":"OFFICE ASSOCIATE",
                   "intvDisposition":"",
                   "offerStatus":"",
                   "offerResults":""
                }
             },
             "ApplicationHistoryList":{  
                "ApplicationHistory":[  
                   {  
                      "store":"0117",
                      "lastUpdatedS":"2013-08-09 13:45:28.744"
                   }
                ]
             },
             "ApplicationHistoryList2":{  
                "ApplicationHistory2":[  
                   {  
                      "position":"DPTSUP",
                      "dept":"021",
                      "lastUpdatedP":"2014-06-20 13:59:41.050682"
                   }
                ]
             }
          }
    };
    var responseThree = {
      "Response":{  
         "status":"SUCCESS"
      }
    }; 
    
  options.callOneSuccess = function(data) {
          CONSTANTS.retailStaffingObj.applicantObj = {
            id : '12345',
            type : 'test',
            applDate : '06/12/2015'
          };
          applicantProfileObj.loadRejectDetailsFail(data);
    };
    options.callTwoSuccess = function(data) {
          reqDet.model.LoadREQDGDltls = [{reqNbr:"54321"}];
          applicantProfileObj.loadCandidateDetailsSuccess(data);
    };
    options.callThreeSuccess = function(data) {
          applicantProfileObj.model.rjctSaveCheck = false;
          applicantProfileObj.createRejectDetailsSuccess(data);
    };
            
    if (!secondCall && !thirdCall){
      secondCall = true;
      options.callOneSuccess(responseOne);
    } 
    else if (secondCall && !thirdCall){
      thirdCall = true;
      options.callTwoSuccess(responseTwo);
    }
    else {
      options.callThreeSuccess(responseThree);
    }
        
    };

  applicantProfileObj.initializePage();
  if ($("#reviewResultsGrid .r0").text() === "2012-05-01"
      && $("#prevPositionGrid .even .r0").text() === "873"
      && $("#phnScrHistGrid .r1").text() === "1301"
      && $("#candStatHistGrid .r3").text() === "OFFICE ASSOCIATE"
      && $("#appHistGrid1 .r0").text() === "0117"
      && $("#appHistGrid2 .r0").text() === "DPTSUP"
      && $("#alertModalId .alertModalBody").text() === 'Details Saved Successfully'
    ) {
    actual = true;
  }
  assert.equal(actual,expected,"Initialize Success on loadRejectDetails failure");
});

QUnit.test("attachReqChange", function(assert){
  var expected = true;
  var actual = false;
  $("[class='attachreq'][value='Y']").attr('checked','true');
  applicantProfileObj.attachReqChange();
  actual = $("[class='rjctRsncb'][disabled]").length > 0 ? true : false;
  assert.equal(actual,expected,"attachReqChange - Attach To Requisition Selected - Success");
});

QUnit.test("attachReqChange", function(assert){
  var expected = true;
  var actual = false;
  $("[class='attachreq'][value='N']").attr('checked','true');
  applicantProfileObj.attachReqChange();
  actual = $("[class='rjctRsncb'][disabled]").length > 0 ? false : true;
  assert.equal(actual,expected,"attachReqChange - Not Attach To Requisition Selected - Success");
});

QUnit.test("showPhoneDetails", function(assert){
  var expected = true;
  var actual = false;

  $.ajax = function(options) {
    var response = {
    			   "Response":{  
    			      "status":"SUCCESS",
    			      "ITIDetailList":{  
    			         "PhoneScreenIntrwDetail":{  
    			            "reqNbr":150166570,
    			            "cndtNbr":113948055,
    			            "phnScrnDate":{  
    			               "month":"08",
    			               "day":"04",
    			               "year":2014
    			            },
    			            "phnScrnTime":{  
    			               "month":"08",
    			               "day":"04",
    			               "year":2014,
    			               "hour":14,
    			               "minute":"05",
    			               "second":"00",
    			               "milliSecond":0
    			            },
    			            "phnScreener":"QAT2780",
    			            "title":"SSDBAD",
    			            "itiNbr":100063616,
    			            "ynstatus":12,
    			            "overAllStatus":0,
    			            "detailTxt":"test",
    			            "MinimumResponseDtlList":[  
    			               {  
    			                  "MinimumResponseDtl":[  
    			                     {  
    			                        "seqNbr":1,
    			                        "minimumResponse":"Yes"
    			                     }
    			                  ]
    			               }
    			            ],
    			            "aid":113489132,
    			            "name":"ESPARSA, LOUIS, F",
    			            "scrTyp":"DETAILED",
    			            "cndStrNbr":9090,
    			            "cndDeptNbr":873,
    			            "contactHistoryTxt":"test",
    			            "emailAdd":"SANJAY.LOHANI@GMAIL.COM",
    			            "interviewStatusTime":{  
    			               "month":"08",
    			               "day":"04",
    			               "year":2014,
    			               "hour":14,
    			               "minute":"05",
    			               "second":26,
    			               "milliSecond":333358
    			            },
    			            "phoneScreenStatusTimestamp":{  
    			               "month":"08",
    			               "day":"04",
    			               "year":2014,
    			               "hour":14,
    			               "minute":"05",
    			               "second":26,
    			               "milliSecond":29263
    			            },
    			            "phoneScreenStatusCode":4,
    			            "interviewStatusCode":11,
    			            "interviewMaterialStatusCode":2,
    			            "phoneScreenDispositionCode":0,
    			            "candRefNbr":194960
    			         }
    			      },
    			      "rejectedReasonId":0,
    			      "PhoneScreenDispositionList":{  
    			         "PhoneScreenDispositionDetail":[  
    			            {  
    			               "dispositionCode":0,
    			               "dispositionDesc":"N\/A"
    			            },
    			            {  
    			               "dispositionCode":1,
    			               "dispositionDesc":"RECOMMEND"
    			            },
    			            {  
    			               "dispositionCode":2,
    			               "dispositionDesc":"HIGHLY RECOMMEND"
    			            },
    			            {  
    			               "dispositionCode":3,
    			               "dispositionDesc":"DO NOT RECOMMEND"
    			            }
    			         ]
    			      }
    			   }
    };
    options.success = function(data) {
          applicantProfileObj.getPhoneScreenDetailsPopUpSuccess(data);
        };
        options.success(response);
    };

  var reqNumLink = document.createElement('a');
  reqNumLink.setAttribute('class','reqNumLink');
  reqNumLink.setAttribute('data-value','9876452310');
  fixture.appendChild(reqNumLink);

  var phnScrDetpopup = document.createElement('div');
  var phnScrDisp = document.createElement('select');
  phnScrDisp.setAttribute('class','phnScrDisp');
  phnScrDetpopup.setAttribute('id','phnScrDetpopup');
  phnScrDetpopup.appendChild(phnScrDisp);
  fixture.appendChild(phnScrDetpopup);

  var selectCont = document.createElement('div');
  var minReq = document.createElement('select');
  selectCont.setAttribute('class','selectCont');
  minReq.setAttribute('class','minReq0');
  selectCont.appendChild(minReq);
  phnScrDetpopup.appendChild(selectCont);

  var contacthistTextarea = document.createElement('div');
  var phnDtlResp = document.createElement('div');
  var phnScrDate = document.createElement('span');
  var phnScrTime = document.createElement('span');
  var phnScrner = document.createElement('span');
  contacthistTextarea.setAttribute('id','contacthistTextarea'); 
  phnDtlResp.setAttribute('id','phnDtlResp');
  phnScrDate.setAttribute('class','phnScrDate');
  phnScrTime.setAttribute('class','phnScrTime');
  phnScrner.setAttribute('class','phnScrner');
  phnScrDetpopup.appendChild(contacthistTextarea);
  phnScrDetpopup.appendChild(phnDtlResp);
  phnScrDetpopup.appendChild(phnScrDate);
  phnScrDetpopup.appendChild(phnScrTime);
  phnScrDetpopup.appendChild(phnScrner);

  applicantProfileObj.createPhnScrHistGrid();
  $(".reqNumLink").trigger('click');

  if ($("#phnScrDetpopup #contacthistTextarea").val() === 'test' &&
      $("#phnScrDetpopup .phnScrDisp option").length === 4 &&
      $("#phnScrDetpopup #phnDtlResp").val() === "test" &&
      $("#phnScrDetpopup .phnScrDate").text() === "08/04/2014" &&
      $("#phnScrDetpopup .phnScrTime").text() === "2:05 PM" &&
      $("#phnScrDetpopup .phnScrner").text() === "QAT2780" &&
      $("#phnScrDetpopup .phnScrDisp").val() === "3") {
    actual = true;
  }
  assert.equal(actual,expected,"showPhoneDetails - Success");
});

QUnit.test("showApplUnavail", function(assert){
  var expected = true;
  var actual = false;
  reqDet.model._hasDuplicateAppl = "true";
  reqDet.model.LoadApplUnavailInfo = [{applUnavailName:"test"}];
  applicantProfileObj.showApplUnavail();
  
  if (reqDet.model._hasDuplicateAppl === "" && reqDet.model.LoadApplUnavailInfo === "") {
        actual = true;
  }
  assert.equal(actual,expected,"showApplUnavail - Success");
});

QUnit.test("initProfileInfo", function(assert){
  var expected = true;
  var actual = false;
  var data = {
      "rejComboRes":{  
         "rejectionReason":[  
            {  
               "employmentPositionCandidateRejectReasonCode":0,
               "lastUpdateSystemUserId":"GXN5764                 ",
               "lastUpdateTimestamp":"2013-01-03 13:14:14.803427",
               "displayReasonCode":"Viewed    ",
               "shortReasonDescription":"Viewed Not Selected ",
               "reasonDescription":"Viewed Not Selected"
            }
         ]
      }
  };
  var response = {  
            "status":"SUCCESS",
            "PersonalInfoList":{  
               "PersonalInfo":{  
                  "applType":"AS",
                  "fullName":"ESPARSA,LOUIS F",
                  "phoneNum":"404-433-8211",
                  "address1":"2455 PACES FERRY RD",
                  "address2":"APT#10206",
                  "cityStateZip":"SMYRNA, GA 30080",
                  "applID":113948055,
                  "candRefNbr":194960
               }
            },
            "AssociateInfoList":{  
               "AssociateInfo":{  
                  "currStore":9090,
                  "currDept":873,
                  "currTitle":"SSDBAD",
                  "currStatus":"FS",
                  "hireDate":"2010-09-20"
               }
            },
            "AssociateReviewList":{  
               "ReviewResults":[  
                  {  
                     "date":"2012-05-01",
                     "results":4
                  }
               ]
            },
            "AssociatePrePosList":{  
               "PreviousPosition":[  
                  {  
                     "dept":873,
                     "position":"SSDBAD",
                     "dateFrom":"2012-04-16",
                     "dateTo":"2099-12-31"
                  }
               ]
            },
            "EducationList":{  
               "Education":[  
                  {  
                     "schoolName":"CAMBRIDGE",
                     "education":"MASTERS",
                     "graduate":"Y"
                  }
               ]
            },
            "WorkHistoryList":{  
               "WorkHistory":[  
                  {  
                     "company":"MAN U",
                     "position":"CASHIER",
                     "location":"",
                     "supervisor":"",
                     "comDateFrom":"2009-01-01",
                     "comDateTo":"2010-01-01",
                     "payAtLeaving":"0.00",
                     "supervisorTitle":"",
                     "reasonLeaving":""
                  }
               ]
            },
            "LanguageList":{  
               "LanguageSet":{  
                  "language":"AS,ENGLISH,"
               }
            },
            "SkillList":{  
               "SkillSet":[  
                  {  
                     "skill":"Carpentry"
                  }
               ]
            },
            "PhnScreenHistoryList":{  
               "PhnScreenHistory":{  
                  "requisitionNum":100063616,
                  "storeNum":1301,
                  "deptNum":"091",
                  "job":"HOFASC",
                  "phnScreenStatus":"PHN SCREEN COMPLETED",
                  "status":"INTERVIEW SCHEDULED",
                  "phnScreenType":"DETAILED",
                  "lastUpdate":"2014-08-04"
               }
            },
            "CandidateHistoryList":{  
               "CandidateHistory":{  
                  "reqNumber":150166570,
                  "store":1301,
                  "dept":"091",
                  "title":"OFFICE ASSOCIATE",
                  "intvDisposition":"",
                  "offerStatus":"",
                  "offerResults":""
               }
            },
            "ApplicationHistoryList":{  
               "ApplicationHistory":[  
                  {  
                     "store":"0117",
                     "lastUpdatedS":"2013-08-09 13:45:28.744"
                  }
               ]
            },
            "ApplicationHistoryList2":{  
               "ApplicationHistory2":[  
                  {  
                     "position":"DPTSUP",
                     "dept":"021",
                     "lastUpdatedP":"2014-06-20 13:59:41.050682"
                  }
               ]
            }
         }; 

  var returnedThis = getApplicantProfile(response);
  applicantProfileObj.model.LoadApplPersonalInfoDtls = returnedThis.LoadApplPersonalInfoDtls;
  var returnVal = loadRejectDetails(data);
  applicantProfileObj.model.rejectComboLoad= returnVal.rejectComboLoad;
  CONSTANTS.retailStaffingObj = {applicantObj:{id:'12345'}};
  reqDet.model.LoadREQDGDltls = [{reqNbr:"54321"}];
  $.ajax = function(options) {
    var response = {
      "Response":{  
         "status":"SUCCESS"
      }
    };
    options.success = function(data) {
          applicantProfileObj.model.rjctSaveCheck = false;
          applicantProfileObj.createRejectDetailsSuccess(data);
          if(returnVal.rejectComboLoad[0].displayField === 'Viewed Not Selected ') {
            actual = true;
          }
        };
        options.success(response);
    };

  applicantProfileObj.initProfileInfo();

  actual = ($("#alertModalId .alertModalBody").text() === 'Details Saved Successfully') ? true : false;
  assert.equal(actual,expected,"initProfileInfo - Details Saved Successfully");
});

QUnit.test("initProfileInfo", function(assert){
  var expected = true;
  var actual = false;
  var data = {
      "rejComboRes":{  
         "rejectionReason":[  
            {  
               "employmentPositionCandidateRejectReasonCode":0,
               "lastUpdateSystemUserId":"GXN5764                 ",
               "lastUpdateTimestamp":"2013-01-03 13:14:14.803427",
               "displayReasonCode":"Viewed    ",
               "shortReasonDescription":"Viewed Not Selected ",
               "reasonDescription":"Viewed Not Selected"
            }
         ]
      }
  };
  var response = {  
            "status":"SUCCESS",
            "PersonalInfoList":{  
               "PersonalInfo":{  
                  "applType":"AP",
                  "fullName":"ESPARSA,LOUIS F",
                  "phoneNum":"404-433-8211",
                  "address1":"2455 PACES FERRY RD",
                  "address2":"APT#10206",
                  "cityStateZip":"SMYRNA, GA 30080",
                  "applID":113948055,
                  "candRefNbr":194960
               }
            },
            "AssociateInfoList":{  
               "AssociateInfo":{  
                  "currStore":9090,
                  "currDept":873,
                  "currTitle":"SSDBAD",
                  "currStatus":"FS",
                  "hireDate":"2010-09-20"
               }
            },
            "AssociateReviewList":{  
               "ReviewResults":[  
                  {  
                     "date":"2012-05-01",
                     "results":4
                  }
               ]
            },
            "AssociatePrePosList":{  
               "PreviousPosition":[  
                  {  
                     "dept":873,
                     "position":"SSDBAD",
                     "dateFrom":"2012-04-16",
                     "dateTo":"2099-12-31"
                  }
               ]
            },
            "EducationList":{  
               "Education":[  
                  {  
                     "schoolName":"CAMBRIDGE",
                     "education":"MASTERS",
                     "graduate":"Y"
                  }
               ]
            },
            "WorkHistoryList":{  
               "WorkHistory":[  
                  {  
                     "company":"MAN U",
                     "position":"CASHIER",
                     "location":"",
                     "supervisor":"",
                     "comDateFrom":"2009-01-01",
                     "comDateTo":"2010-01-01",
                     "payAtLeaving":"0.00",
                     "supervisorTitle":"",
                     "reasonLeaving":""
                  }
               ]
            },
            "LanguageList":{  
               "LanguageSet":{  
                  "language":"AS,ENGLISH,"
               }
            },
            "SkillList":{  
               "SkillSet":[  
                  {  
                     "skill":"Carpentry"
                  }
               ]
            },
            "PhnScreenHistoryList":{  
               "PhnScreenHistory":{  
                  "requisitionNum":100063616,
                  "storeNum":1301,
                  "deptNum":"091",
                  "job":"HOFASC",
                  "phnScreenStatus":"PHN SCREEN COMPLETED",
                  "status":"INTERVIEW SCHEDULED",
                  "phnScreenType":"DETAILED",
                  "lastUpdate":"2014-08-04"
               }
            },
            "CandidateHistoryList":{  
               "CandidateHistory":{  
                  "reqNumber":150166570,
                  "store":1301,
                  "dept":"091",
                  "title":"OFFICE ASSOCIATE",
                  "intvDisposition":"",
                  "offerStatus":"",
                  "offerResults":""
               }
            },
            "ApplicationHistoryList":{  
               "ApplicationHistory":[  
                  {  
                     "store":"0117",
                     "lastUpdatedS":"2013-08-09 13:45:28.744"
                  }
               ]
            },
            "ApplicationHistoryList2":{  
               "ApplicationHistory2":[  
                  {  
                     "position":"DPTSUP",
                     "dept":"021",
                     "lastUpdatedP":"2014-06-20 13:59:41.050682"
                  }
               ]
            }
         }; 

  var returnedThis = getApplicantProfile(response);
  applicantProfileObj.model.LoadApplPersonalInfoDtls = returnedThis.LoadApplPersonalInfoDtls;
  var returnVal = loadRejectDetails(data);
  applicantProfileObj.model.rejectComboLoad= returnVal.rejectComboLoad;
  CONSTANTS.retailStaffingObj = {applicantObj:{id:'12345'}};
  reqDet.model.LoadREQDGDltls = [{reqNbr:"54321"}];
  $.ajax = function(options) {
    var response = {
      "Response":{  
         "status":"SUCCESS"
      }
    };
    options.success = function(data) {
          applicantProfileObj.model.rjctSaveCheck = false;
          applicantProfileObj.createRejectDetailsSuccess(data);
          if(returnVal.rejectComboLoad[0].displayField === 'Viewed Not Selected ') {
            actual = true;
          }
        };
        options.success(response);
    };

  applicantProfileObj.initProfileInfo();

  actual = ($("#alertModalId .alertModalBody").text() === 'Details Saved Successfully') ? true : false;
  assert.equal(actual,expected,"initProfileInfo - Details Saved Successfully");
});

QUnit.test("createRejectDetailsSuccess", function(assert){
  var expected = true;
  var actual = false;
  var response = {
        "Response":{  
          "error":{
            "errorMsg" : "Error Message"
          }
        }
      };
  applicantProfileObj.model.rjctSaveCheck = true;
  applicantProfileObj.createRejectDetailsSuccess(response);
  
  actual = ($("#alertModalId .alertModalBody").text() === 'Error Message') ? true : false;
  assert.equal(actual,expected,"createRejectDetailsSuccess - Error message");
});

QUnit.test("saveClick", function(assert){
  var expected = true;
  var actual = false;
  var option = document.createElement('option');
  option.setAttribute('value','0');
  $(".attachCand .rjctRsncb").append(option);
  $("[class='attachreq'][value='N']").attr('checked','true');
  
  applicantProfileObj.saveClick();
  actual = ($("#alertModalId .alertModalBody").text() === 'Please select a reason for not attaching the candidate') ? true : false;
  assert.equal(actual,expected,"saveClick - When Not Attach to Requisition and no reason is selected");
});

QUnit.test("saveClick", function(assert){
  var expected = true;
  var actual = false;
  var secondCall = false;
  var personalInfo = [{
    "applType":"AP",
    "fullName":"ESPARSA,LOUIS F",
    "phoneNum":"404-433-8211",
    "address1":"2455 PACES FERRY RD",
    "address2":"APT#10206",
    "cityStateZip":"SMYRNA, GA 30080",
    "applID":113948055,
    "candRefNbr":194960
  }];

  reqDet.model.LoadREQDGDltls = [{reqNbr:54321,store:1234}];
  $.ajax = function(options) {
      var response = {
        "Response":{
          "error":{
            "errorMsg":"Error Message"
          }
        }
      };
    options.success = function(data) {
          applicantProfileObj.LoadApplPersonalInfoDtls = personalInfo;
          applicantProfileObj.retailStaffingAttachApplToReqSuccess(data);
    };
    options.get = function() {
          applicantProfileObj.showApplUnavail();
    };
    if(!secondCall){
      secondCall = true;
      options.success(response);
    } else {
      options.get();
    }
    
  };

  $("[class='attachreq'][value='Y']").attr('checked','true');
  applicantProfileObj.model.LoadApplPersonalInfoDtls = personalInfo;
  applicantProfileObj.saveClick();
  actual = reqDet.model.backToQualifiedPool ? true : false;
  assert.equal(actual,expected,"saveClick - AP - When Attach To Requisition is checked");
});

QUnit.test("saveClick", function(assert){
  var expected = true;
  var actual = false;
  var secondCall = false;
  var personalInfo = [{
    "applType":"AP",
    "fullName":"ESPARSA,LOUIS F",
    "phoneNum":"404-433-8211",
    "address1":"2455 PACES FERRY RD",
    "address2":"APT#10206",
    "cityStateZip":"SMYRNA, GA 30080",
    "applID":113948055,
    "candRefNbr":194960
  }];

  reqDet.model.LoadREQDGDltls = [{reqNbr:54321,store:1234}];
  $.ajax = function(options) {
      var response = {
        "Response":{
          "error":{
            "errorMsg":"Error Message"
          }
        }
      };
    options.success = function(data) {
          applicantProfileObj.LoadApplPersonalInfoDtls = personalInfo;
          applicantProfileObj.retailStaffingAttachApplToReqSuccess(data);
    };
    options.get = function() {
          applicantProfileObj.showApplUnavail();
    };
    if(!secondCall){
      secondCall = true;
      options.success(response);
    } else {
      options.get();
    }
    
  };

  $("[class='attachreq'][value='Y']").attr('checked','true');
  applicantProfileObj.model.LoadApplPersonalInfoDtls = personalInfo;
  applicantProfileObj.saveClick();
  actual = reqDet.model.backToQualifiedPool ? true : false;
  assert.equal(actual,expected,"saveClick - AS - When Attach To Requisition is checked");
});

QUnit.test("saveClick", function(assert){
  var expected = true;
  var actual = false;
  var secondCall = false;
  var personalInfo = [{
    "applType":"AP",
    "fullName":"ESPARSA,LOUIS F",
    "phoneNum":"404-433-8211",
    "address1":"2455 PACES FERRY RD",
    "address2":"APT#10206",
    "cityStateZip":"SMYRNA, GA 30080",
    "applID":113948055,
    "candRefNbr":194960
  }];

  reqDet.model.LoadREQDGDltls = [{reqNbr:54321,store:1234}];
  $.ajax = function(options) {
      var response = {
        "Response":{
          "status":"SUCCESS",
          "ApplUnavailList":{
            "ApplUnavail":"AP"
          }
        }
      };
    options.success = function(data) {
          applicantProfileObj.LoadApplPersonalInfoDtls = personalInfo;
          applicantProfileObj.retailStaffingAttachApplToReqSuccess(data);
    };
    options.get = function() {
          applicantProfileObj.showApplUnavail();
    };
    if(!secondCall){
      secondCall = true;
      options.success(response);
    } else {
      options.get();
    }
    
  };

  $("[class='attachreq'][value='Y']").attr('checked','true');
  applicantProfileObj.model.LoadApplPersonalInfoDtls = personalInfo;
  applicantProfileObj.saveClick();
  actual = reqDet.model.backToQualifiedPool ? true : false;
  assert.equal(actual,expected,"saveClick - When Attach To Requisition is checked - SUCCESS");
});

QUnit.test("saveClick", function(assert){
  var expected = true;
  var actual = false;
  var secondCall = false;
  var option = document.createElement('option');
  var personalInfo = [{
    "applType":"AS",
    "fullName":"ESPARSA,LOUIS F",
    "phoneNum":"404-433-8211",
    "address1":"2455 PACES FERRY RD",
    "address2":"APT#10206",
    "cityStateZip":"SMYRNA, GA 30080",
    "applID":113948055,
    "candRefNbr":194960
  }];
  option.setAttribute('value','1');
  $(".attachCand .rjctRsncb").append(option);
  $("[class='attachreq'][value='N']").attr('checked','true');
  CONSTANTS.retailStaffingObj.applicantObj = {
            id : '12345',
            type : 'test',
            applDate : '06/12/2015'
          };
  
  reqDet.model.LoadREQDGDltls = [{reqNbr:"54321"}];
  
  $.ajax = function(options) {
      var response = {
        "Response":{
          "status":"SUCCESS"
        }
      };
    options.success = function(data) {
          $("[class='attachreq'][value='Y']").attr('checked','true');
          reqDet.model.LoadMasterAssociatePoolDgDtls = [];
          applicantProfileObj.model.rjctSaveCheck = false;
          applicantProfileObj.model.LoadApplPersonalInfoDtls = personalInfo;
          applicantProfileObj.createRejectDetailsSuccessAndReturn(data);
    };
    options.get = function() {
          applicantProfileObj.showApplUnavail();
    };
    if(!secondCall){
      secondCall = true;
      options.success(response);
    } else {
      options.get();
    }
    
  };
  applicantProfileObj.saveClick();
  actual = ($("#alertModalId .alertModalBody").text() === 'Details Saved Successfully') ? true : false;
  assert.equal(actual,expected,"saveClick - When Not Attach to Requisition and reason is selected");
});

