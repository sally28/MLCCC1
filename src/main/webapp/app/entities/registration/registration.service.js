(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('Registration', Registration);

    Registration.$inject = ['$resource', 'DateUtils'];

    function Registration ($resource, DateUtils) {
        var resourceUrl =  'api/registrations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertLocalDateFromServer(data.createDate);
                        data.modifyDate = DateUtils.convertLocalDateFromServer(data.modifyDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createDate = DateUtils.convertLocalDateToServer(copy.createDate);
                    copy.modifyDate = DateUtils.convertLocalDateToServer(copy.modifyDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createDate = DateUtils.convertLocalDateToServer(copy.createDate);
                    copy.modifyDate = DateUtils.convertLocalDateToServer(copy.modifyDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
