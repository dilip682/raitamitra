(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('TalukaDialogController', TalukaDialogController);

    TalukaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Taluka'];

    function TalukaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Taluka) {
        var vm = this;

        vm.taluka = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.taluka.id !== null) {
                Taluka.update(vm.taluka, onSaveSuccess, onSaveError);
            } else {
                Taluka.save(vm.taluka, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raitamitraApp:talukaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
