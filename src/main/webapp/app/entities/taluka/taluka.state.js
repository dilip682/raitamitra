(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('taluka', {
            parent: 'entity',
            url: '/taluka',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Talukas'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/taluka/talukas.html',
                    controller: 'TalukaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('taluka-detail', {
            parent: 'entity',
            url: '/taluka/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Taluka'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/taluka/taluka-detail.html',
                    controller: 'TalukaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Taluka', function($stateParams, Taluka) {
                    return Taluka.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'taluka',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('taluka-detail.edit', {
            parent: 'taluka-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taluka/taluka-dialog.html',
                    controller: 'TalukaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Taluka', function(Taluka) {
                            return Taluka.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('taluka.new', {
            parent: 'taluka',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taluka/taluka-dialog.html',
                    controller: 'TalukaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('taluka', null, { reload: 'taluka' });
                }, function() {
                    $state.go('taluka');
                });
            }]
        })
        .state('taluka.edit', {
            parent: 'taluka',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taluka/taluka-dialog.html',
                    controller: 'TalukaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Taluka', function(Taluka) {
                            return Taluka.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('taluka', null, { reload: 'taluka' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('taluka.delete', {
            parent: 'taluka',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taluka/taluka-delete-dialog.html',
                    controller: 'TalukaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Taluka', function(Taluka) {
                            return Taluka.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('taluka', null, { reload: 'taluka' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
