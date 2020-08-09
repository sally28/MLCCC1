(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagDeleteController',AccountFlagDeleteController);

    AccountFlagDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountFlag'];

    function AccountFlagDeleteController($uibModalInstance, entity, AccountFlag) {
        var vm = this;

        vm.accountFlag = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountFlag.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
