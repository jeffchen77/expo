/**
 * Created by lzm on 2017/2/8.
 */
(function($) {

    $.expo = {
    };

    $.common = {
        contextPath: "",
        getContextPath : function() {
            if (!$.common.contextPath) {
                var jsFileRelativePath = "/resources/js/";
                var scripts = document.getElementsByTagName("script");
                if (scripts) {
                    for (var i = 0; i < scripts.length; i++) {
                        var src = scripts[i].src;
                        if (src && src.indexOf(jsFileRelativePath) != -1) {
                            var pathArray = src.split(jsFileRelativePath);
                            $.common.contextPath = pathArray[0];
                            break;
                        }
                    }
                } else {
                    alert("JavaScript初始化异常，请开启浏览器的JavaScript脚本支持！");
                }
            }
            return  $.common.contextPath;
        },

        /**附件上传方法
         * @param
         * fileId:上传附件id
         * fileType:1:图片  2:文件
         * multi:是否多次上传 是：true 否：false
         * filecount:同时上传的文件数量
         * func(fileData,imgCount,id): 回调函数(文件信息、图片数量、列表形式附件用ID)
         * btnName: 按钮名 默认：上传图片 （非必填）
         * id:列表形式附件用ID  （非必填）
         */
        initUploadFile : function(fileId,fileType,multi,filecount,func,btnName,id) {
            var uploadUrl = $.common.getContextPath() + '/sys/upload/uploadFile';
            var buttonText = "选择文件";
            if(fileType == 1){
                buttonText = "选择图片";
            }
            $('#'+fileId).Huploadify({
                auto:true,
                fileTypeExts:'*.jpg;*.png;*.gif;jpeg;*.JPG;*.PNG;*.GIF;JPEG',
                multi:false,
                formData:{key:123456,key2:'vvvv'},
                fileSizeLimit:1024 * 20,
                showUploadedPercent:false,
                showUploadedSize:true,
                removeTimeout:9999999,
                buttonText:buttonText,
                uploader:uploadUrl,
                onUploadComplete:function(file,data,response){
                    var obj = jQuery.parseJSON(data);
                    // $("#"+fileId).uploadify("settings", "formData", {'count':imgCount});
                    if (obj["responseCode"] == "1") {
                        var fileData = obj["datas"];
                        func(fileData);
                        console.log("文件" + fileData.fileNames + "上传成功！");
                    } else {
                        alert(data[$.Constants.respMsg]);
                    }
                },
                onDelete:function(file){
                    console.log('删除的文件：'+file);
                    console.log(file);
                }
            });
        },
        initSelectDictionary: function(element, parentId){
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
                            $("#" + element).append('<option value="' + data[i].value + '">' + data[i].displayName + '</option>');
                        }
                    }

                }
            })
        },

        initSelectDictionaryUseID: function(element, parentId){
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
                            $("#" + element).append('<option value="' + data[i].id + '">' + data[i].displayName + '</option>');
                        }
                    }

                }
            })
        },
        initUpload:function(elementid, succ, err){
            layui.upload.render({
                elem: "#" +elementid,
                url: '/management/ftp/uploadFile',
                accept: 'images',
                exts: 'png|PNG|JPG|jpg|JPEG|jpeg',
                size:4096,
                choose: function(obj){
                    debugger;
                    console.log(obj)
                },
                done: succ,
                error: err
            })
        }

    };

})(jQuery);
