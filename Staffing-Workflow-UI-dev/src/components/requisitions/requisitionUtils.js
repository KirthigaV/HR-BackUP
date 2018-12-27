export default class RequisitionUtils {

  static formatEmploymentType(isFullTime, isPartTime, isSeasonal){
    let employType = "";
    let isPartOrFullTime = "";

    if (isFullTime === "Y" && isPartTime === "Y")
      isPartOrFullTime = "Full Time & Part Time";
    else if (isFullTime === "N" && isPartTime === "Y")
      isPartOrFullTime = "Part Time";
    else if (isFullTime === "Y" && isPartTime === "N")
      isPartOrFullTime = "Full Time";

    if (isSeasonal === "Y") {
      employType = "Seasonal, " + isPartOrFullTime;
    }
    else {
      employType = isPartOrFullTime;
    }

    return employType;
  }

  static formatAvailabilityTime(availability) {
    let formatedAvail='';

    if(availability.length > 0){
      for(let i = 0; availability.length-1 > i; i++){
        formatedAvail = formatedAvail + availability[i] + ' or ';
      }
      formatedAvail = formatedAvail + availability[availability.length-1];
    }
    else{
      formatedAvail = "No Preference";
    }
    return formatedAvail;
  }

  static formatAvailabilityDays(availability){
    let formatedAvail = '';
    if(availability.length === 0){
      formatedAvail = "No Preference";
    }
    else if(availability.length === 1){
      formatedAvail = availability[0];
    }
    else if(availability.length === 2){
      formatedAvail = availability[0] + ' & ' + availability[1];
    }
    else if(availability.length === 3){
      for(let i =0; availability.length-1 > i; i++){
        formatedAvail = formatedAvail + availability[i] + ', ';
      }
      formatedAvail = formatedAvail + '& ' + availability[availability.length-1];
    }
    return formatedAvail;
  }

  static formatRequisitionType(job, deptNum){
    return job + ' (D-' + deptNum + ')';
  }

  static formatUserName(userName) {
    if(userName !== undefined){
      let str = userName;
      let pos = str.indexOf(",");
      return str.slice(pos + 1, str.length) + " " + str.slice(0, pos);
    }
    else{
      return "N/A";
    }

  }

  static formatDate(date) {
    let day = date.slice(8,10);
    let month = date.slice(5,7);
    let year = date.slice(0,4);
    let formatedDate = month + "/" + day + "/" + year;

    return formatedDate;
  }

}
