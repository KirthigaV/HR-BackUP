function cpdForm() {

	this.initialize = function() {
		// candidateDataForm Validation
		$('#candidateDataForm')
				.validate(
						{
							ignore : '',
							rules : {
								'ssnEntry1' : {
									required : true,
									minlength : 9,
									checkValidSSNStartWith9 : "#ssnEntry1",
									checkValidSSNStartWith666 : "#ssnEntry1",
									checkValidSSNStartWith000 : "#ssnEntry1",
									checkValidSSNFakes : "#ssnEntry1",
									checkSSNIsANumber : "#ssnEntry1"
								},
								'ssnEntry2' : {
									required : true,
									minlength : 9,
									equalTo : "#ssnEntry1"
								},
								'dobEntry1' : {
									required : true,
									minlength : 10,
									checkDobFormat : "#dobEntry1",
									checkDobAtLeast16 : "#dobEntry1"
								},
								'dobEntry2' : {
									required : true,
									minlength : 10,
									equalTo : "#dobEntry1"
								},
								'dlNumber1' : {

								},
								'dlNumber2' : {

									equalTo : "#dlNumber1"
								},
								'driversLicenseState' : {
/*									required : ("#dlNumber1:filled" || "#dlNumber2:filled") */
								},
								'zipCode' : {
									minlength : 5
								},
								'middleName' : {
									checkValidMiddleNameStartWithSpace : "#middleName"
								},
								'acceptChecked' : {
									required : true
								},
								'candidateInitials' : {
									required : true
								}
							},

							messages : {
								'ssnEntry1' : {
									checkValidSSNStartWith9 : "Social Security Number cannot start with 9",
									checkValidSSNStartWith666 : "Social Security Number cannot start with 666",
									checkValidSSNStartWith000 : "Social Security Number cannot start with 000",
									checkValidSSNFakes : "Social Security Number is invalid",
									checkSSNIsANumber : "Social Security Number can only contain numbers"
								},
								'ssnEntry2' : {
									equalTo : "Confirm Social Security Number does not match"
								},
								'dobEntry2' : {
									equalTo : "Confirm Date Of Birth does not match"
								},
								'dobEntry1' : {
									checkDobFormat : "Invalid Date Format",
									checkDobAtLeast16 : "Date Of Birth must reflect age 16 or greater"
								},
								'dlNumber2' : {
									equalTo : "Driver's License Number does not match"
								},
								'driversLicenseState' : {
/*									required : "Required when Drivers's License is entered" */
								},
								'middleName' : {
									checkValidMiddleNameStartWithSpace : "Invalid Middle Name"
								}
							}
						}); // End $('#candidateDataForm').validate({

		// mgrEntryAreaForm Validation
		$('#mgrEntryAreaForm').validate({
			rules : {
				'mgrLdap' : {
					required : true,
					equalToIgnoreCase : "#currentMgrLdap"
				},
			},
			messages : {
				'mgrLdap' : {
					equalToIgnoreCase : "LDAP must match logged in User"
				}
			}
		}); // End $('#mgrEntryAreaForm').validate({

		$('input:radio[name="hasMiddleName"]').filter('[value="Y"]').attr(
				'checked', true);

		$('input:radio[name="livedAtCurrentAddressRequiredTime"]').filter(
				'[value="N"]').attr('checked', true);

		// Submit Button Click Event
		$("#submit")
				.click(
						function(e) {
							e.preventDefault();
							if ($("#candidateDataForm").valid()) {
								if ($("#mgrEntryAreaForm").valid()) {
									// Set Minor Flag if true
									if ($('#lessThan18Message').is(':visible')) {
										isCandMinorFlag = true;
									} else {
										isCandMinorFlag = false;
									}
									// Gather form data
									var unindexed_array = $(
											'form#candidateDataForm')
											.serializeArray();
									unindexed_array = unindexed_array.concat($(
											'form#candidateDataSearch')
											.serializeArray())
									unindexed_array = unindexed_array.concat($(
											'form#mgrEntryAreaForm')
											.serializeArray())

									var indexed_array = {};

									$.map(unindexed_array, function(n, i) {
										indexed_array[n['name']] = n['value'];
									});
									$.blockUI({
										message : $('#updating')
									});
									$('#cpdFormModal').modal('hide');

									$
											.ajax({
												type : "POST",
												url : "/RetailStaffing/service/CandidateDataFormService/submitCandidateDetails",
												dataType : "json",
												contentType : "application/json",
												data : {
													request : JSON
															.stringify({
																"SubmitCandidatePersonalDataRequest" : indexed_array
															})
												},
												success : submitResult,
												failure : callFailed
											}); // End - $.ajax({

								} else {
									// Do Nothing, Validation shows the
									// messages.
								} // End - if ($("#mgrEntryAreaForm").valid())
								// {
							} // End - if($("#candidateDataForm").valid()) {
						}); // End - $("#submit").click(function(e){
	}

	function callFailed() {
		alert("Application Failed....");
	}

	function submitResult(json) {
		// Check for Error Messages
		var errorMessage;
		if (json.hasOwnProperty("Exception")) {
			if (json.Exception.userMessage.hasOwnProperty("cause")) {
				errorMessage = json.Exception.userMessage.cause.detailMessage;
			} else if (json.Exception.userMessage
					.hasOwnProperty("detailMessage")) {
				errorMessage = json.Exception.userMessage.detailMessage;
			} else {
				errorMessage = "An error has occurred updating Personal Data."
			}

			jErrorAlert(errorMessage, 'Error Message');
			$("#cpdFormStatus").text("Incomplete");
			$("#cpdFormStatus").css("color", "red");
			isCandMinorFlag = CONSTANTS.CandidateIntvwDtls.isCandMinorFlag;
			$.unblockUI();
		} else if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
			jAlert("Candidate Personal Data was updated successfully.", 'Success Message');	
				$("#candidateId").val("");
				$('#cpdFormModal').modal('hide');
				$("#cpdFormStatus").text("Complete");
				$("#cpdFormStatus").css("color", "green");
				CONSTANTS.CandidateIntvwDtls.cpdFormComplete = true;
				// Set Minor Flag if true
				if (isCandMinorFlag) {
					interviewResults.setMinorConsentPanel()
				}
                // Set Background Consent
                CONSTANTS.interviewResultsObj.setBackgroundConsent();
				
				//Check if candidate is rehire Eligible 
				$.ajax({
					type : "GET",
					url : "/RetailStaffing/service/RetailStaffingService/getCandidateRehireDtls",
					dataType : "json",
					data: {applicantId: $("#applicantId").val()},
					contentType:"application/json",
					success : processRehireCheck,
					failure : callFailed
				});
				$.unblockUI();

		} else if (json.Response.hasOwnProperty("status")
				&& json.Response.status === CONSTANTS.STATUS_SUCCESS) {
			jAlert("Candidate Personal data was updated successfully.",
					'Success Message');
			$("#candidateId").val("");
			$('#cpdFormModal').modal('hide');
			$("#cpdFormStatus").text("Complete");
			$("#cpdFormStatus").css("color", "green");
			CONSTANTS.CandidateIntvwDtls.cpdFormComplete = true;
			// Set Background Consent
			CONSTANTS.interviewResultsObj.setBackgroundConsent();

			// Check if candidate is rehire Eligible
			$
					.ajax({
						type : "GET",
						url : "/RetailStaffing/service/RetailStaffingService/getCandidateRehireDtls",
						dataType : "json",
						data : {
							applicantId : $("#applicantId").val()
						},
						contentType : "application/json",
						success : processRehireCheck,
						failure : callFailed
					});

			$.unblockUI();
		}
	}

	function processRehireCheck(json) {
		if (json.hasOwnProperty("Exception")) {
			if (json.Exception.userMessage.hasOwnProperty("cause")) {
				errorMessage = json.Exception.userMessage.cause.detailMessage;
			} else if (json.Exception.userMessage
					.hasOwnProperty("detailMessage")) {
				errorMessage = json.Exception.userMessage.detailMessage;
			} else {
				errorMessage = "An error has occurred checking for rehire status."
			}
		} else if (json.Response.hasOwnProperty("status")
				&& json.Response.status === CONSTANTS.STATUS_SUCCESS) {
			if (json.Response.CandidateInformation.rehireEligible) {
				CONSTANTS.CandidateIntvwDtls.rehireEligible = true;
				CONSTANTS.interviewResultsObj
						.showCpdFormOrRehireEligibleMessage();
			} else {
				CONSTANTS.CandidateIntvwDtls.rehireEligible = false;
				CONSTANTS.CandidateIntvwDtls.termLoc = json.Response.CandidateInformation.termLoc;
				CONSTANTS.CandidateIntvwDtls.termRsn = json.Response.CandidateInformation.termRsn;
				CONSTANTS.CandidateIntvwDtls.termDt = json.Response.CandidateInformation.termDt;
				CONSTANTS.interviewResultsObj
						.showCpdFormOrRehireEligibleMessage();
			}
			// Unlock CPD Panel if Offer Result is Accept.
			CONSTANTS.interviewResultsObj.setCPDPanel();
		}
	}

	this.cpdFormWindow = function() {
		// Need to clear anything entered previously
		this.displayCandidateConfirmationInfo();
		this.resetHideCandidateConfirm();
		this.clearAllCandidateData();
		$(".showCpdStatus").text("");
		$('input:radio[name="hasMiddleName"]').filter('[value="Y"]').attr(
				'checked', true);
		$('input:radio[name="livedAtCurrentAddressRequiredTime"]').filter(
				'[value="N"]').attr('checked', true);
		$('input:radio[name="prevAddressOutsideUS"]').filter('[value="N"]')
				.attr('checked', true);
		this.updateMiddleNameField();
		this.updatePreviousAddressField();
		this.updateDriversLicenseField();
		if (showMinorConsentPanel){
			$("#lessThan18Message").text("ALERT! Your candidate is under the age of 18. If you are in an approved minor hiring state, hiring for Lot, Cashier, or D28 sales positions only, you will receive a link within the Interview Summary Form to complete the parental consent forms, after submitting this Candidate Personal Data form.  Note: Parent/Guardian MUST be present to complete parental consent forms. (Additional documentation may be required. Refer to Minor Associate SOP for your state's specific requirements. myTHDHR > HR SOPs.)");
		} else {
			$("#lessThan18Message").text("ALERT! The date of birth entered indicates the candidate is under 18 years of age. Please confirm the candidate's age. If candidate is under 18 and you are in an approved minor hiring state, obtain parent/guardian consent for the drug test and background check before processing. (Additional documentation may be required. Refer to the Minor Associates SOP for your state's specific requirements. myTHDHR> HR SOPs.)");	
		};
		$("#lessThan18Message").hide();
		document.getElementById("currentMgrLdap").value = CONSTANTS.userProfile.userId;

		document.getElementById("applicantId").value = CONSTANTS.CandidateIntvwDtls.candId;

		var currentDate = new Date();
		currentDate.setFullYear(currentDate.getFullYear() - 16);
		document.getElementById("maxDOBValue").value = currentDate;
/*		$('#cpdFormModal').modal('show'); */
	};

	this.closeCpdFormWindow = function() {
		$('#cpdFormModal').modal('hide');};
