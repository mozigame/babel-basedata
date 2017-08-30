Array.prototype.contains = function (obj) {  
    var i = this.length;  
    while (i--) {  
        if (this[i] === obj) {  
            return true;  
        }  
    }  
    return false;  
} 

function getIntfCallInfo(jsonParam){
	load();
	var url='/basedata/logdb/stataicIntfCodeShowAll';
	//var obj={id:1, code:'test'};
	$.ajax({			
		url: url,
		type : "get",
		cache:false,
		async:false,
		dataType : "json",
		data:jsonParam,
		error : function() {
			alert("Error loading "+url);
		},
		success : function(ret) {
			//eval("var ret="+text);
			//console.log(ret);
			if(ret.flag!=0){
				alert(ret.msgCode+':'+ret.msgBody);
				return;
			}
			var dataList=ret.dataList;
			addInfo(dataList);
			disLoad();
		}
	});	
}


//弹出加载层
 function load() {  
     $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(window).height() }).appendTo("body");  
     $("<div class=\"datagrid-mask-msg\"></div>").html("正在加载，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 });  
 }
 
 //取消加载层  
 function disLoad() {  
     $(".datagrid-mask").remove();  
     $(".datagrid-mask-msg").remove();  
 }
 




function doSearch(){
	var jsonParam = $('#search-form').serializeJson();
	getIntfCallInfo(jsonParam);
	
}

function nvl(obj1, obj2){
	if(obj1){
		return obj1;
	}
	return obj2;
}

function addInfo(dataList){
	var data;
	var packageCountMap=new Map();
	var count=0;
	for(var i=0; i<dataList.length; i++){
		data=dataList[i];
		count=packageCountMap.get(data.packageName);
		if(!count){
			count=1;
		}
		else{
			count++;
		}
		packageCountMap.put(data.packageName, count);
	}
	var str='<p class="lead">Every interface call static</p>';
	str+='<table class="table table-bordered" style="table-layout:fixed"><tr><th>Package name</th><th>Intf code</th><th width="100px">Title</th><th width="100px">Author</th><th width="100px">Calls</th><th width="100px">Descs</th><th width="70px"><font color="red">Count</font></th>' +
			'<th width="60px">User count</th><th width="60px">IP count</th><th width="60px">Max rows</th><th width="80px">Max runTime</th><th>Max date</th><th width="60px">Error count</th><th>Max error date</th></tr>';
	var curPackageName='';
	for(var i=0; i<dataList.length; i++){
		data=dataList[i];
		count=packageCountMap.get(data.packageName);
		str+='<tr>'
		if(curPackageName!=data.packageName){
			curPackageName=data.packageName;
			str+='<td rowspan='+count+' data-toggle="tooltip" title="'+data.packageName+'('+count+')">'+data.packageName+'('+count+')</td>';
		}
		//str+='<td>'+data.packageName+'</td>';
		str+='<td data-toggle="tooltip" title="'+data.intfCode+'">'+data.intfCode+'</td><td>'+nvl(data.title,'')+'</td><td>'+nvl(data.author,'')+'</td><td data-toggle="tooltip" title="'+data.calls+'">'+nvl(data.calls,'')+'</td><td>'+nvl(data.descs,'')+'</td><td>'+data.count+'</td>' +
				'<td>'+data.userCount+'</td><td>'+data.ipCount+'</td><td>'+nvl(data.maxRows,'')+'</td><td>'+data.maxRunTime+'</td><td>'+Common.DateTimeFormatter(data.maxDate)+'</td>' +
				'<td>'+data.errorCount+'</td><td>'+Common.DateTimeFormatter(data.maxErrorDate)+'</td>';
		str+='</tr>'
	}
	str+='</table>';
	//console.log('runTime--'+str);
	
	//var html=$("#containerReport").html();
	//html+='<br/><br/>'
	$("#containerReport").html(str);
	$("table").colResizable();
}

$(function () {
	var recentlyDate=new Date(new Date().getTime()-7*24*3600*1000);//7天内
	$("#query_startDate").val(Common.DateFormatter(recentlyDate));
	
	
	//日期时间选择器
	$("#query_startDate").datetimepicker({
		format: "yyyy-mm-dd",
		autoclose: true,
		minView: "month",
		maxView: "decade",
		endDate:new Date(),
		//todayBtn: true,
		pickerPosition: "bottom-left"
	}).on("click",function(ev){
		//$("#startTime").datetimepicker("setEndDate", 1470393604852);
	});
	$("#query_endDate").datetimepicker({
		format: "yyyy-mm-dd",
		autoclose: true,
		minView: "month",
		maxView: "decade",
		endDate:new Date(),
		todayBtn: true,
		pickerPosition: "bottom-left"
	}).on('changeDate',function(ev){
		//console.log(ev.currentTarget.value)
		$("#query_startDate").datetimepicker("setEndDate", ev.currentTarget.value);
	});

	 $('#search-btn').click(function(){
 		console.log('----search-btn-----')
    	doSearch();
	});
$('#reset-btn').click(function(){
		$('#search-form')[0].reset();
	});
	doSearch();
	
});