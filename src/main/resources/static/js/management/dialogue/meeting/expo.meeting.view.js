$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/dialogue/meeting/findGuestByPagging',
        where: {meetingId:$("#id").val() 
        },
        page: true,
        cols: [[
            {type:'numbers',title: '序号', width:120},
            {field:'guest', sort: true,templet:function(d){if(d.guest==null)return '';return d.guest.name;}, title: '姓名', width:180},
            {field:'guest', sort: false,templet:function(d){if(d.guest==null || d.guest.enterprise==null)return '';return d.guest.enterprise.name;}, title: '代表企业', width:260},
            {field:'guest', sort: false,templet:function(d){if(d.guest==null)return '';return d.guest.position;}, title: '职位', width:240},
            {field:'guest', sort: false,templet:function(d){
            	if(d.guest==null)return '';
           if(d.guest.signStatus=='0'){
            	return "已认证";
            }else{
            	return "未认证";
            }} ,title: '嘉宾认证状态', width:180},
            {field:'guest', sort: false,templet:function(d){
            	if(d.guest==null)return '';
            	if(d.guest.signTime==null) return '';
            	return d.guest.signTime;}, title: '嘉宾认证时间',width:242}
        ]]
    });

  //表单取消事件
    $('body').on('click', '#btnCancel', function(data) {        
        parent.location.reload();
    });

    //搜索按钮点击事件
    $('body').on('click', '#searchBtn',function(){
    	 var value = $("#searchValue").val();
	      
	      //执行重载
	      layui.table.reload('table', {
	        page: {
	          curr: 1 //重新从第 1 页开始
	        }
	        ,where: {meetingId:$("#id").val(),searchName: value}
	      });
    });
    
    var editIndex = layui.layedit.build('meeting-description', {
        tool:[]
         ,height:'200px'
    });
	
	$("#LAY_layedit_1").contents().find('body').attr("contenteditable","false");
});



