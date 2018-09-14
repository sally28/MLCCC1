(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationStatusController', RegistrationStatusController);

    RegistrationStatusController.$inject = ['RegistrationStatus'];

    function RegistrationStatusController(RegistrationStatus) {

        var vm = this;

        vm.registrationStatuses = [];

        loadAll();

        function loadAll() {
            RegistrationStatus.query(function(result) {
                vm.registrationStatuses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
