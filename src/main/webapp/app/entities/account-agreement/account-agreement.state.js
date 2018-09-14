(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('account-agreement', {
            parent: 'entity',
            url: '/account-agreement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AccountAgreements'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-agreement/account-agreements.html',
                    controller: 'AccountAgreementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('account-agreement-detail', {
            parent: 'account-agreement',
            url: '/account-agreement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AccountAgreement'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-agreement/account-agreement-detail.html',
                    controller: 'AccountAgreementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AccountAgreement', function($stateParams, AccountAgreement) {
                    return AccountAgreement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'account-agreement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('account-agreement-detail.edit', {
            parent: 'account-agreement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-agreement/account-agreement-dialog.html',
                    controller: 'AccountAgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountAgreement', function(AccountAgreement) {
                            return AccountAgreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-agreement.new', {
            parent: 'account-agreement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-agreement/account-agreement-dialog.html',
                    controller: 'AccountAgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('account-agreement', null, { reload: 'account-agreement' });
                }, function() {
                    $state.go('account-agreement');
                });
            }]
        })
        .state('account-agreement.edit', {
            parent: 'account-agreement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-agreement/account-agreement-dialog.html',
                    controller: 'AccountAgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountAgreement', function(AccountAgreement) {
                            return AccountAgreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-agreement', null, { reload: 'account-agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-agreement.delete', {
            parent: 'account-agreement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-agreement/account-agreement-delete-dialog.html',
                    controller: 'AccountAgreementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountAgreement', function(AccountAgreement) {
                            return AccountAgreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-agreement', null, { reload: 'account-agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
