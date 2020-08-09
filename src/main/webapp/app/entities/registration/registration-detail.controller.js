(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationDetailController', RegistrationDetailController);

    RegistrationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Registration', 'Student', 'MlcClass', 'RegistrationStatus'];

    function RegistrationDetailController($scope, $rootScope, $stateParams, previousState, entity, Registration, Student, MlcClass, RegistrationStatus) {
        var vm = this;

        vm.registration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:registrationUpdate', function(event, result) {
            vm.registration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
