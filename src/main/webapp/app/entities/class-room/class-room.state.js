(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('class-room', {
            parent: 'entity',
            url: '/class-room',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClassRooms'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-room/class-rooms.html',
                    controller: 'ClassRoomController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('class-room-detail', {
            parent: 'class-room',
            url: '/class-room/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClassRoom'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-room/class-room-detail.html',
                    controller: 'ClassRoomDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ClassRoom', function($stateParams, ClassRoom) {
                    return ClassRoom.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'class-room',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('class-room-detail.edit', {
            parent: 'class-room-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-dialog.html',
                    controller: 'ClassRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassRoom', function(ClassRoom) {
                            return ClassRoom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-room.new', {
            parent: 'class-room',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-dialog.html',
                    controller: 'ClassRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                roomNumber: null,
                                description: null,
                                projectAvailable: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('class-room', null, { reload: 'class-room' });
                }, function() {
                    $state.go('class-room');
                });
            }]
        })
        .state('class-room.edit', {
            parent: 'class-room',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-dialog.html',
                    controller: 'ClassRoomDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassRoom', function(ClassRoom) {
                            return ClassRoom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-room', null, { reload: 'class-room' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-room.delete', {
            parent: 'class-room',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-room/class-room-delete-dialog.html',
                    controller: 'ClassRoomDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClassRoom', function(ClassRoom) {
                            return ClassRoom.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-room', null, { reload: 'class-room' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
