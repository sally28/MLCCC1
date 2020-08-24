(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('InvoiceController', InvoiceController);

    InvoiceController.$inject = ['Invoice', 'Print', '$state', 'pagingParams', 'paginationConstants', 'ParseLinks'];

    function InvoiceController(Invoice, Print, $state, pagingParams, paginationConstants, ParseLinks) {

        var vm = this;
        vm.noRecord = false;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.invoices = [];

        vm.print = print;
        vm.searchInvoice = searchInvoice;

        loadAll();

        function loadAll() {
            Invoice.query(
                {
                    param: vm.searchTerm,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: 'id,desc'
                }, onSuccess);
        }

        function print(title){
            var printContents = document.getElementById('print-section').innerHTML;
            Print.print(title, printContents);
        }

        function searchInvoice(searchTerm){
            Invoice.query({param: vm.searchTerm}, onSuccess);
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function onSuccess(result, headers){
                vm.invoices = result;
                if(vm.invoices.length == 0){
                    vm.noRecord = true;
                }
                vm.searchQuery = null;
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
        }
    }
})();
