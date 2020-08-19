(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassRegistrationsController', MlcClassRegistrationsController);

    MlcClassRegistrationsController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registration', 'Print'];

    function MlcClassRegistrationsController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registration, Print) {
        var vm = this;

        vm.registrations = entity;
        vm.clear = clear;
        vm.print = print;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function print(className){
            var printContents = document.getElementById('print-section '+className).innerHTML;
            Print.print(className, printContents);
        }
    }
})();
