import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {Link} from 'react-router';
import ReqDetails from "../../components/requisitions/reqDetails";
import Candidates from "../../components/candidates/candidates";
import {showLoadingBar} from "../../utils/loaderHelper";
import {toTitleCase} from "../../utils/nameHelper";

class RequisitionDetails extends Component {

  constructor(props) {
    super(props);
    this.requisitionId =  this.props.params.reqId;
    this.requisitionType = this.props.params.reqType;
    this.reqType = "Open";
    this.getBCData = this.getBCData.bind(this);

    this.state = {
      jobTitle: ''
    };
  }

  checkLoad() {
    return !(this.props.requisitionDetails.requisitionDetails.requisitionInfo !== 0
      && this.props.requisitionDetails.requisitionDetails.requisitionInfo !== null
      && this.props.requisitionDetails.requisitionDetails.requisitionInfo.requisitionNumber === Number(this.requisitionId)
      && this.props.requisitionDetails.candidates.candidates !== null
      && ((this.props.requisitionDetails.candidates.candidates.length > 0) ?
        this.props.requisitionDetails.candidates.candidates[0].requisitionNumber === Number(this.requisitionId) : true));
  }

  getBCData(jobTitleInfo, storeNo){
    this.setState ({
      jobTitle: 'for '+toTitleCase(jobTitleInfo),
      storeNumber: '#'+storeNo
    });
  }

  render() {
    let load = this.checkLoad();
    this.reqType = "Open";
    return (
      <div>
        <div className="breadcrumb-nav" style={{"position" : "absolute"}}>
          <span className="breadcrumb"><Link to={"/home"}>Home</Link></span>
          <span className="breadcrumb"><Link to={"/requisitions"}>Requisitions for location {this.state.storeNumber}</Link></span>
          <span className="breadcrumb active">{this.reqType} Requisition {this.state.jobTitle}</span>
        </div>
        <div style={{"height" : "103px"}}/>
        <div>
          {showLoadingBar(load)}
        </div>
        <div className= "card">
          <div className="requisition-details">
            <ReqDetails requisitionNumber={this.requisitionId} requisitionType={this.requisitionType} sendBCData={this.getBCData}/>
          </div>
          <div className="candidate-table">
              <Candidates requisitionNumber={this.requisitionId} jobDescription={this.state.jobTitle}/>
          </div>
        </div>
      </div>
    );
  }
}

RequisitionDetails.propTypes = {
  params:PropTypes.object,
  requisitionDetails: PropTypes.object
};

const mapStateToProps = (state) => {
  return {requisitionDetails: state};
};

export default connect(mapStateToProps, null)(RequisitionDetails);
