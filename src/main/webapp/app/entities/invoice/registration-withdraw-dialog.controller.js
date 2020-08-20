(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationWithdrawController',RegistrationWithdrawController);

    RegistrationWithdrawController.$inject = ['$state', 'previousState', '$uibModalInstance', 'entity', 'Registration'];

    function RegistrationWithdrawController($state, previousState, $uibModalInstance, entity, Registration) {
        var vm = this;

        vm.registration = entity;
        vm.clear = clear;
        vm.confirmWithdraw = confirmWithdraw;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmWithdraw (registration) {
            registration.status = 'WITHDREW_NEED_REFUND';
            Registration.update(registration,function(){
                $uibModalInstance.close(true);
            });
        }
    }
})();
