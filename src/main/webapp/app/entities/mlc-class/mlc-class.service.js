(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('MlcClass', MlcClass);

    MlcClass.$inject = ['$resource'];

    function MlcClass ($resource) {
        var resourceUrl =  'api/mlc-classes/:id';

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
            'update': { method:'PUT' },
            'search': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
        });
    }
})();
