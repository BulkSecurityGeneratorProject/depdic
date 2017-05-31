(function() {
    'use strict';

    angular
        .module('depdicApp')
        .factory('AboutSearch', AboutSearch);

    AboutSearch.$inject = ['$resource'];

    function AboutSearch($resource) {
        var resourceUrl =  'api/_search/abouts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
