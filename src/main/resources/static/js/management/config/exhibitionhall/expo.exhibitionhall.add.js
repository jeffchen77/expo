$(function() {
	

    
    $.common.initUpload('hallPhoto',
    		function(res){
				console.log(res);
				$("#picture").val(res.relativeUrl);
				$(".layui-upload-img").attr("src",res.url);
				$("#viewPhoto").removeClass("layui-hide");
				layer.msg('上传成功', {icon: 1});
		}, function(res){
			layer.msg('上传失败', {icon: 2});
            }
        );
    
    var editIndex = layui.layedit.build('hall-description', {
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
    layui.form.verify({  
    	description: function(value){  
    	if(layui.layedit.getText(editIndex).length>1000){
    			return '会场介绍字数不能超过1000';
    	} 
    	}
    	}); 
    
    $('body').on( 'click','.img-del',function(){
        $("#picture").val('')
        $("#viewPhoto").addClass("layui-hide");
    })
  //表单取消事件
    $('body').on('click', '#btnCancel', function(data) {        
        parent.location.reload();
    })
    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data) {
    	
    	data.field.description = layui.layedit.getContent(editIndex);
    
        layer.load(1);        
            $.ajax({
                async: false,
                url: "/management/config/exhibitionhall/save",
                data: data.field,
                dataType: "json",
                type: "post",
                success: function(data){
				layer.closeAll('loading');
                if(data.code==200){
				if( $("#id").val()==''){
                var confirmIndex = layer.confirm('添加成功，继续添加？', function(index){                   
                    $("#id").val('');
                    $("#name").val('');
                    $("#exhibition-select").val('');
                    $("#picture").val('');
                    $(".layui-upload-img").attr("src",'');
    				$("#viewPhoto").addClass("layui-hide");
    				$("#description").val('');
    				$("#LAY_layedit_1").contents().find('body').html('');
    				layui.form.render('select');
                    layer.close(index);
                },function () {
                    parent.location.reload();
                });
            	}else{
            		parent.location.reload();
            	}
            
                }else{
                layer.msg(data.msg,{icon: 2});
            	}
               },
				error: function(data){
				layer.closeAll('loading');
				layer.msg('网络或者服务器故障',{icon: 2});
				}
            });
        return false;
    });

   
});





