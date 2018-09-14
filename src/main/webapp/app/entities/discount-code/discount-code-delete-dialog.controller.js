(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountCodeDeleteController',DiscountCodeDeleteController);

    DiscountCodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'DiscountCode'];

    function DiscountCodeDeleteController($uibModalInstance, entity, DiscountCode) {
        var vm = this;

        vm.discountCode = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DiscountCode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
