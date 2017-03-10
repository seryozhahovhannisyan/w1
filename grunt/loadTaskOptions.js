module.exports = function loadTaskOptions(filePath, grunt) {
	var path = require('path');
	var glob = require('glob');
	var object = {};
	var key;

	var baseFilePath = path.join(process.cwd(), filePath);
	glob.sync(baseFilePath + '*.js', {cwd: filePath}).forEach(function(optionFilePath) {
		console.log('baseFilePath',baseFilePath)
		key = optionFilePath.match(/(\w*)\.js$/)[1];
		var required = require(optionFilePath);
		if (typeof required == "function") {
			object[key] = required(grunt);
		} else {
			object[key] = required;
		}
	});

	return object;
};
