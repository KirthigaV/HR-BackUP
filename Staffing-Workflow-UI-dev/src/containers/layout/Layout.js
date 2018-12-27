import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import PropTypes from 'prop-types';
import Header from './../../components/header/header';
import * as LoginActions from './../../actions/loginActions';
import Routes from "../../router";

class Layout extends React.Component{

  constructor(props){
    super(props);
  }

  render(){
    return(
      <div  className="home">
        <Header empty={this.props.userProfile === null} userProfile={this.props.userProfile}
                logout={this.props.actions.logoutUser} history={this.props.history} routes={Routes}/>
        <div className="home-layout">
          <div className="home-content">
            {this.props.children}
          </div>

        </div>
      </div>
    );
  }
}

Layout.propTypes = {
  userProfile: PropTypes.shape(),
  history:PropTypes.object,
  children: PropTypes.node,
  actions: PropTypes.object
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(LoginActions, dispatch)
});

export default connect(null, mapDispatchToProps)(Layout);


