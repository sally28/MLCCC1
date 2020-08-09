(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('ClassTime', ClassTime);

    ClassTime.$inject = ['$resource'];

    function ClassTime ($resource) {
        var resourceUrl =  'api/class-times/:id';

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
