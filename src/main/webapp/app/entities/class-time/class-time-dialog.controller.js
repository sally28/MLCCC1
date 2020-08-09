(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassTimeDialogController', ClassTimeDialogController);

    ClassTimeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClassTime'];

    function ClassTimeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClassTime) {
        var vm = this;

        vm.classTime = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.classTime.id !== null) {
                ClassTime.update(vm.classTime, onSaveSuccess, onSaveError);
            } else {
                ClassTime.save(vm.classTime, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:classTimeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
