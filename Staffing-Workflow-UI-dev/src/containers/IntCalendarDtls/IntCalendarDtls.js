import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {CONFIG} from "../../../config/globals";
import * as CalendarActions from "../../actions/calendarActions";
import {showLoadingBar} from "../../utils/loaderHelper";
import Authentication from "../../utils/authHelper";

class IntCalendarDtls extends Component {

  constructor(props, context) {
    super(props, context);
    let calData = [{}];
    this.store = context.store;
    const loginState = this.store.getState().login;
    this.state = {
      calData: calData,
      load : true,
      storeNo: ''
    };

    if (this.props.login.userProfile == null) {
      Authentication.getUserProfile(loginState).then(res => {
        if(res) {
          this.setState({
            storeNo: res.storeNumber,
            load : true
          });
          this.getStoreInfo(res.storeNumber);
        }
      });
    } else {
      this.state = {
        storeNo: this.props.login.userProfile.storeNumber,
        load : true
      };
      this.getStoreInfo(this.props.login.userProfile.storeNumber);
    }

    this.getStoreInfo = this.getStoreInfo.bind(this);
  }

  getStoreInfo(storeNo){
    this.props.calendarActions.getAllCalendarDetails(storeNo).then(
      response => {
        if (response) {
          this.calArrLen = response.calendarDetails.length;
          this.setState({
            calData: response.calendarDetails,
            load: false
          });
        }
      }
    );
  }

  render() {
    let calElements = [];
    let slotsNeeded = 0;
    if(this.calArrLen >= 1) {
      for (let i = 0; i < this.calArrLen; i++) {
        if (this.state.calData[i].reqCount-this.state.calData[i].slotsAvailable-this.state.calData[i].slotsScheduled < 0) {
          slotsNeeded = 0;
        } else {
          slotsNeeded = this.state.calData[i].reqCount-this.state.calData[i].slotsAvailable-this.state.calData[i].slotsScheduled;
        }
        if (slotsNeeded > 0) {
          calElements.push(
            <div className="child-cal-div" key={this.state.calData[i].calendarID}>
              <Link
                to={CONFIG.intvAvailUrl + '?=sessionID=&reqNbr=&strNo=' + this.state.storeNo + '&calId=' + this.state.calData[i].calendarID}
                target="_blank">
                {/*{showLoadingBar(load)}*/}
                <div className="card card-center" id="cardActive">
                  <div className="card-toolbar card-toolbar-display">
          <span className="card-title card-title-span">
            <i className="icon_calendar-date-range" id="icon-calendarActive"/>
          <h3>{this.state.calData[i].calendarDescription}</h3>
          </span>
                  </div>
                  <div className="card-content card-text">
                    <h5 style={{lineHeight: 24+'px'}}>
                      <table align="center">
                        <tr>
                          <td width="20px" align="right">{this.state.calData[i].reqCount}</td>
                          <td align="left">Requested Interviews</td>
                        </tr>
                        <tr>
                          <td width="20px" align="right">{this.state.calData[i].slotsScheduled}</td>
                          <td align="left">Interview(s) Scheduled</td>
                        </tr>
                        <tr>
                          <td width="20px" align="right">{this.state.calData[i].slotsAvailable}</td>
                          <td align="left">Available Slots</td>
                        </tr>
                        <tr>
                          <td width="20px" align="right"><text style={{fontWeight: 'bold', color:'black'}}>
                            {slotsNeeded}</text></td><td align="left"><text style={{fontWeight: 'bold', color:'black'}}>Time Slots Needed</text></td></tr>
                      </table></h5>
                  </div>
                  <div className="card-actions center">
                    <button className="button primary md">
                      Manage Slots
                    </button>
                  </div>
                </div>
              </Link>
            </div>
          );
        }
        else {
          calElements.push(
            <div className="child-cal-div" key={this.state.calData[i].calendarID}>
              <Link
                to={CONFIG.intvAvailUrl + '?=sessionID=&reqNbr=&strNo=' + this.state.storeNo + '&calId=' + this.state.calData[i].calendarID}
                target="_blank">
                <div className="card card-center">
                  <div className="card-toolbar card-toolbar-display">
          <span className="card-title card-title-span">
          <i className="icon_calendar-date-range" id="icon-calendar"/>
          <h3>{this.state.calData[i].calendarDescription}</h3>
          </span>
                  </div>
                  <div className="card-content card-text">
                    <h5 style={{lineHeight: 24+'px'}}>
                      <table align="center">
                        <tr>
                          <td width="20px" align="right">{this.state.calData[i].reqCount}</td>
                          <td align="left">Requested Interviews</td>
                        </tr>
                        <tr>
                          <td width="20px" align="right">{this.state.calData[i].slotsScheduled}</td>
                          <td align="left">Interview(s) Scheduled</td>
                        </tr>
                        <tr>
                          <td width="20px" align="right">{this.state.calData[i].slotsAvailable}</td>
                          <td align="left">Available Slots</td>
                        </tr>
                        <tr>
                          <td width="20px" align="right"><text style={{fontWeight: 'bold', color:'black'}}>
                            {slotsNeeded}</text></td><td align="left"><text style={{fontWeight: 'bold', color:'black'}}>Time Slots Needed</text></td></tr>
                      </table></h5>
                  </div>
                  <div className="card-actions center">
                    <button className="button secondary md">
                      Manage Slots
                    </button>
                  </div>
                </div>
              </Link>
            </div>
          );
        }
      }
    }

    return (
      <div className="parent-cal-div">
        <div className="breadcrumb-nav" style={{"position" : "absolute"}}>
          <span className="breadcrumb"><Link to={"home"}>Home</Link></span>
          <span className="breadcrumb active">Interview Availability Calendars</span>
        </div>
        <div style={{"height" : "119px"}}/>
        {showLoadingBar(this.state.load)}
        {this.state.storeNo  &&
        calElements
        }
      </div>
    );
  }
}

IntCalendarDtls.contextTypes = {
  store: PropTypes.object
};

IntCalendarDtls.propTypes = {
  login: PropTypes.object,
  calendarActions: PropTypes.object,
};

const mapDispatchToProps = dispatch => ({
  calendarActions: bindActionCreators(CalendarActions, dispatch)
});

const mapStateToProps = (state) => ({
  login: state.login
});

export default connect(mapStateToProps, mapDispatchToProps)(IntCalendarDtls);
