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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>SourceSquare</title>

<script type="text/javascript" src="js/lib/jquery-1.7.1.min.js"></script>
<!-- <script type="text/javascript" src="js/jquery-1.6.2.min.js"></script> -->
<!--[if IE]><script language="javascript" type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="js/jit.js"></script>
<script type="text/javascript" src="js/introCharts.js"></script>
<script type="text/javascript" src="js/lib/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="js/lib/modernizr-2.0.6.min.js"></script>
<script type="text/javascript" src="js/feedback.js"></script>
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/compiled/style2.css">
<link rel="stylesheet" href="css/home.css">
<link type="text/css" href="css/shaper.css" rel="stylesheet" />

</head>


<body>
	<div id="global-width">
		<div id="header">
			<div class="brackets">
				<a href="http://www.sourcesquare.org">
					<div id="logo" class="in-brackets">
						<!-- Logo -->
					</div>
				</a>
			</div>
			<p id="catch-line">
				Find out if you are<br />an <span class="blue">open source</span>
				freak or not!
			</p>
		</div>
		<!--/header-->
		<aside id="contact">
			<a href="#" id="feedback-popup">Feedback</a> 
		</aside>
		
		<div id="wrapper">
			<div id="scanning">
				<span>Building treemap - </span><span id="scanningtext">INITIALIZING</span><span id="countdots" class="countdots">...</span>
			</div>
			<div class="brackets">
				<div class="in-brackets">
					<div id="content">
						<div class="column">
							<strong>Files Scanned</strong><span id="counter-total"
								class="counter" > 0 </span>
							<div id="graph-total">
								<span id="graph-total-figure"> </span>
							</div>
						</div>
						<div class="column">
							<strong>Open Source Files</strong><span id="counter-opensource"
								class="counter"> 0 </span>
							<div id="graph-opensource">
								<span id="graph-opensource-figure"> </span>
							</div>
					</div>
				</div>
			</div>
		</div>
		<!--/wrapper-->
		<%@ include file="feedback.jsp"%>
		<%@ include file="footer.jsp"%>
	</div>
	<!--/global-width-->
</body>

<script type="text/javascript">
	var continue_ = true;
	var pieChart;
	var barChart;
	var progress_oss = 0;

	function loadTime() {
		$.ajax({
			url : 'service/time',
			cache : false,
			success : function(data) {
				lengthTimer(data);
			}
		});
	}
	function lengthTimer(timeDiffmillis) {
		if(timeDiffmillis==0){
			$("#countdots").text("");
		}else{
			var timeDiff = timeDiffmillis / 1000;
			var seconds = Math.floor(timeDiff % 60);
			var minutes = Math.floor(timeDiff / 60);
			var strMinutes;
			var strSeconds;
			if (seconds > 9) {
				strSeconds = "" + seconds;
			} else {
				strSeconds = "0" + seconds;
			}
			if(minutes==0){
				minutes=1;
			}
			if(seconds > 15){
				minutes++;
			}
			strMinutes = "" + minutes;
			
			$("#countdots").text("About "+strMinutes + " min remaining");
		}
	}

	function updateBarChart(nbFilesScanned, nbFilesToScan) {
		var progress_total=0;
		if (!counting_) {
			progress_total = 5 + (nbFilesScanned * 95) / nbFilesToScan;
		}
		var json = {
			'color' : [ '#9C470E', '#e6e6e6' ],
			'label' : [ 'Progress', 'Left' ],
			'values' : [ {
				'label' : 'Progress',
				'values' : [ Math.round(progress_total),
						Math.round(100 - progress_total) ]
			} ]

		};
		barChart.updateJSON(json);
	}
	
	function updatePieChart(nbFilesScanned, nbOSFilesFound) {
		progress_oss = Math.round((nbOSFilesFound * 100) / nbFilesScanned);
		
		if (isNaN(progress_oss) || progress_oss == 0) {
			progress_oss = 0.001;
		}
		if (progress_oss == 100) {
			progress_oss = 99.999;
		}
		var json = {
			'color' : [ '#1BA2FF', '#e6e6e6' ],
			'label' : [ 'Files' ],
			'values' : [ {
				'label' : 'op',
				'values' : [ progress_oss ]
			}, {
				'label' : 'nop',
				'values' : [ 100 - progress_oss ]
			} ]
		};
		pieChart.updateJSON(json);
	}
	
	
	function executeQuery() {
		$.ajax({
			url : 'service/status',
			cache : false,
			success : function(data) {
				if (data.progressState == 'INITIALIZING') {
					$("#scanningtext").text('step 1: counting files...');
				} else {
					$("#scanningtext").replaceWith('searching in <a href="http://www.antepedia.com" target="_blank" >Antepedia</a>...');
				}
				$("#counter-total").text(data.displayedFilesScannedString+" / "+data.nbFilesToScanString);
				$("#counter-opensource").text(data.nbOSFilesFoundString);
				continue_ = (data.progressState != 'COMPLETE');
				counting_ = (data.progressState == 'INITIALIZING');
				updateBarChart(data.displayedFilesScanned,data.nbFilesToScan);	
				updatePieChart(data.nbFilesScanned,data.nbOSFilesFound);
				
			},
			complete : function() {
				if (continue_) {
					loadTime();
					/* movePoints(); */
					setTimeout("executeQuery()", 10);
				} else {
					window.location.href = 'result.jsp';
					return;
				}
			}
		});
	}

	$(document).ready(function() {
		pieChart = pieChart({
			'color' : [ '#1BA2FF', '#e6e6e6' ],
			'label' : [ 'Open Source' ],
			'values' : [ {
				'label' : 'op',
				'values' : [ 0.001 ]
			}, {
				'label' : 'nop',
				'values' : [ 100 ]
			} ]
		});
		barChart = barChart();
        var aclick=function (i) {
			if (this.getAttribute("href")) {
		         this.onclick = function() {
		             return !window.open(this.href);
		      }
			}
	      };
		$("#footer a").each(aclick);
		$("#wrapper a").each(aclick);
		$("#header a").each(aclick);
		setTimeout("executeQuery()", 10);
	});
</script>
</html>