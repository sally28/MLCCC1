(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('InvoiceController', InvoiceController);

    InvoiceController.$inject = ['Invoice'];

    function InvoiceController(Invoice) {

        var vm = this;

        vm.invoices = [];

        loadAll();

        function loadAll() {
            Invoice.query(function(result) {
                vm.invoices = result;
                vm.searchQuery = null;
            });
        }
    }
})();
