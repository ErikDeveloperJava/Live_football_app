$(document).ready(function () {
    loadLeague();
});

function loadLeague() {
    $.ajax({
        type: "GET",
        url: "/leagues",
        success: function (leagues) {
            $.each(leagues,function (i, league) {
                var leagueLi = "<li id='menu-item-" + league.id + "' " +
                    "class='menu-item menu-item-type-post_type menu-item-object-page menu-item-257'>" +
                    "<a href='/league/" + league.id + "'>"  + league.name + "</a></li>";
                $("#leagues-ul").append(leagueLi);
            })
        },
        error: function () {
            window.location = "/error";
        }
    })
}