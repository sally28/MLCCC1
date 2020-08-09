(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassDeleteController',MlcClassDeleteController);

    MlcClassDeleteController.$inject = ['$uibModalInstance', 'entity', 'MlcClass'];

    function MlcClassDeleteController($uibModalInstance, entity, MlcClass) {
        var vm = this;

        vm.mlcClass = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MlcClass.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
