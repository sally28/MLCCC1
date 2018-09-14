(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('NewsLetterController', NewsLetterController);

    NewsLetterController.$inject = ['$scope', '$rootScope', '$stateParams','FileUpload','NewsLetter'];

    function NewsLetterController($scope, $rootScope, $stateParams, FileUpload, NewsLetter) {

        var vm = this;

        vm.newsLetters = [];
        vm.uploadedFile = null;
        vm.fileSelected = false;
        vm.cancel = cancel;
        vm.selectFile = selectFile;
        vm.uploadFile = uploadFile;

        function isFileSelected() {
            if(vm.uploadedFile){
                return true;
            } else {
                return false;
            }
        };

        function selectFile(files) {
            vm.fileSelected = true;
            vm.uploadedFile = files[0];
            $scope.$apply();
        };

        loadAll();

        function loadAll() {
            NewsLetter.query(function(result) {
                vm.newsLetters = result;
                vm.searchQuery = null;
            });
        };

        function cancel() {
            vm.fileSelected = false;
            vm.uploadedFile = null;
        };

        function uploadFile() {
            var file = vm.uploadedFile;
            var fd = new FormData();
            fd.append('file', file);
            var params = {
                type: 'NewsLetter'
            };
            FileUpload.setParameter(params);
            FileUpload.upload().uploadFile(fd, onUploadFinished);
        };

        function onUploadFinished(){
            console.log("upload finished");
            loadAll();
        }
    }
})();
