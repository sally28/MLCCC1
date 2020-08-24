(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('Payment', Payment);

    Payment.$inject = ['$resource'];

    function Payment ($resource) {
        var resourceUrl =  'api/payments/:id?searchTerm=:param';

        return $resource(resourceUrl, {param: '@param'}, {
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
