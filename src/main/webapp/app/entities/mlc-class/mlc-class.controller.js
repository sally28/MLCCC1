(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassController', MlcClassController);

    MlcClassController.$inject = ['$state', 'MlcClass', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'MlcClassCategory', 'SchoolTerm', 'Registration', 'ClassTime'];

    function MlcClassController($state, MlcClass, ParseLinks, AlertService, paginationConstants, pagingParams, MlcClassCategory, SchoolTerm, Registration, ClassTime) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.searchClass = searchClass;
        vm.getCHLClasses = getCHLClasses;
        vm.getCSLClasses = getCSLClasses;
        vm.getNonLangClasses = getNonLangClasses;
        vm.getAllClasses = loadAll;
        vm.mlcClassCategories = [];
        vm.classTimes = [];
        vm.CHLCategoryId;
        vm.CSLCategoryId;
        vm.NonLangCategoryId;
        vm.classTime;
        vm.schoolTerms = loadSchoolTerms;
        vm.getClassesBySchoolTerm = getClassesBySchoolTerm;
        vm.filterClasses = filterClasses;

        /*
        MlcClassCategory.query(null,function(data){
            vm.mlcClassCategories = data;
            vm.mlcClassCategories.forEach(function(cat){
                if(cat.name == 'CSL Chinese'){
                    vm.CSLCategoryId = cat.id;
                } else if(cat.name == 'CHL Chinese'){
                    vm.CHLCategoryId = cat.id;
                } else if(cat.name == 'NonLanguage'){
                    vm.NonLangCategoryId = cat.id;
                }
            });
        }); */
        /* Temp code for summer camp */
        MlcClassCategory.query(null,function(data){
            data.forEach(function(cat){
                if(cat.description === 'SUMMER CAMP'){
                    vm.mlcClassCategories.push(cat);
                }
            });
        });
        ClassTime.query(null, function(data){
            data.forEach(function (classTime){
                if(classTime.classTime === 'FULL DAY' || classTime.classTime === 'HALF DAY' || classTime.classTime === '3/4 DAY'){
                    vm.classTimes.push(classTime);
                }
            });
        });

        //loadAll();

        loadSchoolTerms();

        function loadAll () {
            MlcClass.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'className') {
                    result.push('className');
                }
                return result;
            }
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.mlcClasses = data;
           // vm.page = pagingParams.page;
            /* temp code for summer camp */
            if(vm.classTime){
                vm.mlcClasses = vm.mlcClasses.filter(function(item){
                    return item.classTime.classTime == vm.classTime.classTime;
                });
            }
            vm.mlcClasses.forEach(function(mlcClass){
                mlcClass.confirmed = 0;
                mlcClass.pending = 0;
                Registration.query({param : "registrationsForClass:"+mlcClass.id}, function (registrations){
                    registrations.forEach(function(reg){
                        if(reg.status == 'CONFIRMED'){
                            mlcClass.confirmed += 1;
                        } else if(reg.status == 'PENDING'){
                            mlcClass.pending +=1;
                        }
                    });
                });
            });
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function onSchoolTermLoadSuccess() {
            vm.schoolTerm = vm.schoolTerms[0];
            getClassesBySchoolTerm();
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        /*function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                category: vm.category? vm.category.id : null,
                schoolTerm: vm.schoolTerm? vm.schoolTerm.id : null,
                search: vm.currentSearch
            });
        }*/
        function transition() {
            var categoryId  = vm.category? vm.category.id : null;
            var schoolTermId = vm.schoolTerm? vm.schoolTerm.id : null;
            MlcClass.query({schoolTerm: schoolTermId, category: categoryId,
                page: vm.page - 1,
                size: vm.itemsPerPage,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
            }, onSuccess, onError);
        }

        function searchClass(){
            vm.category = null;
            MlcClass.query({search: vm.searchTerm,
                schoolTerm: vm.schoolTerm.id,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function getCHLClasses(){
            MlcClass.query({category: vm.CHLCategoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function getCSLClasses(){
            MlcClass.query({category: vm.CSLCategoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function getNonLangClasses(){
            MlcClass.query({category: vm.NonLangCategoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function loadSchoolTerms () {
            SchoolTerm.query(function(result) {
                vm.schoolTerms = result;
                vm.searchQuery = null;
                vm.schoolTerm = vm.schoolTerms[0];
                vm.getClassesBySchoolTerm();
            });
        }

        function getClassesBySchoolTerm() {
            MlcClass.query({schoolTerm: vm.schoolTerm.id,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function filterClasses () {
            vm.searchTerm = null;
            var categoryId  = vm.category? vm.category.id : null;
            var schoolTermId = vm.schoolTerm? vm.schoolTerm.id : null;
            MlcClass.query({schoolTerm: schoolTermId, category: categoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }
    }
})();
