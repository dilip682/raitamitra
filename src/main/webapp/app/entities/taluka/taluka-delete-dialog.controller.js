(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('TalukaDeleteController',TalukaDeleteController);

    TalukaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Taluka'];

    function TalukaDeleteController($uibModalInstance, entity, Taluka) {
        var vm = this;

        vm.taluka = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Taluka.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
