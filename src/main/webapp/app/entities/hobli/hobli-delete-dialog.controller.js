(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .controller('HobliDeleteController',HobliDeleteController);

    HobliDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hobli'];

    function HobliDeleteController($uibModalInstance, entity, Hobli) {
        var vm = this;

        vm.hobli = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hobli.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
