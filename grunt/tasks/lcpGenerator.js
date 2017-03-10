module.exports = function(grunt) {

	grunt.registerMultiTask('lcpGenerator', 'lcp generator', function () {
        grunt.log.write('lcp Generator start...').ok();
        var fs = require('fs'),
			done = this.async();

		//our Gruntfile changes the directory to war
		var source = fs.readFileSync('./grunt/lcpTemplate.handlebars', 'utf8'),
			template = require('handlebars').compile(source.toString());
		grunt.util.async.forEach(this.files, function (filePair, filePairDone) {
            var destination = filePair.dest,
				messages = grunt.file.expandMapping(filePair.messages);
			// Get translated message values from message properties
			grunt.util.async.forEach(messages, function (relativeFilePath, fileBuildDone) {
				var content = grunt.file.read(relativeFilePath.src[0], 'utf8'),
					filename = getFilename(relativeFilePath.src[0]),
				 	fields = getFields(content);
				grunt.file.write(destination + '/lcp' + filename + '.js', getContent(template, content, filename, fields), 'utf8');
				fileBuildDone();
			}, filePairDone);
		}, done);
	});

    var messages_dest = './../wallet/src/main/java/com/connectto/wallet/model/wallet/lcp';

	function getFilename(filename) {
		return filename.replace(messages_dest, '').replace('.java', '') ;
	}

	function getFields(content) {
		var fields = [],
 			lines = content.split('\n');

		for(var i = 0;i < lines.length;i++){
			var line = lines[i].trim();
			if( line.indexOf("this")==0){
				line = line.split('=')[0].replace('this.','').trim();
				fields.push(line);
			}
		}
		return fields;
	}

	function getContent(template, content, filename, fields) {
		content = content.replace('public enum','const');
		var msgKeys = {},
			enums = [],
			name = filename.replace('/', ''),
			lines = content.split('\n');
		for(var i = 0;i < lines.length; i++){
			var line = lines[i].trim();
			var data = [];
			if( line.length > 0 &&
				line.indexOf("#")!=0 &&
				line.indexOf("public")!=0 &&
				line.indexOf("private")!=0 &&
				line.indexOf("return")!=0 &&
				line.indexOf("}")!=0 &&
				line.indexOf("for")!=0 &&
				line.indexOf("if")!=0 &&
				line.indexOf("this")!=0 &&
				line.indexOf("/**")!=0 &&
				line.indexOf("*")!=0 &&
				line.indexOf("package")!=0 &&
				line.indexOf("continue")!=0 &&
				line.indexOf("const")!= 0&&
				line.indexOf("final")!= 0&&
				line.indexOf("add")== -1&&
				line.indexOf(name)!=0 &&
				line.indexOf("import")!=0
			){
				line = line.replace("),","").replace(");","").trim();
				var values_string = line.substr(line.indexOf('(')+1);
				var values =  values_string.split(',');

				for (var prop in fields) {
					if (values.hasOwnProperty(prop)) {
						data.push({
							key: fields[prop],
							value: values[prop]
						});
					}
				}
				enums.push({
					name:  line.split('(')[0].trim(),
					value: data
				});

			}
			//code here using lines[i] which will give you each line
		}
		msgKeys.keys = enums;
		msgKeys.filename = name;
		return template({
			msgKeys: msgKeys
		});
	}
};