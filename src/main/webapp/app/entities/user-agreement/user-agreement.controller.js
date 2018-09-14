(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('UserAgreementController', UserAgreementController);

    UserAgreementController.$inject = ['UserAgreement'];

    function UserAgreementController(UserAgreement) {

        var vm = this;

        vm.userAgreements = [];

        loadAll();

        function loadAll() {
            UserAgreement.query(function(result) {
                vm.userAgreements = result;
                vm.searchQuery = null;
            });
        }
    }
})();
