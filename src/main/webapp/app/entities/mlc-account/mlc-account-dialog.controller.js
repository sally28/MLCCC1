(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcAccountDialogController', MlcAccountDialogController);

    MlcAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MlcAccount', 'Contact'];

    function MlcAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MlcAccount, Contact) {
        var vm = this;

        vm.mlcAccount = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.contacts = Contact.query({filter: 'mlcaccount-is-null'});
        $q.all([vm.mlcAccount.$promise, vm.contacts.$promise]).then(function() {
            if (!vm.mlcAccount.contact || !vm.mlcAccount.contact.id) {
                return $q.reject();
            }
            return Contact.get({id : vm.mlcAccount.contact.id}).$promise;
        }).then(function(contact) {
            vm.contacts.push(contact);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mlcAccount.id !== null) {
                MlcAccount.update(vm.mlcAccount, onSaveSuccess, onSaveError);
            } else {
                MlcAccount.save(vm.mlcAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:mlcAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
