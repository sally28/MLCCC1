(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('account-flag', {
            parent: 'entity',
            url: '/account-flag',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AccountFlags'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-flag/account-flags.html',
                    controller: 'AccountFlagController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('account-flag-detail', {
            parent: 'account-flag',
            url: '/account-flag/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AccountFlag'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-flag/account-flag-detail.html',
                    controller: 'AccountFlagDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AccountFlag', function($stateParams, AccountFlag) {
                    return AccountFlag.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'account-flag',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('account-flag-detail.edit', {
            parent: 'account-flag-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag/account-flag-dialog.html',
                    controller: 'AccountFlagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountFlag', function(AccountFlag) {
                            return AccountFlag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-flag.new', {
            parent: 'account-flag',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag/account-flag-dialog.html',
                    controller: 'AccountFlagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                flagType: null,
                                relatedKey: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('account-flag', null, { reload: 'account-flag' });
                }, function() {
                    $state.go('account-flag');
                });
            }]
        })
        .state('account-flag.edit', {
            parent: 'account-flag',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag/account-flag-dialog.html',
                    controller: 'AccountFlagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountFlag', function(AccountFlag) {
                            return AccountFlag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-flag', null, { reload: 'account-flag' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-flag.delete', {
            parent: 'account-flag',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-flag/account-flag-delete-dialog.html',
                    controller: 'AccountFlagDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountFlag', function(AccountFlag) {
                            return AccountFlag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-flag', null, { reload: 'account-flag' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
