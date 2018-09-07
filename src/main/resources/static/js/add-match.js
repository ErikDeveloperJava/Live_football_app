$(document).ready(function () {

    $(".league-table-input").on("input",function (event) {
        var name = $(this).attr("name");
        var value = $(this).val();
        isValidData(name,value,event);
    });

    $("#clubId-select").on("change",function (event) {
        var value = $(this).val();
        var name = $(this).attr("name");
        isValidData(name,value,event);
    });

    $("#add-league-table").on("submit",function (event) {
        var inputTags = $(".league-table-input");
        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            isValidData(name,value,event);
        });
    })
});

function isValidData(name, value, event) {
    switch (name){
        case "played":
            if(value == null || value < 0){
                event.preventDefault();
                $("#played").attr("class","form-control form-control-sm is-invalid");
                $("#playedError").text("in played field wrong data");
            }else {
                $("#played").attr("class","form-control form-control-sm is-valid");
                $("#playedError").text("");
            };
            break;
        case "won":
            if(value == null || value < 0){
                event.preventDefault();
                $("#won").attr("class","form-control form-control-sm is-invalid");
                $("#wonError").text("in won field wrong data");
            }else {
                $("#won").attr("class","form-control form-control-sm is-valid");
                $("#wonError").text("");
            };
            break;
        case "drawn":
            if(value == null || value < 0){
                event.preventDefault();
                $("#drawn").attr("class","form-control form-control-sm is-invalid");
                $("#drawnError").text("in drawn field wrong data");
            }else {
                $("#drawn").attr("class","form-control form-control-sm is-valid");
                $("#drawnError").text("");
            };
            break;
        case "lost":
            if(value == null || value < 0){
                event.preventDefault();
                $("#lost").attr("class","form-control form-control-sm is-invalid");
                $("#lostError").text("in lost field wrong data");
            }else {
                $("#lost").attr("class","form-control form-control-sm is-valid");
                $("#lostError").text("");
            };
            break;
        case "points":
            if(value == null || value < 0){
                event.preventDefault();
                $("#points").attr("class","form-control form-control-sm is-invalid");
                $("#pointsError").text("in points field wrong data");
            }else {
                $("#points").attr("class","form-control form-control-sm is-valid");
                $("#pointsError").text("");
            };
            break;
        case "clubId":
            if(value <= 0){
                event.preventDefault();
                $("#" + name + "Error").text("please choose a club");
            }else {
                $("#" + name + "Error").text("");
            };
            break;
    }
}