(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('AccountAgreementDialogController', AccountAgreementDialogController);

    AccountAgreementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AccountAgreement', 'MlcAccount', 'UserAgreement'];

    function AccountAgreementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AccountAgreement, MlcAccount, UserAgreement) {
        var vm = this;

        vm.accountAgreement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.accounts = MlcAccount.query({filter: 'accountagreement-is-null'});
        $q.all([vm.accountAgreement.$promise, vm.accounts.$promise]).then(function() {
            if (!vm.accountAgreement.account || !vm.accountAgreement.account.id) {
                return $q.reject();
            }
            return MlcAccount.get({id : vm.accountAgreement.account.id}).$promise;
        }).then(function(account) {
            vm.accounts.push(account);
        });
        vm.agreements = UserAgreement.query({filter: 'accountagreement-is-null'});
        $q.all([vm.accountAgreement.$promise, vm.agreements.$promise]).then(function() {
            if (!vm.accountAgreement.agreement || !vm.accountAgreement.agreement.id) {
                return $q.reject();
            }
            return UserAgreement.get({id : vm.accountAgreement.agreement.id}).$promise;
        }).then(function(agreement) {
            vm.agreements.push(agreement);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountAgreement.id !== null) {
                AccountAgreement.update(vm.accountAgreement, onSaveSuccess, onSaveError);
            } else {
                AccountAgreement.save(vm.accountAgreement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:accountAgreementUpdate', result);
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
