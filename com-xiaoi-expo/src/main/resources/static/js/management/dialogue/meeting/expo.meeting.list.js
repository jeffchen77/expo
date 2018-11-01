$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/dialogue/meeting/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers',title: '序号', width:100},
            {field:'typeDic', sort: true,templet:function(d){if(d.typeDic==null)'';return d.typeDic.displayName;}, title: '会议类别', width:200},
            {field:'theme', sort: false, title: '会议主题', width:280},
            {field:'totalPeople', sort: false, title: '参会人员规模', width:140},
            {field:'rang', sort: false, title: '会议时间', width:280},
            {field:'status', sort: false, title: '状态', width:100},
            {align:'center', toolbar: '#barTpl', title: '操作'}
        ]]
    });

   
    $("#addBtn").on("click",function(){
        var index = layui.layer.open({
            title : '',
            type : 2,
            content : "/management/dialogue/meeting/add",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回会议列表', '.layui-layer-setwin .layui-layer-close', {
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
                content : "/management/dialogue/meeting/view?meetingId="+obj.data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回会议列表', '.layui-layer-setwin .layui-layer-close', {
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
                content : "/management/dialogue/meeting/edit?meetingId="+obj.data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回会议列表', '.layui-layer-setwin .layui-layer-close', {
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
function doDelete(meetingId){
	layer.confirm('确定要删除吗？', function(index){
	    layer.close(index);
	    layer.load(1);
	    $.ajax({
            async: false,
            url: "/management/dialogue/meeting/deleteMeeting",
            data: {meetingId: meetingId},
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

