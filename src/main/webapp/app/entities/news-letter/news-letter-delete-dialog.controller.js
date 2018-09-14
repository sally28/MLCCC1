(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('NewsLetterDeleteController',NewsLetterDeleteController);

    NewsLetterDeleteController.$inject = ['$uibModalInstance', 'entity', 'NewsLetter'];

    function NewsLetterDeleteController($uibModalInstance, entity, NewsLetter) {
        var vm = this;

        vm.newsLetter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NewsLetter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
