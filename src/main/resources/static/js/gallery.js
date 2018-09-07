$(document).ready(function () {
    $("#image").on("change",function () {
        var value = $(this).val();
        if(value != null && value.length >= 4 && value.length < 255){
            uploadImage(new FormData($("#gallery-form")[0]));
        }
    })
});

function uploadImage(formData) {
    $.ajax({
        type: "POST",
        url: $("#gallery-form").attr("action"),
        processData: false,
        contentType: false,
        data: formData,
        success:function(){
            //**************
        },
        error: function () {
            window.location = "/error";
        }
    })
}