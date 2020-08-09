(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('NewsLetterDetailController', NewsLetterDetailController);

    NewsLetterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NewsLetter'];

    function NewsLetterDetailController($scope, $rootScope, $stateParams, previousState, entity, NewsLetter) {
        var vm = this;

        vm.newsLetter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:newsLetterUpdate', function(event, result) {
            vm.newsLetter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
