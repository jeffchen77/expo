$(function() {
	layui.form.on('select(locationSelect)', function(data){
  	console.log(data);
  	renderEntDropdowList('');
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
	
	//举办方下拉框
	layui.form.on('select(entSelect)', function(data){
  	  if(data.value=='ENT'){    		      		    		  
  		  //添加按钮点击事件
  		  $("#addEntBtn").on("click",function(){
  		      window.parent.parent.addTab($(this));
  		   });
  		   $("#addEntBtn").click();
  		   $("#ent-select").val("");
  		   layui.form.render('select');
  	  }
  	});
	//展台下拉框
	layui.form.on('select(boothSelect)', function(data){
		renderEntDropdowList($("#booth-select").val());
  	});
	
	//会议厅下拉框
	layui.form.on('select(exhibitionhallSelect)', function(data){
		renderEntDropdowList('');
  	});
	layui.form.on('select(pavilionSelect)', function(data){
	  	  console.log(data);
	  	renderEntDropdowList('');
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
	  	renderEntDropdowList('');
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
    
	//日期时间范围
	layui.laydate.render({
	    elem: '#activityTime'
	    ,type: 'datetime'
	    ,format: 'yyyy/MM/dd HH:mm'
	    ,range: true
	  });
    
  //表单取消事件
    $('body').on('click', '#btnCancel', function(data) {        
        parent.location.reload();
    });
    
    var editIndex = layui.layedit.build('description', {
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
         ,height:'200px'
    });
    
    //表单提交事件
    layui.form.on('submit(btnSubmit)', function(data) {
    	data.field.description = layui.layedit.getContent(editIndex);
        layer.load(1);
        debugger;
        $.post("/management/activity/save", data.field, function(data){
            layer.closeAll('loading');
            if(data.code==200){            	
            	parent.location.reload();
            }else{
                layer.msg(data.msg,{icon: 2});
            }
        }, "JSON");
        return false;
    });  
});

function renderEntDropdowList(id){
	debugger;
	$.get("/management/activity/getEntByExhId",{
		exhId: id,
    }, function(data){
    	$("#ent-select").html("");  	
 	   	var option0 = "<option value=''>-请选择-</option>";
 	   	$("#ent-select").append(option0); 
 	   	$.each(data, function(key, val) {
 	   		var option1 = $("<option>").val(val.id).text(val.name);
 	   		$("#ent-select").append(option1);                           
          });
 	   $("#ent-select").append("<option value='ENT'>前往企业库进行设置</option>"); 
     /*$("#ent-select").get(0).selectedIndex=0;*/
     layui.form.render('select');
        }, "JSON");
}