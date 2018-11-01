var hightCount = 5, hightIndex = 100;
$(function() {
	
	var editIndex = layui.layedit.build('enterpriseDesc', {
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
         ,height:'130px'
    });
	
	//表单提交事件
    layui.form.on('submit(btnSubmit)', function(data) {
        data.field._method = $("#editForm").attr("method");
        data.field.enterpriseDesc = layui.layedit.getContent(editIndex);
        layer.load(1);
        $.post("/management/config/enterprise/update", data.field, function(data){
            layer.closeAll('loading');
            if(data.code==200){
            	layer.msg('修改成功',{icon: 1});
        		parent.location.reload();
            }else{
                layer.msg(data.msg,{icon: 2});
            }
        }, "JSON");
        return false;
    });
    
    $.common.initUpload('enterPrisePhoto',
    		function(res){
				console.log(res);
				$("#enterPriseLog").val(res.relativeUrl);
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
        $("#enterPriseLog").val('')
    });
  //表单取消事件
    $('body').on('click', '#btnCancel', function(data1) {
        debugger;
        parent.location.reload();
    });
    
    //多选下拉框
    initMutipleSelect();

});

function addIconClick(){
	total = $("#ligthContent").children().length;
	if(total == hightCount){
		layui.layer.msg("最多只能添加"+hightCount+"个展出亮点");
		return;
	}
	layui.laytpl($("#contenMinus").html()).render({id:hightIndex}, function (html) {
		$("#ligthContent").append(html);
      });
	hightIndex++;
}

function delIconClick(index){
	$("#tDiv"+index).remove();
}

function initMutipleSelect(){
	//多选下拉框
	$("select.downlist").each(function(index,item) {
		var $this=$(this);
		var $select=$this.next(".layui-form-select");
		$select.addClass("downpanel");
		$select.append("<input class='hiddenSelect' type='hidden'/>");
		var $dl=$select.find("dl");
		$dl.empty();
		var str="";
		var inputStr = '';
		$("option",$this).each(function() {
			if($(this).val()!='' && $(this).val()!=null){
				if($(this).attr('selected')){
					str=["<dd>","<input type='checkbox' checked='true' name='",$(this).attr('id'),"' lay-skin='primary' title='",$(this).text(),"' value='",$(this).val(),"'>","</dd>"].join("");
					inputStr += $(this).text();
					inputStr += "、";
				}else{
					str=["<dd>","<input type='checkbox' name='",$(this).attr('id'),"' lay-skin='primary' title='",$(this).text(),"' value='",$(this).val(),"'>","</dd>"].join("");
				}
				$dl.append(str);
			}
		});
		//设置input框的值
		if(inputStr!=''){
			inputStr = inputStr.substring(0, inputStr.length-'、'.length);
			var $inputText=$select.find(":text");
			$inputText.val(inputStr);
			$select.find(".hiddenSelect").val(inputStr);
		}
		layui.form.render("checkbox");
	});
	
	$(".downpanel").on("click",".layui-select-title",function(e){
		var $select=$(this).parents(".layui-form-select");
		$(".layui-form-select").not($select).removeClass("layui-form-selected");
		$select.addClass("layui-form-selected");

		var $inputText=$(this).find(":text");
		$inputText.val($select.find(".hiddenSelect").val());
		e.stopPropagation();
	}).on("click",".layui-form-checkbox",function(e){
		var inputStr = '';
		var $selectChecked=$(this).parents(".layui-anim-upbit").find(".layui-form-checked");
		if($selectChecked!=null && $selectChecked.length > 0){
			for(j=0; j<$selectChecked.length; j++){
				inputStr += $selectChecked[j].firstChild.innerText;
				if(j != $selectChecked.length-1){
					inputStr += "、";
				}
			}
		}
		var $select=$(this).parents(".layui-form-select");
		var $inputText=$select.find(":text");
		$select.find(".hiddenSelect").val(inputStr);
		$inputText.val(inputStr);
		e.stopPropagation();
	});
}