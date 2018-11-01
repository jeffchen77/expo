$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/message/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            /*{field:'id', sort: false, title: 'id', width:240},*/
            {align:'center',type:'numbers', sort: false, title: '序号', width:100},
            {align:'center',field:'title', sort: false, title: '标题' },
            {field:'picture', sort: false, align:'center',  templet:function(d){
                    var imgTab = '';
                    if(d.picture != null){
                        var pictures = d.picture.split(',');
                        for(var i=0; i<pictures.length; i++){
                            imgTab = imgTab + '<img src="' + pictures[i]+ '" style="margin-left: 5px; margin-right:5px;">';
                        }
                    }
                    return imgTab;
                }, title: '图片', width:260},
            {field:'isShowScreen', sort: false, templet: '#statusTpl',width: 90, title: '大屏显示'},
            {field:'isShowIndex', sort: false, templet: '#statusIndex',width: 90, title: '首页显示'},
            {align:'center', toolbar: '#barTpl', width: 200, title: '操作'}
        ]]
    });

    //工具条点击事件
    layui.table.on('tool(table)', function(obj){
        var data = obj.data;
        var layEvent = obj.event;

        if(layEvent === 'look'){ //查看
            console.log(data);
            showEditModel(data, "look");
        }else if(layEvent === 'delete'){ //删除
            doReSet(obj.data.id);
        }else if(layEvent === 'edit'){ //修改
            showEditModel(data, "edit");
        }
    });

    //监听状态开关操作(是否大屏显示)
    layui.form.on('switch(statusCB)', function(obj){
        updateShow(obj);
    });

    //监听状态开关操作（是否首页显示）
    layui.form.on('switch(statusIndex)', function(obj){
        updateIndex(obj);
    });

    /*//监听状态开关操作
    layui.form.on('switch(statusCBD)', function(obj){
        $("#message-show-switch").val(obj.elem.checked?0:1)
    });*/

    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});

//搜索
function doSearch(table){
    var value = $("#searchValue").val();
    layui.table.reload('table', {where: {searchName: value}});
}

//删除
function doReSet(messageId){
    layer.confirm('确定要删除吗？', function(index){
        layer.close(index);
        layer.load(1);
        $.post("/management/message/delete", {
            id: messageId
        }, function(data){
            layer.closeAll('loading');
            if(data.code==200){
                layer.msg(data.msg,{icon: 1});
            }else{
                layer.msg(data.msg,{icon: 2});
            }
            layui.table.reload('table', {});
        },"JSON");
    });
}

//更改状态（是否在大屏显示）
function updateShow(obj){
    layer.load(1);
    var newStatus = obj.elem.checked?0:1;
    $.post("/management/message/updateShow", {
        messageId: obj.elem.value,
        show: newStatus,
    }, function(data){
        layer.closeAll('loading');
        if(data.code==200){
            layui.table.reload('table', {});
        }else{
            layer.msg(data.msg,{icon: 2});
            layui.table.reload('table', {});
        }
    });
}

//更改状态（是否在首页显示）
function updateIndex(obj){
    layer.load(1);
    var newStatus = obj.elem.checked?0:1;
    $.post("/management/message/updateIndex", {
        messageId: obj.elem.value,
        show: newStatus,
    }, function(data){
        layer.closeAll('loading');
        if(data.code==200){
            layui.table.reload('table', {});
        }else{
            layer.msg(data.msg,{icon: 2});
            layui.table.reload('table', {});
        }
    });
}
//添加
$("#addBtn").on("click",function(){
    var index = layui.layer.open({
        title : '',
        type : 2,
        content : "/management/message/add",
        success : function(layero, index){
            setTimeout(function(){
                layui.layer.tips('点击此处返回图文短讯', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            },500)
        }
    })
    layui.layer.full(index);
})

//修改
function showEditModel(obj, type){
    var id = obj.id;
    var index = layui.layer.open({
        title : '',
        type : 2,
        content : "/management/message/add?id="+id+"&type="+ type,
        success : function(layero, index){
            setTimeout(function(){
                layui.layer.tips('点击此处返回图文短讯', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            },500)

        }
    })
    layui.layer.full(index);
}