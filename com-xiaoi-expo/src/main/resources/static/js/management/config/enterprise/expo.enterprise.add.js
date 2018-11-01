var hightCount = 5, hightIndex = 0;
var typePrarent = "ENTTYPE", showTypeParent = "SHOWTYPE";
$(function() {
    //加载下拉框内容
	initDropDownList("enterpriseType", typePrarent, "enterpriseTypeList[]");
	initDropDownList("themeType", showTypeParent, "themeTypeList[]");
	initExhibitionDropDownList("exhibitionSelect", "exhibitionIds[]");
	layui.form.render('select');
	
	//初始化多选下拉框
	initMutipleSelect();
	
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
        
        $.post("/management/config/enterprise/save", data.field, function(data){
            layer.closeAll('loading');
            if(data.code==200){
            	var confirmIndex = layer.confirm('添加成功，继续添加？', function(index){
                     debugger;
                     clearData();
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
    
    $("#ligthContent").html($("#contenPlus").html());
    
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

function clearData(){
	 $("#enterpriseName").val('');
	 $("#enterpriseType").val('');
	 $("#enterpriseTheme").val('');
	 $("#enterPriseLog").val('')
	 $(".layui-upload-img").attr("src","");
	 $("#viewPhoto").addClass("layui-hide");
	 $("#LAY_layedit_1").contents().find('body').html('');
	 $("#enterpriseType").val('');
	 layui.form.render('select');
	 
	 $("#ligthContent").html($("#contenPlus").html());
	 
	 $("#exhibitionSelect").empty(); 
	 initExhibitionDropDownList("exhibitionSelect", "exhibitionIds[]");
	 layui.form.render('select');
	 //初始化多选下拉框
	 initMutipleSelect();
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
		$("option",$this).each(function() {
			if($(this).val()!='' && $(this).val()!=null){
				//str=["<dd>","<input type='checkbox' name='themeTypeList[]' lay-skin='primary' title='",$(this).text(),"' value='",$(this).val(),"'>","</dd>"].join("");
				str=["<dd>","<input type='checkbox' name='",$(this).attr('id'),"' lay-skin='primary' title='",$(this).text(),"' value='",$(this).val(),"'>","</dd>"].join("");
				$dl.append(str);
			}
		});
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

//初始化下拉框
function initDropDownList(element, parentId, idName){
	$.ajax({
        async: false,
        url: $.common.getContextPath() + "/management/dictionary/findByparentId",
        data: {parentId: parentId},
        dataType: "json",
        type: "GET",
        success: function(data){
            if(!!data){
                for(var i=0; i<data.length; i++){
                	if(i==0){
                		$("#" + element).append('<option value="">请选择</option>');
                	}
                    $("#" + element).append('<option id="'+idName+'" value="' + data[i].id + '">' + data[i].displayName + '</option>');
                }
            }

        }
    });
}

//初始化展台编号下拉框,需要特殊处理
function initExhibitionDropDownList(element, idName){
	$.ajax({
        async: false,
        url: $.common.getContextPath() + "/management/config/exhibitionbooth/listExhibitions",
        dataType: "json",
        type: "GET",
        success: function(data){
            if(!!data){
                for(var i=0; i<data.length; i++){
                	if(i==0){
                		$("#" + element).append('<option value="">请选择</option>');
                	}
                    $("#" + element).append('<option id="'+idName+'" value="' + data[i].id + '">' + data[i].code + '</option>');
                }
            }

        }
    });
}