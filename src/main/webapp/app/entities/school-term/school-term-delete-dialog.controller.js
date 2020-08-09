(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolTermDeleteController',SchoolTermDeleteController);

    SchoolTermDeleteController.$inject = ['$uibModalInstance', 'entity', 'SchoolTerm'];

    function SchoolTermDeleteController($uibModalInstance, entity, SchoolTerm) {
        var vm = this;

        vm.schoolTerm = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SchoolTerm.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
