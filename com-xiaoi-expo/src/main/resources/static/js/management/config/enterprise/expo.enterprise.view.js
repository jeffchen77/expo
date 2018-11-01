$(function() {
	var editIndex = layui.layedit.build('enterpriseDesc', {
        tool:[]
         ,height:'130px'
    });
	
	$("#LAY_layedit_1").contents().find('body').attr("contenteditable","false");
});