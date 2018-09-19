(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AppliedDiscountDetailController', AppliedDiscountDetailController);

    AppliedDiscountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AppliedDiscount', 'Discount', 'Registration', 'Invoice'];

    function AppliedDiscountDetailController($scope, $rootScope, $stateParams, previousState, entity, AppliedDiscount, Discount, Registration, Invoice) {
        var vm = this;

        vm.appliedDiscount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:appliedDiscountUpdate', function(event, result) {
            vm.appliedDiscount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
