(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .factory('HobliSearch', HobliSearch);

    HobliSearch.$inject = ['$resource'];

    function HobliSearch($resource) {
        var resourceUrl =  'api/_search/hoblis/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
