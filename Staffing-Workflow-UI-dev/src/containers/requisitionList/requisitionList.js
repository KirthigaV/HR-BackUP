import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {Link} from 'react-router';
import Requisitions from "../../components/requisitions/requisitions";
import {showLoadingBar} from "../../utils/loaderHelper";
import Authentication from "../../utils/authHelper";

class RequisitionList extends Component {

  constructor(props, context) {
    super(props, context);
    this.store = context.store;
    const loginState = this.store.getState().login;
    this.getReqLoaderDetails = this.getReqLoaderDetails.bind(this);

    this.state = {
      storeNo: '',
      loadReqs : true,
      filterVal : ''
    };
    if (this.props.requisitionList.login.userProfile == null) {
      Authentication.getUserProfile(loginState).then(res => {
        this.setState({
          storeNo: res.storeNumber
        });
      });
    } else {
      this.state = {
        storeNo: this.props.requisitionList.login.userProfile.storeNumber,
        loadReqs : true
      };
    }
  }

  getReqLoaderDetails(activeReqRes) {
    if (activeReqRes) {
      this.setState({loadReqs: false});
    }
  }

  onChangeHandler(e){
    this.setState({
      filterVal: e.target.value,
    });
  }

  render() {

    return (
      <div>
        <div className="breadcrumb-nav" style={{"position" : "absolute"}}>
          <span className="breadcrumb"><Link to={"home"}>Home</Link></span>
          <span className="breadcrumb active">Requisitions for location #{this.state.storeNo}</span>
        </div>
        <div style={{"height" : "100px"}}/>
        {showLoadingBar(this.state.loadReqs)}
        <div className="card">
          <div className="card-toolbar">
            <span className="card-title"><h3>Requisitions for location #{this.state.storeNo}</h3></span>
            <div className="text-input-container">
              <input value={this.state.filterVal} type="text" title="Search" placeholder= "Search for ..." onChange={this.onChangeHandler.bind(this)}/>
            </div>
          </div>
          <div className="card-content">
            {this.state.storeNo &&
            <Requisitions storeNumber={this.state.storeNo} sendReqLoaderData={this.getReqLoaderDetails} filterKey={this.state.filterVal}/>
            }
          </div>
        </div>
        <br/><br/>
      </div>
    );
  }
}

RequisitionList.contextTypes = {
  store: PropTypes.object
};

RequisitionList.propTypes = {
  requisitionList: PropTypes.object,
  requisition: PropTypes.object,
  login: PropTypes.object,
};

const mapStateToProps = (state) => ({
  requisitionList: state,
  login: state.login
});

export default connect(mapStateToProps, null)(RequisitionList);
