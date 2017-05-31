(function() {
    'use strict';

    angular
        .module('depdicApp')
        .controller('AboutController', AboutController);

    AboutController.$inject = ['About', 'AboutSearch'];

    function AboutController(About, AboutSearch) {

        var vm = this;

        vm.abouts = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            About.query(function(result) {
                vm.abouts = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AboutSearch.query({query: vm.searchQuery}, function(result) {
                vm.abouts = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
