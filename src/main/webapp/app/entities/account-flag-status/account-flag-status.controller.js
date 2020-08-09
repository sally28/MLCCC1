(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagStatusController', AccountFlagStatusController);

    AccountFlagStatusController.$inject = ['AccountFlagStatus'];

    function AccountFlagStatusController(AccountFlagStatus) {

        var vm = this;

        vm.accountFlagStatuses = [];

        loadAll();

        function loadAll() {
            AccountFlagStatus.query(function(result) {
                vm.accountFlagStatuses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
