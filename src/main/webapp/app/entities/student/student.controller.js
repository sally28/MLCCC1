(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('StudentController', StudentController);

    StudentController.$inject = ['$state', 'loginUser', 'Student', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function StudentController($state, loginUser, Student, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.showRegistrations = true;
        vm.searchStudent = searchStudent;

        if(loginUser.authorities && loginUser.authorities.indexOf('ROLE_ADMIN') !== -1){
            vm.showRegistrations = false;
        }

        loadAll();

        function loadAll () {
            Student.query( {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.students = data;
            vm.page = pagingParams.page;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        function searchStudent(){
            Student.query({param: vm.searchTerm}, onSuccess, onError);
        }
    }
})();
