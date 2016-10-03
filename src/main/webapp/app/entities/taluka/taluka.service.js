(function() {
    'use strict';
    angular
        .module('raitamitraApp')
        .factory('Taluka', Taluka);

    Taluka.$inject = ['$resource'];

    function Taluka ($resource) {
        var resourceUrl =  'api/talukas/:id';

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
