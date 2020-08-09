(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountDeleteController',DiscountDeleteController);

    DiscountDeleteController.$inject = ['$uibModalInstance', 'entity', 'Discount'];

    function DiscountDeleteController($uibModalInstance, entity, Discount) {
        var vm = this;

        vm.discount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Discount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
