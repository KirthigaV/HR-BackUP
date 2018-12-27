import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Link} from 'react-router';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import * as ActionsNeededActions from "../../actions/actionsNeededActions";
import * as CalendarActions from "../../actions/calendarActions";
import {showLoadingBar} from "../../utils/loaderHelper";


class Home extends Component {

  constructor(props) {
    super(props);
    let calData = [{}];

    this.state = {
      newReleases: "",
      nextReleases: "",
      calActionCount: 0,
      calData: calData,
      loadCalTile: true
    };

    if (this.props.login.userProfile != null) {
      this.selectedStore = this.props.login.userProfile.storeNumber;
    }

/*    Authentication.getReleaseNotes().then(res => {
      if (res) {
        this.setState({
          newReleases: res.newRelease.split("|"),
          nextReleases: res.nextRelease.split("|")
        });
      }
    });*/

    this.getCalendarActionCount = this.getCalendarActionCount.bind(this);

  }

  componentDidMount() {
    if (this.props.login.userProfile != null) {
      this.props.actions.getStoreActionsNeeded(this.selectedStore);
      this.getCalendarActionCount(this.selectedStore);
    }
  }

  componentDidUpdate() {
    if (this.props.login.userProfile != null && this.selectedStore !== this.props.login.userProfile.storeNumber) {
      this.selectedStore = this.props.login.userProfile.storeNumber;
      this.props.actions.getStoreActionsNeeded(this.selectedStore);
      this.getCalendarActionCount(this.selectedStore);
    }
  }

  getCalendarActionCount() {
    let calCount = 0;
    this.props.calendarActions.getAllCalendarDetails(this.selectedStore).then(res => {
      if (res) {
        this.setState({
          calData: res.calendarDetails
        });
        for (let i = 0; i < res.calendarDetails.length; i++) {
          if (this.state.calData[i].reqCount-this.state.calData[i].slotsAvailable-this.state.calData[i].slotsScheduled > 0) {
            calCount++;
          }
        }
        this.setState({
          calActionCount: calCount,
          loadCalTile: false
        });
      }
    });
  }

  populateReleases(releases) {
    return (
      releases.map((val, i) => (
        <li key={i} className="release-list"><h5>{val}</h5></li>
      ))
    );
  }

  render() {
    let load = true;

    if (this.props.actionsNeeded.actionsNeeded !== null) {
      load = false;
    }
    return (

      <div>
        <div className="parent-div">
          {this.props.login.userProfile !== null &&
          <div className="child-div home-start">
            <Link to={"requisitions"}>
              {showLoadingBar(load)}
              <div className="card card-center">
                <div className="card-toolbar card-toolbar-display">
              <span className="card-title card-title-span">
                <i className="icon_person-add" id="icon-person-add"/>
                <h3>Manage Candidates</h3>
              </span>
                </div>
                <div className="card-content card-text">
                  <h5>Manage candidates for all <br/>requisitions at your location. <br/></h5>
                </div>
                {(this.props.actionsNeeded.actionsNeeded === null
                  || this.props.actionsNeeded.actionsNeeded.actionsNeeded === 0
                  || this.props.actionsNeeded.actionsNeeded.actionsNeeded === undefined) ? "" :
                  <div className="card-actions center">
                    <button className="button primary md">{this.props.actionsNeeded.actionsNeeded.actionsNeeded + " "}
                      Candidate{(this.props.actionsNeeded.actionsNeeded.actionsNeeded === 1) ? " Needs Attention" : "s Need Attention"}
                    </button>
                  </div>}
              </div>
            </Link>

            {/* TODO: Release Notes
          <div className="release-div">
            <div className="release-label">
            <b>What's New ?</b>
            </div><br/>
            <ul>
              {this.state.newReleases && this.populateReleases(this.state.newReleases)}
            </ul>
          </div>
          */}

          </div>
          }

          {this.props.login.userProfile !== null &&
          <div className="child-div home-start">
            <Link to={"intCalendarDetails"}>
              {showLoadingBar(this.state.loadCalTile)}
              <div className="card card-center">
                <div className="card-toolbar card-toolbar-display">
              <span className="card-title card-title-span">
                <i className="icon_calendar-date-range" id="icon-calendar"/>
                <h3>Manage Interview Availability</h3>
              </span>
                </div>
                <div className="card-content card-text">
                  <h5>Ensure all interview availability calendars are up<br/> to date so that candidates have
                    accurate<br/>interview times from which to choose.</h5>
                </div>
                {(this.state.calActionCount == 0) ? "" :
                  <div className="card-actions center">
                    <button className="button primary md">{this.state.calActionCount + " "}
                      Calendar{(this.state.calActionCount === 1) ? " Needs Attention" : "s Need Attention"}
                    </button>
                  </div>}
              </div>
            </Link>

            {/* TODO: Release Notes

             <div className="release-div">
            <div className="release-label"><b>What's Next ?</b></div><br/>
            <ul>
              {this.state.nextReleases && this.populateReleases(this.state.nextReleases)}
            </ul>
          </div>*/}
          </div>
          }

        </div>

        {/* TODO: Release Notes
        {this.props.login.userProfile !== null &&
        <div className="common-div">
          <p><i>Thanks for being a part of the Staffing Workflow pilot. We are working to build enhanced functionality
            based on your <br/>
            feedback. You can let us know what you think with the "Help us improve" button to the right. Thanks for your
            partnership.
          </i></p>
          <p><i>-The Staffing Team </i></p>
        </div>
        }*/}
      </div>
    );
  }
}

Home.contextTypes = {
  store: PropTypes.object
};

Home.propTypes = {
  actions: PropTypes.object.isRequired,
  calendarActions: PropTypes.object,
  history: PropTypes.object,
  actionsNeeded: PropTypes.object,
  login: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(ActionsNeededActions, dispatch),
  calendarActions: bindActionCreators(CalendarActions, dispatch)
});

const mapStateToProps = (state) => ({
  actionsNeeded: state.actionsNeeded,
  login: state.login
});

export default connect(mapStateToProps, mapDispatchToProps)(Home);
