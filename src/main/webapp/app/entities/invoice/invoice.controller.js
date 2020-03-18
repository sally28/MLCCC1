(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('InvoiceController', InvoiceController);

    InvoiceController.$inject = ['Invoice'];

    function InvoiceController(Invoice) {

        var vm = this;
        vm.noRecord = false;

        vm.invoices = [];

        loadAll();

        function loadAll() {
            Invoice.query(function(result) {
                vm.invoices = result;
                if(vm.invoices.length == 0){
                    vm.noRecord = true;
                }
                vm.searchQuery = null;
            });
        }
    }
})();
