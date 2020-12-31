(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('StudentInClassController', StudentInClassController);

    StudentInClassController.$inject = ['$state', 'loginUser', 'Student', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Registration', 'MlcClass', 'Print'];

    function StudentInClassController($state, loginUser, Student, ParseLinks, AlertService, paginationConstants, pagingParams, Registration, MlcClass, Print) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = 30;
        vm.showRegistrations = true;
        vm.searchStudent = searchStudent;
        vm.account = loginUser;
        vm.mlcClasses = [];
        vm.print = print;
        vm.emailParents = email;
        loadAll();

        function loadAll () {
            Student.query( {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                param: 'inClass'
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
            vm.students.forEach(function(student){
                student.registrations.forEach(function(registration){
                    if(registration.status == 'CONFIRMED' || registration.status == "PENDING" ){
                        var data = vm.mlcClasses.find( function( ele ) {
                            return ele.name === registration.mlcClassName;
                        } );
                        student.status = registration.status;
                        if(data == null){
                            var classItem = {
                                name: registration.mlcClassName,
                                students: []
                            }
                            classItem.students.push(student);
                            vm.mlcClasses.push(classItem);
                        } else {
                            data.students.push(student);
                        }
                    }
                });
            });
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        function searchStudent(){
            Student.query({param: vm.searchTerm}, onSuccess, onError);
        }

        function print(className){
            var printContents = document.getElementById('print-section '+className).innerHTML;
            Print.print(className, printContents);
        }

        function email(className){
            var parents = []
            vm.mlcClasses.forEach(function(mlcClass) {
                if(mlcClass.name == className){
                    mlcClass.students.forEach(function(student){
                        student.associatedAccounts.forEach(function(account){
                            if(account.primaryContact && account.email){
                                parents.push(account.email);
                            }
                        });
                    });
                }
            })

            $state.params.to = 'principals@mlccc.org';
            $state.params.subject = className;
            $state.params.cc = vm.account.email;
            $state.params.bcc = parents;
            $state.go('student-in-class.email')
        }
    }
})();
