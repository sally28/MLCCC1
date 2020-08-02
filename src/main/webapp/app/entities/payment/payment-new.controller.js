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
                var creditCardPayment = {
                    paymentAmount : vm.invoice.total,
                    cardNumber : '4242424242424242',
                    expirationDate: '0822',
                    cardCode: '234',
                    email: vm.invoice.billToUser.email
                }
                vm.payment.creditCard = creditCardPayment;
            }
        }
    }
})();
