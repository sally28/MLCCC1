(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountAgreementDetailController', AccountAgreementDetailController);

    AccountAgreementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountAgreement', 'MlcAccount', 'UserAgreement'];

    function AccountAgreementDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountAgreement, MlcAccount, UserAgreement) {
        var vm = this;

        vm.accountAgreement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:accountAgreementUpdate', function(event, result) {
            vm.accountAgreement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
