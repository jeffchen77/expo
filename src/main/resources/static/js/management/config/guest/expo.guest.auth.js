$(function() {
    //渲染表格
    layui.table.render({
        elem : '#table',
        url : '/management/config/guest/authFindByPagging',
        where: {
        },
        page: true,
        cols: [[
            {type:'numbers',title: '序号', width:100},
            {field:'name', sort: true, title: '姓名', width:220},
            {field:'enterprise', sort: false,templet:function(d){if(d.enterprise==null)return '';return d.enterprise.name;}, title: '代表企业', width:220},
            {field:'position', sort: false, title: '职务', width:220},
            {field:'signStatus', sort: false,  templet:function(d){            	
                if(d.signStatus == '0'){
                    return '已认证';
                } else if(d.signStatus == '1'){
                    return '未认证';
                }else{
                	return '未知';
                }}, title: '嘉宾认证状态', width:160},
            {field:'signTime', sort: false, title: '嘉宾认证时间'}
                
        ]]
    });
    /*$(".layui-table-view").attr("style","height:520px;overflow-y:auto;padding-bottom:30px;");*/
    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});

//搜索
function doSearch(table){
    var value = $("#searchValue").val();     
    layui.table.reload('table', {where: {searchName: value}, page: { curr: 1}});
}

