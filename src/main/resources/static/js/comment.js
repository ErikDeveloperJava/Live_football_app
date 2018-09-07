$(document).ready(function () {
    loadComments();
    $("#comment-form").on("submit", function (event) {
        event.preventDefault();
        var comment = $("#comment").val();
        if (comment == null || comment.length < 1) {
            $("#comment").css("border", "1px solid red");
        } else {
            $("#comment").css("border", "none");
        }
        ;
        var data = {
            comment: comment,
            news: {
                id: newsId
            }
        };
        addComment(data);
    });
    $("#load-more").on("click",function () {
        loadComments();
    })
});
var page = 0;
var newsId = $("#newsId").attr("content");

function loadComments() {
    $.ajax({
        type: "GET",
        url: "/news/comment/" + newsId + "/page/" + page,
        contentType: "application/json",
        dataType: "JSON",
        success: function (comments) {
            if (comments.length < 4) {
                page = "null";
                $("#load-mor-li").hide();
            }
            if (comments.length != 0) {
                if(comments.length == 4){
                    page = parseInt(page) + 1;
                    $("#load-mor-li").show();
                }
                $.each(comments, function (i, comment) {
                    var liTag = '<li class="comment byuser comment-li comment-author-admin bypostauthor even thread-even depth-1"\n' +
                        'id="comment-5">\n' +
                        '<div class="kode_detail_comment_list list_2">\n' +
                        '<figure><img style="height: 60px" alt=\'\'\n' +
                        'src="/resources/users/' + comment.user.imgUrl + '" ' +
                        'srcset=\'http://1.gravatar.com/avatar/4118b7ad6d3e8b570212ac97af31352d?s=120&amp;d=mm&amp;r=g 2x\'\n' +
                        'class=\'avatar avatar-60 photo\' height=\'60\' width=\'60\'/>\n' +
                        '</figure>\n' +
                        '<div class="kode_detail_comment_text"><h5>' + comment.user.name + '</h5>' +
                        ' <span>' + comment.sendDate + '</span>\n' +
                        '<p>' + comment.comment + '</p>\n' +
                        '</div>\n' +
                        '</div>\n' +
                        '</li>';
                    $("#load-mor-li").before(liTag);
                })
            }
        },
        error: function () {
            window.location = "/error";
        }
    })
};

function addComment(data) {
    $.ajax({
        type: "POST",
        url: "/news/comment",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function () {
            $("#comment").val("");
            $(".comment-li").remove();
            page = 0;
            loadComments();
        },
        statusCode: {
            400: function () {
                $("#comment").css("border", "1px solid red ")
            },
            500: function () {
                window.location = "/error";
            }
        }
    })
}