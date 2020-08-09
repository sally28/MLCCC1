(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('ClassRoomController', ClassRoomController);

    ClassRoomController.$inject = ['ClassRoom'];

    function ClassRoomController(ClassRoom) {

        var vm = this;

        vm.classRooms = [];

        loadAll();

        function loadAll() {
            ClassRoom.query(function(result) {
                vm.classRooms = result;
                vm.searchQuery = null;
            });
        }
    }
})();
