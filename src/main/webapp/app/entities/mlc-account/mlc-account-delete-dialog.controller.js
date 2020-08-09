(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcAccountDeleteController',MlcAccountDeleteController);

    MlcAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'MlcAccount'];

    function MlcAccountDeleteController($uibModalInstance, entity, MlcAccount) {
        var vm = this;

        vm.mlcAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MlcAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
