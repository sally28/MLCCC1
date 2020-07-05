(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('InvoiceController', InvoiceController);

    InvoiceController.$inject = ['Invoice', 'Print'];

    function InvoiceController(Invoice, Print) {

        var vm = this;
        vm.noRecord = false;

        vm.invoices = [];

        vm.print = print;

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

        function print(title){
            var printContents = document.getElementById('print-section').innerHTML;
            Print.print(title, printContents);
        }
    }
})();
