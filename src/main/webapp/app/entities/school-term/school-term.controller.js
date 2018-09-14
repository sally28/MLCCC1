(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolTermController', SchoolTermController);

    SchoolTermController.$inject = ['SchoolTerm'];

    function SchoolTermController(SchoolTerm) {

        var vm = this;

        vm.schoolTerms = [];

        loadAll();

        function loadAll() {
            SchoolTerm.query(function(result) {
                vm.schoolTerms = result;
                vm.searchQuery = null;
            });
        }
    }
})();
