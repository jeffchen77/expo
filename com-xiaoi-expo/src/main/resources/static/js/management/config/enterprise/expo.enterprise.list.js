$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/config/enterprise/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers', width:60, title: '序号'},
            {field:'name', sort: true, title: '参展企业', width:140},
            {field:'typeDic', sort: true, templet:function(d){
            	if(d.typeDic == null || d.typeDic == ''){
            		return '';
            	}
            	else{
            		return d.typeDic.displayName
            	}
                }, title: '企业类别', width:140},
            {field:'logo', sort: false, title: '企业logo', templet:'#logoTpl', width:100},
            {field:'theme', sort: false, title: '展出主题', width:130},
            {field:'showHighlights', sort: false, title: '展出亮点'},
            {field:'showTypeDic', sort: false, templet:function(d){            	
            	if(d.showTypeDic != null && d.showTypeDic.length > 0){
            		var showTypeStr = '';
            		for(j=0; j<d.showTypeDic.length; j++){
            			showTypeStr += d.showTypeDic[j].displayName;
            			if(j!=d.showTypeDic.length-1){
            				showTypeStr +="、";
            			}
            		}
            		return showTypeStr;
            	}
            	else{
            		return '';
            	}
            }, title: '展出类别', width:160},
            {field:'exhibtionList', sort: false, templet:function(d){
            	debugger;
            	if(d.exhibtionList != null && d.exhibtionList.length > 0){
            		var showTypeStr = '';
            		for(j=0; j<d.exhibtionList.length; j++){
            			showTypeStr += d.exhibtionList[j].code;
            			if(j!=d.exhibtionList.length-1){
            				showTypeStr +="、";
            			}
            		}
            		return showTypeStr;
            	}
            	else{
            		return '';
            	}
            }, title: '展台编号', width:100},
            {align:'center', toolbar: '#barTpl', width: 160, title: '操作'}
        ]]
    });

/*    $(".layui-table-view").attr("style","height:520px;overflow-y:auto;padding-bottom:30px;");*/
    //添加按钮点击事件
    $("#addBtn").on("click",function(){
    	var index = layui.layer.open({
            title : '',
            type : 2,
            content : "/management/config/enterprise/add",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回企业库', '.layui-layer-setwin .layui-layer-close', {
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
                content : "/management/config/enterprise/view?id="+data.id,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回企业库', '.layui-layer-setwin .layui-layer-close', {
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
                content : "/management/config/enterprise/edit?id="+data.id,
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
        	doDelete(obj.data.id);
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
        $.post("/management/config/enterprise/delete", {
        	enterpriseId: id
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