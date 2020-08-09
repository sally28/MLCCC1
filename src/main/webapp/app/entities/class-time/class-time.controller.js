(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassTimeController', ClassTimeController);

    ClassTimeController.$inject = ['ClassTime'];

    function ClassTimeController(ClassTime) {

        var vm = this;

        vm.classTimes = [];

        loadAll();

        function loadAll() {
            ClassTime.query(function(result) {
                vm.classTimes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
