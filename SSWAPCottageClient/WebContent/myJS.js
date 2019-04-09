function doQuery()
{
	var inputs = document.getElementById('inputs').childNodes;
	var query = ''
	inputs.forEach(function (input) {
		if(input.type!='button') {
			query = query + '&' + input.name + '=' + input.value;
		}
    })
	var serviceUrl = "http://localhost:9090/SSWAPCottageService/getService"
    var q_str = "reqType=doQuery";
    q_str = q_str+'&serviceURL='+serviceUrl+query;
    doAjax('SSWAPServiceMed',q_str,'doQuery_back','post',0);
}

function doQuery_back(result)
{
	if (result!=null) {
		var result = result.split("|");
		var values = [];
		var i = 0;
		result.forEach(function (r) {
            if (r.indexOf("null")<0 && r != "") {
                values[i] = r;
                i = i + 1;
            }
        });

        var resultList = document.getElementById('resultList');
        while(resultList.hasChildNodes()) {
            resultList.removeChild(resultList.firstChild);
        }

		while(values.length>0){
            var cottage = document.createElement("div");
            cottage.setAttribute("class","cottage");
            var leftColumn = document.createElement("div");
            leftColumn.setAttribute("class","leftColumn");
            var leftColumn1 = document.createElement("div");
            leftColumn1.setAttribute("class","leftColumn");

            let image = document.createElement("div");
            image.setAttribute("class","backgroundImage");
            var booker = document.createElement("h5");
            var num = document.createElement("h5");
            var address = document.createElement("h5");
            var places = document.createElement("h5");
            var bedrooms = document.createElement("h5");
            var lake = document.createElement("h5");
            var city = document.createElement("h5");
            var city_d = document.createElement("h5");
            var start = document.createElement("h5");
            var end = document.createElement("h5");

            for (i =0;i<11;i++){
                slicedValue = values[i].split("\\");
            	if (values[i].indexOf("cottage_image")>-1){
                    image.setAttribute("style","background-image: url("+slicedValue[1]+")");
				} else if (values[i].indexOf("name")>-1){
                    booker.innerHTML = "Booker: "+slicedValue[1];
                } else if (values[i].indexOf("number")>-1){
                    num.innerHTML = "Booking Number: "+slicedValue[1];
                } else if (values[i].indexOf("address")>-1){
                    address.innerHTML = "Address: "+slicedValue[1];
                } else if (values[i].indexOf("places")>-1){
                    places.innerHTML = "Places/People: "+slicedValue[1];
                } else if (values[i].indexOf("bedrooms")>-1){
                    bedrooms.innerHTML = "Bedrooms: "+slicedValue[1];
                } else if (values[i].indexOf("lake")>-1){
                    lake.innerHTML = "Distance from lake: "+slicedValue[1];
                } else if (values[i].indexOf("nearest")>-1){
                    city.innerHTML = "Nearest City: "+slicedValue[1];
                } else if (values[i].indexOf("city_distance")>-1){
                    city_d.innerHTML = "Distance from city: "+slicedValue[1];
                } else if (values[i].indexOf("start")>-1){
                    start.innerHTML = "Available Start Date: "+ slicedValue[1]
                } else if (values[i].indexOf("end")>-1){
                    end.innerHTML = "Available End Date: "+ slicedValue[1];
                }
			}

			cottage.appendChild(image);
			leftColumn.appendChild(booker);
            leftColumn.appendChild(num);
            leftColumn.appendChild(address);
            leftColumn.appendChild(places);
            leftColumn.appendChild(bedrooms);
			cottage.appendChild(leftColumn);
            leftColumn1.appendChild(lake);
            leftColumn1.appendChild(city);
            leftColumn1.appendChild(city_d);
            leftColumn1.appendChild(start);
            leftColumn1.appendChild(end);
            cottage.appendChild(leftColumn1);
            resultList.appendChild(cottage);
            values = values.slice(11);
		}
	}else {
        alert('null');
	}
}



