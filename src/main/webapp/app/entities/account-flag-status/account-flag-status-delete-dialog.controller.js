(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagStatusDeleteController',AccountFlagStatusDeleteController);

    AccountFlagStatusDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountFlagStatus'];

    function AccountFlagStatusDeleteController($uibModalInstance, entity, AccountFlagStatus) {
        var vm = this;

        vm.accountFlagStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountFlagStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
