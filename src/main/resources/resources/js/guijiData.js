		
		var userid='541503070101',isManager;
		
		if (userid) {
			$(".login-btn").hide();
			$(".user-box").show();
		} else{
			$(".login-btn").show();
			$(".user-box").hide();
		}
		
		//获取用户信息
		$.ajax({
			type:"GET",
			async:false,
			url: the_host+'/user/detail?userid='+userid,
			dataType:'json',
			success:function(Data){
				if(Data.status){
					var data=Data.data;
					if (data.avatar) {
						$(".avatar img").attr("src",data.avatar)
					}
					$(".name-number").html('<p>'+data.username+'</p><p>'+data.userId+'</p>');
					
					$(".user-name").text(data.username);
					
					isManager=Data.properties.isManager;
				}
			}
		});
		
		$(".user-box").click(function(){
			if (isManager==1) {
				window.location.href='';
			}
		})
		
		$(".user-mesg-box").click(function(e){
			e.stopPropagation();
		})
		
		// 删除判断框
		$('.user-out').on('click',function(){
			_this=$(this)
			layer.confirm('你确定要退出登录吗？', {
				shade:[0.5,'#000'],
				shadeClose: true, //点击遮罩关闭
				title:'提示',
				closeBtn:[1],//右上角关闭按钮
				btn: ['确认', '取消'],
				icon: 3,
				fadeIn: 1000,
				yes: function(index, layero){ //或者使用btn1
				    //按钮【按钮一】的回调
				    $(".login-btn").show();
					$(".user-box").hide();
					
				    layer.close(index);//关闭弹出层
				    
				    layer.msg('退出成功！');
				},cancel: function(index){ //或者使用btn2
				    //按钮【按钮二】的回调
				}
			});
			return false;
		})
		
		var data;
		$.ajax({
			type:'get',
			url:the_host+'/orgXml?userid='+userid,
			async:false,
			dataType:'xml',
			success:function(msg){
				data=msg;
				var html='';
				$(msg).find("teacher").each(function(a){ 
					html+='<ul class="people-choise-department-list" style="display: block;">';
					var _this=this;
//					$(_this).find("dept").each(function(i,i_item){
//						var otName=$(i_item).attr("name");
//						var otId=$(i_item).attr("id");
//						html+='<li><div class="people-choise-department-name">'+
//								'<i class="people-choise-bumen-label"></i>'+
//								'<cite class="people-choise-bumen">'+otName+'</cite>'+
//								'</div><ul class="people-choise-department-list">';
						$(_this).find("dept").each(function(j,j_item){
							var o1Name=$(j_item).attr("name");
							var o1Id=$(j_item).attr("id");
							html+='<li><div class="people-choise-department-name">'+
								'<i class="people-choise-bumen-label"></i>'+
								'<cite class="people-choise-bumen">'+o1Name+'</cite>'+
								'</div><ul class="people-choise-department-list">';
							$(j_item).find("tea").each(function(k,k_item){
								var teaName=$(k_item).attr("name");
								var teaId=$(k_item).attr("id");
								html+='<li>'+
									'<cite class="people-choise-person">'+teaName+'</cite>'+
									'<div class="checkbox-box checkBox-person-box">'+
									'<input type="checkbox" class="checkBox checkBox-people" id="'+teaId+'" name="'+teaName+'">'+
									'</div></li>';
							})
							html+='</ul></li>'
						})
						html+='</ul></li>'
//					})
					html+='</ul>'
				})
				$(".teaching-staff-choise").html(html);
				
				var htmlStu='',htmlStuClassSelect='';
				$(msg).find("student").each(function(a){
					htmlStu+='<ul class="people-choise-department-list" style="display: block;">';
					var _this=this;
					$(_this).find("ad").each(function(i,i_item){
						var adName=$(i_item).attr("name");
						var adId=$(i_item).attr("id");
						htmlStu+='<li><div class="people-choise-department-name second-student" code="'+adId+'">'+
								'<i class="people-choise-bumen-label"></i>'+
								'<cite class="people-choise-bumen">'+adName+'</cite>'+
								'</div><ul class="people-choise-department-list">';
						htmlStuClassSelect+='<li><div class="class-choise-department-name second-class-student" code="'+adId+'">'+
									'<i class="people-choise-bumen-label"></i>'+
									'<cite class="people-choise-bumen">'+adName+'</cite>'+
									'</div><ul class="class-choise-department-list">';
//						$(i_item).find("mj").each(function(j,j_item){
//							var mjName=$(j_item).attr("name");
//							var mjId=$(j_item).attr("id");
//							htmlStu+='<li><div class="people-choise-department-name second-student" code="'+adId+'">'+
//								'<i class="people-choise-bumen-label"></i>'+
//								'<cite class="people-choise-bumen">'+mjName+'</cite>'+
//								'</div><ul class="people-choise-department-list"></ul></li>'
//							htmlStuClassSelect+='<li><div class="class-choise-department-name second-class-student" code="'+adId+'">'+
//								'<i class="people-choise-bumen-label"></i>'+
//								'<cite class="people-choise-bumen">'+mjName+'</cite>'+
//								'</div><ul class="class-choise-department-list"></ul></li>'
//						})
						htmlStu+='</ul></li>';
						htmlStuClassSelect+='</ul></li>'
					})
					htmlStu+='</ul>'
					htmlStuClassSelect+='</ul></li>'
				})
				//添加至人员选择框教师
				$(".student-choise").html(htmlStu);
				peopleDepartmentClick();
				//添加至班级选择
				$(".class-choise-cont").html(htmlStuClassSelect);
				classDepartmentClick();
			}
		})

