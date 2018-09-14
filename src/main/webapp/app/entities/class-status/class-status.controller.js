(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassStatusController', ClassStatusController);

    ClassStatusController.$inject = ['ClassStatus'];

    function ClassStatusController(ClassStatus) {

        var vm = this;

        vm.classStatuses = [];

        loadAll();

        function loadAll() {
            ClassStatus.query(function(result) {
                vm.classStatuses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
