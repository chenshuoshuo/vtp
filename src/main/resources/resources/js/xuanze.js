$(function(){
	var num=$(".option").length;
	if (num>3) {
		$(".btn").show();
		var w=num*326-3;
		$(".option-out").width(w);
		
		carousel();
		function carousel(){
			$(".btn").show();
			flog=true;
			$(".btn-R").click(function(){
				if (flog) {
					flog=!flog;
					$(".option-out").animate({left: '-351px'},"slow",function(){
						$(this).children().first().appendTo(".option-out");
						$(".option-out").css({left:"0px"});
						flog=!flog;
					});
				};
			})
			$(".btn-L").click(function(){
				// $(".option").first().css({margin:0});
				if (flog) {
					flog=!flog;
					var last=$(".option-out").children().last();
					var first=$(".option-out").children().first();
					$(last).prependTo(".option-out")
					$(".option-out").css({left:"-351px"});
					$(".option-out").animate({left: '0px'},"slow",function(){
						flog=!flog;
					});
				};
				

			})
		}
		
	}else{

	}

	$(".touxiang").mouseenter(function(){
		$(".touxiang-start").show();
	})
	$(".touxiang").mouseleave(function(){
		$(".touxiang-start").hide();
	})
	
})