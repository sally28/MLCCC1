(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PhoneTypeController', PhoneTypeController);

    PhoneTypeController.$inject = ['PhoneType'];

    function PhoneTypeController(PhoneType) {

        var vm = this;

        vm.phoneTypes = [];

        loadAll();

        function loadAll() {
            PhoneType.query(function(result) {
                vm.phoneTypes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
