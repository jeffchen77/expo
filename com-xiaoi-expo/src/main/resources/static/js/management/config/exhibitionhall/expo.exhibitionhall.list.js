$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/config/exhibitionhall/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers',title: '序号', width:160},
            {field:'name', sort: true, title: '议会厅', width:280},   
            {field:'exhibition', sort: false,templet:function(d){if(d.exhibition==null)return'';return d.exhibition.name;}, title: '会场', width:320},
            {field:'picture', sort: false, title: '图片',templet:function(d){
            	if(d.picture!='' && d.picture!=null){
            	var domain=$("#ftpDomain").val();
            	imgTab = '<div><img src="' + domain+d.picture+ '" style="margin-left: 5px; margin-right:5px;"><div>';
            	return imgTab; 
            	}else{
            		return "";
            	}
            }, width:220},
            {align:'center', toolbar: '#barTpl', title: '操作'}
        ]]
    });

   
    $("#addBtn").on("click",function(){
        var index = layui.layer.open({
            title : '',
            type : 2,
            content : "/management/config/exhibitionhall/add",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回议会厅', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
    });
    
    
    //工具条点击事件
    layui.table.on('tool(table)', function(obj){
        var data = obj.data;
        var layEvent = obj.event;
        if(layEvent === 'view'){
        	var index = layui.layer.open({
                title : '',
                type : 2,
                content : "/management/config/exhibitionhall/view?exhibitionHallId="+obj.data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回会场库', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            })
            layui.layer.full(index);
        }else if(layEvent === 'edit'){
        	var index = layui.layer.open({
                title : '',
                type : 2,
                content : "/management/config/exhibitionhall/edit?exhibitionHallId="+obj.data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回会场库', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            })
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
function doDelete(exhibitionHallId){
	layer.confirm('确定要删除吗？', function(index){
	    layer.close(index);
	    layer.load(1);
	    $.ajax({
            async: false,
            url: "/management/config/exhibitionhall/deleteExhibitionHall",
            data: {exhibitionHallId: exhibitionHallId},
            dataType: "json",
            type: "post",
            success: function(data){
            layer.closeAll('loading');
	        if(data.code==200){
	            layui.table.reload('table', {});
	        }else{
	            layer.msg(data.msg,{icon: 2});
	            layui.table.reload('table', {});
	        }    
            },
			error: function(data){
			layer.closeAll('loading');
			layer.msg('网络或者服务器故障',{icon: 2});
			}
        });
	});
}

//搜索
function doSearch(table){
    var value = $("#searchValue").val();
    layui.table.reload('table', {where: {searchName: value}, page: { curr: 1}});
}


