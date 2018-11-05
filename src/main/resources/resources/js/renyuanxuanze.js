
function people(parameter){
	//显示部门、人员选择弹框
	$('.m-choose-people-box').show();
	
	//首次请求部门列表
	var firstAppend=$('.m-people-class');
	
	department_ajax({appendParent:firstAppend});
	
	//部门请求ajax
	function department_ajax(depParam){
		
			//再次请求部门
			$.ajax({
				type:'get',
				url:'json/department.json',
				async:false,
				dataType:'json',
				success:function(msg){
					if('success' == msg.status){
						
						depParam.appendParent.html('');
						
						//判断上级部门是否选中
						if (depParam.appendParent.parent().prev().find(".bumen-choose-check").is(":checked")) {
							
							//当上级部门选中时
							depParam.appendParent.addClass("person-all-checked");
							
							$.each(msg.data,function(i,i_item){
								
								var tempData = JSON.stringify(i_item);
								
								tempData.replace(/\"/g, "'");
								
								var html1 = '<div><li class="m-sub-class-list" code="'+i_item.code+'" is_last="'+i_item.is_last+'" src="https://p1.ssl.qhimg.com/t0146459a1fddca07c5.jpg">'+
										'<span>党政办公室21</span><i class="sub-arrow arrow-right"></i><input type="checkbox" class="bumen-choose-check" checked="checked" name="chose-people" value=""></li>'+
										'</div>'+
										'<div class="m-sub-class-ctns">'+
										'<ul class="m-class-ctns list-parent">'+
										'</ul></div>';
										
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
	
	//点击部门列表
	function departmentClick(){
		
		$(".m-sub-class-list").unbind("click");
		
		$(".m-sub-class-list").click(function(){
			
			$(this).parent().find('.sub-arrow').toggleClass('arrow-right arrow-down');
			
			$(this).parent().next('.m-sub-class-ctns').toggle();
			
			var depParam={};
			
			var is_last=$(this).attr("is_last");
			
			depParam.code=$(this).attr("code");
			
			depParam.appendParent=$(this).parent().next('.m-sub-class-ctns').find(".m-class-ctns");
			
			if (is_last=="false") {
				
				department_ajax(depParam)
				
			} else{
				
				people_ajax(depParam);
				
			}
		})
	}
	
	besure();   //确认选中参会人员
	
	cancel();   //取消选中参会人员
	
	ochecked();  //选中参会人员
	
	//选择部门、人员
	function ochecked(){
		
		if (parameter.type=="morePerson") {
			
			$(".bumen-choose-check").hide();
			
			$('.class-ctns-list').unbind("click");
			
			$('.class-ctns-list').click(function(){
				
				//oin是当前选中的li标签的input子元素
				var oin = $(this).find('.choose-check');
				
				if ($(this).find('.choose-check').is(':checked')) {
					
					oin.removeProp("checked");
					
				} else{
					
					oin.prop("checked","checked");
					
				}
				
				var txt = oin.val();
				
			})
			
			$('.choose-check').unbind("click");
		
			$('.choose-check').click(function(e){
				
				e.stopPropagation();
				
			})
		} else if(parameter.type=="onePerson"){
			
			$(".bumen-choose-check").hide();
			
			$('.class-ctns-list').unbind("click");
			
			$('.class-ctns-list').click(function(){
				
				//oin是当前选中的li标签的input子元素
				var oin = $(this).find('.choose-check');
				
				//将当前选中的人的名字作为对应的input标签的value值
				
				if ($(this).find('.choose-check').is(':checked')) {
					
					oin.removeProp("checked");
					
				} else{
					
					$('.choose-check').removeProp("prop");
					
					oin.prop("checked","checked");
					
					var str=$(this).attr("data");

					var returnVal=JSON.parse(str);
					
					layer.open({
						
					    content: '您确定要选择'+returnVal.name+'吗？'
					    
					    ,btn: ['确定', '取消']
					    
					    ,yes: function(index){
					    	
					      	parameter.calfunction(returnVal);
					      	
							$('.m-choose-people-box').hide();
							
					      	layer.close(index);
					      	
					    },
					    no:function(){
					    	
					    	oin.removeProp("checked");
					    	
					    }
				  });
				}
				
			})
			
		}else if(parameter.type=="companyPerson"){
			
			$(".bumen-choose-check").show();
			
			$('.class-ctns-list').unbind("click");
			
			$('.class-ctns-list').click(function(){
				
				if (!$(this).parent().hasClass("person-all-checked")) {
					
					if ($(this).find(".choose-check").is(":checked")) {
						
						$(this).find(".choose-check").removeProp("checked");
						
					} else{
						
						$(this).find(".choose-check").prop("checked","checked");
						
					}
					
				} else{
					
					alert("已选中部门，无法操作！")
					
				}
				
			})
			
			// 选择请假抄送部门
			$('.bumen-choose-check').unbind("click");
			
			$(".bumen-choose-check").click(function(e){
				
				e.stopPropagation();
				
				if (!$(this).parent().parent().parent().hasClass("person-all-checked")) {
					
					if ($(this).is(":checked")) {
						
						$(this).parent().parent().next().find(".list-parent").addClass("person-all-checked");
						
						$(this).parent().parent().next().find("input").prop("checked","checked");
						
					} else{
						
						$(this).parent().parent().next().children("ul").removeClass("person-all-checked");
						
						$(this).parent().parent().next().find("input").removeProp("checked");
						
					}
					
				}else{
					
					$(this).prop("checked","checked");
					
					alert("已选中部门，无法操作！")
					
				}
				
			})
			
		}
		
	}
	//确认按钮
	function besure(){
		
		$('.btn-sure').unbind("click");
		
		$('.btn-sure').click(function(){
			
			if(parameter.type=="companyPerson"){
				
				var returnDepArr=[],returnPersonArr=[];
				
				$('.m-choose-people-box').hide();
				
				$(".bumen-choose-check:checked").each(function(){
					
					if ($(this).parents(".list-parent").hasClass("person-all-checked")) {
						
						return;
						
					} else{
						
						str=$(this).parent().attr("data");
						
						var returnVal=JSON.parse(str);
						
						returnDepArr.push(returnVal);
						
					}
					
				})
		
				$(".choose-check:checked").each(function(){
					
					if ($(this).parent().parent().hasClass("person-all-checked")) {
						
						return;
						
					} else{
						
						str=$(this).parent().attr("data");
						
						var returnVal=JSON.parse(str);
						
						returnPersonArr.push(returnVal);
						
					}
					
				})
				
				//回调函数
				parameter.calfunction(returnDepArr,returnPersonArr);
				
			}else{
				
				var returnArr=[];
				
				$('.m-choose-people-box').hide();
				
				$(".choose-check:checked").each(function(){
					
					var str=$(this).parent().attr("data");
					
					var returnVal=JSON.parse(str);
					
					returnArr.push(returnVal);
					
				})
				
				//回调函数
				parameter.calfunction(returnArr);
				
			}
			
		})
		
	}
	
	//点击取消按钮
	function cancel(){
		
		$('.btn-cancel').unbind("click");
		
		$('.btn-cancel').click(function(){
			
			$('.m-choose-people-box').hide();
			
		})
		
	}
	
}
	
