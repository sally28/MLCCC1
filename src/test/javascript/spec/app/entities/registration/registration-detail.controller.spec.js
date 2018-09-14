'use strict';

describe('Controller Tests', function() {

    describe('Registration Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRegistration, MockStudent, MockMlcClass, MockRegistrationStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRegistration = jasmine.createSpy('MockRegistration');
            MockStudent = jasmine.createSpy('MockStudent');
            MockMlcClass = jasmine.createSpy('MockMlcClass');
            MockRegistrationStatus = jasmine.createSpy('MockRegistrationStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Registration': MockRegistration,
                'Student': MockStudent,
                'MlcClass': MockMlcClass,
                'RegistrationStatus': MockRegistrationStatus
            };
            createController = function() {
                $injector.get('$controller')("RegistrationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:registrationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
