/*
 * Copyright (C) 2009-2012 Antelink SAS
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License Version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License Version 3
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License Version
 * 3 along with this program. If not, see http://www.gnu.org/licenses/agpl.html
 *
 * Additional permission under GNU AGPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it with
 * Eclipse Java development tools (JDT) or Jetty (or a modified version of these
 * libraries), containing parts covered by the terms of Eclipse Public License 1.0,
 * the licensors of this Program grant you additional permission to convey the
 * resulting work. Corresponding Source for a non-source form of such a combination
 * shall include the source code for the parts of Eclipse Java development tools
 * (JDT) or Jetty used as well as that of the covered work.
 */
/*
 * @Name :         QapTcha - jQuery Plugin
 * @Revison :      4.1
 * @Date :     07/03/2012  - dd/mm/YYYY
 * @Author:       ALPIXEL Agency - (www.myjqueryplugins.com - www.alpixel.fr) 
 * @License :     Open Source - MIT License : http://www.opensource.org/licenses/mit-license.php
 */  
 
jQuery.QapTcha = {
  build : function(options)
  {
        var defaults = {
      txtLock : 'Locked : form can\'t be submited',
      txtUnlock : 'Unlocked : form can be submited',
      disabledSubmit : true,
      autoRevert : true,
      PHPfile : 'Qaptcha.jquery.php',
      autoSubmit : false
        };   
    
    if(this.length>0)
    return jQuery(this).each(function(i) {
      /** Vars **/
      var 
        opts = $.extend(defaults, options),      
        $this = $(this),
        form = $('form').has($this),
        Clr = jQuery('<div>',{'class':'clr'}),
        bgSlider = jQuery('<div>',{'class':'bgSlider'}),
        Slider = jQuery('<div>',{'class':'Slider'}),
        TxtStatus = jQuery('<div>',{'class':' TxtStatus dropError',text:opts.txtLock}),
        inputQapTcha = jQuery('<input>',{name:generatePass(32),value:generatePass(7),type:'hidden'});
      
      /** Disabled submit button **/
      if(opts.disabledSubmit) form.find('input[type=\'submit\']').attr('disabled','disabled');
      
      /** Construct DOM **/
      bgSlider.appendTo($this);
      Clr.insertAfter(bgSlider);
      TxtStatus.insertAfter(Clr);
      inputQapTcha.appendTo($this);
      Slider.appendTo(bgSlider);
      $this.show();
      
      Slider.draggable({ 
        revert: function(){
          if(opts.autoRevert)
          {
            if(parseInt(Slider.css("left")) > 150) return false;
            else return true;
          }
        },
        containment: bgSlider,
        axis:'x',
        stop: function(event,ui){
          if(ui.position.left > 150)
          {
            // set the SESSION iQaptcha in PHP file
            $.post(opts.PHPfile,{
              action : 'qaptcha',
              qaptcha_key : inputQapTcha.attr('name')
            },
            function(data) {
              if(!data.error)
              {
                Slider.draggable('disable').css('cursor','default');
                inputQapTcha.val('');
                TxtStatus.text(opts.txtUnlock).addClass('dropSuccess').removeClass('dropError');
                form.find('input[type=\'submit\']').removeAttr('disabled');
                if(opts.autoSubmit) form.find('input[type=\'submit\']').trigger('click');
              }
            },'json');
          }
        }
      });
      
      function generatePass(nb) {
            var chars = 'azertyupqsdfghjkmwxcvbn23456789AZERTYUPQSDFGHJKMWXCVBN_-#@';
            var pass = '';
            for(i=0;i<nb;i++){
                var wpos = Math.round(Math.random()*chars.length);
                pass += chars.substring(wpos,wpos+1);
            }
            return pass;
        }
      
    });
  }
}; jQuery.fn.QapTcha = jQuery.QapTcha.build;



$.fn.clearForm = function() {
  return this.each(function() {
    var type = this.type, tag = this.tagName.toLowerCase();
    if (tag == 'form')
      return $(':input',this).clearForm();
    if (type == 'text' || type == 'password' || tag == 'textarea'|| type == 'email'|| type == 'url')
      this.value = '';
    else if (type == 'checkbox' || type == 'radio')
      this.checked = false;
    else if (tag == 'select')
      this.selectedIndex = -1;
  });
};

$(function() {

/* Jquery Form Captcha */

    $('.QapTcha').QapTcha({
        txtLock     : 'Security: slide right to unlock the form',
        txtUnlock   : 'Unlock: You can send the form',
		autoRevert  : true,
        PHPfile     : '/service/check/captcha'
    });

/* JQuery popup
 * Open "open-popup" class links on click within a popup
 */

    var $myHeight = $(window).height() - 80;

    $('a.open-popup').each(function() {
        var $dialog = $('<div></div>');
        var $link = $(this).one('click', function() {
            $dialog
                .load($link.attr('href'))
                .dialog({
                    title: $link.attr('title'),
                    show: 'fade',
                    hide: 'fade',
                    height: $myHeight,
                    width: 750,
                    position:'top',
                    modal: true,
                    resizable: false,
                    draggable: false
                });

            $link.click(function() {
                $dialog.dialog('open');
                return false;
            });

            return false;
        });
    });

/* JQuery form popup function
 * Args :   popupOpener: id of the form popup link
 *          form : id of the form to diplay
 */

    function formPopup(popupOpener,oform){

        var $form = $(oform);
        $form.find('legend').hide();

        var $link = $(popupOpener).one('click', function() {
            $form.css('display', 'visible');
            $form.dialog({
                title: $(this).html(),
                show: 'fade',
                hide: 'fade',
                width: 405,
                position:'top',
                modal: true,
                resizable: false,
                draggable: false    
            });

            $link.click(function() {
                $form.dialog('open');	
                return false;
            });

            return false;
        });
		$form.submit(function(){
			$.post($form.attr('action'), $form.serialize());
			$form.dialog('close');
			$form.clearForm();	
			return false;
		});
    }

    formPopup('#feedback-popup','#form-feedback');
});