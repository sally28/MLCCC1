(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassCategoryDetailController', MlcClassCategoryDetailController);

    MlcClassCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MlcClassCategory', 'MlcClass'];

    function MlcClassCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, MlcClassCategory, MlcClass) {
        var vm = this;

        vm.mlcClassCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:mlcClassCategoryUpdate', function(event, result) {
            vm.mlcClassCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
