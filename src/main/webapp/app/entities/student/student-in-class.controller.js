(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('StudentInClassController', StudentInClassController);

    StudentInClassController.$inject = ['$state', 'loginUser', 'Student', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Registration', 'MlcClass'];

    function StudentInClassController($state, loginUser, Student, ParseLinks, AlertService, paginationConstants, pagingParams, Registration, MlcClass) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.showRegistrations = true;
        vm.searchStudent = searchStudent;
        vm.account = loginUser;
        vm.mlcClasses = [];
        vm.print = print;

        loadAll();

        function loadAll () {
            /*MlcClass.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                teacher: vm.account.id,
            }, onSuccess, onError);
            */
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
                    var data = vm.mlcClasses.find( function( ele ) {
                        return ele.name === registration.mlcClassName;
                    } );
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
            var printContents = document.getElementById('print-section').innerHTML;
            var popupWin = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
            popupWin.document.open();
            popupWin.document.write(`
                <html>
                    <head>
                      <title>${className}</title>
                    </head>
                    <body onload="window.print();window.close()">${printContents}</body>
                  </html>`
            );
            popupWin.document.close();
        }
    }
})();
