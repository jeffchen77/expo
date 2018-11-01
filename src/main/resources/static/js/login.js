layui.use(['form'], function() {
    var form = layui.form;
    //提交
    form.on('submit(LAY-user-login-submit)', function(obj) {
        layer.load(1);
        $.post("/management/users/login", obj.field, function(data) {
            if (data.code == 200) {
                layer.msg(data.msg,{icon: 1});
                setTimeout(function() {
                    if(window !=top){
                        top.location.href=location.href;
                    }else{
                        location.replace("/management/users/main");
                    }

                }, 2000);
            } else {
                layer.closeAll('loading');
                layer.msg(data.msg,{icon: 2});
            }
        }, "json");
    });
});
