(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('AccountFlagStatus', AccountFlagStatus);

    AccountFlagStatus.$inject = ['$resource'];

    function AccountFlagStatus ($resource) {
        var resourceUrl =  'api/account-flag-statuses/:id';

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
