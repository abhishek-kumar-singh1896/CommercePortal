(function($){
    
	var opts = {},
		default_opts = {
			url: '',
			refresh: 1000,
			paramname: 'userfile',
			maxfiles: 25,
			maxfilesize: 1, // MBs
			data: {},
			drop: empty,
			dragEnter: empty,
			dragOver: empty,
			dragLeave: empty,
			docEnter: empty,
			docOver: empty,
			docLeave: empty,
			error: function(err, filename){alert(err);},
			uploadStarted: empty,
			uploadFinished: empty,
			progressUpdated: empty,
			speedUpdated: empty
		},
		errors = ["BrowserNotSupported", "TooManyFiles", "FileTooLarge"],
		doc_leave_timer,
		stop_loop = false;
	     
	$.fn.filedrop = function(options) {
        opts = $.extend( {}, default_opts, options );
        
        this.get(0).addEventListener("drop", drop, true);
        this.bind('dragenter', dragEnter).bind('dragover', dragOver).bind('dragleave', dragLeave);
        
        document.addEventListener("drop", docDrop, true);
        $(document).bind('dragenter', docEnter).bind('dragover', docOver).bind('dragleave', docLeave);
	};
     
    function drop(e) {
     	opts.drop(e);
        upload(e.dataTransfer.files);
        e.preventDefault();
        return false;
    }
	
	function upload(files) {
		stop_loop = false;
		if (!files) {
            opts.error(errors[0]);
            return false;
        }
        
        var len = files.length;
	    
	    if (len > opts.maxfiles) {
            opts.error(errors[1]);
            return false;
        }

		var i = 0;
	    function up(i){
	    	if (stop_loop) return false;
			try {
		    	if (i === len) return;
				var reader = new FileReader(),
					max_file_size = 1048576 * opts.maxfilesize;
					
		    	reader.index = i,
		    	reader.file = files[i],
		    	reader.len = len;
		    	if (reader.file.size > max_file_size) {
		    		opts.error(errors[2], reader.file);
		    		return false;
		    	}
		    	
	    		reader.addEventListener("loadend", send, false);
		    	reader.readAsBinaryString(files[i]);
	    	} catch(err) {
	    		opts.error(errors[0]);
	    		return false;
	    	}
	    }
	    
	    function send(e) {
		    var xhr = new XMLHttpRequest(),
		    	upload = xhr.upload,
		    	file = e.target.file,
		    	index = e.target.index,
		    	start_time = new Date().getTime(),
		    	boundary = '------multipartformboundary' + (new Date).getTime(),
		    	mime = file.type;
		    	builder = getBuilder(file.name, e.target.result, mime, boundary);
			
			upload.index = index;
		    upload.file = file;
		    upload.downloadStartTime = start_time;
	        upload.currentStart = start_time;
	        upload.currentProgress = 0;
	        upload.startData = 0;
		    upload.addEventListener("progress", progress, false);
			
		    xhr.open("POST", opts.url, true);
		    xhr.setRequestHeader('content-type', 'multipart/form-data; boundary=' 
		        + boundary);
			
		    if(!xhr.sendAsBinary){
	              xhr.sendAsBinary = function(datastr) {
	                  function byteValue(x) {
	                      return x.charCodeAt(0) & 0xff;
	                  }
	                  var ords = Array.prototype.map.call(datastr, byteValue);
	                  var ui8a = new Uint8Array(ords);
	                  this.send(ui8a.buffer);
	              }
	          }
	          
	        xhr.sendAsBinary(builder);  
	
		    opts.uploadStarted(index, file, e.target.len);  
		    
		    xhr.onload = function() {
	            var serverResponse = null;

	            if (xhr.responseText) {
	              try {
	                serverResponse = jQuery.parseJSON(xhr.responseText);
	              }
	              catch (e) {
	                serverResponse = xhr.responseText;
	              }
	            }

	            var now = new Date().getTime(),
	                timeDiff = now - start_time,
	                result = opts.uploadFinished(index, file, serverResponse, timeDiff, xhr);
	            filesDone++;

	            // Remove from processing queue
	            processingQueue.forEach(function(value, key) {
	              if (value === fileIndex) {
	                processingQueue.splice(key, 1);
	              }
	            });

	            // Add to donequeue
	            doneQueue.push(fileIndex);

	            // Make sure the global progress is updated
	            global_progress[global_progress_index] = 100;
	            globalProgress();

	            if (filesDone === (files_count - filesRejected)) {
	              afterAll();
	            }
	            if (result === false) {
	              stop_loop = true;
	            }


	          // Pass any errors to the error option
	          if (xhr.status < 200 || xhr.status > 299) {
	            opts.error(xhr.statusText, file, fileIndex, xhr.status);
	          }
	        };
		}
		up(i);	
	}
	
	
	function getBuilder(filename, filedata, mime, boundary) {
		var dashdash = '--',
	    	crlf = '\r\n',
	    	builder = '',
	        paramname = opts.paramname;

		if (opts.data) {
	        var params = $.param(opts.data).replace(/\+/g, '%20').split(/&/);

	        $.each(params, function() {
	          var pair = this.split("=", 2),
	              name = decodeURIComponent(pair[0]),
	              val  = decodeURIComponent(pair[1]);

	          if (pair.length !== 2) {
	              return;
	          }

	          builder += dashdash;
	          builder += boundary;
	          builder += crlf;
	          builder += 'Content-Disposition: form-data; name="' + name + '"';
	          builder += crlf;
	          builder += crlf;
	          builder += val;
	          builder += crlf;
	        });
	      }
        
        builder += dashdash;
	    builder += boundary;
	    builder += crlf;
        builder += 'Content-Disposition: form-data; name="'+opts.paramname+'"';
        builder += '; filename="' + filename + '"';
        builder += crlf;
        
        builder += 'Content-Type: ' + mime;
        builder += crlf;
        builder += crlf; 

        builder += filedata;
        builder += crlf;

        /*builder += dashdash;
        builder += boundary;
        builder += crlf;*/
        
	    builder += dashdash;
	    builder += boundary;
	    builder += dashdash;
	    builder += crlf;
	    return builder;
	}

    function progress(e) {
        if (e.lengthComputable) {
            var percentage = Math.round((e.loaded * 100) / e.total);
            if (this.currentProgress != percentage) {

                this.currentProgress = percentage;
                opts.progressUpdated(this.index, this.file, this.currentProgress);

                var elapsed = new Date().getTime();
                var diffTime = elapsed - this.currentStart;
                if (diffTime >= opts.refresh) {
                    var diffData = e.loaded - this.startData;
                    var speed = diffData / diffTime; // KB per second
                    opts.speedUpdated(this.index, this.file, speed);
                    this.startData = e.loaded;
                    this.currentStart = elapsed;
                }
            }
        }
    }
    
    
	function dragEnter(e) {
		clearTimeout(doc_leave_timer);
		e.preventDefault();
	    opts.dragEnter(e);
	}
	
	function dragOver(e) {
	 	clearTimeout(doc_leave_timer);
	    e.preventDefault();
	    opts.docOver(e);
	    opts.dragOver(e);
	}
	 
	function dragLeave(e) {
	 	clearTimeout(doc_leave_timer);
	 	opts.dragLeave(e);
	 	e.stopPropagation();
	}
	 
	function docDrop(e) {
	    e.preventDefault();
	    opts.docLeave(e);
	    return false;
	}
	 
	function docEnter(e) {
	 	clearTimeout(doc_leave_timer);
	    e.preventDefault();
	    opts.docEnter(e);
	    return false;
	}
	 
	function docOver(e) {
	 	clearTimeout(doc_leave_timer);
	    e.preventDefault();
	    opts.docOver(e);
	    return false;
	}
	 
	function docLeave(e) {
	 	doc_leave_timer = setTimeout(function(){
	 		opts.docLeave(e);
	 	}, 200);
	}
	 
	function empty(){}
	
	try {
	    if (XMLHttpRequest.prototype.sendAsBinary) {
	        return;
	    }
	    XMLHttpRequest.prototype.sendAsBinary = function(datastr) {
	      function byteValue(x) {
	        return x.charCodeAt(0) & 0xff;
	      }
	      var ords = Array.prototype.map.call(datastr, byteValue);
	      var ui8a = new Uint8Array(ords);

	      // Not pretty: Chrome 22 deprecated sending ArrayBuffer, moving instead
	      // to sending ArrayBufferView.  Sadly, no proper way to detect this
	      // functionality has been discovered.  Happily, Chrome 22 also introduced
	      // the base ArrayBufferView class, not present in Chrome 21.
	      if ('ArrayBufferView' in window)
	        this.send(ui8a);
	      else
	        this.send(ui8a.buffer);
	    };
	  } catch (e) {}
     
})(jQuery);