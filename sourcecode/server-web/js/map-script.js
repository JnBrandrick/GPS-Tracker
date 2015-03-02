/** Google map **/
var map;

/** Array of markers **/
var markers = new Array();

var MY_MAPTYPE_ID = 'custom_style';




/*******************************************************************
** Function: $(document).ready()
**
** Date: February 27th, 2015
**
** Revisions: 
**
**
** Designer: Rhea Lauzon
**
** Programmer: Rhea Lauzon
**
** Interface:
**			$(document).ready(function()
**
** Returns:
**			void
**
** Notes:
**	When the document is ready it creates the google map and loading
**  the initial markers.
**
**********************************************************************/
$(document).ready(function()
{
	initialize();

	//load the data from the file
	loadData();

});


/*******************************************************************
** Function: initialize
**
** Date: February 27th, 2015
**
** Revisions: 
**
**
** Designer: Rhea Lauzon
**
** Programmer: Rhea Lauzon
**
** Interface:
**			initialize()
**
** Returns:
**			void
**
** Notes:
**	Initializes the google map and centers on user's location.
**
**********************************************************************/
function initialize() 
{

  var featureOpts = [
    {
        "featureType": "all",
        "elementType": "labels.text",
        "stylers": [
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "all",
        "elementType": "labels.text.fill",
        "stylers": [
            {
                "color": "#ffffff"
            }
        ]
    },
    {
        "featureType": "all",
        "elementType": "labels.text.stroke",
        "stylers": [
            {
                "color": "#000000"
            },
            {
                "lightness": 13
            }
        ]
    },
    {
        "featureType": "administrative",
        "elementType": "geometry.fill",
        "stylers": [
            {
                "color": "#000000"
            }
        ]
    },
    {
        "featureType": "administrative",
        "elementType": "geometry.stroke",
        "stylers": [
            {
                "color": "#144b53"
            },
            {
                "lightness": 14
            },
            {
                "weight": 1.4
            }
        ]
    },
    {
        "featureType": "administrative",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "administrative.province",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "administrative.locality",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "administrative.neighborhood",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "landscape",
        "elementType": "all",
        "stylers": [
            {
                "color": "#08304b"
            }
        ]
    },
    {
        "featureType": "poi",
        "elementType": "geometry",
        "stylers": [
            {
                "color": "#0c4152"
            },
            {
                "lightness": 5
            }
        ]
    },
    {
        "featureType": "poi.attraction",
        "elementType": "labels",
        "stylers": [
            {
                "invert_lightness": true
            }
        ]
    },
    {
        "featureType": "poi.attraction",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "poi.park",
        "elementType": "labels",
        "stylers": [
            {
                "visibility": "on"
            },
            {
                "invert_lightness": true
            }
        ]
    },
    {
        "featureType": "poi.park",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "road",
        "elementType": "labels.text",
        "stylers": [
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "road.highway",
        "elementType": "geometry.fill",
        "stylers": [
            {
                "color": "#000000"
            }
        ]
    },
    {
        "featureType": "road.highway",
        "elementType": "geometry.stroke",
        "stylers": [
            {
                "color": "#0b434f"
            },
            {
                "lightness": 25
            }
        ]
    },
    {
        "featureType": "road.highway",
        "elementType": "labels",
        "stylers": [
            {
                "lightness": "0"
            },
            {
                "saturation": "0"
            },
            {
                "invert_lightness": true
            },
            {
                "visibility": "simplified"
            },
            {
                "hue": "#00e9ff"
            }
        ]
    },
    {
        "featureType": "road.highway",
        "elementType": "labels.text",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "road.highway.controlled_access",
        "elementType": "labels.text",
        "stylers": [
            {
                "color": "#a1f7ff"
            }
        ]
    },
    {
        "featureType": "road.arterial",
        "elementType": "geometry.fill",
        "stylers": [
            {
                "color": "#000000"
            }
        ]
    },
    {
        "featureType": "road.arterial",
        "elementType": "geometry.stroke",
        "stylers": [
            {
                "color": "#0b3d51"
            },
            {
                "lightness": 16
            }
        ]
    },
    {
        "featureType": "road.arterial",
        "elementType": "labels",
        "stylers": [
            {
                "invert_lightness": true
            }
        ]
    },
    {
        "featureType": "road.local",
        "elementType": "geometry",
        "stylers": [
            {
                "color": "#000000"
            }
        ]
    },
    {
        "featureType": "road.local",
        "elementType": "labels",
        "stylers": [
            {
                "visibility": "simplified"
            },
            {
                "invert_lightness": true
            }
        ]
    },
    {
        "featureType": "transit",
        "elementType": "all",
        "stylers": [
            {
                "color": "#146474"
            }
        ]
    },
    {
        "featureType": "water",
        "elementType": "all",
        "stylers": [
            {
                "color": "#021019"
            }
        ]
    }
];

	var mapOptions = {
	zoom: 6,
    mapTypeControlOptions: {
      mapTypeIds: [google.maps.MapTypeId.ROADMAP, MY_MAPTYPE_ID]
    },
    mapTypeId: MY_MAPTYPE_ID
  };


	map = new google.maps.Map(document.getElementById('googleMap'), mapOptions);

	var styledMapOptions = {name: 'Custom Style'};

	var customMapType = new google.maps.StyledMapType(featureOpts, styledMapOptions);

  	map.mapTypes.set(MY_MAPTYPE_ID, customMapType);



	// Try to receive the location from HTML
	if(navigator.geolocation)
	{
		navigator.geolocation.getCurrentPosition(function(position) 
		{
			var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

			map.setCenter(pos);
		},

		function() 
		{
			handleNoGeolocation(true);
		});
	}
	else 
	{
		// Browser doesn't support Geolocation
		handleNoGeolocation(false);
	}
}

//function to handle no geolocation available
function handleNoGeolocation(errorFlag) 
{
	if (errorFlag) {
		var content = 'Error: The Geolocation service failed.';
	} else {
		var content = 'Error: Your browser doesn\'t support geolocation.';
	}

	var options = {
		map: map,
		position: new google.maps.LatLng(60, 105),
		content: content
	};

	var infowindow = new google.maps.InfoWindow(options);
	map.setCenter(options.position);
}



/*******************************************************************
** Function: loadData
**
** Date: March 1st, 2015
**
** Revisions: 
**
**
** Designer: Rhea Lauzon
**
** Programmer: Rhea Lauzon
**
** Interface:
**			loadData()
**
** Returns:
**			void
**
** Notes:
**	Parses the XML file for users
**
**********************************************************************/
function loadData()
{

	for (var i = 0; i < markers.length; i++)
	{
		markers[i].setMap(null);
	}

	if (window.XMLHttpRequest)
	{
		xhttp = new XMLHttpRequest();
	}

	//open the file
	xhttp.open("GET", "../data.xml", false);
	xhttp.send();
	xmlDoc = xhttp.responseXML;

	//fetch all the users
	var users = xmlDoc.getElementsByTagName("user");

	//loop through all the users
	for (var i = 0; i < users.length; i++)
	{
		//fetch the latitude and longitude
		var latitude = users[i].getElementsByTagName("latitude")[0].childNodes[0].nodeValue;
        var longitude = users[i].getElementsByTagName("longitude")[0].childNodes[0].nodeValue;

        //fetch the ID
        var name = users[i].getElementsByTagName("name")[0].childNodes[0].nodeValue;

        //fetch the time
        var time = users[i].getElementsByTagName("time")[0].childNodes[0].nodeValue;

        //fetch the IP address
        var ip = users[i].getElementsByTagName("ip")[0].childNodes[0].nodeValue;

        //add the information to the map
        addMarker(name, ip, time, latitude, longitude);
	}

	//set the timer for the next update to refresh data
	window.setTimeout(loadData, 5000);

}

function addMarker(name, ip, time, latitude, longitude)
{
	//create the latLng object
	var location = new google.maps.LatLng(parseFloat(latitude), parseFloat(longitude));

	//make a marker
    var marker = new google.maps.Marker({ position: location, map: map, title: String(name)});

    //TO-DO: add info window on click 
	//var infowindow = new google.maps.InfoWindow({map: map, position: location, content: name + "\n" + time + ip });

	//add the marker to the array
	markers.push(marker);

}