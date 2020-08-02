(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PaymentNewController', PaymentNewController);

    PaymentNewController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payment', 'MlcAccount', 'Invoice'];

    function PaymentNewController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Payment, MlcAccount, Invoice) {
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
        vm.getPaymentMethod = getPaymentMethod;
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
            vm.payment.status = 'PAID';
            vm.payment.amount = vm.invoice.total;
            if(vm.payment.type == 'Credit Card'){
                vm.payment.creditCard = vm.creditCardPayment;
            }
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

        function  getPaymentMethod() {
            if (vm.payment.type == 'Credit Card'){
                vm.creditCardPayment.paymentAmount = vm.invoice.total;
                vm.creditCardPayment.email = vm.invoice.billToUser.email;
            }
        }
    }
})();
