import 'babel-polyfill';
import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {orange500, deepOrange500} from 'material-ui/styles/colors';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import Authentication from '../utils/authHelper';
import {Router} from 'react-router';
import Routes from '../router';
import Layout from './../containers/layout/Layout';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import * as LoginActions from '../actions/loginActions';
import {CONFIG} from "../../config/globals";

const muiTheme = getMuiTheme({
  palette: {
    primaryColor: orange500,
    accent2Color: deepOrange500
  }
});

class App extends Component {
  constructor(props, context) {
    super(props, context);

    window.onblur = function () {
      window.onfocus = function () {
        location.reload(true);
      };
    };
    this.store = context.store;
    const loginState = this.store.getState().login;
    let useLogin = loginState.useLogin;

    if (loginState.userProfile === null) {
      this.userProfile = Authentication.getUserProfileSM(loginState);
      this.props.actions.setUserProfile(this.userProfile);
    } else {
      this.userProfile = useLogin.userProfile;
    }

    if (useLogin === true) {
      Authentication.isAuthenticatedSM()
        .then((authenticated) => {
          if (authenticated && this.userProfile !== null) {
            //this.props.history.push('/home');
          } else {
            if (CONFIG.authUrl === '') {
              window.location = '/ssoLogin';
            }
            else {
              window.location.assign(CONFIG.authUrl + '/ssoLogin');
            }
          }
        });
    } else {
      this.props.history.push('/home');
    }
  }

  render() {
    return (
      <MuiThemeProvider muiTheme={muiTheme}>
        <Layout userProfile={this.userProfile} history={this.props.history} routes={Routes}>
          <Router history={this.props.history} routes={Routes}/>
        </Layout>
      </MuiThemeProvider>
    );
  }
}

App.contextTypes = {
  store: PropTypes.object
};

App.propTypes = {
  history: PropTypes.object,
  actions: PropTypes.object,
  login: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(LoginActions, dispatch)
});

const mapStateToProps = (state) => {
  return {login: state.login};
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
