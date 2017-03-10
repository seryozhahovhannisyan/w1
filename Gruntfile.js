module.exports = function(grunt) {
	//automatically load in any tasks that match grunt- (the ones from the package json)
	require('load-grunt-tasks')(grunt);
	//a nice little tool that times our runs
	if (!grunt.option('no-time')) {
		require('time-grunt')(grunt);
	}

	//reads in the version from the attask pom (like 1.0-SNAPSHOT), so we know where the generated assets go,
	//this creates a global variable in the node process called global.version (so some exported configs can utilize it)
	//require('./grunt/version.js')(grunt);

	//load grunt properties from <developer>.properties file and cache them
	//require('./grunt/gruntProperties.js').init(grunt);

	// Project configuration, merging the config from grunt/tasks/options and the object below via a helper function
    grunt.initConfig(grunt.util._.extend(require('./grunt/loadTaskOptions.js')('./grunt/tasks/options/'), {
		pkg: grunt.file.readJSON('package.json')//,
		//this can't be moved to grunt/tasks because of tileLESSMap which it shares with the other task
		//less: lessTaskConfig
	}));

	//require('./grunt/css/compileLESS.js')(grunt, lessTaskConfig);

	//load in all of the js files/tasks from grunt/tasks
	grunt.loadTasks('grunt/tasks');

    //require('./grunt/registerTaskOptions.js')(grunt);
    grunt.registerTask('default', ['clean', 'concat', 'messageGenerator', 'lcpGenerator']);
};