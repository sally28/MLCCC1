(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('RegistrationStatusDialogController', RegistrationStatusDialogController);

    RegistrationStatusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RegistrationStatus'];

    function RegistrationStatusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RegistrationStatus) {
        var vm = this;

        vm.registrationStatus = entity;
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
            if (vm.registrationStatus.id !== null) {
                RegistrationStatus.update(vm.registrationStatus, onSaveSuccess, onSaveError);
            } else {
                RegistrationStatus.save(vm.registrationStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:registrationStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
