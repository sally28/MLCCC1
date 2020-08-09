(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('AccountFlag', AccountFlag);

    AccountFlag.$inject = ['$resource'];

    function AccountFlag ($resource) {
        var resourceUrl =  'api/account-flags/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
