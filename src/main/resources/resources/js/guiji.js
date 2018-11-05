	var managerId=userid;
	
	var contPageSize=20;
	
	// 查询记录框高度
	var WH=$(window).height();
	$(".query-result-ul-box").height(WH-385);
	
	//alert(time());
	function time(dateType){
		var date = new Date(dateType);
		Y = date.getFullYear() + '-';
		M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
		D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
		h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
		m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
		s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
		
		var newTimeType=Y+M+D+h+m+s;
		
		return newTimeType;
	}
	
	// 登录判断
//	if($(".name-number p").eq(1).text()!=''){
//		$(".smallLogin").hide();
//		$("#mask").hide();
//	}else{
//		$(".smallLogin").show();
//		$("#mask").show();
//	}
//	
//	// 注册回车键提交登录
//	$(".account,.password").bind('keypress',function(e){
//      if(e.keyCode == "13"){
//      	login();
//      }
//  });
//	
//	// 注册点击登录按钮提交登录
//	$(".submitBtn").click(function(){
//		login();
//	})
//	
//	// 注册退出登录
//	$(".user-out").click(function(){
//		$.ajax({
//			type:"POST",
//			url: the_host+'/trajectory/trajectory_logout',
//			async: true,
//			dataType:'json',
//			success:function(Data){
//				if (Data.status) {
//					window.location.reload();
//				}
//				else{
//					layer.alert(Data.message,{icon: 0});
//				}
//			}
//		});
//	})
//	
//	// 登录判定
//	function login(){
//		var _uid = $("#userid").val();
//		var _pwd = $("#password").val();
//		
//		if(_uid == ''){
//			layer.alert('用户账号不能为空',{icon: 0});
//		} else if(_pwd == ''){
//			layer.alert('用户密码不能为空',{icon: 0});
//		} else{
//			$.ajax({
//				type:"POST",
//				url: the_host+'/trajectory/trajectory_login',
//				data:{
//					userid : _uid,
//					password : _pwd
//				},
//				async: true,
//				dataType:'json',
//				success:function(Data){
//					if (Data.status) {
//						layer.msg("登录成功！",{icon: 1});
//						$(".name-number p").eq(0).text(Data.data.name);
//						$(".name-number p").eq(1).text(Data.data.userid);	
//						$(".user-name").html(Data.data.name);
//						
//						$(".smallLogin").hide();
//						$("#mask").hide();
//					}
//					else{
//						layer.alert(Data.message,{icon: 0});
//					}
//				}
//			});
//		}
//	}
	
	function listClick(){
		$(".query-result-ul").children("li").unbind("click");
		$(".query-result-ul").children("li").click(function(){
			
			$(this).addClass("now-list").siblings().removeClass("now-list");
			$(".position-mesg-current").removeClass("now");
			
			var lngLat={};
			lngLat.lng=$(this).attr("lng");
			lngLat.lat=$(this).attr("lat");
			
			var id=$(this).attr("id");
			$("."+id).addClass("now");
			map.setCenter(lngLat);
		})
	}
	
	function mapMarkerClick(){
		$(".position-mesg-current").unbind("click");
		$(".position-mesg-current").click(function(){
			
			$(".position-mesg-current").removeClass("now");
			$(this).addClass("now");
			
			var codeid=$(this).attr("codeid");
			$("#"+codeid).addClass("now-list").siblings().removeClass("now-list");
			
		})
	}
		
	// 单用户位置查询
	$(".unique-location-query").click(function(){
		var _query_arrage = $(".unique-location-query-time").html();
		var timeArray = _query_arrage.split("~");
		var _query_text = $("#unique-location-query-text").val();
		if(timeArray.length != 2){
			layer.msg("请选择查询时间范围！",{icon: 1});
		} else if(_query_text == ''){
			layer.msg("请输入学/工号/MAC地址！",{icon: 1});
		} else{
			var _query_start = timeArray[0];
			var _query_end = timeArray[1];
			
			//清除地图
			clearMap(true, true, true);
			
			var index = layer.load(1, {
				shade: [0.5,'#333'] //0.1透明度的白色背景
			});
			
			managerId=userid;
			
			$.ajax({
				type:"get",
				url:the_host + "/location/loadUserLocation?userids="+_query_text+"&startTime="+_query_start+"&endTime="+_query_end+"&inSchool=1&campusId="+campusId+"&managerId="+managerId,
				async:false,
				dataType:'json',
				success:function(Data){
					layer.closeAll();
					$(".fengye-box").hide();
					if (Data.status) {
						$(".position-show-logo").hide();
						$(".position-query-result-box").show();
						$(".location-fenye").hide();
						$(".position-query-result-ul").html('<li>'+
										'<ul class="query-result-mesg-top">'+
										'<li class="query-result-top-1"></li>'+
										'<li class="query-result-top-2">姓名</li>'+
										'<li class="query-result-top-3">学号</li>'+
										'<li class="query-result-top-4">时间</li>'+
										'</ul></li>');
						var data=Data.data;
						$(".result-data-number").text(Data.data.length);
						$.each(data, function(i,item) {
							var app='<li id="'+item.userid+'" lng="'+item.lng+'" lat="'+item.lat+'">'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"><i class="table-ranking">'+1+'</i></li>'+
									'<li class="query-result-top-2 color-hover">'+item.realname+'</li>'+
									'<li class="query-result-top-3 color-hover">'+item.userid+'</li>'+
									'<li class="query-result-top-4 color-hover" title="'+time(item.locationTime)+'">'+time(item.locationTime)+'</li>'+
									'</ul><p class="query-result-mesg-bottom color-hover">'+item.orgName+'</p>'+
									'</li>';
							$(".position-query-result-ul").append(app);
							
							// 楼层判断
							if(data.floorid != lq_current_floorid){
								$.each(lq_floor_info, function(j, j_item){
									if(j_item.floorid == data.floorid){
										resetFloor(j_item.layer_name);
										floorSwiperInit(data.floorid);
										lq_current_floorid = data.floorid;
									}
								})
							}
							
							var _el = document.createElement("div");
							_el.className = "position-mesg-bomb";
							_el.innerHTML = '<div class="position-mesg-box">'
								+ '<span class="position-mesg-name">'+item.realname+'</span>'
								+ '<span class="position-mesg-time">'+time(item.locationTime)+'</span>'
								+ '</div>'
								+ '<div class="position-mesg-current '+item.userid+'" codeid="'+item.userid+'"></div>';
							
							uniqueMarker = new mapboxgl.Marker(_el,{
								anchor:'bottom'
							})
								.setLngLat([item.lng, item.lat])
							    .addTo(map);
							map.setZoom(18);
							map.setCenter([item.lng, item.lat]);
							
						});
						
						listClick();
						mapMarkerClick();
					} else{
						$(".position-show-logo").show();
						$(".position-query-result-box").hide();
						layer.msg(Data.message,{icon: 0});
					}
				}
			});
		}
		
	})
	
	// 清除自定义MARKER、轨迹
	// t 时候清除单用户位置查询marker：true/false
	// p 时候清除多用户位置查询marker：true/false
	// q 时候清除单用户轨迹查询layer：true/false
	function clearMap(t, p, q){
		// 清除单用户位置查询的marker
		if(t && uniqueMarker != null){
			uniqueMarker.remove();
		}
		
		// 清除多用户位置查询的marker
		if(p && !markers.isEmpty()){
			for(var i = 0; i < markers.values().length; i ++){
				markers.values()[i].remove();
			}
		}
		markers.clear();
		
		// 清除单用户轨迹查询的layer
		if(q && map.getLayer("trajctory_background_layer") != undefined){
			map.removeLayer("trajctory_layer");
			map.removeLayer("trajctory_background_layer");
			map.removeSource("trajectory_source");
		}
	}
	
	
	// 多用户位置查询
	$(".multiple-location-query").click(function(){
		var _query_arrage = $(".unique-location-query-time").html();
		var timeArray = _query_arrage.split("~");
		var _query_text = $("#positionPeolpeHid").val();
		if(timeArray.length != 2){
			layer.msg("请选择查询时间范围！",{icon: 1});
		} else if(_query_text == ''){
			layer.msg("请选择查询人员！",{icon: 1});
		} else{
			
			var _query_start = timeArray[0];
			var _query_end = timeArray[1];
			
			var index = layer.load(1, {
				shade: [0.5,'#333'] //0.1透明度的白色背景
			});
			
			console.log(_query_text);
			$.ajax({
				type:"get",
				url:the_host + "/location/loadUserLocation?userids="+_query_text+"&startTime="+_query_start+"&endTime="+_query_end+"&inSchool=1&campusId="+campusId,
				async:false,
				dataType:'json',
				success:function(Data){
					layer.closeAll();
					if (Data.status) {
						user_location_list = Data.data;
						
						$(".position-show-logo").hide();
						$(".position-query-result-box").show();
						
						refreshLocation(1, contPageSize);
						map.setZoom(18);
					} else{
						$(".position-show-logo").show();
						$(".position-query-result-box").hide();
						layer.msg(Data.message,{icon: 0});
					}
				}
			});
		}
		
	})
	
	
	// 加载位置查询分页数据
	function refreshLocation(page, pageSize){
		//分页计算
		$(".location-total-page").html(user_location_list.length);
		$(".result-data-number").text(user_location_list.length);
		var _flip_html = loadFlipHtml(page, user_location_list.length, pageSize);
		$(".fengye-box").show();
		$(".location-page-num").html(_flip_html);
		$(".location-fenye").show();
		registerLocationFlip();
		
		
		//列表呈现，marker加载
		var apps = loadFlipDataAndAddMarker(page, pageSize, true);
		$(".position-query-result-ul").html(apps);
	
		listClick();
		mapMarkerClick();
	}
	
	//定位数据列表呈现，marker加载
	// t是否清除轨迹图层
	function loadFlipDataAndAddMarker(page, pageSize, t){
		// 移除当前加载的图标
		
		var _start_index = (page - 1) * pageSize;
		var _end_index = page * pageSize > user_location_list.length ? user_location_list.length : page * pageSize;
		
		//清除地图
		clearMap(true, true, t);
		
		var _html = '';
		_html += '<li>'+
			'<ul class="query-result-mesg-top">'+
			'<li class="query-result-top-1"></li>'+
			'<li class="query-result-top-2">姓名</li>'+
			'<li class="query-result-top-3">学号</li>'+
			'<li class="query-result-top-4">时间</li>'+
			'</ul></li>';
		$.each(user_location_list, function(k, k_item){
			if(k >= _start_index && k < _end_index){
				_html +='<li id="'+k_item.userid+'" lng="'+k_item.lng+'" lat="'+k_item.lat+'">'+
					'<ul class="query-result-mesg-top">'+
					'<li class="query-result-top-1"><i class="table-ranking">'+(k + 1)+'</i></li>'+
					'<li class="query-result-top-2 color-hover">'+k_item.realname+'</li>'+
					'<li class="query-result-top-3 color-hover">'+k_item.userid+'</li>'+
					'<li class="query-result-top-4 color-hover" title="'+time(k_item.locationTime)+'">'+time(k_item.locationTime)+'</li>'+
					'</ul><p class="query-result-mesg-bottom color-hover">'+k_item.orgName+'</p>'+
					'</li>';
				
				var _el = document.createElement("div");
				_el.className = "position-mesg-bomb";
				_el.innerHTML = '<div class="position-mesg-box">'
					+ '<span class="position-mesg-name">'+k_item.realname+'</span>'
					+ '<span class="position-mesg-time">'+time(k_item.locationTime)+'</span>'
					+ '</div>'
					+ '<div class="position-mesg-current '+k_item.userid+'" codeid="'+k_item.userid+'"></div>';
				
				var _marker_name = '_marker_name_' + k;
				
				markers.put(_marker_name, new mapboxgl.Marker({
								anchor:'bottom',
								element:_el
							})
					.setLngLat([k_item.lng, k_item.lat])
				    .addTo(map));
			}
		})
		
		return _html;
	}
	
	// 搜索结果分页
	function loadFlipHtml(current_page, all_rows, pageSize){
		var total_page = (all_rows % pageSize) == 0 ? (all_rows / pageSize) : (parseInt(all_rows / pageSize) + 1);
		var page_html = '';
		
		if(total_page > 5){
			if(current_page - 1 > 1){
				page_html += '<li class="paginItem"><a href="javascript:void(0);" rel="1">1</a></li>';
			}
			if(current_page - 2 > 1){
				page_html += '<div class="paginmore">...</div>';
			}
			
			if(current_page > 2){
				if((current_page + 1) < total_page){
					for(var _index = (current_page - 1); _index<(current_page + 2);_index++){
						if(_index != current_page){
							page_html += '<li class="paginItem"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
						}
						else{
							page_html += '<li class="paginItem bg-c"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
						}
					}
				}
				else{
					for(var _index = (total_page - 3); _index<(total_page + 1);_index++){
						if(_index != current_page){
							page_html += '<li class="paginItem"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
						}
						else{
							page_html += '<li class="paginItem bg-c"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
						}
					}
				}
			}
			else{
				for(var _index = 1; _index<5;_index++){
					if(_index != current_page){
						page_html += '<li class="paginItem"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
					}
					else{
						page_html += '<li class="paginItem bg-c"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
					}
				}
			}
			
			if(current_page + 2 < total_page){
				page_html += '<div class="paginmore">...</div>';
			}
			if(current_page + 1 < total_page){
				page_html += '<li class="paginItem"><a href="javascript:void(0);" rel="' + total_page + '">' + total_page + '</a></li>';
			}
		}
		else{
			for(var _index = 1; _index<(total_page+1);_index++){
				if(_index != current_page){
					page_html += '<li class="paginItem"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
				}
				else{
					page_html += '<li class="paginItem bg-c"><a href="javascript:void(0);" rel="'+_index+'">'+_index+'</a></li>';
				}
			}
		}
		
		
		return page_html;	
	}
	
	// 注册位置查询的分页点击事件
	function registerLocationFlip(){
		$(".location-page-num li").unbind("click");
		$(".location-page-num li").click(function(){
			var _page = parseInt($(this).find("a").attr("rel"));
			refreshLocation(_page, contPageSize);
		})
	}
	
	$(".select-page-box").click(function(){
		$(".select-page-box").toggleClass("toggle");
		$(".select-page-list").slideToggle();
	})
	
	$(".select-page-list li").click(function(){
		var val=$(this).attr("value");
		var text=$(this).text();
		$(".select-page-box .nowSelectLi").attr("value",val).text(text);
		contPageSize=val;
		refreshLocation(1, contPageSize);
	})
	
	// 全校用户位置查询
	function queryAllLoction(){
		var _query_arrage = $(".unique-location-query-time").html();
		var timeArray = _query_arrage.split("~");
		if(timeArray.length != 2){
			layer.msg("请选择查询时间范围！",{icon: 1});
		}  else{

			var _query_start = timeArray[0];
			var _query_end = timeArray[1];
			
			
			var index = layer.load(1, {
				shade: [0.5,'#333'] //0.1透明度的白色背景
			});
			
			setTimeout(function(){
				$.ajax({
					type:"get",
					url:the_host + "/location/loadAllLocation?startTime="+_query_start+"&endTime="+_query_end+"&inSchool=1&campusId="+campusId,
					async:false,
					dataType:'json',
					success:function(Data){
						layer.closeAll();
						if (Data.status) {
							user_location_list = Data.data;
							
							$(".position-show-logo").hide();
							$(".position-query-result-box").show();
							
							refreshLocation(1, contPageSize);
							map.setZoom(16);
						} else{
							$(".position-show-logo").show();
							$(".position-query-result-box").hide();
							layer.msg(Data.message,{icon: 0});
						}
					}
				});
			},300)
		}
	}
	
	
	// 单用户轨迹查询
	$(".trajectory-query").click(function(){
		var _query_start = $("#multiuserTime1").val();
		var _query_end = $("#multiuserTime2").val();
		var _query_text = $("#trajectory-userid").val();
		if(_query_start == ''){
			layer.msg("请选择查询开始时间！",{icon: 1});
		} else if(_query_end == ''){
			layer.msg("请选择查询结束时间！",{icon: 1});
		} else if(_query_text == ''){
			layer.msg("请输入学/工号/MAC地址！",{icon: 1});
		} else{
			
			var index = layer.load(1, {
				shade: [0.5,'#333'] //0.1透明度的白色背景
			});
			
			setTimeout(function(){
				$.ajax({
					type:"get",
					url:the_host + "/location/loadTrack?userid="+_query_text+"&startTime="+_query_start+"&endTime="+_query_end+"&inSchool=1&campusId="+campusId,
//					url:'json/track.json',
					async:false,
					dataType:'json',
					success:function(Data){
						layer.closeAll();
						if (Data.status) {
							user_location_list = Data.data;
							$(".trajectory-show-logo").hide();
							$(".trajectory-query-result-box").show();
							
							// 因为在定位数据翻页时，不清除轨迹图层
							// 在这里先删除一次
							clearMap(true, true, true);
							
							// 加载轨迹图层
							addTrajecotryLayer(Data.properties.track);
							
							refreshTrajectory(1, 20);
							map.setZoom(18);
							
						} else{
							$(".trajectory-show-logo").show();
							$(".trajectory-query-result-box").hide();
							layer.msg(Data.message,{icon: 0});
						}
					}
				});
			},300)
		}
		
	})
	
	// 加载轨迹查询定位数据分页数据
	function refreshTrajectory(page, pageSize){
		//分页计算
		$(".trajectory-total-page").html(user_location_list.length);
		var _flip_html = loadFlipHtml(page, user_location_list.length, pageSize);
		$(".fengye-box").show();
		$(".trajectory-page-num").html(_flip_html);
		$(".trajectory-fenye").show();
		registerTrajectoryFlip();
		
		
		//列表呈现，marker加载
//		var apps = loadFlipDataAndAddMarker(page, pageSize, false);

		var apps = '';
		apps += '<li>'+
			'<ul class="query-result-mesg-top">'+
			'<li class="query-result-top-1"></li>'+
			'<li class="query-result-top-2">姓名</li>'+
			'<li class="query-result-top-3">学号</li>'+
			'<li class="query-result-top-4">时间</li>'+
			'</ul></li>';
			
		var firstMesg=user_location_list[0];
		var lastMesg=user_location_list[user_location_list.length-1];
		
		apps +='<li id="'+firstMesg.userid+'" lng="'+firstMesg.lng+'" lat="'+firstMesg.lat+'">'+
				'<ul class="query-result-mesg-top">'+
				'<li class="query-result-top-999"><i class="table-label">开始</i></li>'+
				'<li class="query-result-top-2 color-hover">'+firstMesg.realname+'</li>'+
				'<li class="query-result-top-3 color-hover">'+firstMesg.userid+'</li>'+
				'<li class="query-result-top-4 color-hover" title="'+time(firstMesg.locationTime)+'">'+time(firstMesg.locationTime)+'</li>'+
				'</ul><p class="query-result-mesg-bottom color-hover">'+firstMesg.orgName+'</p>'+
				'</li>';
				
		apps +='<li id="'+lastMesg.userid+'" lng="'+lastMesg.lng+'" lat="'+lastMesg.lat+'">'+
				'<ul class="query-result-mesg-top">'+
				'<li class="query-result-top-999"><i class="table-label end-label">结束</i></li>'+
				'<li class="query-result-top-2 color-hover">'+lastMesg.realname+'</li>'+
				'<li class="query-result-top-3 color-hover">'+lastMesg.userid+'</li>'+
				'<li class="query-result-top-4 color-hover" title="'+time(lastMesg.locationTime)+'">'+time(lastMesg.locationTime)+'</li>'+
				'</ul><p class="query-result-mesg-bottom color-hover">'+lastMesg.orgName+'</p>'+
				'</li>';
		
		var lngLat={};
		lngLat.lng=firstMesg.lng;
		lngLat.lat=firstMesg.lat;
		map.setCenter(lngLat);
		
		$.each(user_location_list, function(k, k_item){
			
			var _el = document.createElement("div");
			_el.className = "position-mesg-bomb";
			_el.innerHTML = '<div class="position-mesg-box">'
				+ '<span class="position-mesg-name">'+k_item.realname+'</span>'
				+ '<span class="position-mesg-time">'+time(k_item.locationTime)+'</span>'
				+ '</div>'
				+ '<div class="position-mesg-current '+k_item.userid+'" codeid="'+k_item.userid+'"></div>';
			
			var _marker_name = '_marker_name_' + k;
			
			markers.put(_marker_name, new mapboxgl.Marker({
							anchor:'bottom',
							element:_el
						})
				.setLngLat([k_item.lng, k_item.lat])
			    .addTo(map));
		})


		$(".trajectory-query-result-ul").html(apps);
		
	
	}
	
	// 轨迹查询分页点击事件注册
	function registerTrajectoryFlip(){
		$(".trajectory-page-num li").unbind("click");
		$(".trajectory-page-num li").click(function(){
			var _page = parseInt($(this).find("a").attr("rel"));
			refreshTrajectory(_page, 20);
		})
	}
	
	// 轨迹查询添加轨迹线layer
	function addTrajecotryLayer(coordinates){
//		var coordinates = new Array();
		
//		for(var i = 1; i < user_location_list.length; i++){
//			
//			$.ajax({
//				type:"post",
//				url:"http://gis.zzuli.edu.cn/lqmap/map/route/find",
//				data:{
//					mapid : '1071',
//					routeType : 'foot',
//					startLoc : user_location_list[i - 1].lat + "," + user_location_list[i - 1].lon + ",0",
//					endLoc : user_location_list[i].lat + "," + user_location_list[i].lon + ",0"
//				},
//				async:false,
//				dataType:'json',
//				success:function(Data){
//					if (Data.status == "true") {
//						$.each(Data.routing, function(r, r_item){
//							$.each(r_item.pointList, function(p, p_item){
//								coordinates.push(p_item);
//							})
//						})
//					}
//				}
//			});
////			coordinates[i] = [user_location_list[i].lon, user_location_list[i].lat];
//		}
		if(map.getSource("trajectory_source") != undefined){
			map.removeSource("trajectory_source");
		}
		var _source = {
            "type": "geojson",
            "data": {
                "type": "Feature",
                "properties": {},
                "geometry": {
                    "type": "LineString",
                    "coordinates": coordinates
                }
            }
        };
		map.addSource("trajectory_source", _source);
		
		var background_layer = {
	        "id": "trajctory_background_layer",
	        "type": "line",
	        "source": "trajectory_source",
	        "layout": {
	            "line-join": "round",
	            "line-cap": "round"
	        },
	        "paint": {
	            "line-color": "#3d93fd",
	            "line-width": 8
	        }
	    }
		
		var trajectory_layer = {
	        "id": "trajctory_layer",
	        "type": "line",
	        "source": "trajectory_source",
	        "layout": {
	            "line-join": "round",
	            "line-cap": "round"
	        },
	        "paint": {
	            "line-width": 8,
	            "line-pattern": "location3"
	        }
	    }
		map.addLayer(background_layer);
		map.addLayer(trajectory_layer);
	}
	
	//安全管理查询请求数据
	function securityQuery(){
		var _query_arrage = $(".security-confines-result-time").html();
		var timeArray = _query_arrage.split("~");
		var _query_text = $("#securityClassHid").val();
		if(timeArray.length != 2){
			layer.msg("请选择查询时间范围！",{icon: 1});
		} else if(_query_text == ''){
			layer.msg("请选择查询班级！",{icon: 1});
		} else{
			// 检索类型，1位置总览（校内），2校外，3未知
			var _query_type = parseInt($(".security-tab-list").find(".active").attr("rel"));
			var _query_start = timeArray[0];
			var _query_end = timeArray[1];
			
			var index = layer.load(1, {
				shade: [0.5,'#333'] //0.1透明度的白色背景
			});
			
			// 位置总览，校内学生
			if(_query_type == 1){
				$.ajax({
					type:"get",
					url:the_host + "/location/loadOrgLocation?orgCodes="+_query_text+"&startTime="+_query_start+"&endTime="+_query_end+"&inSchool=1&campusId="+campusId,
					async:false,
					dataType:'json',
					success:function(Data){
						layer.closeAll();
						if (Data.status) {
							user_location_list = Data.data;
							$(".security-show-logo").hide();
							$(".security-query-result-box").show();
							
							refreshSecurityLocation(1, 20);
							map.setZoom(18);
						} else{
							$(".security-show-logo").show();
							$(".security-query-result-box").hide();
							layer.msg(Data.message,{icon: 0});
						}
					}
				});
			}
			// 校外
			else if(_query_type == 2){
				$.ajax({
					type:"get",
					url:the_host + "/location/loadOrgLocation?orgCodes="+_query_text+"&startTime="+_query_start+"&endTime="+_query_end+"&inSchool=2&campusId="+campusId,
					async:false,
					dataType:'json',
					success:function(Data){
						layer.closeAll();
						if (Data.status) {
							user_location_list = Data.data;
							$(".security-show-logo").hide();
							$(".security-query-result-box").show();
							
							refreshSecurityOutSchool(1, 20);
							map.setZoom(18);
						} else{
							$(".security-show-logo").show();
							$(".security-query-result-box").hide();
							layer.msg(Data.message,{icon: 0});
						}
					}
				});
			}
			// 位置未知
			else{
				$.ajax({
					type:"get",
					url:the_host + "/location/loadOrgUnknown?orgCodes="+_query_text+"&startTime="+_query_start+"&endTime="+_query_end+"&campusId="+campusId,
					async:false,
					dataType:'json',
					success:function(Data){
						layer.closeAll();
						if (Data.status) {
							user_location_list = Data.data;
							$(".security-show-logo").hide();
							$(".security-query-result-box").show();
							
							refreshSecurityUnkown(1, 20);
						} else{
							$(".security-show-logo").show();
							$(".security-query-result-box").hide();
							layer.msg(Data.message,{icon: 0});
						}
					}
				});
			}
		}
	}
	
	// 加载安全管理校内学生分页数据
	function refreshSecurityLocation(page, pageSize){
		//分页计算
		$(".security-total-page").html(user_location_list.length);
		var _flip_html = loadFlipHtml(page, user_location_list.length, pageSize);
		$(".fengye-box").show();
		$(".security-page-num").html(_flip_html);
		$(".security-fenye").show();
		registerSecurityLocationFlip();
		
		
		//列表呈现，marker加载
		var apps = loadFlipDataAndAddMarker(page, pageSize, true);
		$(".security-query-result-ul").html(apps);
	
		
	}
	
	// 安全管理校内学生分页-页码点击事件注册
	function registerSecurityLocationFlip(){
		$(".security-page-num li").unbind("click");
		$(".security-page-num li").click(function(){
			var _page = parseInt($(this).find("a").attr("rel"));
			refreshSecurityLocation(_page, 20);
		})
	}
	
	//学生安全管理校外学生数据列表呈现，marker加载
	function loadOutSchoolFlipDataAndAddMarker(page, pageSize){
		
		var _start_index = (page - 1) * pageSize;
		var _end_index = page * pageSize > user_location_list.length ? user_location_list.length : page * pageSize;
		
		// 清除marker
		if(!gd_markers.isEmpty()){
			for(var i = 0; i < gd_markers.values().length; i ++){
				gd_map.remove(gd_markers.values()[i]);
			}
		}
		gd_markers.clear();
		
		var _html = '';
		_html += '<li>'+
			'<ul class="query-result-mesg-top">'+
			'<li class="query-result-top-1"></li>'+
			'<li class="query-result-top-2">姓名</li>'+
			'<li class="query-result-top-3">学号</li>'+
			'<li class="query-result-top-4">时间</li>'+
			'</ul></li>';
		$.each(user_location_list, function(k, k_item){
			if(k >= _start_index && k < _end_index){
				_html +='<li>'+
					'<ul class="query-result-mesg-top">'+
					'<li class="query-result-top-1"><i class="table-ranking">'+(k + 1)+'</i></li>'+
					'<li class="query-result-top-2 color-hover">'+k_item.realname+'</li>'+
					'<li class="query-result-top-3 color-hover">'+k_item.userid+'</li>'+
					'<li class="query-result-top-4 color-hover" title="'+time(k_item.locationTime)+'">'+time(k_item.locationTime)+'</li>'+
					'</ul><p class="query-result-mesg-bottom color-hover">'+k_item.orgName+'</p>'+
					'</li>';
				
				var _el = document.createElement("div");
				_el.className = "position-mesg-bomb";
				_el.innerHTML = '<div class="position-mesg-box">'
					+ '<span class="position-mesg-name">'+k_item.realname+'</span>'
					+ '<span class="position-mesg-time">'+time(k_item.locationTime)+'</span>'
					+ '</div>'
					+ '<div class="position-mesg-current '+k_item.userid+'" codeid="'+k_item.userid+'"></div>';
				
				var _marker_name = '_marker_name_' + k;
				
				gd_markers.put(_marker_name, new AMap.Marker({
	   				content: _el,
	   				position: [k_item.lon, k_item.lat],
	   				offset: new AMap.Pixel(-24, 5),
	   				map: gd_map
	   			}));
			}
		})
		
		return _html;
	}
	
	
	
	// 加载安全管理校外学生分页数据
	function refreshSecurityOutSchool(page, pageSize){
		//分页计算
		$(".security-total-page").html(user_location_list.length);
		var _flip_html = loadFlipHtml(page, user_location_list.length, pageSize);
		$(".fengye-box").show();
		$(".security-page-num").html(_flip_html);
		$(".security-fenye").show();
		registerSecurityOutSchoolFlip();
		
		
		//列表呈现，marker加载
		var apps = loadOutSchoolFlipDataAndAddMarker(page, pageSize);
		$(".security-query-result-ul").html(apps);
	
		
	}
	
	// 安全管理校外学生分页-页码点击事件注册
	function registerSecurityOutSchoolFlip(){
		$(".security-page-num li").unbind("click");
		$(".security-page-num li").click(function(){
			var _page = parseInt($(this).find("a").attr("rel"));
			refreshSecurityOutSchool(_page, 20);
		})
	}
	
	//学生安全管理位置未知学生数据列表呈现
	function loadUnknownFlipData(page, pageSize){
		
		var _start_index = (page - 1) * pageSize;
		var _end_index = page * pageSize > user_location_list.length ? user_location_list.length : page * pageSize;
		
		// 清除marker
		clearMap(true, true, true);
		
		var _html = '';
		_html += '<li>'+
			'<ul class="query-result-mesg-top">'+
			'<li class="query-result-top-5"></li>'+
			'<li class="query-result-top-6">姓名</li>'+
			'<li class="query-result-top-7">学号</li>'+
			'</ul></li>';
		$.each(user_location_list, function(k, k_item){
			if(k >= _start_index && k < _end_index){
				_html +='<li>'+
					'<ul class="query-result-mesg-top">'+
					'<li class="query-result-top-5"><i class="table-ranking">'+(k + 1)+'</i></li>'+
					'<li class="query-result-top-6 color-hover">'+k_item.name+'</li>'+
					'<li class="query-result-top-7 color-hover">'+k_item.studentno+'</li>'+
					'</ul><p class="query-result-mesg-bottom color-hover"></p>'+
					'</li>';
			}
		})
		
		return _html;
	}
	
	
	
	// 加载安全管理位置未知学生分页数据
	function refreshSecurityUnkown(page, pageSize){
		//分页计算
		$(".security-total-page").html(user_location_list.length);
		var _flip_html = loadFlipHtml(page, user_location_list.length, pageSize);
		$(".fengye-box").show();
		$(".security-page-num").html(_flip_html);
		$(".security-fenye").show();
		registerSecurityUnknownFlip();
		
		
		//列表呈现
		var apps = loadUnknownFlipData(page, pageSize);
		$(".security-query-result-ul").html(apps);
	
		
	}
	
	// 安全管理校外学生分页-页码点击事件注册
	function registerSecurityUnknownFlip(){
		$(".security-page-num li").unbind("click");
		$(".security-page-num li").click(function(){
			var _page = parseInt($(this).find("a").attr("rel"));
			refreshSecurityUnkown(_page, 20);
		})
	}
	
		//点击右上角功能
	$(".allow-click").click(function(){
		// 清除自定义图层
		clearMap(true, true, true);
		// 如果当前地图是高德地图
		// 即点击之前正在使用安全管理（疑似校外）
		// 重新加载灵奇地图
		if(parseInt($(".security-tab-list").find(".active").attr("rel")) == 2){
			initLqMap();
		}
		$(this).siblings("li").removeClass("active");
		$(this).toggleClass("active");
		if ($(this).hasClass("active")) {
			$(".right-operation-box").addClass("show");
			$(".map-floor").addClass("moreRight");
			$(".right-tool-box").addClass("moreRight");
		} else{
			$(".right-operation-box").removeClass("show");
			$(".map-floor").removeClass("moreRight");
			$(".right-tool-box").removeClass("moreRight");
		}
//		var index=$(this).index();
//		$(".right-operation-box").children("div").eq(index).fadeIn(300).siblings("div").hide();
		
		if($(this).hasClass("position-operation-btn")){
			$(".position-query-operation-box").fadeIn(300).siblings("div").hide();
		}else if($(this).hasClass("trajectory-operation-btn")){
			$(".trajectory-query-operation-box").fadeIn(300).siblings("div").hide();
		}else if($(this).hasClass("security-operation-btn")){
			$(".security-management-operation-box").fadeIn(300).siblings("div").hide();
		}
		
	})
	
	//位置查询切换
	$(".user-tab-list li").click(function(){
		clearMap(true, true, true);
		$(this).toggleClass("active").siblings("li").removeClass("active");
		$(".position-query-result-ul").html('<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"></li>'+
									'<li class="query-result-top-2">姓名</li>'+
									'<li class="query-result-top-3">学号</li>'+
									'<li class="query-result-top-4">时间</li>'+
									'</ul></li>');
		$(".position-show-logo").show();
		$(".position-query-result-box").hide();
		
		$(".confines-time-box").hide();
		
		var index=$(this).index();
		if (index=="0") {
			$(".position-user-mesg-tab").children().hide();
			$(".position-confines-time").fadeIn(300);
			$(".position-person-input-li").fadeIn(300);
		} else if(index==1){
			$(".position-user-mesg-tab").children().hide();
			$(".position-confines-time").fadeIn(300);
			$(".position-person-li").fadeIn(300);
		}else{
			$(".position-user-mesg-tab").children().hide();
			$(".position-confines-time").fadeIn(300);
		}
	})
	
	//安全管理切换
	$(".security-tab-list li").click(function(){
		$(this).toggleClass("active").siblings("li").removeClass("active");
		$(".security-query-result-ul").html('<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"></li>'+
									'<li class="query-result-top-2">姓名</li>'+
									'<li class="query-result-top-3">学号</li>'+
									'<li class="query-result-top-4">时间</li>'+
									'</ul></li>');
		$(".security-show-logo").show();
		$(".security-query-result-box").hide();
		
		var _query_type = parseInt($(".security-tab-list").find(".active").attr("rel"));
		if(_query_type == 2){
			$(".unknow-img-box").hide();
			$(".map-box").show();
			initGdMap();
		} else if(_query_type == 1){
			$(".unknow-img-box").hide();
			$(".map-box").show();
			initLqMap();
		}  else if(_query_type == 3){
			$(".map-floor").hide();
			$(".right-tool-box").hide();
			
			$(".unknow-img-box").show();
			$(".map-box").hide();
		} 
		
		
	})
	
	
	//轨迹查询开始
	$(".security-query-sure-btn").click(function(e){
		e.stopPropagation();
		securityQuery();
	})
	
	
	//右上角用户信息框显示、隐藏
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
	
	//点击班级选择按钮，展开班级选择弹出框
	$(".security-class-select").click(function(){
		$(".class-choise-bomb").fadeIn(300);
		$("#mask").show();
	})
	
	//班级选择确定
	$(".class-choise-confirm").click(function(){
		var codeTotal='';
		$(".checkBox-class:checked").each(function(){
			codeTotal+=','+$(this).attr("code");
		})
		if (codeTotal.indexOf(',') == 0) {
			codeTotal = codeTotal.substr(1)
		}
		var num=$(".checkBox-class:checked").length;
		$(".security-class-result").text('已选'+num+'个班级');
		$("#securityClassHid").val(codeTotal);
		$(".class-choise-bomb").fadeOut(300);
		$("#mask").hide();
	})
	
	
	//班级选择弹出框、人员选择弹出框关闭按钮
	$(".class-choise-del,.people-choise-del").click(function(){
		$(".class-choise-bomb").fadeOut(300);
		$(".people-choise-bomb").fadeOut(300);
		$("#mask").hide();
	})
	
	
	
	//人员选择弹出框教职工、学生列表切换
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
	
	
	
	//班级弹出框班级选定事件
	classClick();
	function classClick(){
		$(".checkBox-class").unbind("click");
		$(".checkBox-class").click(function(){
			if ($(this).is(":checked")) {
				$(this).parent().addClass("checked");
			} else{
				$(this).parent().removeClass("checked");
			}
		})
	}
	
	//点击选择用户按钮
	$(".multiuser-select-btn").click(function(){
		$(".people-choise-bomb").fadeIn(300);
		$("#mask").show();
	})
	
	//人员选择弹出框人员部门点击事件
