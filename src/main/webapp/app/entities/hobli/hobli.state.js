(function() {
    'use strict';

    angular
        .module('raitamitraApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hobli', {
            parent: 'entity',
            url: '/hobli',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Hoblis'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hobli/hoblis.html',
                    controller: 'HobliController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('hobli-detail', {
            parent: 'entity',
            url: '/hobli/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Hobli'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hobli/hobli-detail.html',
                    controller: 'HobliDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Hobli', function($stateParams, Hobli) {
                    return Hobli.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hobli',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hobli-detail.edit', {
            parent: 'hobli-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hobli/hobli-dialog.html',
                    controller: 'HobliDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hobli', function(Hobli) {
                            return Hobli.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hobli.new', {
            parent: 'hobli',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hobli/hobli-dialog.html',
                    controller: 'HobliDialogController',
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
                    $state.go('hobli', null, { reload: 'hobli' });
                }, function() {
                    $state.go('hobli');
                });
            }]
        })
        .state('hobli.edit', {
            parent: 'hobli',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hobli/hobli-dialog.html',
                    controller: 'HobliDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hobli', function(Hobli) {
                            return Hobli.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hobli', null, { reload: 'hobli' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hobli.delete', {
            parent: 'hobli',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hobli/hobli-delete-dialog.html',
                    controller: 'HobliDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hobli', function(Hobli) {
                            return Hobli.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hobli', null, { reload: 'hobli' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
