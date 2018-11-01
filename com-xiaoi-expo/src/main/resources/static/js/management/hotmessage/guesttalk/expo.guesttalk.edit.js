$(function() {

    $("#guest-name").select2({
        ajax:({
            url: '/management/config/guest/findByPagging',
            data: function (params) {
                var query = {
                    searchName: params.term,
                    page: 1,
                    limit:10
                }

                // Query parameters will be ?search=[term]&type=public
                return query;
            },
            processResults: function (data) {
                var result = [];
                for(var i=0; i<data.data.length; i++){
                    var temp = {};
                    temp.id=data.data[i].id;
                    temp.text= data.data[i].name;
                    result.push(temp);
                }
                // Tranforms the top-level key of the response object from 'items' to 'results'
                return {
                    results: result
                };
            },
            escapeMarkup: function (markup) { debugger;return markup; }, // let our custom formatter work
            minimumInputLength: 1,
            templateResult: formatRepo
        })
    });
    debugger;
    // $('.selecter').html('<option value="' + value + '">' + text + '</option>').trigger("change");
   $("#guest-name").html('<option value="' + $("#guest-id").val() + '"> 马云 </option>').trigger('change');
    $.common.initUpload('guest-talk-upload',
        function(res){
            if($("#img-list").find('.img-outer').length < 3){
                $("#img-list").append('<div class="img-outer" value="' + res.relativeUrl + '"><img class="upload-img" src="' + res.url +'" value="' + res.relativeUrl + '"><i class="layui-icon img-del">&#x1006;</i> </div>');
            }else{
                layer.msg('最多只能上传3张图片，请删除其他图片重新上传',{icon: 2});
            }

        }, function(res){

        }
    );


    var editIndex = layui.layedit.build('guest-talk-content', {
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

    $('body').on('click', '#btnCancel', function(data1) {
        debugger;
        parent.location.reload();
    })
    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data1) {
        var data = {};
        data.title = $("#guest-talk-title").val();
        data.content = layui.layedit.getContent(editIndex)
        data.id=$("#user-edit-id").val();
        data.guestId= $("#guest-name").select2("data")[0].id;
        if(data.guestId == null || data.guestId == undefined){
            layer.msg('请选择大咖！',{icon: 2});
            return;
        }
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

        data.knowledge =$("#guest-talk-knowledge").val();
        layer.load(1);
        $.post("/management/guesttalk/save", data, function(data){
            layer.closeAll('loading');
            if(data.code==200){
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
