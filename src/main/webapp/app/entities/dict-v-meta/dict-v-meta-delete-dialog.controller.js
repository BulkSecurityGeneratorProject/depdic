(function() {
    'use strict';

    angular
        .module('depdicApp')
        .controller('DictVMetaDeleteController',DictVMetaDeleteController);

    DictVMetaDeleteController.$inject = ['$uibModalInstance', 'entity', 'DictVMeta'];

    function DictVMetaDeleteController($uibModalInstance, entity, DictVMeta) {
        var vm = this;

        vm.dictVMeta = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DictVMeta.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
