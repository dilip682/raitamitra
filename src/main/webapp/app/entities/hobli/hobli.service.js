(function() {
    'use strict';
    angular
        .module('raitamitraApp')
        .factory('Hobli', Hobli);

    Hobli.$inject = ['$resource'];

    function Hobli ($resource) {
        var resourceUrl =  'api/hoblis/:id';

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
