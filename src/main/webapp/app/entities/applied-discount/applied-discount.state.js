(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('applied-discount', {
            parent: 'entity',
            url: '/applied-discount',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AppliedDiscounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/applied-discount/applied-discounts.html',
                    controller: 'AppliedDiscountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('applied-discount-detail', {
            parent: 'applied-discount',
            url: '/applied-discount/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AppliedDiscount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/applied-discount/applied-discount-detail.html',
                    controller: 'AppliedDiscountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AppliedDiscount', function($stateParams, AppliedDiscount) {
                    return AppliedDiscount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'applied-discount',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('applied-discount-detail.edit', {
            parent: 'applied-discount-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applied-discount/applied-discount-dialog.html',
                    controller: 'AppliedDiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AppliedDiscount', function(AppliedDiscount) {
                            return AppliedDiscount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('applied-discount.new', {
            parent: 'applied-discount',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applied-discount/applied-discount-dialog.html',
                    controller: 'AppliedDiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdDate: null,
                                modifiedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('applied-discount', null, { reload: 'applied-discount' });
                }, function() {
                    $state.go('applied-discount');
                });
            }]
        })
        .state('applied-discount.edit', {
            parent: 'applied-discount',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applied-discount/applied-discount-dialog.html',
                    controller: 'AppliedDiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AppliedDiscount', function(AppliedDiscount) {
                            return AppliedDiscount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('applied-discount', null, { reload: 'applied-discount' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('applied-discount.delete', {
            parent: 'applied-discount',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/applied-discount/applied-discount-delete-dialog.html',
                    controller: 'AppliedDiscountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AppliedDiscount', function(AppliedDiscount) {
                            return AppliedDiscount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('applied-discount', null, { reload: 'applied-discount' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
