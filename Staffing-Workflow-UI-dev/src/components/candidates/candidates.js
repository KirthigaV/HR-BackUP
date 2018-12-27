import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import * as CandidateActions from '../../actions/candidateActions';
import { toTitleCase } from '../../utils/nameHelper';
import OrientationModal from "../orientation/orientationModal";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';


class Candidates extends Component {

  constructor(props) {
    super(props);
    this.activeCandColTitles = [
      {key: 'candidate', label: 'Candidate'},
      {key: 'applicantType', label: 'Assoc/Candidate'},
      {key: 'status', label: 'Status'},
      {key: 'actionItems', label: 'Action'}
    ];

    this.releasedCandColTitles = [
      {key: 'candidate', label: 'Candidate'}
    ];

    this.state = {
      isOpen: false,
      candidate: {},
      showCandTable : false,
      filterVal : '',
      activeCandidates: [],
      releasedCandidates: [],
      tempActiveCandidates: [],
      tempReleasedCandidates: [],
      tabIndex: 0,
      isUpdated : false
    };
  }

  componentDidMount() {
    this.getCandidates();
  }

  componentDidUpdate(prevProps, prevState){
    if(prevState.isUpdated !== this.state.isUpdated) {
      this.getCandidates();
    }
  }


  getCandidates() {
    this.props.actions.getCandidates(this.props.requisitionNumber).then(
      response => {
        if(response){
          let activeCandidates = this.props.candidates.candidates.filter(cand => cand.activeFlag !== 'N');
          let releasedCandidates = this.props.candidates.candidates.filter(cand => cand.activeFlag == 'N');
          let newActiveCandidates = [];
          let newReleasedCandidates = [];

          for(let obj of activeCandidates) {
            let newObj = {};
            let count = 0;
            newObj.name = toTitleCase(obj.firstName + " " + obj.lastName);
            newObj.applicantType = obj.applicantType==='EXT' ? "CA" : obj.storeNbr;
            newObj.status = this.determineStatus(obj.statuses);
            newObj.actionLabel = this.displayNextAction(obj,count);
            count++;
            newActiveCandidates.push(newObj);
          }

          for(let obj of releasedCandidates) {
            let newObj = {};
            newObj.name = toTitleCase(obj.firstName + " " + obj.lastName);
            newReleasedCandidates.push(newObj);
          }
          this.setState({
            showCandTable: true,
            activeCandidates: newActiveCandidates,
            releasedCandidates: newReleasedCandidates,
            tempActiveCandidates: newActiveCandidates,
            tempReleasedCandidates:newReleasedCandidates
          });
        }

      }
    );
  }

  determineStatus = (statuses) => {
    let status = "";
    for(let s of statuses){
      status = status + s + "\n";
    }
    return status;
  };



  renderTitles = (titles) => {
    return (
      titles.map(col =>(
        <th key={col.label}>{col.label}</th>
      ))
    );
  };

  orientationButton = (row) =>{
    this.setState({
      isOpen: !this.state.isOpen,
      candidate: row
    });
  };

  closeModal = () =>{
    this.setState({
      isOpen: !this.state.isOpen
    });
  };

  displayNextAction(row,i){

   if(row.link===null || typeof row.link.text === 'undefined'){
     return'';
   } else if(row.link.text === "Schedule Orientation"){
     return <button id={row.link.text + "-"+ i} title={row.link.text} className="button primary md button-width" onClick={this.orientationButton.bind(this,row)}>{row.link.text}</button>;
   } else if(row.link.url !== null && row.link.buttonType === "tertiary"){
     return <a id={row.link.text + "-"+ i} title={row.link.text} className="button tertiary md button-width" href={row.link.url} target="_blank">{row.link.text}</a>;
   } else if(row.link.url !== null){
     return <a id={row.link.text + "-"+ i} title={row.link.text} className="button primary md button-width" href={row.link.url} target="_blank">{row.link.text}</a>;
   } else {
     return <button id={row.link.text + "-"+ i} title={row.link.text} className="button primary md button-width">{row.link.text}</button>;
    }

  }

