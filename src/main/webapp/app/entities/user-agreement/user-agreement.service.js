(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('UserAgreement', UserAgreement);

    UserAgreement.$inject = ['$resource'];

    function UserAgreement ($resource) {
        var resourceUrl =  'api/user-agreements/:id';

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
