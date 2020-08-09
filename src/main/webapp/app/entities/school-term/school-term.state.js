(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('school-term', {
            parent: 'entity',
            url: '/school-term',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SchoolTerms'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/school-term/school-terms.html',
                    controller: 'SchoolTermController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('school-term-detail', {
            parent: 'school-term',
            url: '/school-term/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SchoolTerm'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/school-term/school-term-detail.html',
                    controller: 'SchoolTermDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SchoolTerm', function($stateParams, SchoolTerm) {
                    return SchoolTerm.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'school-term',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('school-term-detail.edit', {
            parent: 'school-term-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-term/school-term-dialog.html',
                    controller: 'SchoolTermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SchoolTerm', function(SchoolTerm) {
                            return SchoolTerm.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('school-term.new', {
            parent: 'school-term',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-term/school-term-dialog.html',
                    controller: 'SchoolTermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                term: null,
                                status: null,
                                register: null,
                                promDate: null,
                                earlyBirdDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('school-term', null, { reload: 'school-term' });
                }, function() {
                    $state.go('school-term');
                });
            }]
        })
        .state('school-term.edit', {
            parent: 'school-term',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-term/school-term-dialog.html',
                    controller: 'SchoolTermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SchoolTerm', function(SchoolTerm) {
                            return SchoolTerm.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('school-term', null, { reload: 'school-term' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('school-term.delete', {
            parent: 'school-term',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/school-term/school-term-delete-dialog.html',
                    controller: 'SchoolTermDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SchoolTerm', function(SchoolTerm) {
                            return SchoolTerm.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('school-term', null, { reload: 'school-term' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
