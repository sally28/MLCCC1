(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationStatusDetailController', RegistrationStatusDetailController);

    RegistrationStatusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RegistrationStatus'];

    function RegistrationStatusDetailController($scope, $rootScope, $stateParams, previousState, entity, RegistrationStatus) {
        var vm = this;

        vm.registrationStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:registrationStatusUpdate', function(event, result) {
            vm.registrationStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
