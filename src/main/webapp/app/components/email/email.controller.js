(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('EmailController',EmailController);

    EmailController.$inject = ['$uibModalInstance', 'Email','$stateParams', 'entity'];

    function EmailController($uibModalInstance, Email, $stateParams, entity) {
        var vm = this;
        vm.clear = clear;
        vm.send = send;
        vm.email = {'recipients':'', 'subject':'', 'content':''}
        if(entity){
            if(entity.to){
                if(entity.to === 'All Parents'){
                    vm.email.recipients = 'All Parents';
                } else {
                    vm.email.recipients = entity.to;
                }
            } else if (entity.account) {
                vm.email.recipients = entity.account.email;
            }
            vm.email.cc = entity.cc;
            vm.email.bcc = entity.bcc;
            vm.subject = entity.subject;
        }
        vm.onSuccess = onSuccess;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function send(){
            Email.setParameter({'recipients':vm.email.recipients, 'cc':vm.cc, 'bcc':vm.bcc, 'subject':vm.email.subject, 'content':vm.email.content});
            Email.send().sendEmail(null, onSuccess, onError);
        }

        function onSuccess(result){
            $uibModalInstance.close(true);
        }

        function onError(data){
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
