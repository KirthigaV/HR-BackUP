import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import * as RequisitionActions from '../../actions/requisitionActions';
import RequisitionUtils from './requisitionUtils';

class Requisitions extends Component {

  constructor(props) {
    super(props);

    this.columnTitles = [
      {key: 'reqType', sortable: true, label: 'Requisition Type'},
      {key: 'reqNumber', sortable: true, label: 'Requisition Number'},
      {key: 'employType', sortable: true, label: 'Employment Type'},
      {key: 'availPref', sortable: true, label: 'Availability Preference'},
      {key: 'actionItems', sortable: true, defaultSort: true, label: 'Action Items'}
    ];
    this.requisitions= [];
    this.state = {
      requisitionInfo: this.props.requisition.requisitionInfo,
      activeReqRes : false,
      reqList: null
    };
  }

  componentWillMount(){

    this.props.actions.getRequisitions(this.props.storeNumber)
      .then(response => {
        if(response){
          this.setState({activeReqRes: true});
          this.props.actions.getReqActionsNeeded(response.requisitionInfo)
            .then(response => {
              if(response){
                if (this.state.activeReqRes) {

                  let activeReqs = [];
                  for(let obj of this.props.requisition.requisitionInfo) {
                    let newObj = {};
                    newObj.reqType = RequisitionUtils.formatRequisitionType(obj.jobDescription, obj.deptNumber);
                    newObj.requisitionNumber = obj.requisitionNumber;
                    newObj.employmentType = RequisitionUtils.formatEmploymentType(obj.isFullTime, obj.isPartTime, obj.isSeasonal);
                    newObj.availPref = RequisitionUtils.formatAvailabilityDays(obj.availPref[0])
                      + '\n' + RequisitionUtils.formatAvailabilityTime(obj.availPref[1]);
                    activeReqs.push(newObj);
                  }

                  let reqMap = new Map();
                  for(let req of activeReqs) {
                    reqMap.set(req.requisitionNumber, req);
                  }

                  let newReqList = [];

                  if(this.props.requisition.requisitionActions != null && this.props.requisition.requisitionActions.length > 0 ) {
                    for (let actions of this.props.requisition.requisitionActions){
                      let obj = {};
                      obj = reqMap.get(actions.requisitionNumber);
                      obj.actionsNeeded = actions.actionsNeeded;
                      newReqList.push(obj);
                    }
                  }

                  let actionsSortList = newReqList.filter(function (el) {
                    return el != null;
                  });

                  this.setState({
                    reqList: actionsSortList,
                    originalReqList:actionsSortList
                    });

                    this.setReqLoaderDataToParent(this.state.activeReqRes);
                    this.requisitions = this.state.reqList;
                }
              }
            });
        }
      });

  }

  componentDidUpdate(prevProps) {
    if(prevProps.filterKey != this.props.filterKey){
      /*eslint-disable react/no-did-update-set-state*/
      this.setState({
        reqList: this.requisitions
      });

    }
  }


  setReqLoaderDataToParent(activeReqRes){
    this.props.sendReqLoaderData(activeReqRes);
  }

  renderTitles = (titles) => {
    return (
      titles.map(col =>(
        <th key={col.label}>{col.label}</th>
      ))
    );
  };


  search(objects, toSearch){
    let results = [];

    if(toSearch!=undefined && objects!=undefined ) {

      for (let i = 0; i < objects.length; i++) {
        for (let key in objects[i]) {
          if (objects[i][key].toString().toLowerCase().indexOf(toSearch.toLowerCase()) != -1) {
            results.push(objects[i]);
            break;
          }

        }
      }
      return results;
    } else {
      return objects;
    }
  }


  populateRows = (reqList, originalList) => {

    reqList = originalList;

    this.requisitionList = this.search(reqList,this.props.filterKey);
    this.requisitions = this.requisitionList;

    if (this.requisitions && this.requisitions.length !== 0 && this.requisitions !== null){
      return (
        this.requisitions.map((req, i) => (
          <tr key={req.requisitionNumber} >
            <td key={this.columnTitles[0].label + req.requisitionNumber}>
              <Link to={"requisitions/"+ req.requisitionNumber + "/" + "active"}>
                {req.reqType}</Link>
            </td>
            <td key={this.columnTitles[1].label + req.requisitionNumber}>
              {req.requisitionNumber}
            </td>
            <td key={this.columnTitles[2].label + req.requisitionNumber}>
              {req.employmentType}
            </td>
            <td key={this.columnTitles[3].label + req.requisitionNumber} className={"availability-column"}>
              {req.availPref}
            </td>
            <td key={this.columnTitles[4].label + req.requisitionNumber}>
              {(this.props.requisition.requisitionActions === null
                || this.props.requisition.requisitionActions.length <= i
                || req.actionsNeeded === 0
                || req.actionsNeeded === undefined) ? "" :
                <b> {req.actionsNeeded} Candidate
                  {(req.actionsNeeded === 1) ? " Needs Attention" : "s Need Attention"} </b>}
            </td>
          </tr>
        ))
      );
    }
  };

  render() {
    return (
      <table className="standard-table" id="reqTable">
        <thead>
        <tr>
          {this.renderTitles(this.columnTitles)}
        </tr>
        </thead>
        {this.state.activeReqRes &&
        <tbody>
          {this.state.reqList && this.populateRows(this.state.reqList, this.state.originalReqList)}
        </tbody> }
      </table>
    );
  }
}

Requisitions.propTypes = {
  storeNumber: PropTypes.string.isRequired,
  requisition: PropTypes.object,
  actions: PropTypes.object,
  sendReqLoaderData: PropTypes.func,
  filterKey: PropTypes.string
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(RequisitionActions, dispatch)
});

const mapStateToProps = (state) => ({
  requisition: state.requisition
});

export default connect(mapStateToProps, mapDispatchToProps)(Requisitions);
