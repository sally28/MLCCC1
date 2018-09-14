(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PaymentDetailController', PaymentDetailController);

    PaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Payment', 'MlcAccount', 'Registration'];

    function PaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, Payment, MlcAccount, Registration) {
        var vm = this;

        vm.payment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:paymentUpdate', function(event, result) {
            vm.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
