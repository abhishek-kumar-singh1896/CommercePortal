module.exports = function(grunt) {
  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    watch: {
        less: {
            files: ['web/webroot/WEB-INF/_ui-src/shared/less/variableMapping.less','web/webroot/WEB-INF/_ui-src/shared/less/generatedVariables.less',
                    'web/webroot/WEB-INF/_ui-src/responsive/lib/ybase-*/less/*', 'web/webroot/WEB-INF/_ui-src/**/themes/**/less/*.less'],
            tasks: ['less'],
        },
        fonts: {
            files: ['web/webroot/WEB-INF/_ui-src/**/themes/**/fonts/*'],
            tasks: ['sync:syncfonts'],
        },
        ybasejs: {
            files: ['web/webroot/WEB-INF/_ui-src/responsive/lib/ybase-0.1.0/js/**/*.js'],
            tasks: ['sync:syncybase'],
        },
        jquery: {
            files: ['web/webroot/WEB-INF/_ui-src/responsive/lib/jquery*.js'],
            tasks: ['sync:syncjquery'],
        },
        css: {
			files: ['web/webroot/WEB-INF/_ui-src/**/themes/**/*.scss'],
			tasks: ['sass']
		}
    },
    less: {
        default: {
            files: [
                {
                    expand: true,
                    cwd: 'web/webroot/WEB-INF/_ui-src/',
                    src: '**/themes/**/less/style.less',
                    dest: 'web/webroot/_ui/',
                    ext: '.css',
                    rename:function(dest,src){
                       var nsrc = src.replace(new RegExp("/themes/(.*)/less"),"/theme-$1/css");
                       return dest+nsrc;
                    }
                }
            ]
        },
    },

    sync : {
    	syncfonts: {
    		files: [{
                expand: true,
    			cwd: 'web/webroot/WEB-INF/_ui-src/',
    			src: '**/themes/**/fonts/**',
    			dest: 'web/webroot/_ui/',
    			rename:function(dest,src){
                	var nsrc = src.replace(new RegExp("/themes/(.*)"),"/theme-$1");
                	return dest+nsrc;
             }
    		}]
    	},
    	syncybase: {
    		files: [{
    			cwd: 'web/webroot/WEB-INF/_ui-src/responsive/lib/ybase-0.1.0/js/',
    			src: '**/*.js',
    			dest: 'web/webroot/_ui/responsive/common/js',
    		}]
    	},
    	syncjquery: {
    		files: [{
    			cwd: 'web/webroot/WEB-INF/_ui-src/responsive/lib/common',
    			src: '*.js',
    			dest: 'web/webroot/_ui/responsive/common/js',
    		}]
    	},
    	syncythemejs: {
    		files: [{
                expand: true,
                cwd: 'web/webroot/WEB-INF/_ui-src/',
                src: '**/themes/**/js/**/*.js',
                dest: 'web/webroot/_ui/',
                ext: '.js',
                rename:function(dest,src){
                   var nsrc = src.replace(new RegExp("/themes/(.*)/js"),"/theme-$1/js");
                   return dest+nsrc;
                }
            }]
    	},
    	synccustomjs: {
    		files: [{
    			cwd: 'web/webroot/WEB-INF/_ui-src/responsive/lib/custom',
    			src: '*.js',
    			dest: 'web/webroot/_ui/responsive/common/js/custom',
    		}]
    	},
    },
    
    sass: {
        dist: {
            files: [
                    {
                        expand: true,
                        cwd: 'web/webroot/WEB-INF/_ui-src/',
                        src: '**/themes/**/scss/theme.scss',
                        dest: 'web/webroot/_ui/',
                        ext: '.css',
                        rename:function(dest,src){
                           var nsrc = src.replace(new RegExp("/themes/(.*)/scss"),"/theme-$1/css");
                           return dest+nsrc;
                        }
                    }
                ]
        }
    },
    
    autoprefixer: {
        dist:{
          files: [{
        		  expand: true,
        		  cwd: 'web/webroot/_ui/',
        		  src: 'responsive/**/css/theme.css',
        		  dest: 'web/webroot/_ui/',
        		  rename:function(dest,src){
        			  var nsrc = src.replace(new RegExp("responsive/theme-(.*)/css"),"responsive/theme-$1/css");
        			  return dest+nsrc;
        		  }
          }]
        }
    }
});
 
  // Plugins
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-sync');
  grunt.loadNpmTasks('node-sass');
  grunt.loadNpmTasks('grunt-sass');
  grunt.loadNpmTasks('grunt-autoprefixer');

  // Default task(s).
  grunt.registerTask('default', ['less', 'sync', 'sass', 'autoprefixer']);



};