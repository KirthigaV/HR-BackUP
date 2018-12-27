import RequisitionUtils from './requisitionUtils';
import expect from 'expect';

describe('requisition table Tests', () => {

  it("formatRequisitionType test", () =>{
    let testJob = "Cashier";
    let testDeptNum = "090";
    let expectedResult = testJob + ' (D-' + testDeptNum + ')';
    let results = RequisitionUtils.formatRequisitionType(testJob, testDeptNum);

    expect(results).toBe(expectedResult);
  });

  it("formatEmploymentType test", () =>{

    //Seasonal FT & PT
    let fullTime = "Y";
    let partTime = "Y";
    let seasonal = "Y";
    let expectedResult = "Seasonal, Full Time & Part Time";
    let results = RequisitionUtils.formatEmploymentType(fullTime,partTime,seasonal);
    expect(results).toBe(expectedResult);

    //Seasonal PT
    fullTime = "N";
    expectedResult = "Seasonal, Part Time";
    results = RequisitionUtils.formatEmploymentType(fullTime,partTime,seasonal);
    expect(results).toBe(expectedResult);

    //Seasonal FT
    fullTime = "Y";
    partTime = "N";
    expectedResult = "Seasonal, Full Time";
    results = RequisitionUtils.formatEmploymentType(fullTime,partTime,seasonal);
    expect(results).toBe(expectedResult);

    //Permanent FT & PT
    partTime = "Y";
    seasonal = "N";
    expectedResult = "Full Time & Part Time";
    results = RequisitionUtils.formatEmploymentType(fullTime,partTime,seasonal);
    expect(results).toBe(expectedResult);

    //Permanent PT
    fullTime = "N";
    partTime = "Y";
    expectedResult = "Part Time";
    results = RequisitionUtils.formatEmploymentType(fullTime,partTime,seasonal);
    expect(results).toBe(expectedResult);

    //Permanent FT
    fullTime = "Y";
    partTime = "N";
    expectedResult = "Full Time";
    results = RequisitionUtils.formatEmploymentType(fullTime,partTime,seasonal);
    expect(results).toBe(expectedResult);
  });

  it("formatAvailabilityTime test", () =>{
    //one time
    let availability = [ [], [ "12pm-5pm" ] ];
    let expectedResult = "12pm-5pm";
    let results = RequisitionUtils.formatAvailabilityTime(availability[1]);
    expect(results).toBe(expectedResult);

    //two times
    availability = [ [], [ "12pm-5pm", "6pm-8pm" ] ];
    expectedResult = "12pm-5pm or 6pm-8pm";
    results = RequisitionUtils.formatAvailabilityTime(availability[1]);
    expect(results).toBe(expectedResult);

    //no times
    availability = [ [], [] ];
    expectedResult = "No Preference";
    results = RequisitionUtils.formatAvailabilityTime(availability[1]);
    expect(results).toBe(expectedResult);
  });

  it("formatAvailabilityDays test", () =>{
    //one day
    let availability = [ ["Weekdays"], [ ] ];
    let expectedResult = "Weekdays";
    let results = RequisitionUtils.formatAvailabilityDays(availability[0]);
    expect(results).toBe(expectedResult);

    //two days
    availability = [ ["Weekdays", "Weekdays"], [ ] ];
    expectedResult = "Weekdays & Weekdays";
    results = RequisitionUtils.formatAvailabilityDays(availability[0]);
    expect(results).toBe(expectedResult);

    //three days
    availability = [ ["Weekdays", "Weekdays", "Weekdays"], [ ] ];
    expectedResult = "Weekdays, Weekdays, & Weekdays";
    results = RequisitionUtils.formatAvailabilityDays(availability[0]);
    expect(results).toBe(expectedResult);

    //no days
    availability = [ [], [] ];
    expectedResult = "No Preference";
    results = RequisitionUtils.formatAvailabilityDays(availability[0]);
    expect(results).toBe(expectedResult);
  });
});
