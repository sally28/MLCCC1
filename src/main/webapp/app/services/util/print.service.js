'use strict';

angular.module('mlcccApp')
    .service('Print', function () {
        this.print = function(title, content){
            var popupWin = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
            popupWin.document.open();
            popupWin.document.write('<html> <head> <title>'+title+'</title> </head> <body onload="window.print();window.close()">'+content+'</body> </html>'
            );
            popupWin.document.close();
        };
    });
