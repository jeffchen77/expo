function navBar(strData){
	var data;
	debugger;
	if(typeof(strData) == "string"){
		var data = JSON.parse(strData); //部分用户解析出来的是字符串，转换一下
	}else{
		data = strData;
	}
	data = data.menu;
	var ulHtml = '<ul class="layui-nav layui-nav-tree">';
	for(var i=0;i<data.length;i++){
		if(data[i].spread){
			ulHtml += '<li class="layui-nav-item layui-nav-itemed">';
		}else{
			ulHtml += '<li class="layui-nav-item">';
		}
		if(data[i].subModules != undefined && data[i].subModules.length > 0){
			ulHtml += '<a href="javascript:;">';
			if(data[i].moduleIcon != undefined && data[i].moduleIcon != ''){
				if(data[i].moduleIcon.indexOf("icon-") != -1){
					ulHtml += '<i class="iconfont '+data[i].moduleIcon+'" data-icon="'+data[i].moduleIcon+'"></i>';
				}else{
					ulHtml += '<i class="layui-icon" data-icon="'+data[i].moduleIcon+'">'+data[i].moduleIcon+'</i>';
				}
			}
			ulHtml += '<cite>'+data[i].moduleName+'</cite>';
			ulHtml += '<span class="layui-nav-more"></span>';
			ulHtml += '</a>';
			ulHtml += '<dl class="layui-nav-child">';
			for(var j=0;j<data[i].subModules.length;j++){
				if(data[i].subModules[j].target == "_blank"){
					ulHtml += '<dd><a href="javascript:;" data-url="'+data[i].subModules[j].moduleValue+'" target="'+data[i].subModules[j].target+'">';
				}else{
					ulHtml += '<dd><a href="javascript:;" data-url="'+data[i].subModules[j].moduleValue+'">';
				}
				if(data[i].subModules[j].moduleIcon != undefined && data[i].subModules[j].moduleIcon != ''){
					if(data[i].subModules[j].moduleIcon.indexOf("icon-") != -1){
						ulHtml += '<i class="iconfont '+data[i].subModules[j].moduleIcon+'" data-icon="'+data[i].subModules[j].moduleIcon+'"></i>';
					}else{
						ulHtml += '<i class="layui-icon" data-icon="'+data[i].subModules[j].moduleIcon+'">'+data[i].subModules[j].moduleIcon+'</i>';
					}
				}
				ulHtml += '<cite>'+data[i].subModules[j].moduleName+'</cite></a></dd>';
			}
			ulHtml += "</dl>";
		}else{
			if(data[i].target == "_blank"){
				ulHtml += '<a href="javascript:;" data-url="'+data[i].moduleValue+'" target="'+data[i].target+'">';
			}else{
				ulHtml += '<a href="javascript:;" data-url="'+data[i].moduleValue+'">';
			}
			if(data[i].moduleIcon != undefined && data[i].moduleIcon != ''){
				if(data[i].moduleIcon.indexOf("icon-") != -1){
					ulHtml += '<i class="iconfont '+data[i].moduleIcon+'" data-icon="'+data[i].moduleIcon+'"></i>';
				}else{
					ulHtml += '<i class="layui-icon" data-icon="'+data[i].moduleIcon+'">'+data[i].moduleIcon+'</i>';
				}
			}
			ulHtml += '<cite>'+data[i].moduleName+'</cite></a>';
		}
		ulHtml += '</li>';
	}
	ulHtml += '</ul>';
	return ulHtml;
}
