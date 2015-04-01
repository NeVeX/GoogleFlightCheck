var theChart;

var options = {

	    ///Boolean - Whether grid lines are shown across the chart
	    scaleShowGridLines : true,

	    //String - Colour of the grid lines
	    scaleGridLineColor : "rgba(0,0,0,.05)",

	    //Number - Width of the grid lines
	    scaleGridLineWidth : 1,

	    //Boolean - Whether to show horizontal lines (except X axis)
	    scaleShowHorizontalLines: true,

	    //Boolean - Whether to show vertical lines (except Y axis)
	    scaleShowVerticalLines: true,

	    //Boolean - Whether the line is curved between points
	    bezierCurve : false,

	    //Number - Tension of the bezier curve between points
	    bezierCurveTension : 0.4,

	    //Boolean - Whether to show a dot for each point
	    pointDot : true,

	    //Number - Radius of each point dot in pixels
	    pointDotRadius : 4,

	    //Number - Pixel width of point dot stroke
	    pointDotStrokeWidth : 1,

	    //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
	    pointHitDetectionRadius : 20,

	    //Boolean - Whether to show a stroke for datasets
	    datasetStroke : true,

	    //Number - Pixel width of dataset stroke
	    datasetStrokeWidth : 2,

	    //Boolean - Whether to fill the dataset with a colour
	    datasetFill : true,

	    //String - A legend template
	    legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"

	};

function clearChart()
{
	if ( theChart != null)
	{
		theChart.destroy();
	}
}

function createChartFromData(flightResult)
{
	console.log("Ready function called in JS. Loading the chart");
	clearChart();
  //Get context with jQuery - using jQuery's .get() method.
    var ctx = $("#flightChart").get(0).getContext("2d");
    // This will get the first returned node in the jQuery collection.
    if ( flightResult != null && flightResult.length > 0)
    {
    	var xAxisLabels = new Array();
    	var lowestPrices = new Array();
    	var shortestPrices = new Array();
    	var i;
    	for( i = flightResult.length - 1; i > -1; i--)
		{
    		xAxisLabels.push(flightResult[i].dateSearchedAsString);
    		lowestPrices.push(flightResult[i].lowestPrice);
    		shortestPrices.push(flightResult[i].shortestTimePrice);
		}
	
    	var data = {
		    labels: xAxisLabels,
		    datasets: [
		        {
		            label: "Lowest Prices",
		            fillColor: "rgba(220,220,220,0.2)",
		            strokeColor: "rgba(220,220,220,1)",
		            pointColor: "rgba(220,220,220,1)",
		            pointStrokeColor: "#fff",
		            pointHighlightFill: "#fff",
		            pointHighlightStroke: "rgba(220,220,220,1)",
		            data: lowestPrices
		        },
		        {
		            label: "Shortest Trip Duration Prices",
		            fillColor: "rgba(151,187,205,0.2)",
		            strokeColor: "rgba(151,187,205,1)",
		            pointColor: "rgba(151,187,205,1)",
		            pointStrokeColor: "#fff",
		            pointHighlightFill: "#fff",
		            pointHighlightStroke: "rgba(151,187,205,1)",
		            data: shortestPrices
		        }
		    ]
    	};
    	// draw the chart with the given data
    	theChart = new Chart(ctx).Line(data, options);
	}	    	
}