/*		if ($("#mgrEntryAreaForm").valid()) {
			$(".showCpdStatus").text("Complete");
			$('#cpdFormModal').modal('hide');
		} else {
			// Do Nothing.
		}
	}; // Allowed checking that LDAP was entered first before closing CPD Form */

	// Tells the web app to fit the entire browser viewport loading.gif
	$(window).resize(function() {
		fit();
	});

	$(".portlet").addClass(
			"ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
			.find(".portlet-header").addClass("ui-widget-header ui-corner-all")
			.end().find(".portlet-content");

	// For class = ui-state-hover, maintain orange call to action, do not remove
	// class
	$('.ui-state-hover').hover(function() {
		$(this).addClass('ui-state-hover');
	});

	// Only allow numbers 0-9 for SSN Entry 1
	$('#ssnEntry1').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[0-9]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow numbers 0-9 for SSN Entry 1

	// Only allow numbers 0-9 for SSN Entry 2
	$('#ssnEntry2').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[0-9]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow numbers 0-9 for SSN Entry 2

	// Only allow numbers 0-9 and / for DOB Entry 1
	$('#dobEntry1').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[0-9\/]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow numbers 0-9 and / for DOB Entry 1

	// Only allow numbers 0-9 and / for DOB Entry 2
	$('#dobEntry2').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[0-9\/]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow numbers 0-9 and / for DOB Entry 1

	// Only allow specified characters for Driver's License number 1
	$('#dlNumber1').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z0-9\* ]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for Driver's License
	// number 1

	// Only allow specified characters for Driver's License number 2
	$('#dlNumber2').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z0-9\* ]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for Driver's License
	// number 2

	// Only allow specified characters for Candidate Initials
	$('#candidateInitials').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z0-9]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for Candidate Initials

	// Only allow specified characters for Middle Name
	$('#middleName').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z\- ]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for Middle Name

	// Only allow specified characters for Address 1
	$('#address1').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z0-9\-\_ ]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for Address 1

	// Only allow specified characters for Address 2
	$('#address2').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z0-9\-\_ ]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for Address 2

	// Only allow specified characters for City
	$('#city').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[a-zA-Z0-9\-\_ ]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow specified characters for City

	// Only allow numbers 0-9 for Zip Code
	$('#zipCode').bind(
			'keypress',
			function(event) {
				var regex = new RegExp("^[0-9\b]");
				var key = String.fromCharCode(!event.charCode ? event.which
						: event.charCode);
				if (!regex.test(key)) {
					event.preventDefault();
					return false;
				}
			}); // End - Only allow numbers 0-9 for Zip Code

	// Compare strings ignoring case
	$.validator.addMethod("equalToIgnoreCase", function(value, element, param) {
		return this.optional(element)
				|| (value.toLowerCase() == $(param).val().toLowerCase());
	}); // End - Compare strings ignoring case

	// Make sure that something has been selected in a combo box
	$.validator.addMethod("selectComboboxCheck", function(value, element) {
		if ($(element).find(":selected").val() == -1) {
			return false;
		} else
			return true;
	}, "Required"); // End - $.validator.addMethod("selectComboboxCheck",
	// function (value, element) {

	// Check the DOB Format
	$.validator.addMethod("checkDobFormat", function(value, element, param) {
		return $(param).val().match(/^\d\d?\/\d\d?\/\d\d\d\d$/);
	}); // End - Check the DOB Format

	// Make sure that the DOB reflects that User is at least 16
	$.validator.addMethod("checkDobAtLeast16", function(value, element, param) {
		var isValid = $(param).val().match(/^\d\d?\/\d\d?\/\d\d\d\d$/);
		if (isValid) {
			var minDate = Date
					.parse(document.getElementById("maxDOBValue").value);
			var DOB = Date.parse($(param).val());
			if (DOB > minDate) {
				return false;
			}
		}

		// Check to see if candidate is 16 or 17, if yes, show message
		var diff = new Date(minDate).getTime() - new Date(DOB).getTime();
		var theVal = Math.floor(diff / (60 * 60 * 24 * 365));
		if (theVal < 2004) {
			$("#lessThan18Message").show();
		} else {
			$("#lessThan18Message").hide();
		}

		return true;
	}); // End - Make sure that the DOB reflects that User is at least 16

	// Make sure that the SSN does not start with 9
	$.validator.addMethod("checkValidSSNStartWith9", function(value, element,
			param) {
		if ($(param).val().substr(0, 1) == '9') {
			return false;
		}
		return true;
	}); // End - Make sure that the SSN does not start with 9

	// Make sure that the SSN does not start with 666
	$.validator.addMethod("checkValidSSNStartWith666", function(value, element,
			param) {
		if ($(param).val().substr(0, 3) == '666') {
			return false;
		}
		return true;
	}); // End - Make sure that the SSN does not start with 666

	// Make sure that the SSN does not start with 000
	$.validator.addMethod("checkValidSSNStartWith000", function(value, element,
			param) {
		if ($(param).val().substr(0, 3) == '000') {
			return false;
		}
		return true;
	}); // End - Make sure that the SSN does not start with 000

	// Make sure that the SSN is not known fakes
	$.validator.addMethod("checkValidSSNFakes",
			function(value, element, param) {
				if ($(param).val() == '078051120'
						|| $(param).val() == '219099999') {
					return false;
				}
				return true;
			}); // End - Make sure that the SSN is not known fakes

	// Make sure that the SSN is a number
	$.validator.addMethod("checkSSNIsANumber", function(value, element, param) {
		// if ($(param).val() == '078051120') {
		return $.isNumeric($(param).val());
		// }
		// return true;
	}); // End - Make sure that the SSN is a number

	// Make sure that the Middle Name does not start with space
	$.validator.addMethod("checkValidMiddleNameStartWithSpace", function(value,
			element, param) {
		if ($(param).val().substr(0, 1) == ' ') {
			return false;
		}
		return true;
	}); // End - Make sure that the Middle Name does not start with space

	// Fits Form to Page based on screen resolution
	function fit() {
		var w = window, d = document, e = d.documentElement, g = d
				.getElementsByTagName('body')[0], y = w.innerHeight
				|| e.clientHeight || g.clientHeight;
		var height = y - $("#header").height() - $("#messageBar").height()
				- $("#release").height() - $("#copyright").height() - 60;

		$("#appBody").css("min-height", height);
	}

	// Clear anything entered.
	this.clearAllCandidateData = function() {
		// Clear all Candidate entered data
		$('#candidateDataForm').trigger("reset");
		// Clear any Validation error messages
		var validator = $("#candidateDataForm").validate();
		validator.resetForm();
		validator = $("#mgrEntryAreaForm").validate();
		validator.resetForm();
		// Reset Current Date
		$("#dateSigned").datepicker({
			dateFormat : "mm/dd/yy"
		}).datepicker("setDate", new Date());
		// Clear Manager LDAP Id
		$("#mgrLdap").val("");
		// Reset to default Middle Name
		this.resetMiddleNameSelection();
		// Reset to default Previous Address section
		this.resetPreviousAddressSelection();
		// Clear the Candidate less than 16 message
		$("#lessThan18Message").hide();
		// Clear DL State Select
		$("#driversLicenseState option[value='']").prop('selected', true);
		$("#driversLicenseState").data("selectBox-selectBoxIt").refresh();
	}

	// Resets Candidate Confirmation section
	this.resetHideCandidateConfirm = function() {
	$('input:radio[name="candidateConfirm"]').filter('[value="N"]').attr(
			'checked', false);
	$('input:radio[name="candidateConfirm"]').filter('[value="Y"]').attr(
			'checked', false);
	$("#candidateConfirmInstructions").css("display", "table");
	$("#candidateConfirmInstructions").css("color", "black");
	$("#candidateDataForm").css("display", "none");

	$("#hiringMgrInfoPortlet").css("display", "none");
	$("#candidateConfirmInstructions").css("display", "none");
	
	$("#candidateConfirmName").val("")
	$("#candidateConfirmAddress1").val("")
	$("#candidateConfirmAddress2").val("")
	$("#submit").prop("disabled", true).css("cursor", "not-allowed");
    
	}
	
	// Resets the Middle Name section
	this.resetMiddleNameSelection = function() {
		$("#middleNameRow").show();
		$("#middleName").prop('required', true);
	}

	// Resets the Previous Address section
	this.resetPreviousAddressSelection = function() {
		$("#address1Row").show();
		$("#address1").prop('required', true);
		$("#address2Row").show();
		$("#cityRow").show();
		$("#city").prop('required', true);
		$("#addressStateRow").show();
		$("#addressState").prop('required', true);
		$("#addressState option[value='']").prop('selected', true);
		$("#addressState").data("selectBox-selectBoxIt").refresh();
		$("#zipCodeRow").show();
		$("#zipCode").prop('required', true);
	}

	this.HideCandidateConfirm = function() {
		if ($('input[name=candidateConfirm]:checked').val() == 'Y') {
			$("#candidateDataForm").css("display", "inherit");
			$("#candidateConfirmInstructions").css("display", "none");
			$("#submit").prop("disabled", false).css("cursor", "pointer");
			$("#hiringMgrInfoPortlet").css("display", "inherit");
		
		} else if ($('input[name=candidateConfirm]:checked').val() == 'N') {
			$('#cpdFormModal').modal('hide');
			
		} else{
			$("#candidateDataForm").css("display", "none");
			$("#candidateConfirmInstructions").css("display", "table");
			$("#candidateConfirmInstructions").css("color", "red");
			$("#submit").prop("disabled", true).css("cursor", "not-allowed");
		}
	}
	
	this.updateMiddleNameField = function() {
		if ($('input[name=hasMiddleName]:checked').val() == 'N') {
			$("#middleName").css("visibility", "hidden");
			$("#middleNameLabel").css("visibility", "hidden");
			$("#middleName").prop('required', false);
			$("#middleName").val("");
			$("#middleName").valid();
		} else {
			$("#middleName").css("visibility", "visible");
			$("#middleNameLabel").css("visibility", "visible");
			$("#middleName").prop('required', true);
		}
	}

	this.updatePreviousAddressField = function() {
		if ($('input[name=livedAtCurrentAddressRequiredTime]:checked').val() == 'Y') {
			$("#prevAddOutsideUSY").css("visibility", "hidden");
			$("#prevAddOutsideUSN").css("visibility", "hidden");
			$("#prevAddOutsideUSLabel").css("visibility", "hidden");
			$("#address1").prop('required', false);
			$("#address1").val("");
			$("#address1").css("visibility", "hidden");
			$("#address1Label").css("visibility", "hidden");
			$("#address1").valid();
			$("#address2").val("");
			$("#address2").css("visibility", "hidden");
			$("#address2Label").css("visibility", "hidden");
			$("#city").prop('required', false);
			$("#city").val("");
			$("#city").css("visibility", "hidden");
			$("#cityLabel").css("visibility", "hidden");
			$("#city").valid();
			$("#addressState").prop('required', false);
			$("#addressState option[value='']").prop('selected', true);
			$("#addressStateTd").css("visibility", "hidden");
			$("#addressState").data("selectBox-selectBoxIt").refresh();
			$("#addressState").valid();
			$("#addressStateLabel").css("visibility", "hidden");
			$("#zipCode").prop('required', false);
			$("#zipCode").val("");
			$("#zipCode").css("visibility", "hidden");
			$("#zipCodeLabel").css("visibility", "hidden");
			$("#zipCode").valid();
		} else if ($('input[name=prevAddressOutsideUS]:checked').val() == 'Y') {
			$("#prevAddOutsideUSY").css("visibility", "visible");
			$("#prevAddOutsideUSN").css("visibility", "visible");
			$("#prevAddOutsideUSLabel").css("visibility", "visible");
			$("#address1").prop('required', false);
			$("#address1").val("");
			$("#address1").css("visibility", "hidden");
			$("#address1Label").css("visibility", "hidden");
			$("#address1").valid();
			$("#address2").val("");
			$("#address2").css("visibility", "hidden");
			$("#address2Label").css("visibility", "hidden");
			$("#city").prop('required', false);
			$("#city").val("");
			$("#city").css("visibility", "hidden");
			$("#cityLabel").css("visibility", "hidden");
			$("#city").valid();
			$("#addressState").prop('required', false);
			$("#addressState option[value='']").prop('selected', true);
			$("#addressStateTd").css("visibility", "hidden");
			$("#addressState").data("selectBox-selectBoxIt").refresh();
			$("#addressState").valid();
			$("#addressStateLabel").css("visibility", "hidden");
			$("#zipCode").prop('required', false);
			$("#zipCode").val("");
			$("#zipCode").css("visibility", "hidden");
			$("#zipCodeLabel").css("visibility", "hidden");
			$("#zipCode").valid();
		} else {
			$("#prevAddOutsideUSY").css("visibility", "visible");
			$("#prevAddOutsideUSN").css("visibility", "visible");
			$("#prevAddOutsideUSLabel").css("visibility", "visible");
			$("#address1").css("visibility", "visible");
			$("#address1Label").css("visibility", "visible");
			$("#address1").prop('required', true);
			$("#address2").css("visibility", "visible");
			$("#address2Label").css("visibility", "visible");
			$("#city").css("visibility", "visible");
			$("#cityLabel").css("visibility", "visible");
			$("#city").prop('required', true);
			$("#addressStateTd").css("visibility", "visible");
			$("#addressStateLabel").css("visibility", "visible");
			$("#addressState").prop('required', true);
			$("#addressState").data("selectBox-selectBoxIt").refresh();
			$("#zipCode").css("visibility", "visible");
			$("#zipCodeLabel").css("visibility", "visible");
			$("#zipCode").prop('required', true);
		}
	}
	this.updateDriversLicenseField = function() {
		if (CONSTANTS.BackgroundCheckDtls.isDrivingPos){
			$("#dlNumber1Label").css("visibility", "visible");
			$("#dlNumber1").css("visibility", "visible");
			$("#dlNumber1").prop('required', true);
			$("#dlNumber2Label").css("visibility", "visible");
			$("#dlNumber2").css("visibility", "visible");
			$("#dlNumber2").prop('required', true);
			$("#driversLicenseStateLabel").css("visibility", "visible");
			$("#driversLicenseState").css("visibility", "visible");
			$("#driversLicenseState").prop('required', true);
			$("#driversLicenseStateTd").css("visibility", "visible");
			$("#driversLicenseState").data("selectBox-selectBoxIt").refresh();
		} else {
			$("#dlNumber1Label").css("visibility", "hidden");
			$("#dlNumber1").css("visibility", "hidden");
			$("#dlNumber1").prop('required', false);
			$("#dlNumber1").val("");
			$("#dlNumber1").valid();
			$("#dlNumber2Label").css("visibility", "hidden");
			$("#dlNumber2").css("visibility", "hidden");
			$("#dlNumber2").prop('required', false);
			$("#dlNumber2").val("");
			$("#dlNumber2").valid();
			$("#driversLicenseStateLabel").css("visibility", "hidden");
			$("#driversLicenseState").css("visibility", "hidden");
			$("#driversLicenseState").prop('required', false);
			$("#driversLicenseStateTd").css("visibility", "hidden");
			$("#driversLicenseState").data("selectBox-selectBoxIt").refresh();
			$("#driversLicenseState").val("");
			$("#driversLicenseState").valid();
		}
	}

	//Make Service Call for Candidate Name and Address
	this.displayCandidateConfirmationInfo = function() { 
		$.blockUI({
			message : $('#loading')
		}); 
	$.ajax({
		type : "POST",
		url : "/RetailStaffing/service/CandidateDataFormService/searchCandidateId",
		dataType : "json",
		contentType:"application/json",
		data: {candidateId: CONSTANTS.interviewResultsObj.selectedCandidate.candRefNbr},
		success : getCandidateNameAddress,
		failure : callFailed
		  }); //End - $.ajax({ 
	}  // End - submitHandler: function(form) {			

