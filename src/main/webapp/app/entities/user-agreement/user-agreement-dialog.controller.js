(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('UserAgreementDialogController', UserAgreementDialogController);

    UserAgreementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserAgreement'];

    function UserAgreementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserAgreement) {
        var vm = this;

        vm.userAgreement = entity;
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
            if (vm.userAgreement.id !== null) {
                UserAgreement.update(vm.userAgreement, onSaveSuccess, onSaveError);
            } else {
                UserAgreement.save(vm.userAgreement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:userAgreementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