//	peopleDepartmentClick();
//	function peopleDepartmentClick(){
//		$(".people-choise-department-name").click(function(){
//			$(this).children(".people-choise-bumen-label").toggleClass("active");
//			$(this).next(".people-choise-department-list").slideToggle();
//		})
//	}
	
	//人员选择弹出框人员选定事件
	peopleClick();
	function peopleClick(){
		$(".checkBox-people").unbind("click");
		$(".checkBox-people").click(function(){  
			var code=$(this).attr("id");
			var name=$(this).attr("name");
			if ($(this).is(":checked")) {
				$(this).parent().addClass("checked");
				var app='<li class="people-choise-show-li" code="'+code+'">'+
						'<span class="people-choise-name-box"><p>'+name+'</p>'+
						'<i></i></span></li>';
				$(".people-choise-show-list").append(app);
				selectedPeopleClick();
			} else{
				$(this).parent().removeClass("checked");
				$("[code="+code+"]").remove();
			}
			
			var length=$(".checkBox-people:checked").length;
			$(".people-choise-choiced-num span").text(length);
		})
	}
	
	
	//人员选择弹出框已选人员删除操作
	function selectedPeopleClick(){
		$(".people-choise-show-li").unbind("click");
		$(".people-choise-show-li").click(function(){
			var id=$(this).attr("code");
			$("#"+id).removeProp("checked").parent().removeClass("checked");
			$(this).remove();
		})
	}
	
	//人员选择弹出框确定按钮，人员选择弹出框关闭
	$(".people-choise-confirm").click(function(){
		var codeTotal='';
		$(".people-choise-show-li").each(function(){
			codeTotal+=','+$(this).attr("code");
		})
		if (codeTotal.indexOf(',') == 0) {
			codeTotal = codeTotal.substr(1)
		}
		
		var num=$(".people-choise-show-li").length;
		$(".multiuser-selected-num").text('已选人数'+num+'人');
		$("#positionPeolpeHid").val(codeTotal);
		$(".people-choise-bomb").fadeOut(300);
		$("#mask").hide();
	})
	
