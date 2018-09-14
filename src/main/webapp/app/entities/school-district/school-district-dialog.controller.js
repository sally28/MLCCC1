(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolDistrictDialogController', SchoolDistrictDialogController);

    SchoolDistrictDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SchoolDistrict'];

    function SchoolDistrictDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SchoolDistrict) {
        var vm = this;

        vm.schoolDistrict = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.schoolDistrict.id !== null) {
                SchoolDistrict.update(vm.schoolDistrict, onSaveSuccess, onSaveError);
            } else {
                SchoolDistrict.save(vm.schoolDistrict, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:schoolDistrictUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
