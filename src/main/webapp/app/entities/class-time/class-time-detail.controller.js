(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassTimeDetailController', ClassTimeDetailController);

    ClassTimeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClassTime'];

    function ClassTimeDetailController($scope, $rootScope, $stateParams, previousState, entity, ClassTime) {
        var vm = this;

        vm.classTime = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:classTimeUpdate', function(event, result) {
            vm.classTime = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
