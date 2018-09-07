$(document).ready(function () {

    $(".login-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidUsernameOrPassword(name,value,event);

    });

    $("#login-form").on("submit",function (event) {
        var inputTags = [$("#username-login"),$("#password-login")];
        var size = 0;
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            if(isValidUsernameOrPassword(name,value,event)){
                size++;
            }
        });

        var data;
        if($("#remember-me").val() == "on"){
            data = {
                username: $("#username-login").val(),
                password: $("#password-login").val(),
                "remember-me":""
            };
        }else {
            data = {
                username: $("#username-login").val(),
                password: $("#password-login").val()
            };
        }
        if(size == 2){
            event.preventDefault();
            $.ajax({
                type: "POST",
                url: $("#login-form").attr("action"),
                data: data,
                success: function () {
                    window.location = "/";
                },
                statusCode: {
                    401:function () {
                        $("#loginError").text("you entered wrong username and password");
                    },
                    500: function () {
                        window.location = "/error";
                    }
                }
            })
        }
    })
});

function isValidUsernameOrPassword(name,value,event) {
    switch (name){
        case "username":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#username-login").css("border","1px solid red");
                return false;
            }else {
                $("#username-login").css("border","none");
                return true;
            };
            break;
        case "password":
            if(value == null || value.length < 4 || value.length > 255){
                event.preventDefault();
                $("#password-login").css("border","1px solid red")
                return false;
            }else {
                $("#password-login").css("border","none")
                return true;
            }
    }
}