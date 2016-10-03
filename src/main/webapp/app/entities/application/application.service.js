(function() {
    'use strict';
    angular
        .module('raitamitraApp')
        .factory('Application', Application);

    Application.$inject = ['$resource', 'DateUtils'];

    function Application ($resource, DateUtils) {
        var resourceUrl =  'api/applications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.appDate = DateUtils.convertDateTimeFromServer(data.appDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
