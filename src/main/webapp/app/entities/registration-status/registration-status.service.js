(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('RegistrationStatus', RegistrationStatus);

    RegistrationStatus.$inject = ['$resource'];

    function RegistrationStatus ($resource) {
        var resourceUrl =  'api/registration-statuses/:id';

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
