(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .factory('TalukaSearch', TalukaSearch);

    TalukaSearch.$inject = ['$resource'];

    function TalukaSearch($resource) {
        var resourceUrl =  'api/_search/talukas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
