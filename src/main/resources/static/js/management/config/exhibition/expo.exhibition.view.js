$(function() {
	
	//表单取消事件
    $('body').on('click', '#btnCancel', function(data) {        
        parent.location.reload();
    })
	var editIndex = layui.layedit.build('exhibition-description', {
        tool:[]
         ,height:'200px'
    });
	
	$("#LAY_layedit_1").contents().find('body').attr("contenteditable","false");
});