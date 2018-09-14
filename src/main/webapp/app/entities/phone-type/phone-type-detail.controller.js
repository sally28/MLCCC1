(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PhoneTypeDetailController', PhoneTypeDetailController);

    PhoneTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PhoneType'];

    function PhoneTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, PhoneType) {
        var vm = this;

        vm.phoneType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:phoneTypeUpdate', function(event, result) {
            vm.phoneType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
