$(document).ready(function () {
    var isCompleted = $("#isCompleted").attr("content");

    $(".update-match").on("click", function () {
        var id = $(this).attr("id");
        $("#table-" + id).hide();
        $("#input-" + id).show();
    });

    $(".close-update").on("click", function () {
        var id = $(this).attr("id").split("-");
        $("#master-input-" + id).val($("#master-" + id).text());
        $("#guest-input-" + id).val($("#guest-" + id).text());
        $("#date-input-" + id).val($("#date-" + id).text());
        $("#time-input-" + id).val($("#time-" + id).text());
        if(isCompleted){
            $("#account-input-" + id).val($("#account-" + id).text());
        }
        $("#input-" + id).hide();
        $("#table-" + id).show();
    });

    $(".update-match-button").on("click", function () {
        var id = $(this).attr("id").split("-");
        var inputTags = $(".match-input-" + id);
        var size = 0;
        $.each(inputTags, function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            if (isValidData(name, value, id)) {
                size++;
            }
        });
        var data;
        if (isCompleted == "true" && size == 5) {
            data = {
                id: parseInt(id),
                master: $("#master-input-" + id).val(),
                guest: $("#guest-input-" + id).val(),
                date: $("#date-input-" + id).val(),
                time: $("#time-input-" + id).val(),
                account: $("#account-input-" + id).val(),
                completed: true
            }
        }
        if (size == 4 && isCompleted == "false") {
            data = {
                id: parseInt(id),
                master: $("#master-input-" + id).val(),
                guest: $("#guest-input-" + id).val(),
                date: $("#date-input-" + id).val(),
                time: $("#time-input-" + id).val(),
                completed: false
            }
        }

        if(size == 4 && isCompleted == "false"){
            update(id,data)
        }
        if(size == 5 && isCompleted == "true"){
            update(id,data)
        }
    })
});

function update(id,data) {
    $.ajax({
        type: "POST",
        url: "/admin/match/update",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (match) {
            $("#master-input-" + id).val(match.master);
            $("#guest-input-" + id).val(match.guest);
            $("#date-input-" + id).val(match.date);
            $("#time-input-" + id).val(match.time);
            if(match.completed){
                $("#account-input-" + id).val(match.account);
            }

            $("#master-" + id).text(match.master);
            $("#guest-" + id).text(match.guest);
            $("#date-" + id).text(match.date);
            $("#time-" + id).text(match.time);
            if(match.completed){
                $("#account-" + id).text(match.account);
            }
            $("#input-" + id).hide();
            $("#table-" + id).show();

        },
        statusCode:{
            400:function (response) {
                if(response.responseJSON.masterError){
                    $("#master-input-" + id).css("border", "1px solid red");
                }else if(response.responseJSON.guestError){
                    $("#guest-input-" + id).css("border", "1px solid red");
                }else if(response.responseJSON.dateError){
                    $("#date-input-" + id).css("border", "1px solid red");
                }else if(response.responseJSON.timeError){
                    $("#time-input-" + id).css("border", "1px solid red");
                }else if(response.responseJSON.accountError){
                    $("#account-input-" + id).css("border", "1px solid red");
                }
            },
            500: function () {
                window.location = "/error";
            }
        }
    })
}
function isValidData(name, value, id) {
    switch (name) {
        case "master":
            if (value == null || value.length < 2 || value.length > 255) {
                $("#master-input-" + id).css("border", "1px solid red");
                return false;
            } else {
                $("#master-input-" + id).css("border", "1px solid white");
                return true;
            }
            break;
        case "guest":
            if (value == null || value.length < 2 || value.length > 255) {
                $("#guest-input-" + id).css("border", "1px solid red");
                return false;
            } else {
                $("#guest-input-" + id).css("border", "1px solid white");
                return true;
            }
            break;
        case "date":
            if (value == null || value.length != 10) {
                $("#date-input-" + id).css("border", "1px solid red");
                return false;
            } else {
                $("#date-input-" + id).css("border", "1px solid white");
                return true;
            }
            break;
        case "time":
            if (value == null || value.length != 5) {
                $("#time-input-" + id).css("border", "1px solid red");
                return false;
            } else {
                $("#time-input-" + id).css("border", "1px solid white");
                return true;
            }
            break;
        case "account":
            if (value == null || value.length < 2 || value.length > 10) {
                $("#account-input-" + id).css("border", "1px solid red");
                return false;
            } else {
                $("#account-input-" + id).css("border", "1px solid white");
                return true;
            }
            break;
    }
}
