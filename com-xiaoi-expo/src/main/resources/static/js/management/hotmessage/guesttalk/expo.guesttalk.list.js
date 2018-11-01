$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/guesttalk/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers', width:60,title: '序号', align:'center'},
            {field:'title', sort: true, title: '标题', align:'center'},
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
            {field:'isShowScreen', sort: false, templet: '#statusTpl',width: 100, title: '大屏显示'},
            {align:'center', toolbar: '#barTpl', width: 200, title: '操作'}
        ]]
    });


    $("#addBtn").on("click",function(){
        openModal();
    })

    //工具条点击事件
    layui.table.on('tool(table)', function(obj){
        var data = obj.data;
        var layEvent = obj.event;

        if(layEvent === 'edit'){ //修改
            console.log(data);
            openModal(data, 'edit');
        }else if(layEvent === 'delete'){ //删除
            debugger;
            doDelete(data);
        }else if(layEvent == 'view'){
            openModal(data, 'view');
        }
    });

    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });

});
//监听状态开关操作
layui.form.on('switch(statusCB)', function(obj){
    updateStatus(obj);
});
function openModal(data, type) {
    var url = "/management/guesttalk/add";
    if(data != null && type != null){
        url = "/management/guesttalk/" + type + "?id=" + data.id;
    }

    var index = layui.layer.open({
        title : '',
        type : 2,
        content : url,
        success : function(layero, index){
            setTimeout(function(){
                layui.layer.tips('点击此处返回大佬说', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            },500)
        }
    })
    layui.layer.full(index);
}
//搜索
function doSearch(table){
    var value = $("#searchValue").val();
    layui.table.reload('table', {where: {searchName: value}});
}


//删除
function doDelete(obj){
    debugger;
    layer.confirm('确定要删除吗？', function(index){
        layer.close(index);
        layer.load(1);
        $.ajax({
            url: "/management/guesttalk/delete?id=" + obj.id,
            type: "GET",
            dataType: "JSON",
            success: function(data){
                layer.closeAll('loading');
                if(data.code==200){
                    layer.msg(data.msg,{icon: 1});
                    doSearch();
                }else{
                    layer.msg(data.msg,{icon: 2});
                }
            }
        });
    });
}

//更改状态
function updateStatus(obj){
    layer.load(1);
    var newStatus = obj.elem.checked?0:1;
    $.post("/management/guesttalk/updateStatus", {
        id: obj.elem.value,
        status: newStatus,
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