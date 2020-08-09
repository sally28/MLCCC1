(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('class-time', {
            parent: 'entity',
            url: '/class-time',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClassTimes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-time/class-times.html',
                    controller: 'ClassTimeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('class-time-detail', {
            parent: 'class-time',
            url: '/class-time/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClassTime'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-time/class-time-detail.html',
                    controller: 'ClassTimeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ClassTime', function($stateParams, ClassTime) {
                    return ClassTime.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'class-time',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('class-time-detail.edit', {
            parent: 'class-time-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-time/class-time-dialog.html',
                    controller: 'ClassTimeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassTime', function(ClassTime) {
                            return ClassTime.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-time.new', {
            parent: 'class-time',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-time/class-time-dialog.html',
                    controller: 'ClassTimeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                classTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('class-time', null, { reload: 'class-time' });
                }, function() {
                    $state.go('class-time');
                });
            }]
        })
        .state('class-time.edit', {
            parent: 'class-time',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-time/class-time-dialog.html',
                    controller: 'ClassTimeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassTime', function(ClassTime) {
                            return ClassTime.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-time', null, { reload: 'class-time' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-time.delete', {
            parent: 'class-time',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-time/class-time-delete-dialog.html',
                    controller: 'ClassTimeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClassTime', function(ClassTime) {
                            return ClassTime.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-time', null, { reload: 'class-time' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
