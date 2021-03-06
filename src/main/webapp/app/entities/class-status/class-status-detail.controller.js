(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassStatusDetailController', ClassStatusDetailController);

    ClassStatusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClassStatus'];

    function ClassStatusDetailController($scope, $rootScope, $stateParams, previousState, entity, ClassStatus) {
        var vm = this;

        vm.classStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:classStatusUpdate', function(event, result) {
            vm.classStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
