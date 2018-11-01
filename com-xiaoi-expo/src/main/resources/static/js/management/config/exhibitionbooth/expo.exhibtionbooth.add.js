$(function() {
	
	$.ajax({
        async: false,
        url: $.common.getContextPath() + "/management/dictionary/findByparentId",
        data: {parentId: 'PAV'},
        dataType: "json",
        type: "GET",
        success: function(data){
            if(!!data){
                for(var i=0; i<data.length; i++){
                	if(i==0){
                		$("#position").append('<option value="">请选择</option>');
                	}
                    $("#position").append('<option value="' + data[i].id + '">' + data[i].displayName + '</option>');
                }
            }
            layui.form.render('select');
        }
    });
	
    $.common.initUpload('exhibitionPhoto',
    		function(res){
				console.log(res);
				$("#pictureAddr").val(res.relativeUrl);
				$(".layui-upload-img").attr("src",res.url);
				$("#viewPhoto").removeClass("layui-hide");
				layer.msg('上传成功', {icon: 1});
		}, function(res){
			layer.msg('上传失败', {icon: 2});
            }
        );
    $('body').on( 'click','.img-del',function(){
        //$(this).parent().remove();
    	$(".layui-upload-img").attr("src","");
		$("#viewPhoto").addClass("layui-hide");
        $("#pictureAddr").val('')
    });
    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data) {
        data.field._method = $("#editForm").attr("method");
        layer.load(1);
        
        $.post("/management/config/exhibitionbooth/save", data.field, function(data){
            layer.closeAll('loading');
            if(data.code==200){
            	console.log(data.id);
            	if(data.id === ''){
            	var confirmIndex = layer.confirm('添加成功，继续添加？', function(index){
                     debugger;
                     $("#code").val('');
                     $("#position").val('');
                     $("#pictureAddr").val('')
                     $(".layui-upload-img").attr("src","");
                     $("#viewPhoto").addClass("layui-hide");
                     layer.close(index);
                 },function () {
                     parent.location.reload();
                 });
            	}
            	else{
            		layer.msg('修改成功',{icon: 1});
            		parent.location.reload();
            	}
             }else{
                 layer.msg(data.msg,{icon: 2});
             }
        }, "JSON");
        return false;
    });
    
  //表单取消事件
    $('body').on('click', '#btnCancel', function(data1) {
        debugger;
        parent.location.reload();
    })
   
});