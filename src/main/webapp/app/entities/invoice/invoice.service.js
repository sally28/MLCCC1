(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('Invoice', Invoice);

    Invoice.$inject = ['$resource', 'DateUtils'];

    function Invoice ($resource, DateUtils) {
        var resourceUrl =  'api/invoices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.invoiceDate = DateUtils.convertLocalDateFromServer(data.invoiceDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.invoiceDate = DateUtils.convertLocalDateToServer(copy.invoiceDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.invoiceDate = DateUtils.convertLocalDateToServer(copy.invoiceDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
