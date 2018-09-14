(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PhoneTypeDeleteController',PhoneTypeDeleteController);

    PhoneTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PhoneType'];

    function PhoneTypeDeleteController($uibModalInstance, entity, PhoneType) {
        var vm = this;

        vm.phoneType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PhoneType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
