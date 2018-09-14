(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagStatusDialogController', AccountFlagStatusDialogController);

    AccountFlagStatusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountFlagStatus'];

    function AccountFlagStatusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccountFlagStatus) {
        var vm = this;

        vm.accountFlagStatus = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountFlagStatus.id !== null) {
                AccountFlagStatus.update(vm.accountFlagStatus, onSaveSuccess, onSaveError);
            } else {
                AccountFlagStatus.save(vm.accountFlagStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:accountFlagStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
