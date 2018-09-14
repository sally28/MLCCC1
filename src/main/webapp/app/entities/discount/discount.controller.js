(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountController', DiscountController);

    DiscountController.$inject = ['Discount'];

    function DiscountController(Discount) {

        var vm = this;

        vm.discounts = [];

        loadAll();

        function loadAll() {
            Discount.query(function(result) {
                vm.discounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
