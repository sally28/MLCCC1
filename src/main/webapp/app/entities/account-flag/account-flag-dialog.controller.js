(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagDialogController', AccountFlagDialogController);

    AccountFlagDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountFlag', 'MlcAccount', 'AccountFlagStatus'];

    function AccountFlagDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccountFlag, MlcAccount, AccountFlagStatus) {
        var vm = this;

        vm.accountFlag = entity;
        vm.clear = clear;
        vm.save = save;
        vm.mlcaccounts = MlcAccount.query();
        vm.accountflagstatuses = AccountFlagStatus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountFlag.id !== null) {
                AccountFlag.update(vm.accountFlag, onSaveSuccess, onSaveError);
            } else {
                AccountFlag.save(vm.accountFlag, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:accountFlagUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
