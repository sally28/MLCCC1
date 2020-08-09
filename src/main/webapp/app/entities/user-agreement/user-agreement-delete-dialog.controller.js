(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('UserAgreementDeleteController',UserAgreementDeleteController);

    UserAgreementDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserAgreement'];

    function UserAgreementDeleteController($uibModalInstance, entity, UserAgreement) {
        var vm = this;

        vm.userAgreement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserAgreement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
