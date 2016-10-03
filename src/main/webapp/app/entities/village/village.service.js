(function() {
    'use strict';
    angular
        .module('raitamitraApp')
        .factory('Village', Village);

    Village.$inject = ['$resource'];

    function Village ($resource) {
        var resourceUrl =  'api/villages/:id';

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
