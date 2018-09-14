(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountDetailController', DiscountDetailController);

    DiscountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Discount', 'DiscountCode', 'SchoolTerm'];

    function DiscountDetailController($scope, $rootScope, $stateParams, previousState, entity, Discount, DiscountCode, SchoolTerm) {
        var vm = this;

        vm.discount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:discountUpdate', function(event, result) {
            vm.discount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
