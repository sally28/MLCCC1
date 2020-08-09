(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('UserManagementResetController', UserManagementResetController);

    UserManagementResetController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User'];

    function UserManagementResetController ($stateParams, $uibModalInstance, entity, User) {
        var vm = this;

        vm.changePassword = changePassword;
        vm.clear = clear;
        vm.doNotMatch = null;
        vm.error = null;
        vm.success = null;
        vm.user = entity;

        function changePassword () {
            if (vm.password !== vm.confirmPassword) {
                vm.error = null;
                vm.success = null;
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;
                vm.user.password = vm.password;
                User.update(vm.user, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
