(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationWithdrawController',RegistrationWithdrawController);

    RegistrationWithdrawController.$inject = ['$state', '$uibModalInstance', 'entity', 'Registration'];

    function RegistrationWithdrawController($state, $uibModalInstance, entity, Registration) {
        var vm = this;

        vm.registration = entity;
        vm.clear = clear;
        vm.confirmWithdraw = confirmWithdraw;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmWithdraw (registration) {
            registration.status = 'WITHDRAWAL';
            Registration.update(registration,function(){
                $uibModalInstance.close(true);
            });
        }
    }
})();
