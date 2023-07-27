function updateRequest() {

    const chk = confirm("모델에 적용하시겠습니까?");
    if (chk === true) {
        $.ajax({
            type: "GET",
            url: "/api/modelupdate/",
            data: null,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function () {
                var width = 0;
                var height = 0;
                var left = 0;
                var top = 0;

                width = 50;
                height = 50;
                top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
                left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

                if($("#div_ajax_load_image").length != 0) {
                    $("#div_ajax_load_image").css({
                        "top": top+"px",
                        "left": left+"px"
                    });
                    $("#div_ajax_load_image").show();
                }
                else {
                    $('body')
                        .append('<div id="div_ajax_load_image" style="position:absolute; ' +
                            'top:' + top + 'px; ' +
                            'left:' + left + 'px; ' +
                            'width:' + width + 'px; ' +
                            'height:' + height + 'px; ' +
                            'z-index:9999; ' +
                            'background:#f0f0f0; ' +
                            'filter:alpha(opacity=50); ' +
                            'opacity:alpha*0.5; ' +
                            'margin:auto; ' +
                            'padding:0; ' +
                            '"><img src="../image/ajax_loader.gif" style="width:80px; height:80px;"></div>');
                }

            },
            complete: function () {
                $("#div_ajax_load_image").hide();
            }
        }).done(function () {
            alert("제출하였습니다");
        }).fail(function (error) {
            alert(실패하였습니다 + (error));
        });
    }
}

    /*지난 삽질
    // Create an XMLHttpRequest object.
    var xhr = new XMLHttpRequest();

    // Set the request method to POST.
    xhr.open("GET", "http://127.0.0.1:5000/api/modelupdate/");

    // Send the request.
    xhr.send();

    // Handle the response.
    xhr.onload = function () {
        if (xhr.status === 200) {
            // The request was successful.
            console.log("Request successful!");
        } else {
            // The request failed.
            console.log("Request failed!");
        }
    };
}*/

//     const data = {
//         idx: $("#idx").val(),
//         // declaration: $("#declaration").val()
//     };
//
//     var confirm = window.confirm("AI에 적용 하시겠습니까?");
//
//     if(confirm) {
//         $.ajax({
//             type: "POST",
//             url: "/api/modelupdate/",
//             data: JSON.stringify(data),
//             contentType: "application/json; charset=utf-8",
//             dataType: "json"
//         }).done(function () {
//             alert("적용되었습니다");
//         }).fail(function (error) {
//             alert("실패했습니다.")
//         });
//     }
//
// }