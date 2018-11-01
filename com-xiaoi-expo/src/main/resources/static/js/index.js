var $,tab,skyconsWeather;
layui.config({
	base : "/js/"
}).use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		layer = layui.layer,
		element = layui.element;
		$ = layui.jquery;
		tab = layui.bodyTab({
			openTabNum : "50",  //最大可打开窗口数量
			url : "/management/users/menu" //获取菜单json地址
		});

	$("#my-info-btn").click(function(){
		var user = getCurrentUser();
		var sex = "男";
		if(user.sex == '1'){
			sex = "女";
		}

		var role = "普通用户";
		if(user.role == '0'){
			role = "管理员";
		}
		var content = '<ul class="site-dir" style="padding:25px 35px 8px 35px;"><li>用户名：'+user.userName+'</li><li>姓名：'+user.realUserName+'</li>';
		content += '<li>手机号：'+user.userPhone+'</li><li>性别：'+sex+'</li><li>角色：'+role+'</li></ul>';
		layer.open({
			type: 1,
			title: '个人信息',
			area: '350px',
			offset: '120px',
			content: content,
			btn: ['关闭'],
			btnAlign: 'c'
		});
	});

	$("#change-pwd-btn").click(function(){
		layer.open({
			type: 1,
			title: "修改密码",
			area: '400px',
			offset: '120px',
			content: $("#pswModel").html()
		});
		$("#pswForm")[0].reset();
		$("#pswCancel").click(function(){
			layer.closeAll('page');
		});

	});

	//隐藏左侧导航
	$(".hideMenu").click(function(){
		$(".layui-layout-admin").toggleClass("showMenu");
		//渲染顶部窗口
		tab.tabMove();
	})

	//渲染左侧菜单
	tab.render();

	//手机设备的简单适配
	var treeMobile = $('.site-tree-mobile'),
		shadeMobile = $('.site-mobile-shade')

	treeMobile.on('click', function(){
		$('body').addClass('site-mobile');
	});

	shadeMobile.on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a",function(){
		//如果不存在子级
		if($(this).siblings().length == 0){
			addTab($(this));
			$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	})

	//刷新后还原打开的窗口
	if(window.sessionStorage.getItem("menu") != null){
		menu = JSON.parse(window.sessionStorage.getItem("menu"));
		curmenu = window.sessionStorage.getItem("curmenu");
		var openTitle = '';
		for(var i=0;i<menu.length;i++){
			openTitle = '';
			if(menu[i].icon){
				if(menu[i].icon.split("-")[0] == 'icon'){
					openTitle += '<i class="iconfont '+menu[i].icon+'"></i>';
				}else{
					openTitle += '<i class="layui-icon">'+menu[i].icon+'</i>';
				}
			}
			openTitle += '<cite>'+menu[i].title+'</cite>';
			openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="'+menu[i].layId+'">&#x1006;</i>';
			element.tabAdd("bodyTab",{
				title : openTitle,
		        content :"<iframe src='"+menu[i].href+"' data-id='"+menu[i].layId+"'></frame>",
		        id : menu[i].layId
			})
			//定位到刷新前的窗口
			if(curmenu != "undefined"){
				if(curmenu == '' || curmenu == "null"){  //定位到后台首页
					element.tabChange("bodyTab",'');
				}else if(JSON.parse(curmenu).title == menu[i].title){  //定位到刷新前的页面
					element.tabChange("bodyTab",menu[i].layId);
				}
			}else{
				element.tabChange("bodyTab",menu[menu.length-1].layId);
			}
		}
		//渲染顶部窗口
		tab.tabMove();
	}

	//刷新当前
	$(".refresh").on("click",function(){  //此处添加禁止连续点击刷新一是为了降低服务器压力，另外一个就是为了防止超快点击造成chrome本身的一些js文件的报错(不过貌似这个问题还是存在，不过概率小了很多)
		if($(this).hasClass("refreshThis")){
			$(this).removeClass("refreshThis");
			$(".clildFrame .layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload(true);
			setTimeout(function(){
				$(".refresh").addClass("refreshThis");
			},2000)
		}else{
			layer.msg("您点击的速度超过了服务器的响应速度，还是等两秒再刷新吧！");
		}
	})

	//关闭其他
	$(".closePageOther").on("click",function(){
		var indexName = $("#top_tabs").find("cite").eq(0).text();
		if($("#top_tabs li").length>2 && $("#top_tabs li.layui-this cite").text()!=indexName){
			var menu = JSON.parse(window.sessionStorage.getItem("menu"));
			$("#top_tabs li").each(function(){
				if($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")){
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					//此处将当前窗口重新获取放入session，避免一个个删除来回循环造成的不必要工作量
					for(var i=0;i<menu.length;i++){
						if($("#top_tabs li.layui-this cite").text() == menu[i].title){
							menu.splice(0,menu.length,menu[i]);
							window.sessionStorage.setItem("menu",JSON.stringify(menu));
						}
					}
				}
			})
		}else if($("#top_tabs li.layui-this cite").text()==indexName && $("#top_tabs li").length>1){
			$("#top_tabs li").each(function(){
				if($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")){
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					window.sessionStorage.removeItem("menu");
					menu = [];
					window.sessionStorage.removeItem("curmenu");
				}
			})
		}else{
			layer.msg("没有可以关闭的窗口了@_@");
		}
		//渲染顶部窗口
		tab.tabMove();
	})
	//关闭全部
	$(".closePageAll").on("click",function(){
		if($("#top_tabs li").length > 1){
			$("#top_tabs li").each(function(){
				if($(this).attr("lay-id") != ''){
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					window.sessionStorage.removeItem("menu");
					menu = [];
					window.sessionStorage.removeItem("curmenu");
				}
			})
		}else{
			layer.msg("没有可以关闭的窗口了@_@");
		}
		//渲染顶部窗口
		tab.tabMove();
	})

	//切换导航栏按钮点击事件
	$("#switchNav").click(function(){
		var sideNavExpand = !$('body').hasClass('nav-mini');
		switchNav(!sideNavExpand);
	});
//手机遮罩层点击事件
	$('.site-mobile-shade').click(function(){
		switchNav(true);
	});

	//修改密码表单提交事件
	layui.form.on('submit(pswSubmit)', function(data){
		layer.load(1);
		$.post("/management/users/changePwd", data.field, function(data){
			if(data.code==200){
				layer.msg(data.msg,{icon: 1});
				setTimeout(function() {
					logout();
				}, 1500);
			}else{
				layer.closeAll('loading');
				layer.msg(data.msg,{icon: 2});
			}
		}, "JSON");
		return false;
	});


})

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}

function closeCurrent(){
	debugger;
	$(".layui-this").trigger('click');
}
function getCurrentUser(){
	var user = {};
	user.id= $("#user-id").val();
	user.role= $("#user-role").val();
	user.userName= $("#user-name").val();
	user.realUserName= $("#user-realName").val();
	user.userPhone= $("#user-phone").val();
	user.sex= $("#user-sex").val();

	return user;

}

function logout(){
	window.sessionStorage.removeItem("menu");
	menu = [];
	window.sessionStorage.removeItem("curmenu");
	$.ajax({
		url: "/management/users/logout",
		type: "POST",
		dataType: "JSON",
		success: function(data){
			location.replace("/management/users/tologin");
		}
	});
}
