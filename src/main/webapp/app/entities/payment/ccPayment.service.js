(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('CCPayment', CCPayment);

    CCPayment.$inject = ['$http'];

    function CCPayment ($http) {
        return  {
            getToken: function() {
                return $http.get('api/payments/token');
            }
        }
    }
})();