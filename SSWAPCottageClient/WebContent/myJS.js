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
		var list = document.getElementById("resultList").toString();
		list = list.substring(2,list.length);
		var result = result.split("|");
		var values = [];
		var i = 0;
		result.forEach(function (r) {
            if (r.indexOf("null")<0 && r != "" && r != "]") {
                values[i] = r;
                i = i + 1;
            }
        });

        var resultList = document.getElementById('resultList');
        var index = 0;
		while(index < values.length){
			if (values[index] == "null"){
                index = index+11;
                continue;
			}
			var cottage = document.createElement("div");
			cottage.setAttribute("class","cottage");
			var image = document.createElement("div");
			image.setAttribute("class","backgroundImage");
			image.setAttribute("style","background-image: url("+values[7+index]+")");
			cottage.appendChild(image);

			var leftColumn = document.createElement("div");
            leftColumn.setAttribute("class","leftColumn");
			var booker = document.createElement("h5");
			booker.innerHTML = "Booker: "+values[0+index];
			leftColumn.appendChild(booker);
            var num = document.createElement("h5");
            num.innerHTML = "Booking Number: "+values[10+index];
            leftColumn.appendChild(num);
            var address = document.createElement("h5");
            address.innerHTML = "Address: "+values[2+index];
            leftColumn.appendChild(address);
            var places = document.createElement("h5");
            places.innerHTML = "Places/People: "+values[4+index];
            leftColumn.appendChild(places);
            var bedrooms = document.createElement("h5");
            bedrooms.innerHTML = "Bedrooms: "+values[3+index];
            leftColumn.appendChild(bedrooms);
			cottage.appendChild(leftColumn);

            var leftColumn1 = document.createElement("div");
            leftColumn1.setAttribute("class","leftColumn");
            var lake = document.createElement("h5");
            lake.innerHTML = "Distance from lake: "+values[1+index];
            leftColumn1.appendChild(lake);
            var city = document.createElement("h5");
            city.innerHTML = "Nearest City: "+values[9+index];
            leftColumn1.appendChild(city);
            var city_d = document.createElement("h5");
            city_d.innerHTML = "Distance from city: "+values[8+index];
            leftColumn1.appendChild(city_d);
            var period = document.createElement("h5");
            period.innerHTML = "Available from "+values[5+index]+"to"+values[6+index];
            leftColumn1.appendChild(period);
            cottage.appendChild(leftColumn1);

            resultList.appendChild(cottage)
			index = index+11;
		}
	}else {
        alert('null');
	}
}



