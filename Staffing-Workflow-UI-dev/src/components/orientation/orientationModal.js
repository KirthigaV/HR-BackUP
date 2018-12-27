import React from 'react';
import PropTypes from 'prop-types';
import {Datepicker} from 'ux-react-styleguide';
import {formatPhoneNumber} from '../../utils/phoneNumberFormatter';
import * as OrientationActions from '../../actions/orientationActions';
import * as CandidateActions from '../../actions/candidateActions';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import TimePicker from 'rc-time-picker';
import moment from 'moment';
import * as ValidStoreActions from "../../actions/storeActions";
import {toTitleCase} from "../../utils/nameHelper";

class OrientationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: document.cookie.replace(/(?:(?:^|.*;\s*)username\s*\=\s*([^;]*).*$)|^.*$/, "$1"),
      date: null,
      time: null,
      format: 'h:mm a',
      storeNo: '',
      invalidStoreNumber: false,
      showAddress: false,
      addressRespone: null,
      dateError: false,
      timeError: false,
      locationError :false
    };

    this.setStoreNumber = this.setStoreNumber.bind(this);
    this.clearAddress = this.clearAddress.bind(this);
    this.validateOrientationFields= this.validateOrientationFields.bind(this);
    this.setOrientationDetails= this.setOrientationDetails.bind(this);
  }

  callUpdate = () => {
    this.validateOrientationFields();
    if(this.state.date!=null && this.state.time != null && this.state.storeNo != '' && this.state.storeNo.length == 4 && !this.state.invalidStoreNumber && !this.state.locationError) {
      this.jobDescription = this.props.jobDescription.substr(0, this.props.jobDescription.indexOf('('));
      this.orientation = this.setOrientationDetails();

      this.props.ortnActions.updateOrientation(this.orientation)
        .then(
          response => {
            if (response) {
              this.props.isUpdated();
              this.props.candActions.getCandidates(this.props.requisitionNumber);
            }
          }
        );
      this.clearAddress();
    }
  };

  validateOrientationFields(){

    if(this.state.date == null) {
      this.setState({
        dateError: true
      });
    } else {
      this.setState({
        dateError: false
      });
    }
    if(this.state.time == null) {
      this.setState({
        timeError: true
      });
    } else {
      this.setState({
        timeError: false
      });
    }
    if((this.state.storeNo == '') || (this.state.storeNo!= '' && this.state.storeNo.length < 4) || (this.state.invalidStoreNumber)) {
      this.setState({
        locationError: true
      });
    } else {
      this.setState({
        locationError: false
      });
    }
  }

  clearAddress(){
    this.setState({
      storeNo: '',
      invalidStoreNumber: false,
      showAddress: false,
      addressRespone: null,
      dateError: false,
      timeError: false,
      locationError :false,
      date: null,
      time:null
    });
    this.props.onClose();
  }

  validateStoreNumber(storeNumber) {
    this.props.storeActions.getStoreAddress(storeNumber).then(
      response => {
        if (response.storeAddress!== '') {
          this.setState({
            invalidStoreNumber: false,
            locationError: false,
            showAddress: true,
            addressRespone:response.storeAddress
          });
        } else {
          this.setState({
            invalidStoreNumber: true,
            locationError: true,
            showAddress: false
          });
        }
      }
    );
  }

  setDate = (date) => {
    this.setState({
      date: date
    });
  };

  setTime = (time) => {
    this.setState({
      time: time.format(this.state.format)
    });
  };

  setStoreNumber(event) {
    if (event.target.validity.valid) {
      this.setState({
        storeNo: event.target.value
      });

      if(event.target.value.length == 4) {
        this.validateStoreNumber(event.target.value);
      } else {
        this.setState({
          invalidStoreNumber: false,
          locationError: false,
          showAddress: false
        });
      }
    }
  }

  setOrientationDetails() {

    this.orientation = {};

    this.orientation.applicantId = this.props.candidate.applicantID;
    this.orientation.requisitionNumber = this.props.requisitionNumber;
    this.orientation.orientationDate = this.state.date;
    this.orientation.orientationTime = this.state.time;
    this.orientation.username = this.state.username;
    this.orientation.storeNo = this.state.storeNo;
    this.orientation.firstName = toTitleCase(this.props.candidate.firstName);
    this.orientation.jobDescription = this.jobDescription.substr(3).trim();
    this.orientation.email = this.props.candidate.email;
    this.orientation.addressLine1 = toTitleCase(this.state.addressRespone.addresLine1);
    this.orientation.addressLine2 = toTitleCase(this.state.addressRespone.addressLine2);
    this.orientation.city = toTitleCase(this.state.addressRespone.city);
    this.orientation.stateCode = this.state.addressRespone.stateCode;
    this.orientation.zipCode = this.state.addressRespone.zipcode;
    this.orientation.locationPhoneNo = formatPhoneNumber(this.state.addressRespone.phoneNumber);

    return this.orientation;
  }


  render() {
    if (!this.props.show) {
      return null;
    }

    return (
      <div className="backdrop-style card dialog">
        <div id= "schedOrientModal" className="modal-style">
          <div className="card-toolbar">
              <span className="card-title">
                  <h3 className="h3-label">Please contact the candidate then enter the agreed upon<br/> orientation date, time and location for your records.</h3>
              </span>
            <i className="icon_close" onClick={() => {this.clearAddress();}}/>
          </div>
          <div id="cardOrientation" className="card-content ">
            <div className="ortTable">
              <div className="ortTableRow">
                <div className="ortTableHead"><h5 className="h5-label">Candidate Phone Number</h5></div>
                <div className="ortTableHead"><h5 className="h5-label">Candidate Email</h5></div>
              </div>

              <div className="ortTableRow">
                <div className="ortTableCell">
                  <h5 className="h5-val">{formatPhoneNumber(this.props.candidate.phoneNmbr) === null ?
                  'Unavailable' : formatPhoneNumber(this.props.candidate.phoneNmbr)} </h5>
                </div>
                <div className="ortTableCell">
                  <h5 className="h5-val">{this.props.candidate.email === null ?
                  'Unavailable' : this.props.candidate.email}</h5>
                </div>
              </div>
            </div>

            <div className="ortTable">
              <div className="ortTableRow">
                <div className="ortTableHead"><h5 className={this.state.dateError ? "h5-label-error" : "h5-label"}>Orientation Date</h5></div>
                <div className="ortTableHead"><h5 className={this.state.timeError ? "h5-label-error" : "h5-label"}>Orientation Time</h5></div>
              </div>
            </div>

            <section id="orntDate">
              <div id="orntDateDiv">
                <Datepicker
                  id="pickerId"
                  callback={this.setDate}
                  placeholder="YYYY-MM-DD" error={this.state.dateError}/>
              </div>
              <div id="orientation-label">
                at
              </div>
              <div id="orntDateDiv orntTimeDiv">
                <TimePicker
                  defaultOpenValue={moment("12:00 am", 'HH:mm a')}
                  minuteStep={15}
                  showSecond={false}
                  onChange={this.setTime}
                  format={this.state.format}
                  use12Hours
                  inputReadOnly
                  className = {this.state.timeError ? "timepicker-error" : ""}
                />
              </div>
            </section>

            <div style={{display:'inline-flex',width:'100%'}}>
              <div id="orntLocationDiv" style={{width:'50%'}}>
                <div className="ortTable">
                  <div className="ortTableRow">
                    <div className="ortTableHead">
                      <h5 id="orntLocLabel" className={this.state.locationError ? "h5-label-error" :"h5-label"}>Location Of Orientation</h5>

                      <div className="ortNumAddress">
                        <div className={this.state.locationError ? "text-input-container error" : "text-input-container"}
                             id="orntLocation">
                          <input id="orntLocationInput" type="text" name="orntLocation" pattern="^\d{1,4}$" min="0" onChange={this.setStoreNumber}
                                 value={this.state.storeNo || ''}/>
                          <h5 id="storeNoLabel" className={this.state.locationError ? "h5-label-error" : "h5-label"}>Location Number</h5>
                          {this.state.invalidStoreNumber ?
                            <div>
                              <p id="storeNoLabel" className="error-msg">Invalid Location Number</p>
                            </div> : null}

                        </div>


                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div style={{width:'50%'}}>
              <div style={{width:'200px', height:'180px', marginTop:'-118px', marginLeft: '0px'}}>
                {this.state.showAddress && this.state.addressRespone &&
                <div className="ortTable">
                  <div className="ortTableRow"><h5 className="h5-label">Address : {toTitleCase(this.state.addressRespone.addresLine1)}</h5>  </div>
                  <div className="ortTableRow"><h5 className="h5-label">Address Line 2 : {toTitleCase(this.state.addressRespone.addressLine2)}</h5>  </div>
                  <div className="ortTableRow"><h5 className="h5-label">City : {toTitleCase(this.state.addressRespone.city)}</h5>  </div>
                  <div className="ortTableRow"><h5 className="h5-label">State : {this.state.addressRespone.stateCode}</h5>  </div>
                  <div className="ortTableRow"><h5 className="h5-label">Postal Code : {this.state.addressRespone.zipcode}</h5>  </div>
                </div>}
              </div>
              </div>
            </div>




          </div>
          <div className="buttons">
            <a className="button primary md" onClick={() => { this.callUpdate();}}     > Enter </a>
            <a className="button md" onClick={() => {this.clearAddress();}} > Cancel </a>
          </div>

        </div>
      </div>
    );
  }
}

OrientationModal.contextTypes = {
  store: PropTypes.object
};

OrientationModal.propTypes = {
  show: PropTypes.bool,
  onClose: PropTypes.func.isRequired,
  isUpdated: PropTypes.func.isRequired,
  candidate: PropTypes.object.isRequired,
  requisitionNumber: PropTypes.string.isRequired,
  jobDescription: PropTypes.string.isRequired,
  children: PropTypes.node,
  candActions: PropTypes.object,
  ortnActions: PropTypes.object,
  updateSuccess: PropTypes.number,
  storeActions: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  candActions: bindActionCreators(CandidateActions, dispatch),
  ortnActions: bindActionCreators(OrientationActions, dispatch),
  storeActions: bindActionCreators(ValidStoreActions, dispatch),
});

const mapStateToProps = (state) => ({
  updateSuccess: state.updateSuccess
});

export default connect(mapStateToProps, mapDispatchToProps)(OrientationModal);
