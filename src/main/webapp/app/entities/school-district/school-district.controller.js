(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolDistrictController', SchoolDistrictController);

    SchoolDistrictController.$inject = ['SchoolDistrict'];

    function SchoolDistrictController(SchoolDistrict) {

        var vm = this;

        vm.schoolDistricts = [];

        loadAll();

        function loadAll() {
            SchoolDistrict.query(function(result) {
                vm.schoolDistricts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
