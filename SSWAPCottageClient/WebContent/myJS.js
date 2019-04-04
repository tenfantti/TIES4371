function doQuery()
{
alert('doQuery...');	
	if((document.getElementById('rc').value!=''))
	{
		var q_str = 'reqType=doQuery';
		q_str = q_str+'&serviceURL='+document.getElementById('rc').value;
		doAjax('SSWAPServiceMed',q_str,'doQuery_back','post',0);
	}else
	{
		alert('Please, fill the Service URL...');
	}
}

function doQuery_back(result)
{
	if (result!=null) {
		var form = document.getElementById('SearchForm');
        var nameC = document.getElementById('nameC');
        var valueC = document.getElementById('valueC');
		var result = result.split("url:");
        document.getElementById('serviceUrl').innerHTML = "serviceUrl:" +result[1];
        //get name list
		var names = result[0].substring(1,result[0].length - 1);
		var names = names.split(",")
        form.style.display = "block";
		//put names into form
        names.forEach(function (lookUpName) {
			var name = document.createElement("h4");
			name.innerHTML = lookUpName;
			var input = document.createElement("input");
			input.type = "text";
			input.name = lookUpName;
			nameC.appendChild(name);
			valueC.appendChild(input);
        })
	}else {
        alert('null');
	}
}

function doSearch()
{
    alert('doSearch...');
    var valueC = document.getElementById('valueC');
    var inputs = valueC.childNodes;
    var query = "";
    inputs.forEach(function (input) {
		query = query+'&'+input.name+'='+input.value;
    });
	var url = document.getElementById('serviceUrl').innerHTML.split("serviceUrl:")[1];
    if(query!=null && url!=null)
    {
        var q_str = 'reqType=doSearch';
        q_str = q_str+'&serviceURL='+url+query;
        doAjax('SSWAPServiceMed',q_str,'doSearch_back','post',0);
    }else
    {
        alert('No input values');
    }
}

function doSearch_back(result) {
	alert(result);

}



