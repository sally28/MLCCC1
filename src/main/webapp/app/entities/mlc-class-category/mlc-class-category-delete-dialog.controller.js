(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassCategoryDeleteController',MlcClassCategoryDeleteController);

    MlcClassCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'MlcClassCategory'];

    function MlcClassCategoryDeleteController($uibModalInstance, entity, MlcClassCategory) {
        var vm = this;

        vm.mlcClassCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MlcClassCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
