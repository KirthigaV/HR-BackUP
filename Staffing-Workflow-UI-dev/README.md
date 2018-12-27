**staffing-workflow-0.1**  
**NOTE** - Please update ssoUrl and apiUrl in production configuration in config.json before building (yarn run build:production) to the corresponding PCF zones in order for Siteminder/SSO to work.  Also use generated prodza.manifest.yml.
===================  
Project Setup  
===================  

#### **Installation**  

Ensure `node`, `gulp` and `yarn` are installed globally prior to setup.  
- Install Yarn Package Manager here https://yarnpkg.com/en/docs/install.  

#### **Login Functionality**

If your application does not need the Login page and related functionality set the `useLogin` variable to `false` in the `src/reducers/initialState.js` file.

#### **Running Local Server**  

Run `yarn run start:local` for a local dev server.  
`http://localhost.homedepot.com:3001/` will automatically launch to access the app.  
The app will automatically reload if you change any of the source files.  

#### **Running unit tests**  

Run `yarn run test` to execute the unit tests for a single run via Jest & Enzyme
Run `yarn run test:watch` to execute the unit tests and watch for changes.  
Run `yarn run open:coverage` to view code coverage reports published at `/coverage/lcov-report/index.html`.   

#### **Building Release**  

In terminal navigate to project root directory and run following command:  

- Run one of the following to increment version number:
    - `gulp bump-major` to update major version (breaking changes)
    - `gulp bump-minor` to update minor version (non-breaking feature changes)
    - `gulp bump-patch` to update patch version (bux fixes)
- `yarn run build:develop` to build for development environment.  
- `yarn run build:production` to build for production environment.  

**NOTE** - `Environments` are based on environments set in `config/config.json` file.  

#### **Cloud Foundry Integration**  

**NOTE** - Windows users please ensure CF CLI has been updated to version 6.16.0 or greater. Visit https://docs.cloudfoundry.org/cf-cli/install-go-cli.html to download update.  

- Ensure you are logged in to the correct Cloud Foundry Org and Space in terminal.  
- Ensure correct environment manifest.yml has been generated from previous step.  
- Execute command in terminal `cf push -f {environment}.manifest.yml`.  

#### **Analytics Integration**  

- Contact Wesley Hall (wesley_d_hall@homedepot.com) to setup Google Tag Manager and Google Analytics IDs.  
- Replace `$GTMID` with Google Tag Manager ID in `src/index.ejs`.  
