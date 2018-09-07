$(document).ready(function () {

    var size = 0;

    $(".register-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value,event);
    });

    $("#register-select").on("change",function () {
        var value = $(this).val();
        if(value == "null"){
            $("#roleError").text("please choose a category");
        }else {
            $("#roleError").text("");
        }
    });

    $("#register-form").on("submit",function (event) {
        var inputTags = $(".register-input");
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        });
    })
});

function isValidData(name, value, event) {
    switch (name){
        case "name":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#name").attr("class","form-control form-control-sm is-invalid");
                $("#nameError").text("in name field wrong data");
            }else {
                $("#name").attr("class","form-control form-control-sm is-valid");
                $("#nameError").text("");
            };
            break;
        case "username":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#username").attr("class","form-control form-control-sm is-invalid");
                $("#usernameError").text("in username field wrong data");
            }else {
                $("#username").attr("class","form-control form-control-sm is-valid");
                $("#usernameError").text("");
            };
            break;
        case "password":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#password").attr("class","form-control form-control-sm is-invalid");
                $("#passwordError").text("in password field wrong data");
            }else {
                $("#password").attr("class","form-control form-control-sm is-valid");
                $("#passwordError").text("");
            };
            break;
        case "role":
            if(value == null || value == "null"){
                event.preventDefault();
                $("#roleError").text("please choose a role");
            }else {
                $("#roleError").text("");
            };
            break;
        case "image":
            if(value == null || value.length < 4 || value.length > 255){
                event.preventDefault();
                $("#imageError").text("in image field wrong data");
            }else {
                $("#imageError").text("");
            };
            break;
    }
}