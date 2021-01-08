/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
ACC.gallaghercustomerticketaddon = {

	_autoload: [
		"onStatusChange",
		"bindMessageArea",
		"toggleAllMessages",
		"postNewMessage",
		"onFileChosen",
		"bindTicketAddActions",
		"bindTicketUpdateActions"
	],


	disableMessage: function(_this){
		var currentTicketStatus = $('input[id="currentTicketStatus"]').val();
		var selectedStatus = $(_this).val();

		if((currentTicketStatus === 'COMPLETED' && selectedStatus === 'COMPLETED') || (currentTicketStatus === 'CLOSED' && selectedStatus === 'CLOSED')) {
			$('textarea[id="message"]').attr('disabled','disabled');
			$('button[id="updateTicket"]').attr('disabled','disabled');
		} else {
			$('textarea[id="message"]').removeAttr('disabled');
		}
	},

	onStatusChange: function () {
        $(document).on('change', '.js-add-message-status', function () {
            ACC.gallaghercustomerticketaddon.disableMessage(this);
		});
	},

    onFileChosen: function () {
        $(document).on('change', '#supportTicketForm input[name=files]', function () {
        	$('#addTicket').prop('disabled',false);
        	$("#supportTicketForm").find(".js-file-upload__file-name").html("");
            ACC.gallaghercustomerticketaddon.clearAlerts();
            var isValidUpload = true;
            var selectedFile = document.getElementById('attachmentFiles');
            if (!ACC.gallaghercustomerticketaddon.isSelectedFilesValid(selectedFile))
            {
                var message = "<span style='color:red'>" + $('#file-too-large-message').text() + "</span>";
                $("#supportTicketForm").find(".js-file-upload__file-name").html(message);
                isValidUpload = false;
                $('#fileUploadStatus').val(isValidUpload);
                $('#addTicket').prop('disabled',true);
                return isValidUpload;
            } 
            
            if (ACC.gallaghercustomerticketaddon.isSelectedFilesFormatValid(selectedFile) != undefined)
            {
            	
                var fileFormats = ACC.gallaghercustomerticketaddon.isSelectedFilesFormatValid(selectedFile);
                var extension = "";
                
                for (var i = 0; i < selectedFile.files.length; ++i){
                	
                    var path = selectedFile.files[i].name;
                    var basename = path.split(/[\\/]/).pop(),  
                    pos = basename.lastIndexOf(".");        
                    
                    if (basename === "" || pos < 1) {
                    	extension = "";
                    }
                    
                    extension = basename.slice(pos + 1);
                    
                    if(fileFormats.includes(extension)){
                        break;
                    }
                }
                
                if(fileFormats.includes(extension)){
                    var message = "<span style='color:red'>" + extension +" "+ $('#file-format-message').text() + "</span>";
                    $("#supportTicketForm").find(".js-file-upload__file-name").html(message);
                    isValidUpload = false;
                    $('#fileUploadStatus').val(isValidUpload);
                    $('#addTicket').prop('disabled',true);
                    return isValidUpload;
                }

            }
            
            if(ACC.gallaghercustomerticketaddon.isTotalSelectedFilesValid(selectedFile) >0){
            	var totalFiles = ACC.gallaghercustomerticketaddon.isTotalSelectedFilesValid(selectedFile);
            	var message = "";
            	var limit = $('.total-Uploaded-Files-limit').data('max-files-upload');
            	
            	if(totalFiles > limit){
            		message =  "<span style='color:red'>" + $('#file-upload-limit-message').text() + "</span>";
            		$("#supportTicketForm").find(".js-file-upload__file-name").html(message);
               	    isValidUpload = false;
               	    $('#addTicket').prop('disabled',true);
               	    $('#fileUploadStatus').val(isValidUpload);
               	    return isValidUpload;
            	} else{
            		message = totalFiles+" Files Attached";
            		$("#supportTicketForm").find(".js-file-upload__file-name").html(message);
               	    isValidUpload = true;
               	    $('#addTicket').prop('disabled',false);
               	    $('#fileUploadStatus').val(isValidUpload);
               	    return isValidUpload;
            	}
            }
            
        });
    },

    bindMessageArea: function () {
        $(document).on('keyup', '.js-add-message', function () {
            if($(this).val().length > 0) {
                $('button[id="updateTicket"]').removeAttr('disabled');
                $('#NotEmpty-supportTicketForm-message').hide();
            } else {
                $('button[id="updateTicket"]').attr('disabled','disabled');
            }
        });
    },

	toggleAllMessages: function() {
		$('#ct-toggle-all-messages').on('click touchstart', function() {
			$('.cts-msg-history-item:not(.ct-msg-visible)').show();
			$(this).hide();
		});
	},

	postNewMessage: function () {
		var title = $('#ct-overlay-title').html();
		$('.ct-add-new-msg-btn').on('click touchstart', function(e) {
			e.preventDefault();
			$.colorbox({
				href: "#ct-add-new-msg",
				maxWidth:"100%",
				width: 525,
				opacity:0.7,
				title: title,
				inline: true,
                close: '<span class="glyphicon glyphicon-remove"></span>',
                onOpen: function () {
                    $('#ct-add-new-msg').fadeIn();                   
                },
                onComplete: function () {                	
                    ACC.gallaghercustomerticketaddon.disableMessage($('.js-add-message-status'));
                    
                    if (!$.trim($("#message").val())) {
                    	  $('button[id="updateTicket"]').attr('disabled', 'disabled');
                    }                                    
                  
                    ACC.csvimport.changeFileUploadAppearance();
                },
                onCleanup: function () {       	
                  $('#ct-add-new-msg').hide();
                }
            });
        });
    },

    isSelectedFilesValid: function (selectedFiles) {
        if (window.File && window.Blob) {
            var fileMaxSize = $('.js-file-upload__input').data('max-upload-size');
            var totalSize = 0;
            
            for (var i = 0; i < selectedFiles.files.length; ++i){
            	var totalSize = selectedFiles.files[i].size;
                if ($.isNumeric(fileMaxSize) && totalSize > parseFloat(fileMaxSize)) {
                    return false;
                }
            }
        }

        return true;
    },
    
    isTotalSelectedFilesValid: function (selectedFiles) {
    	var totalFiles = selectedFiles.files.length;
    	
    	return totalFiles;
    },
    isSelectedFilesFormatValid: function (selectedFiles) {
    	var fileFormats = $('.invalid-Uploaded-Formats').data('invalid-formats').split(",");

        return fileFormats;
    },

    displayCustomerTicketingAlert: function (options) {
        var alertTemplateSelector;

        switch (options.type) {
            case 'error':
                alertTemplateSelector = '#global-alert-danger-template';
                break;
            case 'warning':
                alertTemplateSelector = '#global-alert-warning-template';
                break;
            default:
                alertTemplateSelector = '#global-alert-info-template';
        }

        if (typeof options.message !== 'undefined') {
            $('#customer-ticketing-alerts').append($(alertTemplateSelector).tmpl({message: options.message}));
        }

        if (typeof options.messageId !== 'undefined') {
            $('#customer-ticketing-alerts').append($(alertTemplateSelector).tmpl({message: $('#' + options.messageId).text()}));
        }
    },

    displayGlobalAlert: function (options) {
        var alertTemplateSelector;

        switch (options.type) {
            case 'error':
                alertTemplateSelector = '#global-alert-danger-template';
                break;
            case 'warning':
                alertTemplateSelector = '#global-alert-warning-template';
                break;
            default:
                alertTemplateSelector = '#global-alert-info-template';
        }

        if (typeof options.message !== 'undefined') {
            $('#global-alerts').append($(alertTemplateSelector).tmpl({message: options.message}));
        }

        if (typeof options.messageId !== 'undefined') {
            $('#global-alerts').append($(alertTemplateSelector).tmpl({message: $('#' + options.messageId).text()}));
        }
    },

    bindTicketAddActions: function () {
        $(document).on('click', '#addTicket',
            function (event) {
                event.preventDefault();
                $('#addTicket').prop('disabled',true);
                ACC.gallaghercustomerticketaddon.formPostAction("support-tickets?ticketAdded=true");
            });
    },

    bindTicketUpdateActions: function () {
        $(document).on('click', '#updateTicket',
            function (event) {        	
                event.preventDefault();
                $('#updateTicket').prop('disabled',true);
                ACC.gallaghercustomerticketaddon.formPostAction('?ticketUpdated=true');
            });
    },

    formPostAction: function (successRedirectUrl) {

        ACC.gallaghercustomerticketaddon.clearAlerts();

        var form = document.getElementById("supportTicketForm");
        var formData = new window.FormData(form);

        var selectedFile = document.getElementById('attachmentFiles');
        if (!ACC.gallaghercustomerticketaddon.isSelectedFilesValid(selectedFile)) {
            ACC.gallaghercustomerticketaddon.displayCustomerTicketingAlert({
                type: 'error',
                messageId: 'attachment-file-max-size-exceeded-error-message'
            });
            return;
        }

        $.ajax({
            url: form.action,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function () {
                window.location.replace(successRedirectUrl);
            },
            error: function (jqXHR) {
                $(form).find(':submit').prop('disabled',false);
                ACC.gallaghercustomerticketaddon.processErrorResponse(jqXHR);
            }
        });
    },

    processErrorResponse: function (jqXHR) {
        ACC.gallaghercustomerticketaddon.clearAlerts();
        if (jqXHR.status === 400 && jqXHR.responseJSON) {

            $.each(jqXHR.responseJSON, function() {
                $.each(this, function(k, v) {
                    var target = '#' + k;
                    $(target).show();
                    $(target).text(v);
                    if (k === 'NotEmpty-supportTicketForm-subject'
                        || k === 'Size-supportTicketForm-subject'
                        || k === 'NotEmpty-supportTicketForm-message'
                        || k === 'Size-supportTicketForm-message') {
                        ACC.gallaghercustomerticketaddon.addHasErrorClass();
                    }
                    else {
                        ACC.gallaghercustomerticketaddon.displayGlobalAlert({type: 'error', message: v});
                    }
                });
            });

            return;
        }

       // ACC.gallaghercustomerticketaddon.displayCustomerTicketingAlert({type: 'error', messageId: 'supporttickets-tryLater'});
    },

    addHasErrorClass: function () {
        $('#createTicket-message').parent().addClass('has-error');
    },

    clearAlerts: function () {
        $('#customer-ticketing-alerts').empty();
        $('#global-alerts').empty();
        $('#NotEmpty-supportTicketForm-subject').hide();
        $('#Size-supportTicketForm-message').hide();
        $('#Size-supportTicketForm-subject').hide();
        $('#createTicket-subject').parent().removeClass('has-error');
        $('#NotEmpty-supportTicketForm-message').hide();
        $('#createTicket-message').parent().removeClass('has-error');
    }
};
