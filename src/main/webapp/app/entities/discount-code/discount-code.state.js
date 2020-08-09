(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('discount-code', {
            parent: 'entity',
            url: '/discount-code',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DiscountCodes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount-code/discount-codes.html',
                    controller: 'DiscountCodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('discount-code-detail', {
            parent: 'discount-code',
            url: '/discount-code/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DiscountCode'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount-code/discount-code-detail.html',
                    controller: 'DiscountCodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DiscountCode', function($stateParams, DiscountCode) {
                    return DiscountCode.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'discount-code',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('discount-code-detail.edit', {
            parent: 'discount-code-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-code/discount-code-dialog.html',
                    controller: 'DiscountCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiscountCode', function(DiscountCode) {
                            return DiscountCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount-code.new', {
            parent: 'discount-code',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-code/discount-code-dialog.html',
                    controller: 'DiscountCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('discount-code', null, { reload: 'discount-code' });
                }, function() {
                    $state.go('discount-code');
                });
            }]
        })
        .state('discount-code.edit', {
            parent: 'discount-code',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-code/discount-code-dialog.html',
                    controller: 'DiscountCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiscountCode', function(DiscountCode) {
                            return DiscountCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount-code', null, { reload: 'discount-code' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount-code.delete', {
            parent: 'discount-code',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-code/discount-code-delete-dialog.html',
                    controller: 'DiscountCodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DiscountCode', function(DiscountCode) {
                            return DiscountCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount-code', null, { reload: 'discount-code' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
