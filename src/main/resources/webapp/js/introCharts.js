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
var labelType, useGradients, nativeTextSupport, animate;

(function() {
	var ua = navigator.userAgent, iStuff = ua.match(/iPhone/i)
			|| ua.match(/iPad/i), typeOfCanvas = typeof HTMLCanvasElement, nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'), textSupport = nativeCanvasSupport
			&& (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
	// I'm setting this based on the fact that ExCanvas provides text support
	// for IE
	// and that as of today iPhone/iPad current text support is lame
	labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native'
			: 'HTML';
	nativeTextSupport = labelType == 'Native';
	useGradients = nativeCanvasSupport;
	animate = !(iStuff || !nativeCanvasSupport);
})();

function pieChart(json,offset) {

	// init PieChart
	var pieChart = new $jit.PieChart({
		injectInto : 'graph-opensource',
		// whether to add animations
		animate : true,
		// offsets
		offset : 10,
		sliceOffset : 0,
		labelOffset : 20,
		// slice style
		// type: useGradients? 'stacked:gradient' : 'stacked',
		// whether to show the labels for the slices
		showLabels : false,
		// resize labels according to
		// pie slices values set 7px as
		// min label size
		resizeLabels : 7,

		// enable tips
		Tips : {
			enable : true,
			onShow : function(tip, elem) {
				var uprogress_oss=progress_oss;
				if(progress_oss==0.001){
					uprogress_oss=0;
				}
				if(progress_oss==99.999){
					uprogress_oss=100;
				}
				tip.innerHTML = "<b>Open source</b>: " +uprogress_oss+"%";
			}
		}
	});
	// load JSON data.
	pieChart.loadJSON(json);
	// end
	return pieChart;
}

function barChart() {
	// init data
	var json = {
		'color' : [ '#9C470E', '#e6e6e6' ],
		'label' : [ 'Progress','Left'],
		'values' : [ {
			'label' : 'Progress',
			'values' : [ 1, 99 ]
		} ]

	};
	// init BarChart
	var barChart = new $jit.BarChart({
		// id of the visualization container
		injectInto : 'graph-total-figure',
		// whether to add animations
		animate : true,
		// horizontal or vertical barcharts
		orientation : 'vertical',
		// bars separation
		barsOffset : 0,
		// visualization offset
		Margin : {
			top : 5,
			left : 5,
			right : 5,
			bottom : 5
		},
		// labels offset position
		labelOffset : 5,
		// bars style
		// type: useGradients? 'stacked:gradient' : 'stacked',
		// whether to show the aggregation of the values
		showAggregates : false,
		// whether to show the labels for the bars
		showLabels : false,
		// labels style
		// add tooltips
		Tips : {
			enable : true,
			onShow : function(tip, elem) {
				tip.innerHTML = "<b>" + elem.name + "</b>: " + elem.value+"%";
			}
		}
	});
	// load JSON data.
	barChart.loadJSON(json);

	return barChart;
}
