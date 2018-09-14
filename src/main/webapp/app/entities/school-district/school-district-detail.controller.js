(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SchoolDistrictDetailController', SchoolDistrictDetailController);

    SchoolDistrictDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SchoolDistrict'];

    function SchoolDistrictDetailController($scope, $rootScope, $stateParams, previousState, entity, SchoolDistrict) {
        var vm = this;

        vm.schoolDistrict = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:schoolDistrictUpdate', function(event, result) {
            vm.schoolDistrict = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
