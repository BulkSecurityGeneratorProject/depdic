(function() {
    'use strict';
    angular
        .module('depdicApp')
        .factory('DictVMeta', DictVMeta);

    DictVMeta.$inject = ['$resource', 'DateUtils'];

    function DictVMeta ($resource, DateUtils) {
        var resourceUrl =  'api/dict-v-metas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dtm = DateUtils.convertDateTimeFromServer(data.dtm);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
