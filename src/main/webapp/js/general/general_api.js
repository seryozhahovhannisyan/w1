/**
 * General API.
 */

var GENERAL_API = GENERAL_API || {};
GENERAL_API.createNameSpace = function (namespace) {
    var nsparts = namespace.split(".");
    var parent = GENERAL_API;

    if (nsparts[0] === 'GENERAL_API') {
        nsparts = nsparts.slice(1);
    }

    for (var i = 0; i < nsparts.length; i++) {
        var partname = nsparts[i];
        if (typeof parent[partname] === "undefined") {
            parent[partname] = {};
        }
        parent = parent[partname];
    }
    // the parent is now constructed with empty namespaces and can be used.
    // we return the outermost namespace
    return parent;
};