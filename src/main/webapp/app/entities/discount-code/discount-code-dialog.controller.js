(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountCodeDialogController', DiscountCodeDialogController);

    DiscountCodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiscountCode'];

    function DiscountCodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DiscountCode) {
        var vm = this;

        vm.discountCode = entity;
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
            if (vm.discountCode.id !== null) {
                DiscountCode.update(vm.discountCode, onSaveSuccess, onSaveError);
            } else {
                DiscountCode.save(vm.discountCode, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:discountCodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
