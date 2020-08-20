(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PaymentRefundController', PaymentRefundController);

    PaymentRefundController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payment', 'MlcAccount', 'Invoice'];

    function PaymentRefundController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Payment, MlcAccount, Invoice) {
        var vm = this;
        vm.payment = { id: null, amount: null, type: null, status: null};
        vm.invoice = entity;
        vm.creditCardPayment = {
            paymentAmount : null,
            cardNumber : null,
            expirationDate: null,
            cardCode: null,
            email: null
        };
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
            vm.payment.invoiceDto = vm.invoice;
            vm.payment.account = vm.invoice.billToUser;
            vm.payment.status = 'REFUND';
            vm.payment.amount = 0-vm.invoice.refund;
            Payment.save(vm.payment, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:paymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
