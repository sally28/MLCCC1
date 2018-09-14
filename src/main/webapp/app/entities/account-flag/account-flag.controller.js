(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagController', AccountFlagController);

    AccountFlagController.$inject = ['AccountFlag'];

    function AccountFlagController(AccountFlag) {

        var vm = this;

        vm.accountFlags = [];

        loadAll();

        function loadAll() {
            AccountFlag.query(function(result) {
                vm.accountFlags = result;
                vm.searchQuery = null;
            });
        }
    }
})();
