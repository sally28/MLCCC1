(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('StudentDialogController', StudentDialogController);

    StudentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Student', 'User'];

    function StudentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Student, User) {
        var vm = this;

        vm.student = entity;
        vm.clear = clear;
        vm.save = save;
        vm.removeAssociatedAccount = removeAssociatedAccount;
        vm.showSearchField = showSearchField;
        vm.searchUser = searchUser;
        vm.addAccountToStudent = addAccountToStudent;
        vm.showSearch = false;
        vm.searchTerm = '';

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

        function showSearchField(id) {
            vm.showSearch = true;
        }

        function searchUser(){
            vm.availableUsers = User.search({search:vm.searchTerm});
        }

        function addAccountToStudent(user) {
            vm.student.associatedAccounts.push(angular.fromJson(angular.toJson(user)));
        }

        function removeAssociatedAccount (user){
            vm.student.associatedAccounts.splice(user);
        }
    }
})();
