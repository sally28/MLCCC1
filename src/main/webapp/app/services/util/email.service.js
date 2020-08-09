(function () {
    'use strict';

    angular
        .module('mlcccApp')
        .factory('Email', Email);

    Email.$inject = ['$resource'];

    function Email ($resource) {
        var _params = {};
        var factory = {
            send: function () {
                return $resource('api/email', {}, {
                    'sendEmail': {
                        method: 'Post',
                        transformRequest: angular.identity,
                        params: _params,
                        headers: {'Content-type': undefined},
                        transformResponse: function (data) {
                            data = angular.fromJson(data);
                            return data;
                        }
                    }
                });
            },

            setParameter: function (parameters) {
                _params = parameters;
            }
        };
        return {
            send: factory.send,
            setParameter: factory.setParameter
        }
    }
})();
