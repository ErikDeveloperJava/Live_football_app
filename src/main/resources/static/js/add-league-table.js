$(document).ready(function () {

    $(".news-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value,event);
    });

    $("#league-select").on("change",function (event) {
        var value = $(this).val();
        var name = $(this).attr("name");
        isValidData(name,value,event);
    });

    $("#add-news").on("submit",function (event) {
        var inputTags = $(".news-input");
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        });
    })
});

function isValidData(name, value, event) {
    switch (name){
        case "title":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#title").attr("class","form-control form-control-sm is-invalid");
                $("#titleError").text("in title field wrong data");
            }else {
                $("#title").attr("class","form-control form-control-sm is-valid");
                $("#titleError").text("");
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
        case "leagueId":
            if(value <= 0){
                event.preventDefault();
                $("#" + name + "Error").text("please choose a league");
            }else {
                $("#" + name + "Error").text("");
            };
            break;
    }
}