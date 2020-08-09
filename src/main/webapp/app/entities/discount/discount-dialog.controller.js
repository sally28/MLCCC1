(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountDialogController', DiscountDialogController);

    DiscountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Discount', 'DiscountCode', 'SchoolTerm'];

    function DiscountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Discount, DiscountCode, SchoolTerm) {
        var vm = this;

        vm.discount = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.discountcodes = DiscountCode.query();
        vm.schoolterms = SchoolTerm.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.discount.id !== null) {
                Discount.update(vm.discount, onSaveSuccess, onSaveError);
            } else {
                Discount.save(vm.discount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:discountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
