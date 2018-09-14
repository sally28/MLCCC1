(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('StudentDialogUserController', StudentDialogUserController);

    StudentDialogUserController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Student', 'User'];

    function StudentDialogUserController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Student, User) {
        var vm = this;

        vm.student = entity;
        vm.clear = clear;
        vm.save = save;
        vm.availableUsers = User.query();
        vm.deleteAssociatedAccount = deleteAssociatedAccount;
        vm.addAccountToStudent = addAccountToStudent;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.student.id !== null) {
                Student.update(vm.student, onSaveSuccess, onSaveError);
            } else {
                Student.save(vm.student, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:studentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function deleteAssociatedAccount (login){
            alert('delete association '+login);
        }

        function addAccountToStudent() {
            alert('addAccountToStudent ')
        }
    }
})();
