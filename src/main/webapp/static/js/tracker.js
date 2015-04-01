var originOptions = $("#originSelect");
var destinationOptions = $("#destinationSelect");
var departureDateOptions = $("#departureDateSelect");

function populateOriginList(searchData)
{
	if ( searchData == null )
	{
		return;
	}
	console.log('populating the origin list with data');
	
	var originArray = new Array();
	originOptions.html(''); // remove all the previous options
	originOptions.change(searchData, populateDestinationListFromChangeEvent);
	$.each(searchData, function() {
		// check if we already have this origin in the list
		if ( $.inArray(this.origin, originArray) == -1)
		{
			originArray.push(this.origin);
			// Just append options to this list
			var newOption = $("<option />").val(this.origin).text(this.origin);
			originOptions.append(newOption);
		}
		
	});
	populateDestinationList(searchData);
}

function populateDestinationListFromChangeEvent(event)
{
	populateDestinationList(event.data);
}

function populateDepartureDateListFromChangeEvent(event)
{
	populateDepartureDateList(event.data);
}

function populateDestinationList(searchData)
{
	console.log('origin list selection changed');
	var originSelectedValue = $("#originSelect option:selected").val();
	destinationOptions.html(''); // remove all the previous options
	
	var destinationArray = new Array();
	destinationOptions.change(searchData, populateDepartureDateListFromChangeEvent);
	$.each(searchData, function() {
		if ( this.origin === originSelectedValue)
		{
			if ( $.inArray(this.destination, destinationArray) == -1)
			{
				destinationArray.push(this.destination);
				var newDestOption = $("<option />").val(this.destination).text(this.destination);
				destinationOptions.append(newDestOption);
			}
		}	
	});
	populateDepartureDateList(searchData);
}

function populateDepartureDateList(searchData)
{
	console.log('destination list selection changed');
	var departureDateArray = new Array();
	var originSelectedValue = $("#originSelect option:selected").val();
	var destinationSelectedValue = $("#destinationSelect option:selected").val();
	departureDateOptions.html(''); // remove all the previous options
	$.each(searchData, function() {
		if ( this.origin === originSelectedValue && this.destination === destinationSelectedValue)
		{
			if ( $.inArray(this.departureDateAsString, departureDateArray) == -1)
			{
				departureDateArray.push(this.departureDateAsString);
				var newDepDateOption = $("<option />").val(this.departureDate).text(this.departureDateAsString);
				departureDateOptions.append(newDepDateOption);
			}
		}	
	});
}


function trackingButtonClicked()
{
	console.log('show tracked flight details clicked');
	var originSelectedValue = $("#originSelect option:selected").val();
	var destinationSelectedValue = $("#destinationSelect option:selected").val();
	var departureDateSelectedValue = $("#departureDateSelect option:selected").val();
	console.log('form data is ['+originSelectedValue+'], ['+destinationSelectedValue+'], ['+departureDateSelectedValue+']');
	
	

    var searchInput = {
		origin: originSelectedValue,
		destination: destinationSelectedValue,
		departureDate: departureDateSelectedValue
    }
    console.log('json: '+JSON.stringify(searchInput));
    clearChart();
    $.ajax({
        url: '/flight/api/history',
        type: 'post',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify(searchInput), //{'data': searchInput},//,
        success: function (data) {
        	
        	if (data != null)
    		{
        		console.log("Recieved data in response from api");
        		createChartFromData(data.history); // create the chart
    		}
        	else
    		{
        		console.log("No data returned from api");
    		}
    	},
        error: function(errorData)
        {
        	console.log('Error trying to get data for tracking search: '+errorData);
        	alert("There was a problem getting the chart data");
        }
    });
    
}

$( document ).ready(function() {
	originOptions = $("#originSelect");
	destinationOptions = $("#destinationSelect");
	departureDateOptions = $("#departureDateSelect");
	
	populateOriginList(savedFlightResults);
	$("#showTrackedButton").click(trackingButtonClicked);
  });