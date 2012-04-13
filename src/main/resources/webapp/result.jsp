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
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/treemap.css">
<link rel="stylesheet" href="css/compiled/style2.css">
<link type="text/css" href="css/shaper.css" rel="stylesheet" />
<!--[if IE]><script language="javascript" type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="js/jit.js"></script>
<script type="text/javascript" src="js/treemap.js"></script>
<!-- <script type="text/javascript" src="js/jquery-1.6.2.min.js"></script> -->
<script type="text/javascript" src="js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/lib/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="js/lib/modernizr-2.0.6.min.js"></script>
<script type="text/javascript" src="js/feedback.js"></script>
<script type="text/javascript" src="js/introCharts.js"></script>


<title>SourceSquare</title>
</head>

<body>
	<div id="global-width">
	<aside id="contact">
				<a href="#" id="feedback-popup">Feedback</a> 
			</aside>
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
			<!-- <a class="button" href="download.html"> Download Me! </a> -->
		</div>
		<!--/header-->
				<div id="location">
		</div>
		<div id="wrapper">

			<div id="content-treemap"></div>
			
			<div id="sidebar">
			<div id="advertising">File details available in <a href="http://www.antelink.com/product/antepedia-suite-tools.html">Antelink products</a></div>
				<div class="brackets">
					<ul class="in-brackets">
						<li>Total Files <span id="counter-total">
								${modelData.rootNode.cumulatedFilesString} </span>
						</li>
						<li>Open Source Files <span id="counter-opensource">
								${modelData.rootNode.cumulatedOSFilesString} </span><br /> 
								<fmt:formatNumber var="progress_oss" value="${(modelData.rootNode.cumulatedOSFiles*100)/modelData.rootNode.cumulatedFiles}" maxFractionDigits="0" />
								<span
							id="graph-opensource-figure" class="blue"> ${progress_oss}% </span><span
							id="graph-opensource"></span>
						</li>
                        <li id="legend">
<!--								Fully Open Source <span id="blue-square"> &nbsp; &nbsp; &nbsp;</span><br /><br />
								Not Open Source <span id="brown-square"> &nbsp; &nbsp; &nbsp;</span><br /><br />-->
								Open Source<br /><br /><br />
								<em>Click to drill down, right-click to go back!</em>
						</li>
					
					</ul>
				</div>
				<div id="badges">


					<ul>
						<li id="badge-1">
						<c:if test="${not empty modelData.badges[0]}">
						<span class="badge">
						<img
							src="images/badges/${modelData.badges[0].path}"
							alt="${modelData.badges[0].alt}">
							<span class="ttip">
								${modelData.badges[0].toolTip}
							</span>
							</span>
							</c:if>
							</li>
						<li id="badge-3">
						<c:if test="${not empty modelData.badges[2]}">
						<span class="badge">
						<img
							src="images/badges/${modelData.badges[2].path}"
							alt="${modelData.badges[2].alt}">
														<span class="ttip">
								${modelData.badges[2].toolTip}
							</span>
							</span>
							</c:if>
							</li>
						<li id="badge-5">
						<c:if test="${not empty modelData.badges[4]}">
						<span class="badge">
						<img
							src="images/badges/${modelData.badges[4].path}"
							alt="${modelData.badges[4].alt}">
														<span class="ttip">
								${modelData.badges[4].toolTip}
							</span>
							</span>
							</c:if>
							</li>
						<li id="badge-7">
						<c:if test="${not empty modelData.badges[6]}">
						<span class="badge">
						<img
							src="images/badges/${modelData.badges[6].path}"
							alt="${modelData.badges[6].alt}">
														<span class="ttip">
								${modelData.badges[6].toolTip}
							</span>
							</span>
						</c:if>
						</li>
					</ul>
					<ul>
						<li id="badge-2">
						<c:if test="${not empty modelData.badges[1]}">
						<span class="badge">
								<img src="images/badges/${modelData.badges[1].path}" alt="${modelData.badges[1].alt}">
															<span class="ttip">
								${modelData.badges[1].toolTip}
							</span>
								</span>
							</c:if>
						</li>
						<li id="badge-4">
						<c:if test="${not empty modelData.badges[3]}">
						<span class="badge">
								<img src="images/badges/${modelData.badges[3].path}" alt="${modelData.badges[3].alt}">
															<span class="ttip">
								${modelData.badges[3].toolTip}
							</span>
								</span>
							</c:if></li>
						<li id="badge-6">
						<c:if test="${not empty modelData.badges[5]}">
						<span class="badge">
							<img src="images/badges/${modelData.badges[5].path}" alt="${modelData.badges[5].alt}">
														<span class="ttip">
								${modelData.badges[5].toolTip}
							</span>
							</span>
						</c:if>
						</li>
						<li id="badge-8">
						<c:if test="${not empty modelData.badges[7]}">
						<span class="badge">
							<img src="images/badges/${modelData.badges[7].path}" alt="${modelData.badges[7].alt}">
							<span class="ttip">
								${modelData.badges[7].toolTip}
							</span>
							</span>
						</c:if>
						</li>
					</ul>
				</div>
				<div id="share">
			<!-- 		<button id="shutdown-button">Shutdown</button> -->
					<div  id="publish-button" class="addthis_toolbox addthis_default_style button">
						<span id="publish-text">Share!</span><span id="countdots"><span id="disclaimer">
								Clicking on this button will share your treemap and badges to our private server.<br /><br />
								<span class="blue">Your treemap folder names will be removed</span> prior publication</span></span>
					</div>
				</div>
			</div>
		</div>
		<!--/wrapper-->
			<%@ include file="feedback.jsp"%>
		<%@ include file="footer.jsp" %>
	</div>
	<!--/global-width-->
</body>

<script type="text/javascript">
	var progress_oss=${progress_oss};
	function movePoints(){
		var count=$("#countdots").text().length;
		switch(count){
		case 3:
			$("#countdots").text(".");
			break;
		case 2:
			$("#countdots").text("...");
			break;
		case 1:
			$("#countdots").text("..");
			break;
		}
		setTimeout("movePoints()", 700); 
	}
	function bindButtons() {

		$("#publish-button")
				.click(
						function() {
							$("#publish-text").text("Sharing");
							$("#countdots").text("...");
							movePoints();
							$.ajax({
										url : 'service/publish',
										cache : false,
										success : function(data) {
											if (data) {
												$.ajax({
															url : 'service/publish',
															cache : false,
															type : "POST",
															success : function(data) {
																window.location=unescape(data);
															}
														});
											}
										}
									});
							
						});
	}

	$(document).ready(function() {
		bindButtons();
		var json = ${treemapData};
		drawTreemap(json,true,${modelData.nodeLevel});
		
		if(progress_oss==0){
			progress_oss=0.001
		}
		if (progress_oss >= 100) {
			progress_oss = 99.999;
		}
		pieChart({
			'color' : [ '#1BA2FF', '#e6e6e6' ],
			'label' : [ 'Open Source'],
			'values' : [ {
				'label' : 'op',
				'values' : [ progress_oss ]
			}, {
				'label' : 'nop',
				'values' : [ 100-progress_oss ]
			} ]
		},0);
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
	});
</script>

</html>
