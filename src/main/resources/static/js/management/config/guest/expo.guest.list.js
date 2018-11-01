$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/config/guest/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers',title: '序号', width:100},
            {field:'name', sort: true, title: '姓名', width:180},
            {field:'sex', sort: false,  templet:function(d){            	
                if(d.sex == '0'){
                    return '男';
                } else if(d.sex == '1'){
                    return '女';
                }}, title: '性别', width:160},
            {field:'photoAddr', sort: false, title: '照片',templet:function(d){
            	if(d.photoAddr!='' && d.photoAddr!=null){
            	var domain=$("#ftpDomain").val();
            	imgTab = '<div><img src="' + domain+d.photoAddr+ '" style="margin-left: 5px; margin-right:5px;"><div>';
            	return imgTab; 
            	}else{
            		return "";
            	}
            }, width:180},
            {field:'enterprise', sort: false,templet:function(d){if(d.enterprise==null)return '';return d.enterprise.name;}, title: '企业', width:220},
            {field:'position', sort: false, title: '职位', width:220},
            {align:'center', toolbar: '#barTpl', title: '操作'}
        ]]
    });

   
    $("#addBtn").on("click",function(){
        var index = layui.layer.open({
            title : '',
            type : 2,
            content : "/management/config/guest/add",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回嘉宾库', '.layui-layer-setwin .layui-layer-close', {
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
                content : "/management/config/guest/view?guestId="+obj.data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回嘉宾库', '.layui-layer-setwin .layui-layer-close', {
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
                content : "/management/config/guest/edit?guestId="+obj.data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回嘉宾库', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            })
            layui.layer.full(index);
        }else  if(layEvent === 'delete'){ //删除
            doDelete(obj.data.id);
        }
    });

    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});


//删除
function doDelete(guestId){
	layer.confirm('确定要删除吗？', function(index){
	    layer.close(index);
	    layer.load(1);
	    $.ajax({
            async: false,
            url: "/management/config/guest/deleteGuest",
            data: {guestId: guestId},
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

