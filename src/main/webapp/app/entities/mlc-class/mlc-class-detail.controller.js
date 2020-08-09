(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassDetailController', MlcClassDetailController);

    MlcClassDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MlcClass', 'ClassStatus', 'ClassTime', 'Teacher', 'ClassRoom', 'SchoolTerm'];

    function MlcClassDetailController($scope, $rootScope, $stateParams, previousState, entity, MlcClass, ClassStatus, ClassTime, Teacher, ClassRoom, SchoolTerm) {
        var vm = this;

        vm.mlcClass = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:mlcClassUpdate', function(event, result) {
            vm.mlcClass = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
