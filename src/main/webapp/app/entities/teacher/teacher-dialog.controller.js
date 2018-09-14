(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('TeacherDialogController', TeacherDialogController);

    TeacherDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Teacher', 'MlcAccount'];

    function TeacherDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Teacher, MlcAccount) {
        var vm = this;

        vm.teacher = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.accounts = MlcAccount.query({filter: 'teacher-is-null'});
        $q.all([vm.teacher.$promise, vm.accounts.$promise]).then(function() {
            if (!vm.teacher.account || !vm.teacher.account.id) {
                return $q.reject();
            }
            return MlcAccount.get({id : vm.teacher.account.id}).$promise;
        }).then(function(account) {
            vm.accounts.push(account);
        });

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
    }
})();
