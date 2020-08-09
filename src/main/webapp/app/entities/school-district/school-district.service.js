(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('SchoolDistrict', SchoolDistrict);

    SchoolDistrict.$inject = ['$resource'];

    function SchoolDistrict ($resource) {
        var resourceUrl =  'api/school-districts/:id';

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
