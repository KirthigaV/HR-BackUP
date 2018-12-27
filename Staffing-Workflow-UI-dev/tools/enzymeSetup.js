import { configure } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import mockGlobals from '../src/utils/mockGlobals';

global.fetch = require('jest-fetch-mock');

configure({ adapter: new Adapter() });
mockGlobals();
