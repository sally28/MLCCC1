(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AppliedDiscountController', AppliedDiscountController);

    AppliedDiscountController.$inject = ['AppliedDiscount'];

    function AppliedDiscountController(AppliedDiscount) {

        var vm = this;

        vm.appliedDiscounts = [];

        loadAll();

        function loadAll() {
            AppliedDiscount.query(function(result) {
                vm.appliedDiscounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
