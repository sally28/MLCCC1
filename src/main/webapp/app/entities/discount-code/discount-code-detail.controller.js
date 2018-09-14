(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('DiscountCodeDetailController', DiscountCodeDetailController);

    DiscountCodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DiscountCode'];

    function DiscountCodeDetailController($scope, $rootScope, $stateParams, previousState, entity, DiscountCode) {
        var vm = this;

        vm.discountCode = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:discountCodeUpdate', function(event, result) {
            vm.discountCode = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
