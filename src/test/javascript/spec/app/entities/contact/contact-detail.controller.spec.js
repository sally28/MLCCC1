'use strict';

describe('Controller Tests', function() {

    describe('Contact Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockContact, MockPhoneType, MockSchoolDistrict;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockContact = jasmine.createSpy('MockContact');
            MockPhoneType = jasmine.createSpy('MockPhoneType');
            MockSchoolDistrict = jasmine.createSpy('MockSchoolDistrict');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Contact': MockContact,
                'PhoneType': MockPhoneType,
                'SchoolDistrict': MockSchoolDistrict
            };
            createController = function() {
                $injector.get('$controller')("ContactDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:contactUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
