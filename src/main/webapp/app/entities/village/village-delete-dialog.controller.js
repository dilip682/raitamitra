(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('VillageDeleteController',VillageDeleteController);

    VillageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Village'];

    function VillageDeleteController($uibModalInstance, entity, Village) {
        var vm = this;

        vm.village = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Village.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
