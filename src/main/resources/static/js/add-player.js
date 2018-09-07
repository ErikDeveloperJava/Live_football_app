$(document).ready(function () {


    $(".player-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value,event);
    });

    $(".player-select").on("change",function (event) {
        var value = $(this).val();
        var name = $(this).attr("name");
        isValidSelect(value,name,event)
    });

    $("#add-player").on("submit",function (event) {
        var inputTags = $(".player-input");
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        });
        var value = $("#position-select").val();
        var name = $("#position-select").attr("name");
        isValidSelect(value,name,event);
        value = $("#clubId-select").val();
        name = $("#clubId-select").attr("name");
        isValidSelect(value,name,event);
    })
});

function isValidSelect(value, name,event) {
    if(value <= 0){
        event.preventDefault();
        $("#" + name + "Error").text("please choose a position");
        $("#" + name + "-select").attr("class","is-invalid player-select")
    }else {
        $("#" + name + "Error").text("");
        $("#" + name + "-select").attr("class","is-valid player-select")
    }
}

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
        case "surname":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#surname").attr("class","form-control form-control-sm is-invalid");
                $("#surnameError").text("in surname field wrong data");
            }else {
                $("#surname").attr("class","form-control form-control-sm is-valid");
                $("#surnameError").text("");
            };
            break;
        case "nationality":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#nationality").attr("class","form-control form-control-sm is-invalid");
                $("#nationalityError").text("in nationality field wrong data");
            }else {
                $("#nationality").attr("class","form-control form-control-sm is-valid");
                $("#nationalityError").text("");
            };
            break;
        case "birthDate":
            if(value == null || value.length != 10){
                event.preventDefault();
                $("#birthDate").attr("class","is-invalid player-input");
                $("#birthDateError").text("in birth date field wrong data");
            }else {
                $("#birthDate").attr("class","is-valid player-input");
                $("#birthDateError").text("");
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