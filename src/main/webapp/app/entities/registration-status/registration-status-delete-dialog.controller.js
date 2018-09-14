(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationStatusDeleteController',RegistrationStatusDeleteController);

    RegistrationStatusDeleteController.$inject = ['$uibModalInstance', 'entity', 'RegistrationStatus'];

    function RegistrationStatusDeleteController($uibModalInstance, entity, RegistrationStatus) {
        var vm = this;

        vm.registrationStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RegistrationStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
