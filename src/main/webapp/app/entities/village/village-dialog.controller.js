(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('VillageDialogController', VillageDialogController);

    VillageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Village'];

    function VillageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Village) {
        var vm = this;

        vm.village = entity;
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
            if (vm.village.id !== null) {
                Village.update(vm.village, onSaveSuccess, onSaveError);
            } else {
                Village.save(vm.village, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raitamitraApp:villageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
