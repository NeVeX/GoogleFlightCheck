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
	
	originOptions.html(''); // remove all the previous options
	originOptions.change(searchData, populateDestinationListFromChangeEvent);
	$.each(searchData, function() {
		// Just append options to this list
		var newOption = $("<option />").val(this.origin).text(this.origin);
		originOptions.append(newOption);
	});
	populateDestinationList(searchData);
}

function populateDestinationListFromChangeEvent(event)
{
	populateDestinationList(event.data);
}

function populateDestinationList(searchData)
{
	console.log('origin list selection changed');
	var originSelectedValue = $("#originSelect option:selected").val();
	destinationOptions.html(''); // remove all the previous options
	departureDateOptions.html(''); // remove all the previous options
	$.each(searchData, function() {
		if ( this.origin === originSelectedValue)
		{
			var newDestOption = $("<option />").val(this.destination).text(this.destination);
			destinationOptions.append(newDestOption);
			var dateText = this.departureDate.substring(0, 10);
			var newDepDateOption = $("<option />").val(dateText).text(dateText);
			departureDateOptions.append(newDepDateOption);
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
    $.ajax({
        url: '/flight/api',
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
        	console.log('Error trying to get data for tracking search');
        }
    });
    
}

$( document ).ready(function() {
	originOptions = $("#originSelect");
	destinationOptions = $("#destinationSelect");
	departureDateOptions = $("#departureDateSelect");
	
	populateOriginList(savedSearches);
	$("#showTrackedButton").click(trackingButtonClicked);
  });