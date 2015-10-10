CKEDITOR.editorConfig = function(config) {
    config.allowedContent = true; 
    var contextPath = $('.contextPath').html();
    config.filebrowserImageUploadUrl = '/ckeditor/uploadImage';
    config.filebrowserBrowseUrl = '/app/administrator/system-resources/resource_manager/index.jsf';
    config.filebrowserImageBrowseUrl = '/app/administrator/system-resources/resource_manager/index.jsf?Type=Images';
    config.filebrowserFlashBrowseUrl = '/app/administrator/system-resources/resource_manager/index.jsf?Type=Flash';
    config.filebrowserUploadUrl = '/app/administrator/system-resources/resources?command=QuickUpload&type=Files';
    config.filebrowserFlashUploadUrl = '/app/administrator/system-resources/resources?command=QuickUpload&type=Flash'
};





