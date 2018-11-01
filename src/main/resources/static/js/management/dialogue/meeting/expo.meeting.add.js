$(function() {
	

	layui.form.on('select(locationSelect)', function(data){
  	  console.log(data);
  	var option0 = "<option value=''>-请选择-</option>";
  	  if(data.value=='0'){ 
  		//选展馆
  		$("#pavilition-div").removeClass("layui-hide");
  		$("#pavilition-div").addClass("layui-show");
  		$("#booth-div").removeClass("layui-hide");
  		$("#booth-div").addClass("layui-show");
  		$("#exhibition-div").removeClass("layui-show");
  		$("#exhibition-div").addClass("layui-hide");
  		$("#exhibitionhall-div").removeClass("layui-show");
  		$("#exhibitionhall-div").addClass("layui-hide");
  		
  		$.get("/management/dictionary/findByparentId",{
	    	parentId: "PAV",
	    }, function(data){           
  			
            	   $("#pavilion-select").html(""); 
            	   $("#pavilion-select").append(option0); 
                   $.each(data, function(key, val) {
                	var option1 = $("<option>").val(val.id).text(val.displayName);
                     $("#pavilion-select").append(option1);                           
                        }); 
                   
                   $("#pavilion-select").get(0).selectedIndex=0;
                   $("#booth-select").html("");
               	  $("#booth-select").append(option0);
            
            layui.form.render('select');
        }, "JSON");
  	  }else if(data.value=='1'){
		  //选会场
	  		$("#pavilition-div").removeClass("layui-show");
	  		$("#pavilition-div").addClass("layui-hide");
	  		$("#booth-div").removeClass("layui-show");
	  		$("#booth-div").addClass("layui-hide");
	  		$("#exhibition-div").removeClass("layui-hide");
	  		$("#exhibition-div").addClass("layui-show");
	  		$("#exhibitionhall-div").removeClass("layui-hide");
	  		$("#exhibitionhall-div").addClass("layui-show");
  		  $.get("/management/config/exhibition/findAll", function(data){           
  			
           
            	   $("#exhibition-select").html("");             	   
            	   $("#exhibition-select").append(option0); 
                   $.each(data, function(key, val) {
                	   var option1 = $("<option>").val(val.id).text(val.name);
                     $("#exhibition-select").append(option1);                           
                        }); 
                   $("#exhibition-select").get(0).selectedIndex=0;
                   
                   $("#exhibitionhall-select").html("");
               	  $("#exhibitionhall-select").append(option0);
           
            layui.form.render('select');
        }, "JSON");
		  
	  }else{
		  //清空
		$("#pavilion-select").html("");
      	$("#pavilion-select").append(option0); 
      	$("#booth-select").html("");
      	$("#booth-select").append(option0);
		$("#exhibition-select").html("");
      	$("#exhibition-select").append(option0); 
      	$("#exhibitionhall-select").html("");
      	$("#exhibitionhall-select").append(option0); 
      	layui.form.render('select');
	  }
	
  		
  	  });	
	
	layui.form.on('select(pavilionSelect)', function(data){
	  	  console.log(data);
       
	  	$.get("/management/config/exhibitionbooth/findExhibitionBooth",{
	  		pavilionId: $("#pavilion-select").val(),
	    }, function(data){
	  		
	            	   $("#booth-select").html("");  	
	            	   var option0 = "<option value=''>-请选择-</option>";
	            	   $("#booth-select").append(option0); 
	                   $.each(data, function(key, val) {
	                 var option1 = $("<option>").val(val.id).text(val.code);
	                     $("#booth-select").append(option1);                           
	                        }); 
	                   $("#booth-select").get(0).selectedIndex=0;
	           
	            layui.form.render('select');
	        }, "JSON");
		
	 
	});
	
	layui.form.on('select(exhibitionSelect)', function(data){
	  	  console.log(data);
	  	 var data1 = {};
	  	 data1.exhibitionId = $("#exhibition-select").val();
	  	var option0 = "<option value=''>-请选择-</option>";
	  	$.get("/management/config/exhibitionhall/findExhibitionHall",{
	    	exhibitionId: $("#exhibition-select").val(),
	    }, function(data){
	  			            
	            	   $("#exhibitionhall-select").html("");  
	            	   var option0 = "<option value=''>-请选择-</option>";
	            	   $("#exhibitionhall-select").append(option0); 
	                   $.each(data, function(key, val) {
	                 var option1 = $("<option>").val(val.id).text(val.name);
	                     $("#exhibitionhall-select").append(option1);                           
	                        }); 
	                   $("#exhibitionhall-select").get(0).selectedIndex=0;
	           
	            layui.form.render('select');
	        }, "JSON");
		
	 
	});
	
	layui.form.on('select(guestsSelect)', function(data){
	  	  console.log(data);
	  	var $this=$(this);
	  	
	  	if(data.value=='GUE'){    		      		  
  		  
    		//添加按钮点击事件
    		    $("#addGuestBtn").on("click",function(){
    		        window.parent.parent.addTab($(this));
    		    })
    		    $("#addGuestBtn").click();
    		    $("#guest-select").val("");
    		    layui.form.render('select');
    	  }
	  	var ent=$this.parents(".layui-name").find("[value='"+$this.attr("lay-value")+"']").attr("data-ent");
	  	var pos=$this.parents(".layui-name").find("[value='"+$this.attr("lay-value")+"']").attr("data-pos");
	  	$this.parents(".layui-guest").find(".layui-ent").html(ent);
	  	$this.parents(".layui-guest").find(".layui-pos").html(pos);
	  	
	  	
	 
	});
    
	//日期时间范围
	layui.laydate.render({
	    elem: '#rang'
	    ,type: 'datetime'
	    ,range: true
	    ,format: 'yyyy/MM/dd HH:mm'
	  });

    
    var editIndex = layui.layedit.build('meeting-description', {
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
            url: "/management/dialogue/meeting/save",
            data: data.field,
            dataType: "json",
            type: "post",
            success: function(data){
			layer.closeAll('loading');
			if(data.code==200){
				
				parent.location.reload();
			
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


var hightCount = 20, hightIndex = $("#ligthContent").children().length+1;
var guestls;
$.ajax({
         async: false,
         url: "/management/config/guest/findAll",
         dataType: "json",
         type: "GET",
         success: function(data){
        	 guestls=data;
         }
     });
if($("#ligthContent").children().length<hightCount){
	addIconClick();
}
function addIconClick(){
	total = $("#ligthContent").children().length;
	if(total == hightCount){
		layui.layer.msg("最多只能添加"+hightCount+"个嘉宾");
		return;
	}
	
	layui.laytpl($("#contenGuest").html()).render({id:hightIndex,guestls:guestls}, function (html) {
		$("#ligthContent").append(html);
		 layui.form.render('select');
      });
	hightIndex++;
}

function delIconClick(obj){
	$this=$(obj);
	$this.parents(".layui-guest").remove();
}





