(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .factory('VillageSearch', VillageSearch);

    VillageSearch.$inject = ['$resource'];

    function VillageSearch($resource) {
        var resourceUrl =  'api/_search/villages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
