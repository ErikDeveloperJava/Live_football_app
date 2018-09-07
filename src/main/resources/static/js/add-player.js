$(document).ready(function () {

    var size = 0;

    $(".club-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value,event);
    });

    $("#league-select").on("change",function () {
        var value = $(this).val();
        if(value <= 0){
            $("#leagueIdError").text("please choose a category");
            $("#leagueIdError").attr("class","form-control is-invalid")
        }else {
            $("#leagueIdError").text("");
            $("#leagueIdError").attr("class","form-control is-valid")
        }
    });

    $(".champion").on("change",function () {
        size++;
        $("#championError").text("");
    })

    $("#add-club").on("submit",function (event) {
        var inputTags = [$("#name"),$("#trainer"),$("#stadium"),
        $("#owner"),$("#file"),$("#description")];
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        });
        var value = $("#league-select").val();
        if(value <= 0){
            $("#leagueIdError").text("please choose a category");
            $("#leagueIdError").attr("class","form-control is-invalid")
        }else {
            $("#leagueIdError").text("");
            $("#leagueIdError").attr("class","form-control is-valid")
        };
        if(size == 0){
            $("#championError").text("please choose Yes or No");
        }else {
            $("#championError").text("");
        }
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
        case "trainer":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#trainer").attr("class","form-control form-control-sm is-invalid");
                $("#trainerError").text("in trainer field wrong data");
            }else {
                $("#trainer").attr("class","form-control form-control-sm is-valid");
                $("#trainerError").text("");
            };
            break;
        case "stadium":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#stadium").attr("class","form-control form-control-sm is-invalid");
                $("#stadiumError").text("in stadium field wrong data");
            }else {
                $("#stadium").attr("class","form-control form-control-sm is-valid");
                $("#stadiumError").text("");
            };
            break;
        case "owner":
            if(value == null || value.length < 2 || value.length > 255){
                event.preventDefault();
                $("#owner").attr("class","form-control form-control-sm is-invalid");
                $("#ownerError").text("in owner field wrong data");
            }else {
                $("#owner").attr("class","form-control form-control-sm is-valid");
                $("#ownerError").text("");
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