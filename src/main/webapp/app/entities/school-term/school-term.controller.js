(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolTermController', SchoolTermController);

    SchoolTermController.$inject = ['SchoolTerm'];

    function SchoolTermController(SchoolTerm) {

        var vm = this;
        vm.finish = finishSchoolTerm;
        vm.schoolTerms = [];

        loadAll();

        function loadAll() {
            SchoolTerm.query(function(result) {
                vm.schoolTerms = result;
                vm.searchQuery = null;
            });
        }

        function finishSchoolTerm(data) {
            data.status = 'FINISHED';
            SchoolTerm.update(data, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:schooolTermUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
