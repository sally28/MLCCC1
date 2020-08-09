'use strict';

describe('Controller Tests', function() {

    describe('AccountAgreement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAccountAgreement, MockMlcAccount, MockUserAgreement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAccountAgreement = jasmine.createSpy('MockAccountAgreement');
            MockMlcAccount = jasmine.createSpy('MockMlcAccount');
            MockUserAgreement = jasmine.createSpy('MockUserAgreement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AccountAgreement': MockAccountAgreement,
                'MlcAccount': MockMlcAccount,
                'UserAgreement': MockUserAgreement
            };
            createController = function() {
                $injector.get('$controller')("AccountAgreementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:accountAgreementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
