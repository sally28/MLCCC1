(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountAgreementController', AccountAgreementController);

    AccountAgreementController.$inject = ['AccountAgreement'];

    function AccountAgreementController(AccountAgreement) {

        var vm = this;

        vm.accountAgreements = [];

        loadAll();

        function loadAll() {
            AccountAgreement.query(function(result) {
                vm.accountAgreements = result;
                vm.searchQuery = null;
            });
        }
    }
})();
