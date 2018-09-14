(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AppliedDiscountDeleteController',AppliedDiscountDeleteController);

    AppliedDiscountDeleteController.$inject = ['$uibModalInstance', 'entity', 'AppliedDiscount'];

    function AppliedDiscountDeleteController($uibModalInstance, entity, AppliedDiscount) {
        var vm = this;

        vm.appliedDiscount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AppliedDiscount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
