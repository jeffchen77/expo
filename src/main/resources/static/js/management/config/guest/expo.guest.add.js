$(function() {
	
    layui.form.on('select(entSelect)', function(data){
    	  console.log(data);
    	  if(data.value=='ENT'){    		      		  
    		  
    		//添加按钮点击事件
    		    $("#addEntBtn").on("click",function(){
    		        window.parent.parent.addTab($(this));
    		    })
    		    $("#addEntBtn").click();
    		    $("#ent-select").val("");
    		    layui.form.render('select');
    	  }
    	});
    
      
    $.common.initUpload('guestPhoto',
    		function(res){
				console.log(res);
				$("#photoAddr").val(res.relativeUrl);
				$(".layui-upload-img").attr("src",res.url);
				$("#viewPhoto").removeClass("layui-hide");
				layer.msg('上传成功', {icon: 1});
		}, function(res){
			layer.msg('上传失败', {icon: 2});
            }
        );
    
    $('body').on( 'click','.img-del',function(){
        $("#photoAddr").val('')
        $("#viewPhoto").addClass("layui-hide");
    })
  //表单取消事件
    $('body').on('click', '#btnCancel', function(data) {        
        parent.location.reload();
    })
    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data) {
    
        layer.load(1);
        	$.ajax({
                async: false,
                url: "/management/config/guest/save",
                data: data.field,
                dataType: "json",
                type: "post",
                success: function(data){
    			layer.closeAll('loading');
                  layer.closeAll('loading');
                if(data.code==200){
                if( $("#id").val()==''){
                    var confirmIndex = layer.confirm('添加成功，继续添加？', function(index){                   
                        $("#id").val('');
                        $("#name").val('');
                        $("#sex-select").val('');
                        $("#ent-select").val('');                   
                        $("#position").val('');
                        $("#orderNum").val('');
                        $("#photoAddr").val('');
                        $(".layui-upload-img").attr("src",'');
        				$("#viewPhoto").addClass("layui-hide");
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

