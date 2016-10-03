(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .config(bootstrapMaterialDesignConfig);

//    compileServiceConfig.$inject = [];

    function bootstrapMaterialDesignConfig() {
        $.material.init();

    }
})();
