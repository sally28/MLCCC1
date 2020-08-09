(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('class-status', {
            parent: 'entity',
            url: '/class-status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClassStatuses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-status/class-statuses.html',
                    controller: 'ClassStatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('class-status-detail', {
            parent: 'class-status',
            url: '/class-status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClassStatus'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-status/class-status-detail.html',
                    controller: 'ClassStatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ClassStatus', function($stateParams, ClassStatus) {
                    return ClassStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'class-status',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('class-status-detail.edit', {
            parent: 'class-status-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-status/class-status-dialog.html',
                    controller: 'ClassStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassStatus', function(ClassStatus) {
                            return ClassStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-status.new', {
            parent: 'class-status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-status/class-status-dialog.html',
                    controller: 'ClassStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                colorDisplayed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('class-status', null, { reload: 'class-status' });
                }, function() {
                    $state.go('class-status');
                });
            }]
        })
        .state('class-status.edit', {
            parent: 'class-status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-status/class-status-dialog.html',
                    controller: 'ClassStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassStatus', function(ClassStatus) {
                            return ClassStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-status', null, { reload: 'class-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-status.delete', {
            parent: 'class-status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-status/class-status-delete-dialog.html',
                    controller: 'ClassStatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClassStatus', function(ClassStatus) {
                            return ClassStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-status', null, { reload: 'class-status' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