//加载学生三、四级
function continueLoad(pram){
	var htmlStuCon='';
	if (pram.hierarchy=="third") {
		if (pram.type=="people") {
			$(data).find("#"+pram.code).find("ci").each(function(m,m_item){
				var ciName=$(m_item).attr("name");
				var ciId=$(m_item).attr("id");
				htmlStuCon+='<li><div class="people-choise-department-name third-student" code="'+ciId+'">'+
					'<i class="people-choise-bumen-label"></i>'+
					'<cite class="people-choise-bumen">'+ciName+'</cite>'+
					'</div><ul class="people-choise-department-list">';
				htmlStuCon+='</ul></li>'
			})
		} else if(pram.type=="class"){
			$(data).find("#"+pram.code).find("ci").each(function(m,m_item){
				var ciName=$(m_item).attr("name");
				var ciId=$(m_item).attr("id");
				htmlStuCon+='<li><div class="people-choise-department-name" code="'+ciId+'">'+
					'<i class="people-choise-bumen-label"></i>'+
					'<cite class="people-choise-bumen">'+ciName+'</cite>'+
					'<div class="checkbox-box checkBox-person-box">'+
					'<input type="checkbox" class="checkBox checkBox-class" code="'+ciId+'" name="'+ciName+'">'+
					'</div></div><ul class="people-choise-department-list">';
				htmlStuCon+='</ul></li>'
			})
		}
	} else{
		$(data).find("#"+pram.code).children("stu").each(function(n,n_item){
			var stuName=$(n_item).attr("name");
			var stuId=$(n_item).attr("id");
				htmlStuCon+='<li>'+
					'<cite class="people-choise-person">'+stuName+'</cite>'+
					'<div class="checkbox-box checkBox-person-box">'+
					'<input type="checkbox" class="checkBox checkBox-people" id="'+stuId+'" name="'+stuName+'">'+
					'</div></li>';
		})
		
	}
	$(pram.elem).html(htmlStuCon);
	peopleDepartmentClick();
	peopleClick();
	classClick();
}


function peopleDepartmentClick(){
	$(".people-choise-department-name").unbind("click");
	$(".people-choise-department-name").click(function(){
		$(this).children(".people-choise-bumen-label").toggleClass("active");
		$(this).next(".people-choise-department-list").slideToggle();
		var pram={};
		pram.code=$(this).attr("code");
		pram.elem=$(this).next("ul");
		if($(this).hasClass("second-student")){
			pram.type="people";
			pram.hierarchy="third";
			continueLoad(pram);
		}else if($(this).hasClass("third-student")){
			pram.type="people";
			pram.hierarchy="fourth";
			continueLoad(pram);
		}
		
	})
}

//班级弹出框上级部门点击事件
//	classDepartmentClick();
	function classDepartmentClick(){
		$(".class-choise-department-name").unbind("click");
		$(".class-choise-department-name").click(function(){
			$(this).children(".people-choise-bumen-label").toggleClass("active");
			$(this).next(".class-choise-department-list").slideToggle();
			if($(this).hasClass("second-class-student")){
				var pram={};
				pram.code=$(this).attr("code");
				pram.elem=$(this).next("ul");
				pram.type="class";
				pram.hierarchy="third";
				continueLoad(pram);
			}
		})
	}