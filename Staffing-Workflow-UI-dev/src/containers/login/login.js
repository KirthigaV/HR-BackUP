import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import { RadioButtons } from 'ux-react-styleguide';
import * as LoginActions from "../../actions/loginActions";
import Header from '../../components/header/header';

let classNames = require('classnames');
class Login extends Component {

  constructor(props, context) {
    super(props, context);
    let urlParams = new URLSearchParams(window.location.search);
    let authorized = urlParams.get('auth');

    this.store = context.store;
    this.loginBtn = null;

    this.state = {
      location: 'store',
      storeNumber: '',
      username: '',
      password: '',
      passwordType: 'password',
      showError: false,
      showAuth: authorized
    };
    this.storeRadioButtons = [
      {
        label: 'Store Number',
        checked: true,
        value: 'store'
      },
      {
        label: 'Other Location',
        checked: false,
        value: 'other'
      }
    ];

    this.changeLocation = this.changeLocation.bind(this);
    this.changeStoreNumber = this.changeStoreNumber.bind(this);
    this.changeUsername = this.changeUsername.bind(this);
    this.changePassword = this.changePassword.bind(this);
    this.disableLogin = this.disableLogin.bind(this);
    this.showPassword = this.showPassword.bind(this);
    this.clearError = this.clearError.bind(this);
    this.clearAuth = this.clearAuth.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this);
    this.login = this.login.bind(this);

  }

  componentDidMount() {
    this.loginBtn = global.LoaderBtn.create(document.querySelector('#loginBtn'));
    this.unsubscribe = this.store.subscribe(() => {
      let loginState = this.store.getState().login;
      this.setState({ showError: loginState.error });
      this.loginBtn.stop();
      if (loginState.userProfile) {
        this.unsubscribe();
        this.props.history.push('/home');
      }
    });
  }

  componentWillUnmount() {
    this.unsubscribe();
  }

  changeLocation(location) {
    this.setState({ location: location });
  }

  changeStoreNumber(event) {
    this.setState({ storeNumber: event.target.value });
  }

  changeUsername(event) {
    this.setState({ username: event.target.value });
  }

  changePassword(event) {
    this.setState({ password: event.target.value });
  }

  disableLogin() {
    return (this.state.username === null || this.state.username === '') ||
      (this.state.password === null || this.state.password === '') ||
      (this.state.location === 'store' && (this.state.storeNumber === null || this.state.storeNumber === ''));
  }

  showPassword() {
    let type = this.state.passwordType;
    if (type === 'password') {
      this.setState({ passwordType: 'text' });
    } else {
      this.setState({ passwordType: 'password' });
    }
  }

  clearError() {
    this.setState({ showError: false });
  }

  clearAuth() {
    this.setState({ showAuth: false });
  }

  handleKeyPress(event) {
    if (event.key === 'Enter' && !this.disableLogin()) {
      this.login();
    }
  }

  login() {
    this.loginBtn.start('Logging in...');
    let storeNumber = this.state.storeNumber;
    if (this.state.location === 'other') {
      storeNumber = '9100';
    }
    this.props.actions.loginUser(storeNumber, this.state.username, btoa(this.state.password));
  }

  render() {

    const loginDisabled = this.disableLogin();
    const inputContainerClasses = classNames({
      'text-input-container': true,
      'error': this.state.showError
    });
    return (
      <div className="login">
       <Header empty />
        <section className="main-container">
          {this.state.showError &&
          <div className="banner error">
            <span className="message">Invalid Credentials: The information you provided did not match. Please try again.</span>
            <span className="close" onClick={this.clearError} />
          </div>
          }
          {this.state.showAuth &&
          <div className="banner error">
            <span className="message">Unauthorized. You have received this error because you do not currently have permission to access this system. Please see your manager if you believe you should have access.</span>
            <span className="close" onClick={this.clearAuth} />
          </div>
          }
          <div className="flex-row">
            <div id="loginCard" className="card login-card">
              <div className="card-toolbar">
                <span className="card-title">Provide your login credentials below.</span>
              </div>
              <div className="card-content">
                <RadioButtons groupId="storeLocation" title=""
                              radioButtons={this.storeRadioButtons} setValue={this.changeLocation} />
                {this.state.location === 'store' &&
                <div className={inputContainerClasses}>
                  <input type="text" id="storeNumber" name="storeNumber" title="Store Number" placeholder="#0000" maxLength="4"
                         value={this.state.storeNumber} onChange={this.changeStoreNumber} onKeyPress={this.handleKeyPress} />
                  <label htmlFor="storeNumber">Store Number</label>
                </div>
                }
                <div className={inputContainerClasses}>
                  <input type="text" id="userId" name="userId" title="User ID" placeholder="Enter Your User ID"
                         value={this.state.username} onChange={this.changeUsername} onKeyPress={this.handleKeyPress} />
                  <label htmlFor="userId">User ID</label>
                </div>
                <div className={inputContainerClasses}>
                  <input type={this.state.passwordType} id="password" name="password" title="Password" placeholder="Enter Your Password"
                         value={this.state.password} onChange={this.changePassword} onKeyPress={this.handleKeyPress} />
                  <label htmlFor="password">Password</label>
                </div>
              </div>
              <div className="card-actions">
                <button className="loader-btn primary" id="loginBtn" disabled={loginDisabled}
                        onClick={this.login} indeterminate="true">Login</button>
              </div>
            </div>
          </div>
        </section>
      </div >
    );
  }
}

Login.contextTypes = {
  store: PropTypes.object
};

Login.propTypes = {
  actions: PropTypes.object.isRequired,
  history: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(LoginActions, dispatch)
});


export default connect(null, mapDispatchToProps)(Login);

