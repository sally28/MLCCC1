(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassRoomDetailController', ClassRoomDetailController);

    ClassRoomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClassRoom'];

    function ClassRoomDetailController($scope, $rootScope, $stateParams, previousState, entity, ClassRoom) {
        var vm = this;

        vm.classRoom = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:classRoomUpdate', function(event, result) {
            vm.classRoom = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
