'use strict';

describe('Controller Tests', function() {

    describe('AppliedDiscount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAppliedDiscount, MockDiscount, MockRegistration, MockInvoice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAppliedDiscount = jasmine.createSpy('MockAppliedDiscount');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockRegistration = jasmine.createSpy('MockRegistration');
            MockInvoice = jasmine.createSpy('MockInvoice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AppliedDiscount': MockAppliedDiscount,
                'Discount': MockDiscount,
                'Registration': MockRegistration,
                'Invoice': MockInvoice
            };
            createController = function() {
                $injector.get('$controller')("AppliedDiscountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:appliedDiscountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
