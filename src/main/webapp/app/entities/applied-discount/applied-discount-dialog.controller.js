(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AppliedDiscountDialogController', AppliedDiscountDialogController);

    AppliedDiscountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AppliedDiscount', 'Discount', 'Registration', 'Invoice'];

    function AppliedDiscountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AppliedDiscount, Discount, Registration, Invoice) {
        var vm = this;

        vm.appliedDiscount = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.discounts = Discount.query();
        vm.registrations = Registration.query();
        vm.invoices = Invoice.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.appliedDiscount.id !== null) {
                AppliedDiscount.update(vm.appliedDiscount, onSaveSuccess, onSaveError);
            } else {
                AppliedDiscount.save(vm.appliedDiscount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:appliedDiscountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
