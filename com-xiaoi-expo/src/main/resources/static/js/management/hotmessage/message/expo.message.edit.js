$(function() {
    $.common.initUpload('message-img-upload',
        function(res){
            if($("#img-list").find('.img-outer').length < 3){
                $("#img-list").append('<div class="img-outer" value="' + res.relativeUrl + '"><img class="upload-img" src="' + res.url +'" value="' + res.relativeUrl + '"><i class="layui-icon img-del">&#x1006;</i> </div>');
            }else{
                layer.msg('最多只能上传3张图片，请删除其他图片重新上传',{icon: 2});
            }

        }, function(res){

        }
    );
    /*var type = $("#type").val();
    layui.use(['layer', 'form'], function(){
        //debugger;
        var layer = layui.layer
            ,form = layui.form;

        //var type = $("#type").val();
        if(type == "look"){
            $("#typeBtn").hide();
        }else if (type == "edit"){
            $("#typeBtn").val("修改");
        }

        //layer.msg('Hello World');
    });*/

    var messageContent = layui.layedit.build('message-content', {
        tool:[
            'strong' //加粗
            ,'italic' //斜体
            ,'underline' //下划线
            ,'del' //删除线
            ,'|' //分割线
            ,'left' //左对齐
            ,'center' //居中对齐
            ,'right' //右对齐
            ,'link' //超链接
            ,'unlink' //清除链接
        ]

    });
    $('body').on( 'click','.img-del',function(){
        $(this).parent().remove();
    })
    //表单提交事件
    $('body').on('click', '#btnCancel', function(data1) {
        debugger;
        parent.location.reload();
    })

    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data1) {
        var data = {};
        data.id = $("#user-edit-id").val();
        data.title = $("#message-title").val();
        data.content = layui.layedit.getContent(messageContent);
        /*var guest = {};
        guest.id = $("#guest-name").select2("data")[0].id;*/

        if($("#img-list").find('.img-outer').length != 0){
            var picture = "";
            debugger;
            $('.img-outer').each(function () {
                picture = picture + $(this).attr("value");
                picture = picture + ",";
            })

            picture = picture.substring(0, picture.length - 1);
            // for (var i=0; i< $("#img-list").find('.img-outer').length; i++){
            //
            //     picture = picture + $("#img-list").find('.img-outer')[i].attr('value');
            //     if(i != $("#img-list").find('.img-outer').length -1){
            //         picture = picture + ',';
            //     }
            // }
            data.picture = picture;
        }

        data.knowledge =$("#message-knowledge").val();
        layer.load(1);
        $.post("/management/message/save", data, function(data){
            layer.closeAll('loading');
            if(data.code==200){
                /*var confirmIndex = layer.confirm( "短讯修改成功！" , function(index){
                    debugger;
                    $("#user-edit-id").val('');
                    $("#message-title").val('');
                    //$("#message-content").val('');
                    //layui.layedit.getContent(messageContent);
                    //$("#guest-name").val(null).trigger("change");
                    $("#img-list").empty();
                    $("#message-knowledge").val('')
                    layer.close(index);
                },function () {
                    parent.location.reload();
                });*/
                parent.location.reload();
            }else{
                layer.msg(data.msg,{icon: 2});
            }
        }, "JSON");
        return false;
    });

    function formatRepo(repo){
        debugger;
        return repo.name;
    }

});
