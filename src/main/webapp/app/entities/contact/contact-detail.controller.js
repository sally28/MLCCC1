(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ContactDetailController', ContactDetailController);

    ContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contact', 'PhoneType', 'SchoolDistrict'];

    function ContactDetailController($scope, $rootScope, $stateParams, previousState, entity, Contact, PhoneType, SchoolDistrict) {
        var vm = this;

        vm.contact = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:contactUpdate', function(event, result) {
            vm.contact = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