  search(objects, toSearch){
    let results = [];
    if(toSearch!=undefined) {

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

  populateTable = (tempCandidates, activeCandidates) => {

    tempCandidates = activeCandidates;
    this.candidates = this.search(tempCandidates,this.state.filterVal);
    if(this.candidates !== null && this.candidates.length !== 0 && this.candidates){
      return (
        this.candidates.map((row , i) =>(
          <tr key={i + row.name}>
            <td className="candidate-row">{row.name}</td>
            <td className="candidate-row">{row.applicantType}</td>
            <td className="candidate-row status-column">{row.status}</td>
            <td className="button-column">{row.actionLabel}</td>
          </tr>
        ))
      );
    }
  };

  populateReleasedCandTable = (tempCandidates, releasedCandidates) => {
    tempCandidates = releasedCandidates;
    this.releasedCandidates = this.search(tempCandidates,this.state.filterVal);
    if(this.releasedCandidates !== null && this.releasedCandidates.length !== 0 && this.releasedCandidates){
      return (
        this.releasedCandidates.map((row , i) => (
          <tr key={i + row.name}>
            <td className="candidate-row">{row.name}</td>
          </tr>
        ))
      );
    }
  };

  onChangeHandler(e){
    this.setState({
      filterVal: e.target.value,
    });

  }

  isUpdated = () =>{
    this.setState({
      isUpdated: !this.state.isUpdated
    });
  };



  render() {

    return (

      <Tabs selectedIndex={this.state.tabIndex} onSelect={tabIndex => {
        this.setState({
          tabIndex: tabIndex,
          filterVal: ''
        });
      }}>
        <TabList>
          <Tab>View Active Candidates</Tab>
          <Tab>View Released Candidates</Tab>
          <div className="text-input-container candidate-search"  >
            <input type="text" title="Search" value={this.state.filterVal} placeholder= "Search for ..." onChange={this.onChangeHandler.bind(this)}/>
          </div>
        </TabList>

        <TabPanel>
          <div>
            <br/>
            <h3 className={"candidate-title"}>Showing all candidates attached to this requisition</h3>
            <table className="standard-table" id="candTable">
              <thead>
              <tr className="column-titles">
                {this.renderTitles(this.activeCandColTitles)}
              </tr>
              </thead>
              <tbody>
              {this.state.showCandTable &&
              this.populateTable(this.state.tempActiveCandidates, this.state.activeCandidates)
              }
              </tbody>
            </table>
            <OrientationModal candidate={this.state.candidate} requisitionNumber={this.props.requisitionNumber}
                              jobDescription={this.props.jobDescription}
                              show={this.state.isOpen} onClose={this.closeModal} isUpdated = {this.isUpdated}/>
          </div>
        </TabPanel>
        <TabPanel>
          <div>
            <br/>
            <h3 className={"candidate-title"}>Showing all candidates released from this requisition</h3>
            <table className="standard-table" id="candTable">
              <thead>
              <tr className="column-titles">
                {this.renderTitles(this.releasedCandColTitles)}
              </tr>
              </thead>

              <tbody>
              {this.state.showCandTable &&
              this.populateReleasedCandTable(this.state.tempReleasedCandidates,this.state.releasedCandidates)
              }
              </tbody>

            </table>
          </div>
        </TabPanel>
      </Tabs>


    );
  }
}

Candidates.contextTypes = {
  store: PropTypes.object
};

Candidates.propTypes = {
  requisitionNumber: PropTypes.string.isRequired,
  jobDescription: PropTypes.string.isRequired,
  candidates: PropTypes.object,
  actions: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(CandidateActions, dispatch)
});

const mapStateToProps = (state) => ({
  candidates: state.candidates
});

export default connect(mapStateToProps, mapDispatchToProps)(Candidates);
