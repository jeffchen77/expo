$(function() {
	var editIndex = layui.layedit.build('description', {
        tool:[]
         ,height:'200px'
    });
	
	$("#LAY_layedit_1").contents().find('body').attr("contenteditable","false");
});