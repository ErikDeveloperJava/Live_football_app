$(document).ready(function () {

    $(".register-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value);
    });

    $("#register-form").on("submit",function (event) {
        event.preventDefault();
        var inputTags = [$("#name"),$("#username"),$("#password")];
        var size = 0;
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            if(isValidData(name,value)){
                size++;
            }
        });
        if(size == 3){
            $(".register-error").text("");
            $("#register-success").text("");
            $.ajax({
                type: "POST",
                url: $("#register-form").attr("action"),
                data: new FormData($("#register-form")[0]),
                processData: false,
                contentType: false,
                success: function (response) {
                    if(!response.success){
                        $.each(response.errors,function (i, fieldError) {
                            $("#" + fieldError.field + "Error").text(fieldError.defaultMessage);
                        })
                    }else {
                        $(".register-input").val("");
                        $("#register-success").text("thank you for registering")
                    }
                },
                error:function () {
                    window.location = "/error";
                }
            })
        }
    })
});

function isValidData(name, value) {
    switch (name){
        case "name":
            if(value == null || value.length < 2 || value.length > 255){
                $("#nameError").text("in name field wrong data");
                $("#name").css("border","1px solid red")
                return false;
            }else {
                $("#nameError").text("");
                $("#name").css("border","none")
                return true;
            };
            break;
        case "username":
            if(value == null || value.length < 2 || value.length > 255){
                $("#usernameError").text("in username field wrong data");
                $("#username").css("border","1px solid red");
                return false;
            }else {
                $("#usernameError").text("");
                $("#username").css("border","none")
                return true;
            };
            break;
        case "password":
            if(value == null || value.length < 2 || value.length > 255){
                $("#passwordError").text("in password field wrong data");
                $("#password").css("border","1px solid red")
                return false;
            }else {
                $("#passwordError").text("");
                $("#password").css("border","none")
                return true;
            };
            break;
    }
}