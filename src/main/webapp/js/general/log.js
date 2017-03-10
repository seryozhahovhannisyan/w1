GENERAL_API.createNameSpace("GENERAL_API.ACTION");


GENERAL_API.ACTION.LOGS = function () {

    var LOG_LEVEL = {ERROR : 'error',DEBUG: 'debug',WARN :'warn',INFO : 'info'};

    var interface = {
        tableLogger: tableLogger,
        createTable: createTable,
        removeTable: removeTable,
        logger: logger,
        save : save,
        LOG_LEVEL : LOG_LEVEL
    };
    init();
    GENERAL_API.ACTION.LOGS = function () {
        return interface;
    };
    return interface;

    function init () {
        initEventsListeners ();
    }
    function openTableLog () {
        if ( $('#tablesLog').hasClass('j-dn')) {
            $('#tablesLog').removeClass('j-dn');
            $('#testingLog').addClass('j-dn');
        } else {
            $('#tablesLog').addClass('j-dn');
        }
    }
    function openLog () {
        if ( $('#testingLog').hasClass('j-dn')) {
            $('#testingLog').removeClass('j-dn');
            $('#tablesLog').addClass('j-dn');
        } else {
            $('#testingLog').addClass('j-dn');
        }
    }
    function tableLogger (id, p) {
        var h = '<tr>',
            l = p.length,
            i = 0,
            o;
        for (; i < l; ++i) {
            h += '<td>' + p[i] + '</td>'
        }
        h += '</tr>';
        o = $(h);
        o.bind('click', function () {
            var o = $(this).find('td'),
                col = [], i = 0;
            $.each(o, function () {
                col[i++] = $(this).text();
            });
            openSelectedRow (col);
        });
        $('#'+ id + ' .j-nm').after(o);

    }

    function createTable (id, name, col) {
        var h = '<div class="table-holder"><span class="table-name">'
                + name + '<i class="t-closer"></i></span><table id="' + id +'"><tr class=" j-nm">',
            l = col.length,
            i = 0;
        for (; i < l; ++i) {
            h += '<th class="h-p">' + col[i] + '</th>';
        }
        h += '</tr></table></div>';
        h = $(h);
        h.find('.t-closer').bind('click', function () {
            $(this).parents('.table-holder').remove();
            $("#testingLog").hide();
            $("#tablesLog").hide();
            $("#selected-testingLog").hide();
        });
        $('#tablesLog').append($(h));
    }

    function removeTable (id) {
        $('#'+ id).parent('.table-holder').remove();
    }
    function clearLog () {
        $('#logger').empty();
    }
    function logger (n, text) {
        var s = '<span class="testing-entry"><i># ' + n + ' </i>' + text + '</span>',
            o = $(s);
        o.bind('click', function () {
            openSelectedLog ($(this).text());
        });
        $('#logger').prepend(o);
    }
    function openSelectedLog ( text) {
        var o = $('#selected-row-logger');
        $('#selected-row').removeClass('j-dn');
        $('#selected-row-table').addClass('j-dn');
        o.empty();
        o.append($('<span class="testing-entry">' + text + '</span>'));
        o.removeClass('j-dn');
    }
    function openSelectedRow (col) {
        var o = $('#selected-row-table'),
            i = 0;
        $('#selected-row').removeClass('j-dn');
        $('#selected-row-logger').addClass('j-dn');
        o.empty();
        for (; i < col.length; ++i) {
            o.append($('<span class="s-r-entry">' + col[i] + '</span>'));
        }
        o.removeClass('j-dn');
    }
    function closeSelected () {
        $('#selected-row').addClass('j-dn');
        $('#selected-row-logger').addClass('j-dn');
        $('#selected-row-table').addClass('j-dn');
    }
    function initEventsListeners () {
        $('#show-table').click(function () {
            openTableLog ();
        });
        $('#show-log').bind('click', function () {
//            openLog ();
            alert(2)
        });
        $('#clear-log').click(function () {
            clearLog ();
        });
        $('#w-128').click(function () {
            closeSelected ();
        });
        $('.w-128').on('click',function () {
            $("#testingLog").hide();
            $("#selected-testingLog").hide();
        });
        $('.w-128').click(function () {
            $("#testingLog").hide();
            $("#selected-testingLog").hide();
        });
    }

    //(level,method, status, msg){
    //LOG_LEVEL.ERROR, 'init', 0, "navigator.geolocation is null"
    function save( logLevel, method, location, message) {
        var logLevel_ = logLevel == null ? '-' : logLevel.key;
        location = location == null ? '-' : location;
        method = method == null ? '-' : method;
        message = message == null ? '-' : message;


        $.ajax({
            url: 'javascript_logger_action.htm',
            dataType: "json",
            type: "post",
            data: {
                logLevel: logLevel_,
                location: location,
                method: method,
                message: message
            },
            success: function (data) {
            }
        });

    }
};
$(document).ready(function () {
    (function () {
        var dok = new GENERAL_API.ACTION.LOGS();
        var id1 = 'kk-ll',
            name = 'soxak',
            col = ['Index', 'kuku', 'tuyyyyyyytu', 'vovo'],
            p1 = [1, 'dfghj', 'mnbvc', 'jjj jjj'],
            p2 = [2, 'dfghj', 'mnbvc', 'jjj jjj'],
            p3 = [3, 'dfghj', 'mnbvc', 'jjj jjj'],
            p4 = [4, 'dfghj', 'mnbvc', 'jjj jjj'],
            p5 = [5, 'dfghj', 'mnbvc', 'jjj jjj'];
        dok.createTable(id1, name, col);
        dok.tableLogger(id1, p1);
        dok.tableLogger(id1, p2);
        dok.tableLogger(id1, p3);
        dok.tableLogger(id1, p4);
        dok.tableLogger(id1, p5);

        dok.logger(1, 'oooooo');
        dok.logger(2, 'ssssssssssss');
        dok.logger(3, 'dddddddddddd');
        dok.logger(4, 'vvvvvvvvvvvv');
        dok.logger(5, 'vvvvvvvvvvvv');
    }());
});