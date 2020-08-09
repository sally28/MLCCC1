(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountCodeController', DiscountCodeController);

    DiscountCodeController.$inject = ['DiscountCode'];

    function DiscountCodeController(DiscountCode) {

        var vm = this;

        vm.discountCodes = [];

        loadAll();

        function loadAll() {
            DiscountCode.query(function(result) {
                vm.discountCodes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
