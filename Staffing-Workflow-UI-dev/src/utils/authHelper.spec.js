import Authentication from './authHelper';

describe('AuthHelper', () => {

    describe('isAuthenticated', () => {

        it('should return false if no "thdsso" cookie', () => {
            Authentication.isAuthenticated();
        });

    });

    describe('getUserProfile', () => {
        let userProfile, state ;
        beforeEach(() => {
            state = {userProfile:{"userID":"QAT2368","name":"SMITH,STEPHANIE","storeNumber":"0132","storeName":"NORTH PHOENIX (H2H)"}};
            userProfile = {"userID":"QAT2368","name":"SMITH,STEPHANIE","storeNumber":"0132","storeName":"NORTH PHOENIX (H2H)"};
        });

        it('should set userProfile to passed in userProfile', () => {
          return expect(Authentication.getUserProfile(state)).resolves.toEqual(userProfile);
        });

        it('should return reject promise if no userProfile found', () => {
          state = undefined;
          return expect(Authentication.getUserProfile(state)).reject;
        });
    });
});
