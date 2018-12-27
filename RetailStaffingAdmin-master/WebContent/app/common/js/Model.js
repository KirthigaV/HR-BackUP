/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: model.js
 * Application: Retail Staffing Admin
 *
 */
var MODEL = new model();
//This function is used to construct response for user profile details
function model() {
    this.loginUserId = "";
    this.loginAssocId = "";
    this.loginUserApplnRole = "";
    this.userProfileDetails = {};
}