(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('DiscountCode', DiscountCode);

    DiscountCode.$inject = ['$resource'];

    function DiscountCode ($resource) {
        var resourceUrl =  'api/discount-codes/:id';

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
