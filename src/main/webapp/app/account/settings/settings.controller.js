(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'SchoolDistrict'];

    function SettingsController (Principal, Auth, SchoolDistrict) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.schoolDistricts = [];

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                address: account.address,
                city: account.city,
                state: account.state,
                zip: account.zip,
                phone: account.phone,
                schoolDistrict: account.schoolDistrict
            };
        };
        //load all school districts
        loadSchoolDistricts();

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        function loadSchoolDistricts() {
            SchoolDistrict.query(function(result) {
                vm.schoolDistricts = result;
            });
        }
    }
})();
