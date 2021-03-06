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
        vm.searchStudent = searchStudent;
        vm.searchClasses = searchClasses;
        vm.checked = false;
        vm.select = select;

        if($stateParams.studentId){
            vm.students = [Student.get({id : $stateParams.studentId})];
            vm.registration.student = vm.students[0];
        } else {
            vm.students = Student.query({}, function(data){
                vm.registration.student = data[0];
            });
        }
        if($stateParams.classId){
            MlcClass.get({id : $stateParams.classId}, onGetClass);
        } else {
        vm.mlcClassCategories = MlcClassCategory.query({}, onSuccess);
        }

        function onGetClass(data){
            vm.mlcclasses = [];
            vm.mlcClassCategories = [];
            vm.mlcclasses.push(data);
            vm.registration.mlcClass = vm.mlcclasses[0];
            vm.mlcClassCategories.push(vm.registration.mlcClass.mlcClassCategory);
            vm.mlcClassCategory = vm.mlcClassCategories[0];
        }

        function onSuccess(data) {
            if(vm.mlcClassCategory == null){
                vm.mlcClassCategory = data[0];
            }
            vm.mlcclasses = MlcClass.query({category: vm.mlcClassCategory.id, newRegistration: true, size: 50, sort: 'className'}, function(data){
                vm.registration.mlcClass = data[0];
            });
        }

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

        function searchClasses (){
            vm.mlcclasses = MlcClass.search({category: vm.mlcClassCategory.id, newRegistration: true, size:50, sort: 'className'}, function(data){
                vm.registration.mlcClass = data[0];
            });
        }

        function searchStudent (){
            vm.students = Student.query({param: vm.searchTerm,
                page: 0,
                size: 50,
                sort: 'firstName'
            }, function(data){
                vm.students = data;
                vm.registration.student = data[0];
            });
        }

        function select(){
            vm.checked = !vm.checked;
        }

    }
})();
