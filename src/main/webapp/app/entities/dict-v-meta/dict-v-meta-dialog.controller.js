(function() {
    'use strict';

    angular
        .module('depdicApp')
        .controller('DictVMetaDialogController', DictVMetaDialogController);

    DictVMetaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DictVMeta'];

    function DictVMetaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DictVMeta) {
        var vm = this;

        vm.dictVMeta = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dictVMeta.id !== null) {
                DictVMeta.update(vm.dictVMeta, onSaveSuccess, onSaveError);
            } else {
                DictVMeta.save(vm.dictVMeta, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('depdicApp:dictVMetaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dtm = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
