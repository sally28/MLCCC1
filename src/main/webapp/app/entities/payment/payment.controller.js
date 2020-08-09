(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PaymentController', PaymentController);

    PaymentController.$inject = ['Payment'];

    function PaymentController(Payment) {

        var vm = this;

        vm.payments = [];

        loadAll();

        function loadAll() {
            Payment.query(function(result) {
                vm.payments = result;
                vm.searchQuery = null;
            });
        }
    }
})();
