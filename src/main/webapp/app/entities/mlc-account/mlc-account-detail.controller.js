(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcAccountDetailController', MlcAccountDetailController);

    MlcAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MlcAccount', 'Contact'];

    function MlcAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, MlcAccount, Contact) {
        var vm = this;

        vm.mlcAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:mlcAccountUpdate', function(event, result) {
            vm.mlcAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
