(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassCategoryDialogController', MlcClassCategoryDialogController);

    MlcClassCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MlcClassCategory', 'MlcClass'];

    function MlcClassCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MlcClassCategory, MlcClass) {
        var vm = this;

        vm.mlcClassCategory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.mlcclasses = MlcClass.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mlcClassCategory.id !== null) {
                MlcClassCategory.update(vm.mlcClassCategory, onSaveSuccess, onSaveError);
            } else {
                MlcClassCategory.save(vm.mlcClassCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:mlcClassCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
