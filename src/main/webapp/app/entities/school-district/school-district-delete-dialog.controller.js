(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolDistrictDeleteController',SchoolDistrictDeleteController);

    SchoolDistrictDeleteController.$inject = ['$uibModalInstance', 'entity', 'SchoolDistrict'];

    function SchoolDistrictDeleteController($uibModalInstance, entity, SchoolDistrict) {
        var vm = this;

        vm.schoolDistrict = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SchoolDistrict.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
