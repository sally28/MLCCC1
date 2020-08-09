(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolTermDetailController', SchoolTermDetailController);

    SchoolTermDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SchoolTerm'];

    function SchoolTermDetailController($scope, $rootScope, $stateParams, previousState, entity, SchoolTerm) {
        var vm = this;

        vm.schoolTerm = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:schoolTermUpdate', function(event, result) {
            vm.schoolTerm = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
