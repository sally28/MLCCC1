(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('Student', Student);

    Student.$inject = ['$resource'];

    function Student ($resource) {
        var resourceUrl =  'api/students/:id?searchTerm=:param';

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
