$(function(){
	//点击右上角功能
	$(".operation-list").children("li").click(function(){
		$(this).siblings("li").removeClass("active");
		$(this).toggleClass("active");
		if ($(this).hasClass("active")) {
			$(".right-operation-box").addClass("show");
		} else{
			$(".right-operation-box").removeClass("show");
		}
		var index=$(this).index();
		$(".right-operation-box").children("div").eq(index).fadeIn(300).siblings("div").hide();
	})
	
	//用户切换
	$(".user-tab-list li").click(function(){
		$(this).toggleClass("active").siblings("li").removeClass("active");
//		var index=$(this).index();
//		$(".position-query-box ul").eq(index).fadeIn(300).siblings("ul").hide();
		$(".position-query-result-ul").html('<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"></li>'+
									'<li class="query-result-top-2">姓名</li>'+
									'<li class="query-result-top-3">学号</li>'+
									'<li class="query-result-top-4">时间</li>'+
									'</ul></li>');
		$(".position-show-logo").show();
		$(".position-query-result-box").hide();
	})
	
	
	//位置查询方法
//	positionQuery();
	function positionQuery(){
		var index = layer.load(1, {
		  shade: [0.5,'#333'] //0.1透明度的白色背景
		});
		$.ajax({
			type:"get",
			url:"json/distribution.json",
			async:false,
			success:function(Data){
				layer.closeAll();
				if (Data.status=="success") {
					$(".position-show-logo").hide();
					$(".position-query-result-box").show();
					$(".position-query-result-ul").html('<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"></li>'+
									'<li class="query-result-top-2">姓名</li>'+
									'<li class="query-result-top-3">学号</li>'+
									'<li class="query-result-top-4">时间</li>'+
									'</ul></li>');
					var data=Data.data;
					$.each(data,function(i,i_item){
						var app='<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"><i class="table-ranking">'+i_item.ranking+'</i></li>'+
									'<li class="query-result-top-2 color-hover">'+i_item.name+'</li>'+
									'<li class="query-result-top-3 color-hover">'+i_item.code+'</li>'+
									'<li class="query-result-top-4 color-hover">'+i_item.time+'</li>'+
									'</ul><p class="query-result-mesg-bottom color-hover">'+i_item.major+'</p>'+
									'</li>';
						$(".position-query-result-ul").append(app);
					})
				}
			}
		});
	}
	
	//位置查询事件
	$(".position-query-sure-btn").click(function(){
		positionQuery();
	})
	
	//轨迹查询方法
//	trajectoryQuery();
	function trajectoryQuery(){
		var index = layer.load(1, {
		  shade: [0.5,'#333'] //0.1透明度的白色背景
		});
		$.ajax({
			type:"get",
			url:"json/trajectory.json",
			async:false,
			success:function(Data){
				layer.closeAll();
				if (Data.status=="success") {
					$(".trajectory-show-logo").hide();
					$(".trajectory-query-result-box").show();
					$(".trajectory-query-result-ul").html('<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"></li>'+
									'<li class="query-result-top-2">姓名</li>'+
									'<li class="query-result-top-3">学号</li>'+
									'<li class="query-result-top-4">时间</li>'+
									'</ul></li>');
					var data=Data.data;
					$.each(data,function(i,i_item){
						var app='<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"><i class="table-ranking">'+i_item.ranking+'</i></li>'+
									'<li class="query-result-top-2 color-hover">'+i_item.name+'</li>'+
									'<li class="query-result-top-3 color-hover">'+i_item.code+'</li>'+
									'<li class="query-result-top-4 color-hover">'+i_item.time+'</li>'+
									'</ul><p class="query-result-mesg-bottom color-hover">'+i_item.major+'</p>'+
									'</li>';
						$(".trajectory-query-result-ul").append(app);
					})
				}
			}
		});
	}
	
	//轨迹查询事件
	$(".trajectory-query-sure-btn").click(function(){
		trajectoryQuery();
	})
	
	
	//用户信息框显示隐藏
	var flag=true;
	$(".user-box").hover(function(){
		if (flag) {
			flag=false;
			$(".user-mesg-box").fadeIn(300);
		}
	},
	function(){
		$(".user-mesg-box").fadeOut(300,function(){
			flag=true;
		});
	})
	
	
	//人员选择
	$(".choise-type-tab-list li").click(function(){
		var index=$(this).index();
		$(this).addClass("active").siblings("li").removeClass("active");
		if (index==0) {
			$(".teaching-staff-choise").fadeIn(300);
			$(".student-choise").hide();
		} else{
			$(".student-choise").fadeIn(300);
			$(".teaching-staff-choise").hide();
		}
	})
})
