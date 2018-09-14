(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountAgreementDeleteController',AccountAgreementDeleteController);

    AccountAgreementDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountAgreement'];

    function AccountAgreementDeleteController($uibModalInstance, entity, AccountAgreement) {
        var vm = this;

        vm.accountAgreement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountAgreement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
