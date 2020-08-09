(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('TeacherDialogController', TeacherDialogController);

    TeacherDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Teacher', 'User'];

    function TeacherDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Teacher, User) {
        var vm = this;

        vm.searchUser = searchUser;
        vm.teacher = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.accounts = []
        vm.accounts.push(vm.teacher.account);

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.teacher.id !== null) {
                Teacher.update(vm.teacher, onSaveSuccess, onSaveError);
            } else {
                Teacher.save(vm.teacher, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:teacherUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.hireDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function searchUser(){
            User.search({search:vm.searchTerm}, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.queryCount = vm.totalItems;
                vm.accounts = data;
                vm.teacher.account = data[0];
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }
})();
