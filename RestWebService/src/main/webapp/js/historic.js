$("#button").click(function(){
	alert("coming");
	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/RestWebService/rest/weather/forecast/20190103',
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		success: function(data){
			var output= "<ul>";
			for (i=0; i < data.length; i++){
				alert(data[i].DATE);
				$(".date"+i).append(data[i].DATE);
				$(".tmax"+i).append(data[i].TMAX);
				$(".tmin"+i).append(data[i].TMIN);
			}	
		}
	});
});	
