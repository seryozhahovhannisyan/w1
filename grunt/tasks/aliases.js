module.exports = function (grunt) {

	//watch tasks
	grunt.registerTask('i18n', ['messageGenerator', 'concat:i18n']);

};