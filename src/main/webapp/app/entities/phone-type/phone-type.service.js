(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('PhoneType', PhoneType);

    PhoneType.$inject = ['$resource'];

    function PhoneType ($resource) {
        var resourceUrl =  'api/phone-types/:id';

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