//	department_ajax(depParam);
	//部门请求ajax
	function department_ajax(depParam){
		
			//请求部门
			$.ajax({
				type:'get',
				url:'json/department.json',
				async:false,
				dataType:'json',
				success:function(msg){
					layer.closeAll();
					if('success' == msg.status){
						
						depParam.appendParent.html('');
						
						//判断上级部门是否选中
						if (depParam.appendParent.hasClass("person-all-checked")) {
							
							//当上级部门选中时
//							depParam.appendParent.addClass("person-all-checked");
							
							$.each(msg.data,function(i,i_item){
								
								var tempData = JSON.stringify(i_item);
								
								tempData.replace(/\"/g, "'");
								
								var html1 = '<li><div class="people-choise-department-name"'+
											'code="'+i_item.code+'" is_last="'+i_item.is_last+'">'+
											'<i class="people-choise-bumen-label"></i>'+
											'<cite class="people-choise-bumen">'+i_item.is_last+'</cite>'+
											'</div><ul class="people-choise-department-list person-all-checked"></ul></li>'
								
								$(html1).appendTo(depParam.appendParent);
								
								depParam.appendParent.find(".m-sub-class-list").last().attr("data",tempData);
								
							})
							
						} else{
							
							//当上级部门未选中
							$.each(msg.data,function(i,i_item){
								
								var tempData = JSON.stringify(i_item);
								
								tempData.replace(/\"/g, "'")
								
								var html1 = '<div><li class="m-sub-class-list" code="'+i_item.code+'" is_last="'+i_item.is_last+'" src="https://p1.ssl.qhimg.com/t0146459a1fddca07c5.jpg">'+
										'<span>党政办公室21</span><i class="sub-arrow arrow-right"></i><input type="checkbox" class="bumen-choose-check" name="chose-people" value=""></li>'+
										'</div>'+
										'<div class="m-sub-class-ctns">'+
										'<ul class="m-class-ctns list-parent">'+
										'</ul></div>';
										
								$(html1).appendTo(depParam.appendParent);
								
								depParam.appendParent.find(".m-sub-class-list").last().attr("data",tempData);
								
							})
						}
						departmentClick();
						ochecked();
					}
				}
			})
	}
	
	//人员请求
	function people_ajax(peoParam){
		//再次请求部门
		$.ajax({
			type:'get',
			url:'json/MailList.json',
			async:false,
			dataType:'json',
			success:function(msg){
				if('success' == msg.status){
					
					//判断上级部门是否选中
					if (peoParam.appendParent.parent().prev().find(".bumen-choose-check").is(":checked")) {
						
						//当上级部门选中
						peoParam.appendParent.addClass("person-all-checked");
						
						$.each(msg.data,function(i,i_item){
							
							var tempData = JSON.stringify(i_item);
							
							tempData.replace(/\"/g, "'")
							
							var html1 = '<li class="class-ctns-list">'+
										'<img class="ctns-list-head head-add" src="'+i_item.photo+'">'+
										'<p class="ctns-list-name">'+i_item.name+'</p>'+
										'<p class="ctns-list-tel">'+i_item.tel+'</p>'+
										'<input type="checkbox" class="choose-check" name="chose-people" checked="checked" value="18066778816">'
										'</li>';
										
							$(html1).appendTo(peoParam.appendParent);
							
							peoParam.appendParent.find(".class-ctns-list").last().attr("data",tempData);
							
						})
					} else{
						
						//当上级部门未选中
						$.each(msg.data,function(i,i_item){
							
							var tempData = JSON.stringify(i_item);
							
							tempData.replace(/\"/g, "'")
							
							var html1 = '<li class="class-ctns-list">'+
										'<img class="ctns-list-head head-add" src="'+i_item.photo+'">'+
										'<p class="ctns-list-name">'+i_item.name+'</p>'+
										'<p class="ctns-list-tel">'+i_item.tel+'</p>'+
										'<input type="checkbox" class="choose-check" name="chose-people">'
										'</li>';
										
							$(html1).appendTo(peoParam.appendParent);
							
							peoParam.appendParent.find(".class-ctns-list").last().attr("data",tempData);
							
						})
					}
					
					departmentClick();
					ochecked();
				}
			}
		})
	}


	//以下为时间选择功能的实现
	
	//位置查询时间范围选择
	$(".position-confines-time").click(function(){
		$(this).find(".confines-time-box").show();
		var obj=$(this).find(".position-confines-result-time")
		new confinesTime({
			elem:obj
		})
	})
	
	//安全管理时间范围选择
	$(".security-confines-time").click(function(){
		$(this).find(".confines-time-box").show();
		var obj=$(this).find(".security-confines-result-time")
		new confinesTime({
			elem:obj
		})
	})
	
	
	//时间范围方法
	function confinesTime(pram){
		var obj=$(pram.elem).next(".confines-time-box");
		//时间选择框初始化
		$(obj).find(".confinesStartTime").val('')
		$(obj).find(".confinesEndTime").val('');
		//阻止冒泡
		$(obj).click(function(e){
			e.stopPropagation();
		})
		
		//执行确认操作
		var result='';
		$(obj).find(".confines-time-sure-btn").unbind("click");
		$(obj).find(".confines-time-sure-btn").click(function(){
			result=$(obj).find(".confinesStartTime").val()+'~'+$(obj).find(".confinesEndTime").val();
			$(pram.elem).html(result);
			$(obj).hide();
			
			if($(".position-operation-btn").hasClass("active") 
					&& $(".user-tab-list li").eq(2).hasClass("active")){
				queryAllLoction();
			}
		})
		
		//执行清空操作
		$(obj).find(".confines-time-clear-btn").unbind("click");
		$(obj).find(".confines-time-clear-btn").click(function(){
			$(obj).find(".confinesStartTime").val('')
			$(obj).find(".confinesEndTime").val('');
			$(obj).hide();
		})
	}
	
	
	//轨迹查询时间选择
	var multiuserTime1 = {
      elem: '#multiuserTime1',
      format: 'YYYY-MM-DD hh:mm:ss',
      min: '2018-03-18 00:00:00',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	multiuserTime2.min = datas;
      }
    };
    laydate(multiuserTime1);
    
    var multiuserTime2 = {
      elem: '#multiuserTime2',
      format: 'YYYY-MM-DD hh:mm:ss',
      min: '2018-03-18 00:00:00',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	multiuserTime1.max = datas;
      }
    };
    laydate(multiuserTime2);
    
    //位置查询时间范围选择注册
	var positionConfinesStartTime = {
      elem: '#positionConfinesStartTime',
      format: 'YYYY-MM-DD hh:mm:ss',
      min: '2018-03-18 00:00:00',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	positionConfinesEndTime.min=datas;
      }
    };
    laydate(positionConfinesStartTime);
    
    var positionConfinesEndTime = {
      elem: '#positionConfinesEndTime',
      format: 'YYYY-MM-DD hh:mm:ss',
      min: '2018-03-18 00:00:00',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	positionConfinesStartTime.max=datas;
      }
    };
    laydate(positionConfinesEndTime);
    
    //安全管理时间范围选择注册
	var securityConfinesStartTime = {
      elem: '#securityConfinesStartTime',
      format: 'YYYY-MM-DD hh:mm:ss',
      min: '2018-03-18 00:00:00',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	securityConfinesEndTime.min=datas;
      }
    };
    laydate(securityConfinesStartTime);
    
    var securityConfinesEndTime = {
      elem: '#securityConfinesEndTime',
      format: 'YYYY-MM-DD hh:mm:ss',
      min: '2018-03-18 00:00:00',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	securityConfinesStartTime.max=datas;
      }
    };
    laydate(securityConfinesEndTime);
    
    
    //楼层切换
   var holdPosition = 0;
  var mySwiper = new Swiper('.map-container',{
    slidesPerView:'auto',
    mode:'vertical',
    watchActiveIndex: true,
    nextButton: '.button-next',
    prevButton: '.button-prev',
    onTouchStart: function() {
      holdPosition = 0;
    },
    onResistanceBefore: function(s, pos){
      holdPosition = pos;
    },
    onTouchEnd: function(){
      if (holdPosition>100) {
        // Hold Swiper in required position
        mySwiper.setWrapperTranslate(0,100,0)

        //Dissalow futher interactions
        mySwiper.params.onlyExternal=true

        //Show loader
        $('.preloader').addClass('visible');
		loadNewSlides();
      }
      var now=mySwiper.activeLoopIndex;
	  $(".swiper-slide").eq(now).addClass("floor-now").siblings().removeClass("floor-now");
    },
    onSlideClick: function(){
      
    }
  })
  	// 楼层控件的上一层
	$('.button-prev').click(function(){
		mySwiper.swipePrev(); 
		var now=mySwiper.activeLoopIndex;
		$(".swiper-slide").eq(now).addClass("floor-now").siblings().removeClass("floor-now");
		var _obj = $(".swiper-slide").eq(now);
		resetFloor(_obj.attr("rel"));
		lq_current_floorid = _obj.attr("floorid");
	})
	// 楼层控件的下一层
	$('.button-next').click(function(){
		mySwiper.swipeNext(); 
		var now=mySwiper.activeLoopIndex;
		$(".swiper-slide").eq(now).addClass("floor-now").siblings().removeClass("floor-now");
		var _obj = $(".swiper-slide").eq(now);
		resetFloor(_obj.attr("rel"));
		lq_current_floorid = _obj.attr("floorid");
	})
	
	// 注册楼层控件的点击事件
	function registerFloorSwitch(){
	  $(".swiper-slide").unbind("click");
	  $(".swiper-slide").click(function(){
      var index=$(this).index();
      $(".swiper-wrapper").css({transform:'translate3d(0px, -'+25*index+'px, 0px)'});
      $(".swiper-slide").eq(index).addClass("floor-now").siblings().removeClass("floor-now");
      resetFloor($(this).attr("rel"));
      lq_current_floorid = $(this).attr("floorid");
	})
	}
	
  	// 根据楼层ID控制楼层控件的显示
	function floorSwiperInit(nowfloor){
		$(".map-floor-box .swiper-slide").each(function(i){
			if ($(this).attr("floorid")==nowfloor) {
				$(".swiper-wrapper").css({transform:'translate3d(0px, -'+25*i+'px, 0px)'});
      			$(".swiper-slide").eq(i).addClass("floor-now").siblings().removeClass("floor-now");
			}
		})
	}
	
	  var slideNumber = 0;
	  function loadNewSlides() {
	    setTimeout(function(){
	      mySwiper.setWrapperTranslate(0,0,0)
	      mySwiper.params.onlyExternal=false;
	
	      //Update active slide
	      mySwiper.updateActiveSlide(0)
	
	      //Hide loader
	      $('.preloader').removeClass('visible')
	    },1000)
	
	    slideNumber++;
	  }
	  
	  // 切换室内地图楼层
	  // t图层名称
	  function resetFloor(t){
		  map.style.setGeoJSONSourceData('indoor',
			  'http://gis.zzuli.edu.cn/lqmap/geojson/1071/'
			  + t + '/www.lqkj.com/adb8505600a2142d63ebdea8040ecca2');
	  }
