(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('NewsLetterDialogController', NewsLetterDialogController);

    NewsLetterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NewsLetter'];

    function NewsLetterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NewsLetter) {
        var vm = this;

        vm.newsLetter = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.newsLetter.id !== null) {
                NewsLetter.update(vm.newsLetter, onSaveSuccess, onSaveError);
            } else {
                NewsLetter.save(vm.newsLetter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:newsLetterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.uploadDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
