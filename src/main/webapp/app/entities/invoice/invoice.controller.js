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
        vm.searchInvoice = searchInvoice;

        loadAll();

        function loadAll() {
            Invoice.query({param: vm.searchTerm}, function(result) {
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

        function searchInvoice(searchTerm){
            Invoice.query({param: vm.searchTerm}, function(result) {
                vm.invoices = result;
                if(vm.invoices.length == 0){
                    vm.noRecord = true;
                }
                vm.searchQuery = null;
            });
        }
    }
})();
