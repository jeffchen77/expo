$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/activity/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers', width:60, title: '序号'},
            {field:'name', sort: true, title: '活动名称', width:280},
            {field:'locationType', sort: true, templet:function(d){
            	debugger;
            	if(d.locationType == null || d.locationType == ''){
            		return '';
            	}else{
            		if(d.locationType == '0'){
            			var displayname = d.pavilionDic?d.pavilionDic.displayName : '';
            			var code = d.booth?d.booth.code : '';
            			return displayname + ' ' + code;
            		}else if(d.locationType == '1'){
            			var exhibitionname = d.exhibition?d.exhibition.name : '';
            			var exhibitionHallname = d.exhibitionHall?d.exhibitionHall.name : '';
            			return exhibitionname + ' ' + exhibitionHallname;
            		}else{
            			return '';
            		}
            	}
            }, title: '活动地点'},
            {field:'activityTime', sort: false, templet:function(d){
            	if(d.activityStartTime != null && d.activityEndTime != null){
            		return d.activityStartTime+' - '+d.activityEndTime;
            	}else{
                	return '';
            	}
            }, title: '活动时间', width:300},
            {field:'enterprise', sort: false, templet:function(d){
            	if(d.enterprise == null || d.enterprise == ''){
            		return '';
            	}else{
                	return d.enterprise.name;
            	}
            }, title: '举办方', width:160},
            {align:'center', toolbar: '#barTpl', width: 160, title: '操作'}
        ]]
    });

    /*$(".layui-table-view").attr("style","height:520px;overflow-y:auto;padding-bottom:30px;");*/
    //添加按钮点击事件
    $("#addBtn").on("click",function(){
    	var index = layui.layer.open({
            title : '',
            type : 2,
            content : "/management/activity/add",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回活动列表', '.layui-layer-setwin .layui-layer-close', {
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
        if(layEvent === 'view'){
        	var index = layui.layer.open({
                title : '',
                type : 2,
                content : "/management/activity/view?id="+data.activityId,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回活动列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            });
            layui.layer.full(index);
        }else if(layEvent === 'edit'){
        	var index = layui.layer.open({
                title : '',
                type : 2,
                content : "/management/activity/edit?id="+data.activityId,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回企业库', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            });
            layui.layer.full(index);
        }else if(layEvent === 'delete'){ //删除记录
        	debugger;
        	doDelete(obj.data.activityId);
        }
    });

    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});
//删除
function doDelete(id){
	console.log(id);
    layer.confirm('确定要删除吗？', function(index){
        layer.close(index);
        layer.load(1);
        var value = $("#searchValue").val();
        $.post("/management/activity/delete", {
        	activityId: id
        }, function(data){
        	layer.closeAll('loading');
            if(data.code==200){
                layui.table.reload('table', {where: {searchName: value}});
            }else{
            	layer.msg(data.msg,{icon: 2});
            	layui.table.reload('table', {where: {searchName: value}});
            }
        });
    });
}
//搜索
function doSearch(table){
    var value = $("#searchValue").val();
    layui.table.reload('table', {where: {searchName: value}, page: { curr: 1}});
}