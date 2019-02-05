(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('InvoiceDialogController', InvoiceDialogController);

    InvoiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Invoice', 'User', 'Registration', 'Payment', 'AppliedDiscount'];

    function InvoiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Invoice, User, Registration, Payment, AppliedDiscount) {
        var vm = this;

        vm.invoice = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.registrations = Registration.query();
        vm.payments = Payment.query();
        vm.applieddiscounts = AppliedDiscount.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invoice.id !== null) {
                Invoice.update(vm.invoice, onSaveSuccess, onSaveError);
            } else {
                Invoice.save(vm.invoice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:invoiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.invoiceDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
