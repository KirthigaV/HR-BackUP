import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {AppVersion, CONFIG} from 'globals';
import Cookies from "react-cookie";
import {toTitleCase} from '../../utils/nameHelper';
import * as ValidStoreActions from "../../actions/storeActions";
import {connect} from "react-redux";
import {bindActionCreators} from 'redux';
import * as LoginActions from "../../actions/loginActions";
import * as NeededActions from "../../actions/actionsNeededActions";
import * as ReqActions from "../../actions/requisitionActions";
import * as ReqDetailsActions from "../../actions/reqDetailsActions";
import * as CandidateActions from "../../actions/candidateActions";

class Header extends Component {

  constructor(props, context) {
    super(props, context);

    this.name = CONFIG.projectInfo.appName;
    this.version = AppVersion;
    this.state = {
      active: false,
      value: '',
      isloggedIn: true
    };
    this.openStoreDropdown = this.openStoreDropdown.bind(this);
    this.handleOutsideClick = this.handleOutsideClick.bind(this);
    this.changeStoreNumber = this.changeStoreNumber.bind(this);
    this.saveStoreDetails = this.saveStoreDetails.bind(this);
    this.logoutUser = this.logoutUser.bind(this);
    this.isStoreChanged = false;
    this.tagfirecount = 0;

    if (this.props.userProfile != null || this.props.userProfile != undefined) {
      this.storeSelected = this.props.userProfile.storeNumber;
      this.storeNumOrig = this.props.userProfile.storeNumOrig;
      this.setState ({
        loggedIn: true
      });
    }

  }

  openStoreDropdown() {
    const currentState = this.state.active;
    if (!currentState) {
      document.addEventListener('click', this.handleOutsideClick, false);
    } else {
      document.removeEventListener('click', this.handleOutsideClick, false);
    }

    this.setState({
      active: !currentState,
      value: this.storeSelected
    });

  }

  handleOutsideClick(event) {

    if (this.node.contains(event.target)) {
      return;
    } else {
      this.openStoreDropdown();
    }

  }

  changeStoreNumber(event) {
    if (event.target.validity.valid) {
      this.setState({value: event.target.value});
    }
  }

  saveStoreDetails(event) {

    event.preventDefault();

    let userInfo = JSON.parse(Cookies.load('userInfo'));
    let userDetails = JSON.parse(userInfo);
    if (userDetails.storeNumber == this.state.value) {
      //Add Validation for same store
    } else {
      this.props.storeActions.isValidStore(this.state.value)
        .then(
          response => {
            if (response) {
              if (response.isValidStore['isValid'] === 'true') {
                this.isStoreChanged = true;
                userDetails.storeNumber = this.state.value;
                this.storeSelected = userDetails.storeNumber;
                Cookies.remove('userInfo', { path: '/' });
                Cookies.save('userInfo', JSON.stringify(JSON.stringify(userDetails)), { path: '/' });
                this.props.loginActions.setUserProfile(userDetails);
                this.props.neededActions.resetActionsNeeded();
                this.props.reqActions.resetRequisitionsList();
                this.props.reqDetailsActions.resetRequisitionsInfo();
                this.props.candActions.resetCandidates();
                this.props.history.push('/home');
                this.setState({active: true});
                this.storeNumOrig = userDetails.storeNumOrig;
                this.tagfirecount=0;
                this.openStoreDropdown();
              } else {
                // Validation for Invalid Store
              }

            }
          }
        );
    }

  }

  storeDisplayed() {
    let selectedStore = this.props.userProfile.storeNumber;

    if (this.isStoreChanged) {
      selectedStore = this.storeSelected;
    }
    if (this.tagfirecount === 0) {
      window.dataLayer = window.dataLayer || [];
      window.dataLayer.push({
        event: 'storeLocUpdated',
        storeNumOrig: this.props.userProfile.storeNumOrig,
        storeName: ' - ' + this.props.userProfile.storeName,
        selectedStore: selectedStore,
        LDAP: this.props.userProfile.userID
      });
      this.tagfirecount++;
    }
    return (selectedStore);
  }

  logoutUser() {
    this.props.loginActions.logoutUser();
    //this.props.loginActions.logoutUserSM();
    this.props.history.push('/signin');
    //location.reload(true);
    this.setState({
      active: !this.state.active,
      isloggedIn: false
    });
    window.dataLayer.push({
      event: 'storeLocUpdated',
      storeNumOrig: undefined,
      selectedStore: undefined,
      storeName: undefined,
      LDAP: undefined
    });
    this.openStoreDropdown();
}

  render() {
    return (
      <div>
        {!this.props.empty && this.props.userProfile && this.state.isloggedIn &&
        <div className="header">
          <div className="header-logo">
            <i className="icon_homedepot"/>
          </div>
          <div className="header-info">
            <h2>Staff Up</h2>
          </div>

          <div className="header-action">

            <div className="header-dropdown p-div" onClick={this.openStoreDropdown}>
              <div className="c-div icon-div">
                <i className="icon_account"/>
              </div>

              <div className="c-div ">
                <div className="user-name"><label
                  className="util-section user-label">Hi, {toTitleCase(this.props.userProfile.name.substring(this.props.userProfile.name.indexOf(",") + 1))}</label><br/>
                </div>
                <br/>
                <div>
                  <label className="util-section store-number-label">Location# {this.storeDisplayed()}</label></div>
              </div>

            </div>

            <span className={this.state.active ? 'store-switcher-span' : 'hide-store-switcher-span'} ref={node => {
              this.node = node;
            }}>
              <form onSubmit={this.saveStoreDetails}>

                <div className="store-label">
                  Location#
                </div>
                <div>
                <input type="text" pattern="^\d{1,4}$" min="0" className="store-input" value={this.state.value || ''}
                       onChange={this.changeStoreNumber}/>
                <button type="submit" className="button primary sm store-submit">Change</button>
                </div>
              </form>

              <a id="logoutLink" onClick={this.logoutUser}>Logout</a>

            </span>

          </div>

        </div>
        }

      </div>
    );
  }
}

Header.propTypes = {
  logout: PropTypes.func,
  empty: PropTypes.bool.isRequired,
  storeActions: PropTypes.object.isRequired,
  loginActions: PropTypes.object,
  neededActions: PropTypes.object,
  reqActions: PropTypes.object,
  candActions: PropTypes.object,
  reqDetailsActions: PropTypes.object,
  history: PropTypes.object,
  userProfile: PropTypes.shape({
    name: PropTypes.string,
    storeNumber: PropTypes.string,
    storeName: PropTypes.string,
    storeNumOrig: PropTypes.string,
    userID: PropTypes.string
  }),
  login: PropTypes.object
};

const mapStateToProps = (state) => ({
  isValidStore: state.isValidStore,
  login: state.login
});

const mapDispatchToProps = dispatch => ({
  storeActions: bindActionCreators(ValidStoreActions, dispatch),
  loginActions: bindActionCreators(LoginActions, dispatch),
  neededActions: bindActionCreators(NeededActions, dispatch),
  reqActions: bindActionCreators(ReqActions, dispatch),
  candActions: bindActionCreators(CandidateActions, dispatch),
  reqDetailsActions: bindActionCreators(ReqDetailsActions, dispatch)
});
export default connect(mapStateToProps, mapDispatchToProps)(Header);
