(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ContactDialogController', ContactDialogController);

    ContactDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contact', 'PhoneType', 'SchoolDistrict'];

    function ContactDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contact, PhoneType, SchoolDistrict) {
        var vm = this;

        vm.contact = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phonetypes = PhoneType.query();
        vm.schooldistricts = SchoolDistrict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contact.id !== null) {
                Contact.update(vm.contact, onSaveSuccess, onSaveError);
            } else {
                Contact.save(vm.contact, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:contactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
