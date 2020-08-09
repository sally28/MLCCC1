(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'NewsLetter'];

    function HomeController ($scope, Principal, LoginService, $state, NewsLetter) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.newsletter_url = 'files/newsletter/newsletter.html';
        vm.changeNewsletter = changeNewsletter;
        vm.newsLetters = [];

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        getNewsletters();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        function changeNewsletter(fileName){
            vm.newsletter_url = 'files/newsletter/'+fileName;
        }
        function getNewsletters(){
            NewsLetter.query(function(result) {
                vm.newsLetters = result;
                vm.searchQuery = null;
            });
        }
    }
})();