//Display the Candidate Name and Address based on the Candidate Reference Number
function getCandidateNameAddress(json) {
//Check for Error Messages
	if (json.hasOwnProperty("Exception")) {
		if (json.Exception.userMessage.hasOwnProperty("cause")) {
			errorMessage = json.Exception.userMessage.cause.detailMessage;
		} else if (json.Exception.userMessage
				.hasOwnProperty("detailMessage")) {
			errorMessage = json.Exception.userMessage.detailMessage;
		} else {
			errorMessage = "An error has occurred retrieving Candidate Data."
		}
		jErrorAlert(errorMessage, 'Error Message');
		$(".showCpdStatus").text("incomplete");
		$(".showCpdStatus").css("color", "red");
		$('#cpdFormModal').modal('hide');
	} else if (json.Response.hasOwnProperty("status")
			&& json.Response.status === CONSTANTS.STATUS_SUCCESS) {
		$("#candidateConfirmName").val(json.Response.CandidateDetails.name);
		$("#candidateConfirmAddress1").val(json.Response.CandidateDetails.address1+' '+json.Response.CandidateDetails.address2);
		$("#candidateConfirmAddress2").val(json.Response.CandidateDetails.city+', '+json.Response.CandidateDetails.stateCode+' '+json.Response.CandidateDetails.zip);
	    $.unblockUI();
		$('#cpdFormModal').modal('show');
	}
}
	
};