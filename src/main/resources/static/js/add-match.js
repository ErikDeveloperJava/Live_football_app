$(document).ready(function () {

    $(".matches-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value,event);
    });

    $(".matches-select").on("change",function (event) {
        var value = $(this).val();
        var name = $(this).attr("name");
        isValidData(name,value,event);
    });

    $("#add-matches").on("submit",function (event) {
        var inputTags = $(".matches-input");
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        });
        var master =$("#masterId-select").val();
        var guest =$("#guestId-select").val();
        if(master != 0 && guest != 0 && master == guest){
            event.preventDefault();
            $("#masterIdError").text("master does not must be equals guest");
            $("#guestIdError").text("guest does not must be equals master")
        }
    })
});

function isValidData(name, value, event) {
    switch (name){
        case "masterId":
            if(value == null || value <= 0){
                event.preventDefault();
                $("#masterIdError").text("please choose a club");
            }else {
                $("#masterIdError").text("");
            };
            break;
        case "guestId":
            if(value == null || value <= 0){
                event.preventDefault();
                $("#guestIdError").text("please choose a club");
            }else {
                $("#guestIdError").text("");
            };
            break;
        case "date":
            if(value == null || value.length != 10){
                event.preventDefault();
                $("#dateError").text("invalid date format");
            }else {
                $("#dateError").text("");
            };
            break;
        case "time":
            if(value == null || value.length != 5){
                event.preventDefault();
                $("#time").attr("class","form-control form-control-sm is-invalid");
                $("#timeError").text("in time field wrong data");
            }else {
                $("#time").attr("class","form-control form-control-sm is-valid");
                $("#timeError").text("");
            };
            break;
        case "account":
            if(value != null && value.length >5){
                event.preventDefault();
                $("#account").attr("class","form-control form-control-sm is-invalid");
                $("#accountError").text("in account field wrong data");
            }else {
                $("#account").attr("class","form-control form-control-sm is-valid");
                $("#accountError").text("");
            };
            break;
    }
}