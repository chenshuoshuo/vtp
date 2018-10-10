	//页面高度
	var WH=$(window).height();
	$(".query-result-ul-box").height(WH-385);
	
	//点击右上角功能
	$(".allow-click").click(function(){
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
		var index=$(this).index();
		$(".right-operation-box").children("div").eq(index).fadeIn(300).siblings("div").hide();
	})
	
	//位置查询用户切换
	$(".user-tab-list li").click(function(){
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
	})
	
	
	//位置查询数据请求
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
	
	//位置查询开始
	$(".position-query-sure-btn").click(function(){
		positionQuery();
	})
	
	//轨迹查询请求数据
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
						if (i_item.state==0) {
							var app='<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"><i class="table-label">开始</i></li>'+
									'<li class="query-result-top-2 color-hover">'+i_item.name+'</li>'+
									'<li class="query-result-top-3 color-hover">'+i_item.code+'</li>'+
									'<li class="query-result-top-4 color-hover">'+i_item.time+'</li>'+
									'</li>';
						} else{
							var app='<li>'+
									'<ul class="query-result-mesg-top">'+
									'<li class="query-result-top-1"><i class="table-label end-label">结束</i></li>'+
									'<li class="query-result-top-2 color-hover">'+i_item.name+'</li>'+
									'<li class="query-result-top-3 color-hover">'+i_item.code+'</li>'+
									'<li class="query-result-top-4 color-hover">'+i_item.time+'</li>'+
									'</li>';
						}
						$(".trajectory-query-result-ul").append(app);
					})
				}
			}
		});
	}
	
	//轨迹查询开始
	$(".trajectory-query-sure-btn").click(function(){
		trajectoryQuery();
	})
	
	//安全管理查询请求数据
	function securityQuery(){
		var index = layer.load(1, {
		  shade: [0.5,'#333'] //0.1透明度的白色背景
		});
		$.ajax({
			type:"get",
			url:"json/security.json",
			async:false,
			success:function(Data){
				layer.closeAll();
				if (Data.status=="success") {
					$(".security-show-logo").hide();
					$(".security-query-result-box").show();
					$(".security-query-result-ul").html('<li>'+
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
						$(".security-query-result-ul").append(app);
					})
				}
			}
		});
	}
	
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
			codeTotal+='、'+$(this).attr("code");
		})
		if (codeTotal.indexOf('、') == 0) {
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
			codeTotal+='、'+$(this).attr("code");
		})
		if (codeTotal.indexOf('、') == 0) {
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
								
//								var html1 = '<div><li class="m-sub-class-list" code="'+i_item.code+'" is_last="'+i_item.is_last+'" src="https://p1.ssl.qhimg.com/t0146459a1fddca07c5.jpg">'+
//										'<span>党政办公室21</span><i class="sub-arrow arrow-right"></i><input type="checkbox" class="bumen-choose-check" checked="checked" name="chose-people" value=""></li>'+
//										'</div>'+
//										'<div class="m-sub-class-ctns">'+
//										'<ul class="m-class-ctns list-parent">'+
//										'</ul></div>';
										
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
			result=$(obj).find(".confinesStartTime").val()+'-'+$(obj).find(".confinesEndTime").val();
			$(pram.elem).html(result);
			$(obj).hide();
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
      format: 'YYYY/MM/DD hh:mm',
      max: laydate.now(),
      istime: true,
      istoday: false,
      choose: function(datas){
      	
      }
    };
    laydate(multiuserTime1);
    
    var multiuserTime2 = {
      elem: '#multiuserTime2',
      format: 'YYYY/MM/DD hh:mm',
      max: '2099-06-16 23:59:59',
      istime: true,
      istoday: false,
      choose: function(datas){
      	
      }
    };
    laydate(multiuserTime2);
    
    //位置查询时间范围选择注册
	var positionConfinesStartTime = {
      elem: '#positionConfinesStartTime',
      format: 'YYYY/MM/DD hh:mm',
      max: '2099-06-16 23:59:59',
      istime: true,
      istoday: false,
      choose: function(datas){
      	positionConfinesEndTime.min=datas;
      }
    };
    laydate(positionConfinesStartTime);
    
    var positionConfinesEndTime = {
      elem: '#positionConfinesEndTime',
      format: 'YYYY/MM/DD hh:mm',
      max: '2099-06-16 23:59:59',
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
      format: 'YYYY/MM/DD hh:mm',
      max: '2099-06-16 23:59:59',
      istime: true,
      istoday: false,
      choose: function(datas){
      	securityConfinesEndTime.min=datas;
      }
    };
    laydate(securityConfinesStartTime);
    
    var securityConfinesEndTime = {
      elem: '#securityConfinesEndTime',
      format: 'YYYY/MM/DD hh:mm',
      max: '2099-06-16 23:59:59',
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