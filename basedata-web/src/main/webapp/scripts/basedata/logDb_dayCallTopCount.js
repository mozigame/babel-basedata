Array.prototype.contains = function (obj) {  
    var i = this.length;  
    while (i--) {  
        if (this[i] === obj) {  
            return true;  
        }  
    }  
    return false;  
} 

function getDayCallCount(jsonParam){
	load();
	var url='/basedata/logdb/dayIntfCallTopCount';
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
			
			var xAxis_days=getValueByAttr(dataList, 'cdate');
			//console.log(xAxis_days);
			
			var series_intfCode=getSeries_IntfCode(dataList);
			
			showHChart(xAxis_days, series_intfCode);
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
 
function getValueByAttr(list, attr){
	var arrays=[];
	var v;
	//console.log(list);
	for(var i=0; i<list.length; i++){
		v=list[i][attr];
		if(!arrays.contains(v)){
			arrays.push(v);	
		}
	}
	return arrays;
}

function getSeries_IntfCode(list){
	var xAxis_intfCode=getValueByAttr(list, 'intfCode');
	var series=[];
	var obj;
	var v;
	for(var i=0; i<xAxis_intfCode.length; i++){
		v=xAxis_intfCode[i];
		//console.log(v);
		obj={'name':v, data:this.getIntfCodeCount(list, v)};
		series.push(obj);
	}
	//console.log(series);
	return series;
}

function getIntfCodeCount(list, intfCode){
	var arrays=[];
	var o;
	for(var i=0; i<list.length; i++){
		o=list[i];
		if(o.intfCode==intfCode){
			arrays.push(o.count);
		}
	}
	return arrays;
}

function objValue(obj, attr){
	return obj[attr];
}

function showAttr(obj) {
	var str = "", cur;
	for (cur in obj)
		str = str + cur + "="+obj[cur]+",\t";
	alert(str)
}

function doSearch(){
	var jsonParam = $('#search-form').serializeJson();
	getDayCallCount(jsonParam);
}

function showHChart(data_xAxis, data_series){
	var topN=$("#query_topN").val();
	if(!topN){
		topN=10;
	}
	    $('#containerReport').highcharts({
	        chart: {
	            type: 'area'
	        },
	        title: {
	            text: '近期接口调用次数前'+topN+'个接口分析'
	        },
	        subtitle: {
	            text: 'Source: Wikipedia.org'
	        },
	        xAxis: {
	            categories: data_xAxis,//['1750', '1800', '1850', '1900', '1950', '1999', '2050'],
	            tickmarkPlacement: 'on',
	            title: {
	                enabled: false
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Call count'
	            },
	            labels: {
	                formatter: function () {
	                    return this.value;
	                }
	            }
	        },
	        tooltip: {
	            shared: true,
	            valueSuffix: ' count'
	        },
	        plotOptions: {
	            area: {
	                stacking: 'normal',
	                lineColor: '#666666',
	                lineWidth: 1,
	                marker: {
	                    lineWidth: 1,
	                    lineColor: '#666666'
	                }
	            }
	        },
	        series:data_series
	    });
	
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