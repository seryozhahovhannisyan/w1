module.exports = function(grunt) {
	grunt.registerMultiTask('messageGenerator', 'i18n message generator', function () {
        grunt.log.write('msg Generator start...').ok();
        var fs = require('fs'),
			done = this.async();

		//our Gruntfile changes the directory to war
		var source = fs.readFileSync('./grunt/i18nTemplate.handlebars', 'utf8'),
			template = require('handlebars').compile(source.toString());
		console.log('this.files', this.files);
		grunt.util.async.forEach(this.files, function (filePair, filePairDone) {
            var destination = filePair.dest,
				messages = grunt.file.expandMapping(filePair.messages),
				msgKeys = [],
				msgKeyLocations = {};


			// Extract all the message keys from the messages properties
			grunt.util.async.forEach(messages, function (relativeFilePath, fileBuildDone) {
				var content = grunt.file.read(relativeFilePath.src[0], 'utf8'),
                    keys= extractMsgKeys(content),
					i = keys.length;
				// Keep msgKeys unique
				while (i--) {
					if (msgKeys.indexOf(keys[i]) === -1) {
						msgKeys.push(keys[i]);
					}
					msgKeyLocations[keys[i]] = msgKeyLocations[keys[i]] || [];
					//msgKeyLocations[keys[i]].push(relativeFilePath.src[0].replace('static/js/amd/angularModules/', ''));
				}
				fileBuildDone();
			}, filePairDone);

			// Get translated message values from message properties
			grunt.util.async.forEach(messages, function (relativeFilePath, fileBuildDone) {
				var content = grunt.file.read(relativeFilePath.src[0], 'utf8'),
					mapping = {};
				msgKeys.forEach(function (key) {
                    try{
						mapping[key] = extractMessageValue(content, key);
					} catch (e){
						console.log('error',e)
					}
				});

                var locale = getLocale(relativeFilePath.src[0]);
				console.log('locale',locale)
				grunt.file.write(destination + '/i18n' + locale + '.js', getContent(template, mapping, msgKeyLocations, locale), 'utf8');
				fileBuildDone();
			}, filePairDone);
		}, done);
	});

    var RX_QUOTE = /"/g;
    var messages_dest = './../wallet/src/main/resources/message';

    function extractMsgKeys(content) {
        var keys = [];

        var lines = content.split('\n');
        for(var i = 0;i < lines.length;i++){
            var line = lines[i].trim();
            if(line.indexOf("#")!=0 ){
                keys.push(line.split('=')[0]);
            }
            //code here using lines[i] which will give you each line
        }
        return keys;
    }

    function extractMessageValue(content, key) {
		var value = null;
		content.replace(new RegExp('^\\s*(' + key + ')\\s*=(.*)$', 'gm'), function (match, key, val) {
			value = val.replace(RX_QUOTE, '\\"');
		});
		return value;
	}

	function getLocale(filename) {
		return filename.replace(messages_dest, '').replace('.properties', '') || '_en';
	}

	function getContent(template, mapping, msgKeyLocations, locale) {
		var msgKeys = {};
		var keys = [];

		for (var prop in mapping) {
			if (mapping.hasOwnProperty(prop)) {
				keys.push({
					key: prop,
					value: mapping[prop],
					uses: msgKeyLocations[prop].join(', ')
				});
			}
		}

		msgKeys.keys = keys;
		msgKeys.locale = locale;
		return template({
			msgKeys: msgKeys
		});
	}
};