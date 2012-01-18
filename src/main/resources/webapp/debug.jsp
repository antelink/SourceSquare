<%--

    Copyright (C) 2009-2012 Antelink SAS

    This program is free software; you can redistribute it and/or modify it under
    the terms of the GNU Affero General Public License Version 3 as published
    by the Free Software Foundation.

    This program is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
    FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License Version 3
    for more details.

    You should have received a copy of the GNU Affero General Public License Version
    3 along with this program. If not, see http://www.gnu.org/licenses/agpl.html

    Additional permission under GNU AGPL version 3 section 7

    If you modify this Program, or any covered work, by linking or combining it with
    Eclipse Java development tools (JDT) or Jetty (or a modified version of these
    libraries), containing parts covered by the terms of Eclipse Public License 1.0,
    the licensors of this Program grant you additional permission to convey the
    resulting work. Corresponding Source for a non-source form of such a combination
    shall include the source code for the parts of Eclipse Java development tools
    (JDT) or Jetty used as well as that of the covered work.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>SourceSquare</title>
<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
<link type="text/css" href="css/cupertino/jquery-ui-1.8.16.custom.css"
	rel="stylesheet" />
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
</head>
<body>
	<h1>SourceSquare</h1>

	<button id="simulate-button">RestartSimulation</button>
	<button id="go-button">Go</button>
	<br>
	<p />
	<div>
		<span id="text-nbFilesToScan" style="color: #FF6633"></span><span>
			Files to scan</span>
	</div>
	<div>
		<span id="text-nbOSFilesFound" style="color: #FF6633"></span><span>
			Open source files scanned</span>
	</div>
	<div>
		<span id="text-nbFilesScanned" style="color: #FF6633"></span><span>
			Files scanned</span>
	</div>
	<div>
		<span id="text-complete" style="color: #FF6633"></span><span>
			Scan completed</span>
	</div>
	<div>
		<span id="text-continue" style="color: #FF6633"></span><span>
			shoud continue polling the server?</span>
	</div>
	<p />
	Total:
	<div id="progressbar2"></div>
	<br> Open source:
	<div id="progressbar1"></div>
</body>
<script type="text/javascript">
    var continue_ = true;
    function executeQuery() {
        $.ajax({
            url: 'service/status',
            cache: false,
            success: function(data) {
            	$("#text-nbFilesToScan").text(data.nbFilesToScan);
            	$("#text-nbOSFilesFound").text(data.nbOSFilesFound);
            	$("#text-nbFilesScanned").text(data.nbFilesScanned);
            	$("#text-complete").text(data.complete);
            	continue_=!data.complete;
            	$("#text-continue").text(continue_);
            	var progress_total=(data.nbFilesScanned*100)/data.nbFilesToScan;
            	var progress_oss=(data.nbOSFilesFound*progress_total)/data.nbFilesScanned;
            	
            	$("#progressbar2").progressbar( "option", "value", progress_total );
            	$("#progressbar1").progressbar( "option", "value", progress_oss );
            },
            complete:function() {
                if (continue_){
                	$("#simulate-button").attr("disabled", true);
                    setTimeout("executeQuery()", 10);
                }
                else{
                	$("#simulate-button").attr("disabled", false);
                	$("#go-button").attr("disabled", false);
                	return;
                }
            }
        });
    }

    
    
    $(document).ready(function() {
        $("#progressbar2").progressbar({ value: 10 });
        $("#progressbar1").progressbar({ value: 10 });
    	$("#go-button").attr("disabled", true);
    //	setTimeout("executeQuery()", 10);
    	$("#simulate-button").click(function(){
   		 
   		 $.ajax({
   	            url: 'service/simulation',
   	            cache: false,
   	           // data:{"requestText":$("#text-data-field").attr('value')},
   	            success: function(data) {
   	         	$	("#go-button").attr("disabled", true);
   	             	setTimeout("executeQuery()", 10);
   	            }
   	        });
   
   	 });
    	$("#go-button").click(function(){
    		window.location.href='result.jsp';
    	});
    });

	</script>

</html>