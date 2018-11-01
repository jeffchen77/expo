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

    //表单提交事件
    $('body').on('click', '#btnCancel', function(data1) {
        debugger;
        parent.location.reload();
    })
    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data1) {
        var data = {};
        data.title = $("#guest-talk-title").val();
        data.content = layui.layedit.getContent(editIndex)
        var guest = {};
        // guest.id = $("#guest-name").select2("data")[0].id;
        data.guestId= $("#guest-name").select2("data")[0].id;
        if(data.guestId == null || data.guestId == undefined){
            layer.msg('请选择大咖！',{icon: 2});
            return;
        }
        if($("#img-list").find('.img-outer').length != 0){
            var picture = "";
            $('.img-outer').each(function () {
                picture = picture + $(this).attr("value");
                picture = picture + ",";
            })

            picture = picture.substring(0, picture.length - 1);
            data.picture = picture;
        }

        data.knowledge =$("#guest-talk-knowledge").val();
        //获取是否大屏展示
        data.isShowScreen = $("input[name='isShowScreen']:checked").val();
        layer.load(1);
        $.post("/management/guesttalk/save", data, function(data){
            layer.closeAll('loading');
            if(data.code==200){
                var confirmIndex = layer.confirm('添加成功，继续添加？', function(index){
                    debugger;
                    $("#guest-talk-title").val('');
                    $(window.frames["LAY_layedit_1"].document).find('body').html("");
                    layui.layedit.setContent();
                    $("#guest-name").val(null).trigger("change");
                    $("#img-list").empty();
                    $("#guest-talk-knowledge").val('')
                    layer.close(index);
                },function () {
                    parent.location.reload();
                });
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
