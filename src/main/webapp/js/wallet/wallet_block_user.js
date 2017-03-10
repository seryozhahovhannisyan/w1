
$(document).ready(function () {
    //blok_user_popup();

})

function blok_user_popup(submit, title, cancel, reason, id, ns, src, img_src, elem) {
    var elems = {
        elem: elem,
        id: id
    };
    var cont = '<div class="popup_cont">' +
        '<div class="row">' +
        '<div class="popup_main">' +
              '<div class="popup_sm_header_parent">' +
                 '<div class="popup_sm_header">' +
                         '<div>' +
                            '<span onclick="close_block_popup()">' +
                                '<img src="'+img_src+'"' +
                            '</span>' +
                            '<span class="close_popup" onclick="close_block_popup()">' +
                                ''+cancel+'' +
                             '</span>' +
                         '</div>' +
                         '<div>' +
        '<span class="submit_block">' +
                            ''+submit+'' +

                            '</span>' +
                         '</div>' +
                 '</div>' +
              '</div>' +
              '<div class="left_first">' +
                '<img src="'+src+'" alt="profile img"/>' +
              '</div>' +'' +
              '<div class="a_href_name">' +
                    '<a href="#" id="user_id" data-id="0">' +
                    ''+ns+'' +
                    '</a>' +
              '</div>' +
              '<div class="title">' +
                '<input type="text" placeholder="'+title+'"/>' +
              '</div>' +
              '<div class="popup_textarea_div">' +
                '<textarea class="popup_textarea" rows="4" cols="50" placeholder="'+reason+'">' +
                '</textarea>' +
              '</div>' +
        '</div>' +
        '</div>' +
        '</div>';

    $("body").append(cont);
    $(".submit_block").click(function () {
        block_user(elems)
    })
    $(".popup_main").draggable();

}
 function close_block_popup(){
     $(".popup_cont").hide();


 }
function block_user(elems) {
    var id = elems.id ? elems.id : "";
    var elem = elems.elem ? elems.elem : "";

    var li = $(elem).closest("li");
    loader_show();
    var d = {
        blockedId: id,
        title: 'title ',
        content: ' content '
    };

    $.ajax({
        url: 'block-user.htm',
        type: "post",
        dataType: "json",
        data: d,
        success: function (data) {
            loader_hide();
            $(".popup_cont").hide();
            li.hide(1000, function () {
                li.remove();
            });
        },
        error: function (xhr, ajaxOptions, thrownError) {
            var handle = {status: xhr.status, responseText: xhr.responseText};
            loader_hide();


        }

    })
}
$(document).keydown(function (e) {
    if (e.which == 27) {
        close_block_popup()
    }
})

