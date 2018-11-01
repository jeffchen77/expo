$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/config/exhibitionbooth/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers',title: '序号', width:60},
            {field:'code', sort: true, title: '展台编号', width:180},
            {field:'position', sort: true, templet:function(d){
            	if(d.position == null || d.position == ''){
            		return '';
            	}
            	else{
            		return d.position.displayName;
            	}
                }, title: '所属展馆'},
            {field:'pictureAddr', sort: false, title: '图片',templet:function(d){
            	if(d.pictureAddr!='' && d.pictureAddr!=null){
                	imgTab = '<div><img src="' +d.pictureAddr+ '" style="margin-left: 5px; margin-right:5px;"><div>';
                	return imgTab; 
                	}else{
                		return "";
                	}
                }, width:180},
            {align:'center', toolbar: '#barTpl', width: 220, title: '操作'}
        ]]
    });

    /*$(".layui-table-view").attr("style","height:520px;overflow-y:auto;padding-bottom:30px;");*/
    //添加按钮点击事件
    $("#addBtn").on("click",function(){
        var index = layui.layer.open({
            title : '',
            type : 2,
            content : "/management/config/exhibitionbooth/add",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回展台库', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        });
        layui.layer.full(index);
    });
    
    //工具条点击事件
    layui.table.on('tool(table)', function(obj){
        var data = obj.data;
        var layEvent = obj.event;
        if(layEvent === "view"){
        	//打开查看窗口
        	var index = layui.layer.open({
                title : '',
                type : 2,
                content : "/management/config/exhibitionbooth/view?id="+data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回展台库', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            });
            layui.layer.full(index);
        }
        else if(layEvent === "edit"){
        	//打开查看窗口
        	var index = layui.layer.open({
                title : '',
                type : 2,
                content : "/management/config/exhibitionbooth/edit?id="+data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回展台库', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            });
            layui.layer.full(index);
        }else if(layEvent === 'delete'){ //删除
            doDelete(obj.data.id);
        }
    });

    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});

//删除
function doDelete(exhibitionId){
	layer.confirm('确定要删除吗？', function(index){
	    layer.close(index);
	    layer.load(1);
	    $.post("/management/config/exhibitionbooth/delete", {
	    	exhibitionId: exhibitionId,
	    }, function(data){
	        layer.closeAll('loading');
	        if(data.code==200){
	            layui.table.reload('table', {});
	        }else{
	            layer.msg(data.msg,{icon: 2});
	            layui.table.reload('table', {});
	        }
	    });
	});
}

//搜索
function doSearch(table){
    var value = $("#searchValue").val();
    layui.table.reload('table', {where: {searchName: value}, page: { curr: 1}});
}