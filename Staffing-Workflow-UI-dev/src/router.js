import React from 'react';
import {Route, IndexRoute} from 'react-router';
import App from "./components/App";
import Home from "./containers/home/home";
import RequisitionList from "./containers/requisitionList/requisitionList";
import RequisitionDetails from "./containers/requisitionDetails/requisitionDetails";
import IntCalendarDtls from "./containers/IntCalendarDtls/IntCalendarDtls";
import Login from "./containers/login/login";

export default (

  <Route path="/" component={App}>
    <IndexRoute component={Home} />
    <Route path="/home" component={Home} />
    <Route path="/login" component={Home} />
    <Route path="/logout" component={Login} />
    <Route path="/signin" component={Login} />
    <Route path="/requisitions" component={RequisitionList} />
    <Route path="/requisitions/:reqId/:reqType" component={RequisitionDetails} />
    <Route path="/intCalendarDetails" component={IntCalendarDtls} />
  </Route>

);
