(function() {
    'use strict';

    angular
        .module('depdicApp')
        .controller('DictVMetaDetailController', DictVMetaDetailController);

    DictVMetaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DictVMeta'];

    function DictVMetaDetailController($scope, $rootScope, $stateParams, previousState, entity, DictVMeta) {
        var vm = this;

        vm.dictVMeta = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('depdicApp:dictVMetaUpdate', function(event, result) {
            vm.dictVMeta = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
