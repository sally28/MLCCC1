'use strict';

describe('Controller Tests', function() {

    describe('Discount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDiscount, MockDiscountCode, MockSchoolTerm;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockDiscountCode = jasmine.createSpy('MockDiscountCode');
            MockSchoolTerm = jasmine.createSpy('MockSchoolTerm');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Discount': MockDiscount,
                'DiscountCode': MockDiscountCode,
                'SchoolTerm': MockSchoolTerm
            };
            createController = function() {
                $injector.get('$controller')("DiscountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:discountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
