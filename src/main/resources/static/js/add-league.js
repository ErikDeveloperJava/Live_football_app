$(document).ready(function () {
    $(".league-input").on("input",function (event) {
        var value = $(this).val();
        var name = $(this).attr("name");
        isValidData(name,value,event)
    });

    $("#add-league").on("submit",function (event) {
        var inputTags = [$("#name"),$("#description"),$("#file")];
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        })
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
        case "description":
            if(value == null || value.length < 8){
                event.preventDefault();
                $("#description").attr("class","form-control form-control-sm is-invalid");
                $("#descriptionError").text("in description field wrong data");
            }else {
                $("#description").attr("class","form-control form-control-sm is-valid");
                $("#descriptionError").text("");
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