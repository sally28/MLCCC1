(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassTimeDeleteController',ClassTimeDeleteController);

    ClassTimeDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClassTime'];

    function ClassTimeDeleteController($uibModalInstance, entity, ClassTime) {
        var vm = this;

        vm.classTime = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClassTime.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
