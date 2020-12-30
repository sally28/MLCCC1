(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SwitchClassController',SwitchClassController);

    SwitchClassController.$inject = ['$scope', '$state', 'previousState', '$uibModalInstance', 'entity', 'Registration', 'MlcClassCategory', 'MlcClass'];

    function SwitchClassController($scope, $state, previousState, $uibModalInstance, entity, Registration, MlcClassCategory, MlcClass) {
        var vm = this;

        vm.registration = entity;
        vm.switchClass = false;
        vm.saving = false;
        vm.checked = false;

        vm.searchClasses = searchClasses;
        vm.save = save;
        vm.clear = clear;
        vm.confirmSwitchClass = confirmSwitchClass;
        vm.checkDisclaimer = checkDisclaimer;

        vm.existingClass = vm.registration.mlcClass;
        vm.mlcClassCategories = [];
        vm.mlcClassCategory = null;
        vm.mlcClasses = [];
        vm.mlcClass = null;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmSwitchClass () {
            vm.mlcClassCategories = MlcClassCategory.query({}, onLoadCategoriesSuccess);
            vm.switchClass = true;
        }

        function onLoadCategoriesSuccess(){
            if(vm.mlcClassCategories){
                vm.mlcClassCategory = vm.mlcClassCategories[0];
                vm.mlcClasses = MlcClass.query({category: vm.mlcClassCategory.id, newRegistration: true, size: 50, sort: 'className'}, function(data){
                    vm.registration.mlcClass = data[0];
                });
            }
        }

        function searchClasses (){
            vm.mlcClasses = MlcClass.search({category: vm.mlcClassCategory.id, newRegistration: true, size:50, sort: 'className'}, function(data){
                vm.registration.mlcClass = data[0];
            });
        }

        function checkDisclaimer(){
            vm.checked = !vm.checked;
        }

        function save () {
            vm.isSaving = true;
            Registration.update(vm.registration, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:registrationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
