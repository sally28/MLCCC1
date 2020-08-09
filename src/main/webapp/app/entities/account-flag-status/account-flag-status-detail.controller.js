(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagStatusDetailController', AccountFlagStatusDetailController);

    AccountFlagStatusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountFlagStatus'];

    function AccountFlagStatusDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountFlagStatus) {
        var vm = this;

        vm.accountFlagStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:accountFlagStatusUpdate', function(event, result) {
            vm.accountFlagStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
