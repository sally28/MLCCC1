'use strict';

describe('Controller Tests', function() {

    describe('AccountFlag Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAccountFlag, MockMlcAccount, MockAccountFlagStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAccountFlag = jasmine.createSpy('MockAccountFlag');
            MockMlcAccount = jasmine.createSpy('MockMlcAccount');
            MockAccountFlagStatus = jasmine.createSpy('MockAccountFlagStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AccountFlag': MockAccountFlag,
                'MlcAccount': MockMlcAccount,
                'AccountFlagStatus': MockAccountFlagStatus
            };
            createController = function() {
                $injector.get('$controller')("AccountFlagDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:accountFlagUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
