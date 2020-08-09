(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassCategoryController', MlcClassCategoryController);

    MlcClassCategoryController.$inject = ['MlcClassCategory'];

    function MlcClassCategoryController(MlcClassCategory) {

        var vm = this;

        vm.mlcClassCategories = [];

        loadAll();

        function loadAll() {
            MlcClassCategory.query(function(result) {
                vm.mlcClassCategories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
