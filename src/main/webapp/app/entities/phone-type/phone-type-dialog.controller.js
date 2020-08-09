(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PhoneTypeDialogController', PhoneTypeDialogController);

    PhoneTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PhoneType'];

    function PhoneTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PhoneType) {
        var vm = this;

        vm.phoneType = entity;
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
            if (vm.phoneType.id !== null) {
                PhoneType.update(vm.phoneType, onSaveSuccess, onSaveError);
            } else {
                PhoneType.save(vm.phoneType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:phoneTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
