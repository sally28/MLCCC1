(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassDialogController', MlcClassDialogController);

    MlcClassDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MlcClass', 'ClassStatus', 'ClassTime', 'Teacher', 'ClassRoom', 'SchoolTerm'];

    function MlcClassDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MlcClass, ClassStatus, ClassTime, Teacher, ClassRoom, SchoolTerm) {
        var vm = this;

        vm.mlcClass = entity;
        vm.clear = clear;
        vm.save = save;
        vm.classstatuses = ClassStatus.query();
        vm.classtimes = ClassTime.query();
        vm.teachers = Teacher.query({
            page: 0,
            size: 1000,
            sort: 'asc'
        });
        vm.classrooms = ClassRoom.query();
        vm.schoolterms = SchoolTerm.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mlcClass.id !== null) {
                MlcClass.update(vm.mlcClass, onSaveSuccess, onSaveError);
            } else {
                MlcClass.save(vm.mlcClass, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:mlcClassUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
