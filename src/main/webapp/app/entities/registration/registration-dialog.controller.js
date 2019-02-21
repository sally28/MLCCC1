(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationDialogController', RegistrationDialogController);

    RegistrationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Registration', 'Student', 'MlcClass', 'RegistrationStatus', 'MlcClassCategory'];

    function RegistrationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Registration, Student, MlcClass, RegistrationStatus, MlcClassCategory) {
        var vm = this;

        vm.registration = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        if($stateParams.studentId){
            vm.students = [Student.get({id : $stateParams.studentId})];
        } else {
            vm.students = Student.query();
        }

        if(vm.students.length >= 1){
            vm.registration.student = vm.students[0];
        }

        vm.mlcClassCategories = MlcClassCategory.query({}, onSuccess);
        function onSuccess(data) {
            if(vm.mlcClassCategory == null){
                vm.mlcClassCategory = data[0];
            }
            vm.mlcclasses = MlcClass.search({category: vm.mlcClassCategory.id});
        }

        //vm.mlcclasses = MlcClass.query();

        vm.registrationstatuses = RegistrationStatus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if(vm.registration.student == null){
                vm.registration.student = vm.students[0];
            }
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
