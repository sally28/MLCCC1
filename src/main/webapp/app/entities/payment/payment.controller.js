(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PaymentController', PaymentController);

    PaymentController.$inject = ['Payment', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants'];

    function PaymentController(Payment, ParseLinks, AlertService, $state, pagingParams, paginationConstants) {

        var vm = this;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.payments = [];
        vm.searchPayment = searchPayment;

        if($state.params.invoiceId){
            getPaymentsByInvoiceId($state.params.invoiceId);
        } else {
            loadAll();
        }

        function loadAll() {
            Payment.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: 'id,desc'
            }, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.payments = data;
            vm.page = pagingParams.page;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function searchPayment(searchTerm){
            Payment.query({param: vm.searchTerm}, onSuccess);
        }

        function getPaymentsByInvoiceId(invoiceId){
            Payment.query({invoiceId: invoiceId}, onSuccess);
        }
    }
})();
