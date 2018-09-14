(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('UserAgreementDetailController', UserAgreementDetailController);

    UserAgreementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserAgreement'];

    function UserAgreementDetailController($scope, $rootScope, $stateParams, previousState, entity, UserAgreement) {
        var vm = this;

        vm.userAgreement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:userAgreementUpdate', function(event, result) {
            vm.userAgreement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
