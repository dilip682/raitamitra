(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('HobliDialogController', HobliDialogController);

    HobliDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hobli'];

    function HobliDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hobli) {
        var vm = this;

        vm.hobli = entity;
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
            if (vm.hobli.id !== null) {
                Hobli.update(vm.hobli, onSaveSuccess, onSaveError);
            } else {
                Hobli.save(vm.hobli, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('raitamitraApp:hobliUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
