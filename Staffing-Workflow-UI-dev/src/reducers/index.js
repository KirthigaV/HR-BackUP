import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';

import login from './loginReducer';
import requisition from './requisitionReducer';
import requisitionDetails from './reqDetailsReducer';
import candidates from './candidateReducer';
import orientation from './orientationReducer';
import actionsNeeded from './actionsNeededReducer';

const rootReducer = combineReducers({
    login,
    requisition,
    requisitionDetails,
    candidates,
    orientation,
    actionsNeeded,
    routing: routerReducer
});

export default rootReducer;
