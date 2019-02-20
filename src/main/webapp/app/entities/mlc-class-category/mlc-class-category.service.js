(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('MlcClassCategory', MlcClassCategory);

    MlcClassCategory.$inject = ['$resource'];

    function MlcClassCategory ($resource) {
        var resourceUrl =  'api/mlc-class-categories/:id';

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
