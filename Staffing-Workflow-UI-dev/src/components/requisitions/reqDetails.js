import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import * as ReqDetailsActions from '../../actions/reqDetailsActions';
import RequisitionUtils from './requisitionUtils';
import { toTitleCase } from '../../utils/nameHelper';


class ReqDetails extends Component {

  constructor(props, context) {
    super(props, context);

    this.titles = [
      {index: 0, key: 'dateCreated', label: 'Date Created:'},
      {index: 1, key: 'employType', label: 'Employment Type:'},
      {index: 2, key: 'interviewSch', label: 'Interviews Scheduled:'},
      {index: 3, key: 'position', label: 'Positions Filled:'},
      {index: 4, key: 'TAC', label: 'Talent Acquisition Center to Manage:'},
      {index: 5, key: 'createdBy', label: 'Created By:'},
      {index: 6, key: 'availdays', label: 'Availability Days:'},
      {index: 7, key: 'interviewReq', label: 'Interviews Requested:'},
      {index: 8, key: 'payRate', label: 'Pay Rate:'},
      {index: 9, key: 'calendar', label: 'Selected Calendar:'},
      {index: 10, key: 'reqNum', label: 'Requisition Number:'},
      {index: 11, key: 'availType', label: 'Availability Times:'},
    ];
    this.reqType = "Open";
    this.reqDataList = [];
    this.state = {
      requisitionInfo: [],
      bcInfo: '',
      storeNumber: ''
    };

    this.props.actions.getReqDetails(this.props.requisitionNumber).then(
      response => {
        if (response) {
          this.setState({
            requisitionInfo: this.props.requisitionDetails.requisitionInfo,
            bcInfo: response.requisitionInfo.jobDescription + ' (D-'+response.requisitionInfo.deptNumber+')',
            storeNumber: response.requisitionInfo.storeNumber
            });
          this.setBreadcrumbVal(this.state.bcInfo, this.state.storeNumber);
        }
      }
    );
  }

  setBreadcrumbVal(bcInfo, storeNo){
    this.props.sendBCData(bcInfo, storeNo);
  }

  getAndFormatReqDataList = (req) => {
    let reqList = [];
    if(req !== null && req.length !== 0 && req)
    {
      reqList.push(RequisitionUtils.formatDate(req.createTimeStamp));
      reqList.push(RequisitionUtils.formatEmploymentType(req.isFullTime, req.isPartTime, req.isSeasonal));
      reqList.push(req.interviewsScheduled);
      if(req.positionsRequested - req.openPositions < 0){
        reqList.push("0/" + req.positionsRequested);
      } else {
        reqList.push(req.positionsRequested - req.openPositions + "/" + req.positionsRequested);
      }
      reqList.push((req.isTACScheduled === "Y") ? "YES" : "NO");
      reqList.push(toTitleCase(RequisitionUtils.formatUserName(req.userName)));
      reqList.push(RequisitionUtils.formatAvailabilityDays(req.availability[0]));
      reqList.push(req.interviewCandCount);
      reqList.push("$" + req.payRate + "/hour");
      reqList.push(req.calendar);
      reqList.push(req.requisitionNumber);
      reqList.push(RequisitionUtils.formatAvailabilityTime(req.availability[1]));

      reqList.push(toTitleCase(req.jobDescription));
      reqList.push(req.deptNumber);
    }
    return reqList;
  };

  renderData = (data) => {
    return (<td className="detail-box" key={data.index}><span className="requisition-data-title">
                {data.label}</span>{((data.index !== 4) ? "\n" : " ") + this.reqDataList[data.index]}</td>);
  };

  render() {
    this.reqDataList = this.getAndFormatReqDataList(this.state.requisitionInfo);
    this.reqType = "Open";
    return (
      <div>
        <div className="card-toolbar">
        <span className="card-title">
          <h2>{this.reqType} Employment Requisition for {this.reqDataList[12]} (D-{this.reqDataList[13]})</h2>
        </span>
        </div>
        <table className={"details"}>
          <tbody>
          <tr>
            {this.titles.slice(0,5).map(this.renderData)}
          </tr>
          <tr>
            {this.titles.slice(5, 10).map(this.renderData)}
          </tr>
          <tr>
            {this.titles.slice(10, this.titles.length).map(this.renderData)}
          </tr>
          </tbody>
        </table>
      </div>
    );
  }
}

ReqDetails.contextTypes = {
  store: PropTypes.object
};

ReqDetails.propTypes = {
  requisitionNumber: PropTypes.string.isRequired,
  requisitionType : PropTypes.string.isRequired,
  requisitionDetails: PropTypes.object,
  sendBCData: PropTypes.func,
  actions: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(ReqDetailsActions, dispatch)
});

const mapStateToProps = (state) => ({
  requisitionDetails: state.requisitionDetails
});

export default connect(mapStateToProps, mapDispatchToProps)(ReqDetails);

