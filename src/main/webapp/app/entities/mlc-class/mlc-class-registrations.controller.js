(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassRegistrationsController', MlcClassRegistrationsController);

    MlcClassRegistrationsController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registration'];

    function MlcClassRegistrationsController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registration) {
        var vm = this;

        vm.registrations = entity;
        vm.clear = clear;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
