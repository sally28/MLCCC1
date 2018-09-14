(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('Teacher', Teacher);

    Teacher.$inject = ['$resource', 'DateUtils'];

    function Teacher ($resource, DateUtils) {
        var resourceUrl =  'api/teachers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.hireDate = DateUtils.convertLocalDateFromServer(data.hireDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.hireDate = DateUtils.convertLocalDateToServer(copy.hireDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.hireDate = DateUtils.convertLocalDateToServer(copy.hireDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
