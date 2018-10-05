(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('GalleryController', GalleryController);

    GalleryController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'NewsLetter'];

    function GalleryController ($scope, Principal, LoginService, $state, NewsLetter) {
        var vm = this;


    }
})();
