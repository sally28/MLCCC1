(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolTermDialogController', SchoolTermDialogController);

    SchoolTermDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SchoolTerm'];

    function SchoolTermDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SchoolTerm) {
        var vm = this;

        vm.schoolTerm = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        vm.statusList = ["ACTIVE", "PENDING", "FINISHED"];

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.schoolTerm.id !== null) {
                SchoolTerm.update(vm.schoolTerm, onSaveSuccess, onSaveError);
            } else {
                SchoolTerm.save(vm.schoolTerm, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:schoolTermUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.promDate = false;
        vm.datePickerOpenStatus.earlyBirdDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
