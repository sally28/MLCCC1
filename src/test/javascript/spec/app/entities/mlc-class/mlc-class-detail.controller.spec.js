'use strict';

describe('Controller Tests', function() {

    describe('MlcClass Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMlcClass, MockClassStatus, MockClassTime, MockTeacher, MockClassRoom, MockSchoolTerm;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMlcClass = jasmine.createSpy('MockMlcClass');
            MockClassStatus = jasmine.createSpy('MockClassStatus');
            MockClassTime = jasmine.createSpy('MockClassTime');
            MockTeacher = jasmine.createSpy('MockTeacher');
            MockClassRoom = jasmine.createSpy('MockClassRoom');
            MockSchoolTerm = jasmine.createSpy('MockSchoolTerm');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MlcClass': MockMlcClass,
                'ClassStatus': MockClassStatus,
                'ClassTime': MockClassTime,
                'Teacher': MockTeacher,
                'ClassRoom': MockClassRoom,
                'SchoolTerm': MockSchoolTerm
            };
            createController = function() {
                $injector.get('$controller')("MlcClassDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mlcccApp:mlcClassUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
