(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-agreement', {
            parent: 'entity',
            url: '/user-agreement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserAgreements'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-agreement/user-agreements.html',
                    controller: 'UserAgreementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('user-agreement-detail', {
            parent: 'user-agreement',
            url: '/user-agreement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserAgreement'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-agreement/user-agreement-detail.html',
                    controller: 'UserAgreementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserAgreement', function($stateParams, UserAgreement) {
                    return UserAgreement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-agreement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-agreement-detail.edit', {
            parent: 'user-agreement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-agreement/user-agreement-dialog.html',
                    controller: 'UserAgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserAgreement', function(UserAgreement) {
                            return UserAgreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-agreement.new', {
            parent: 'user-agreement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-agreement/user-agreement-dialog.html',
                    controller: 'UserAgreementDialogController',
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
                    $state.go('user-agreement', null, { reload: 'user-agreement' });
                }, function() {
                    $state.go('user-agreement');
                });
            }]
        })
        .state('user-agreement.edit', {
            parent: 'user-agreement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-agreement/user-agreement-dialog.html',
                    controller: 'UserAgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserAgreement', function(UserAgreement) {
                            return UserAgreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-agreement', null, { reload: 'user-agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-agreement.delete', {
            parent: 'user-agreement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-agreement/user-agreement-delete-dialog.html',
                    controller: 'UserAgreementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserAgreement', function(UserAgreement) {
                            return UserAgreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-agreement', null, { reload: 'user-agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
