(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountFlagDetailController', AccountFlagDetailController);

    AccountFlagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountFlag', 'MlcAccount', 'AccountFlagStatus'];

    function AccountFlagDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountFlag, MlcAccount, AccountFlagStatus) {
        var vm = this;

        vm.accountFlag = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:accountFlagUpdate', function(event, result) {
            vm.accountFlag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
