(function() {
    'use strict';

    angular
        .module('depdicApp')
        .controller('AboutDetailController', AboutDetailController);

    AboutDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'About'];

    function AboutDetailController($scope, $rootScope, $stateParams, previousState, entity, About) {
        var vm = this;

        vm.about = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('depdicApp:aboutUpdate', function(event, result) {
            vm.about = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
