$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/users/findByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers', width:60},
            {field:'userName', sort: true, title: '用户名', width:140},
            {field:'realUserName', sort: true, title: '姓名', width:140},
            {field:'role', sort: false,  templet:function(d){
                if(d.role == '0'){
                    return '管理员';
                } else if(d.role == '1'){
                    return '普通用户'
                }else {
                    return '访客'
                }}, title: '角色', width:100},
            {field:'userPhone', sort: false, title: '手机', width:120},
            {field:'createTime', sort: false, title: '创建时间', width:160},
            {field:'updateDate', edit:'false', sort: false, title: '上次修改时间', width:160},
            {field:'status', sort: false, templet: '#statusTpl',width: 80, title: '状态'},
            {align:'center', toolbar: '#barTpl', width: 153, title: '操作'}
        ]]
    });


    //自定义验证规则
    layui.form.verify({
        username: function(value, item){
            if(value.length > 20 || value.length < 3){
                return '用户名最多20个字符, 最少3个字符';
            }

            var id = $("#user-edit-id").val();
            var flag = true;
            debugger;
            $.get({
                url: "/management/users/checkUserName?id=" + id + "&userName=" + value,
                async: false,
                dataType: 'JSON',
                success: function(data){
                    if(data.flag == false){
                        flag = data.flag;
                    }
                }
            });
            if(!flag){
                return "用户名已存在";
            }
        }
    });
    //添加按钮点击事件
    $("#addBtn").click(function(){
        showEditModel(null);
    });

    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data) {
        data.field._method = $("#editForm").attr("method");
        layer.load(1);
        $.post("/management/users/save", data.field, function(data){
            layer.closeAll('loading');
            if(data.code==200){
                layer.msg(data.msg,{icon: 1});
                layer.closeAll('page');
                layui.table.reload('table', {});
            }else{
                layer.msg(data.msg,{icon: 2});
            }
        }, "JSON");
        return false;
    });

    //工具条点击事件
    layui.table.on('tool(table)', function(obj){
        var data = obj.data;
        var layEvent = obj.event;

        if(layEvent === 'edit'){ //修改
            console.log(data);
            showEditModel(data);
        }else if(layEvent === 'reset'){ //重置密码
            doReSet(obj.data.id);
        }
    });

    //监听状态开关操作
    layui.form.on('switch(statusCB)', function(obj){
        updateStatus(obj);
    });

    //监听状态开关操作
    layui.form.on('switch(statusCBD)', function(obj){
        $("#user-status-switch").val(obj.elem.checked?0:1)
    });

    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});

//显示表单弹窗
function showEditModel(data){
    layer.open({
        type: 1,
        title: data==null?"添加用户":"修改用户",
        area: '450px',
        offset: '30px',
        content: $("#addModel").html()
    });
    $("#editForm")[0].reset();
    $("#editForm").attr("method","POST");
    var selectItem = "";
    if(data!=null){
        $("#editForm input[name=id]").val(data.id);
        $("#editForm input[name=userName]").val(data.userName);
        $("#editForm input[name=realUserName]").val(data.realUserName);
        $("#editForm input[name=userPhone]").val(data.userPhone);
        $("#role-select").val(data.role);
        $("#sex-select").val(data.sex);
        $("#user-status-switch").val(data.status);
        debugger;
        if(data.status == '0' || data.status == ''){
            // $("#editForm input[name=status]").attr("checked", true);
            $("input[name='statusCBD']").prop('checked','checked');
            $("#user-status-switch").val(data.status);
        }
        layui.form.render('radio');
    }else{
        $("input[name='statusCBD']").prop('checked','checked');
        $("#user-status-switch").val("0");
    }
    $("#btnCancel").click(function(){
        layer.closeAll('page');
    });

    // getRoles(selectItem);
}


//删除
function doDelete(obj){
    layer.confirm('确定要删除吗？', function(index){
        layer.close(index);
        layer.load(1);
        $.ajax({
            url: "api/user/"+obj.data.userId+"?token="+getToken(),
            type: "DELETE",
            dataType: "JSON",
            success: function(data){
                layer.closeAll('loading');
                if(data.code==200){
                    layer.msg(data.msg,{icon: 1});
                    obj.del();
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
    $.post("/management/users/updateStatus", {
        userId: obj.elem.value,
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

//搜索
function doSearch(table){
    var value = $("#searchValue").val();
    layui.table.reload('table', {where: {searchName: value}});
}

//删除
function doReSet(userId){
    layer.confirm('确定要重置密码吗？', function(index){
        layer.close(index);
        layer.load(1);
        $.post("/management/users/resetPwd", {
            id: userId
        }, function(data){
            layer.closeAll('loading');
            if(data.code==200){
                layer.msg(data.msg,{icon: 1});
            }else{
                layer.msg(data.msg,{icon: 2});
            }
        },"JSON");
    });
}