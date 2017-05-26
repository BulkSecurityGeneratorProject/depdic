(function() {
    'use strict';

    angular
        .module('depdicApp')
        .factory('DictVMetaSearch', DictVMetaSearch);

    DictVMetaSearch.$inject = ['$resource'];

    function DictVMetaSearch($resource) {
        var resourceUrl =  'api/_search/dict-v-metas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
