(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationDialogController', RegistrationDialogController);

    RegistrationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registration', 'Student', 'MlcClass', 'RegistrationStatus'];

    function RegistrationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registration, Student, MlcClass, RegistrationStatus) {
        var vm = this;

        vm.registration = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.students = Student.query();
        vm.mlcclasses = MlcClass.query();
        vm.registrationstatuses = RegistrationStatus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.registration.id !== null) {
                Registration.update(vm.registration, onSaveSuccess, onSaveError);
            } else {
                Registration.save(vm.registration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:registrationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.modifyDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
