(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('SchoolTerm', SchoolTerm);

    SchoolTerm.$inject = ['$resource', 'DateUtils'];

    function SchoolTerm ($resource, DateUtils) {
        var resourceUrl =  'api/school-terms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.promDate = DateUtils.convertLocalDateFromServer(data.promDate);
                        data.earlyBirdDate = DateUtils.convertLocalDateFromServer(data.earlyBirdDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.promDate = DateUtils.convertLocalDateToServer(copy.promDate);
                    copy.earlyBirdDate = DateUtils.convertLocalDateToServer(copy.earlyBirdDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.promDate = DateUtils.convertLocalDateToServer(copy.promDate);
                    copy.earlyBirdDate = DateUtils.convertLocalDateToServer(copy.earlyBirdDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
