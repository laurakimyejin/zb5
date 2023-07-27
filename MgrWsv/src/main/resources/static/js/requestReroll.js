// 재학습요청하는 코드
function requestReroll(){


    // Confirm with the user if they want to retrain.
    // const data = {
    //         idx: $("#idx").val(),
    //         declaration: $("#declaration").val()
    //     };

    // const idx = [[${voicedata.idx}]];
    // var voicedata = [[${voicedata}]];

    // const idx = {${user.idx}};
    // const declaration = {${voicedata.declaration}};

    const data = {
        idx: $("#idx").val(),
        declaration: $("#dec").val(),
        content: $("#content").val()
    };

    var confirmmm = window.confirm("재학습 요청 하시겠습니까?");

    if(confirmmm) {
        $.ajax({
            type: "POST",
            url: "/api/text/" + data.idx + "/" + data.declaration,
            data: JSON.stringify({
                idx: data.idx,
                declaration: data.declaration,
                content: data.content
        }),
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
            alert("요청되었습니다");
        }).fail(function (error) {
            alert("요청되었습니다.")
        });
    }

/*삽질5
    const data = {
        idx: $("#name").val(),
        declaration: $("#dec").val(),
        content : $("#con").val
    };

    var confirmation = confirm("재학습 하시겠습니까?");
    if (confirmation) {
        // Send the request.
        var xhr = new XMLHttpRequest();
        // xhr.open("POST", "http://127.0.0.1:5000/api/text/${data.idx}/${data.declaration}");
        // xhr.open("POST", "http://127.0.0.1:5000/api/text/" +"/"+ voicedata.declaration);
        xhr.open("POST", "/api/text/" + data.idx +"/"+ data.declaration);
        //"http://127.0.0.1:8989
        // console.log(data.idx);
        xhr.onload = function() {
            if (xhr.status === 200) {
                // The request was successful.
                alert("요청되었습니다!");
            } else {
                // The request failed.
                alert("실패했습니다!");
            }
        };
        xhr.send();
    }

*/



/* 삽질4, html에 id값 줬음. detail.html 확인요

    var idx = parseInt(document.getElementById("idx"));
    var declaration = document.getElementById("declaration");
    //console.log(idx);
    alert(idx);


    var confirmation = confirm("재학습 하시겠습니까?");
    if (confirmation) {
        // Send the request.
        var xhr = new XMLHttpRequest();
        // xhr.open("POST", "http://127.0.0.1:5000/api/text/${data.idx}/${data.declaration}");
        // xhr.open("POST", "http://127.0.0.1:5000/api/text/" +"/"+ voicedata.declaration);
        xhr.open("POST", "http://127.0.0.1:5000/api/text/" + idx +"/"+ declaration);
        // console.log(data.idx);
        xhr.onload = function() {
            if (xhr.status === 200) {
                // The request was successful.
                console.log("요청되었습니다!");
            } else {
                // The request failed.
                console.log("실패했습니다!");
            }
        };
        xhr.send();
    }

    */

    /*삽질3
    const data = {
        idx: $("#idx").val(),
        declaration: $("#declaration").val()
    };

    const formData = new FormData();
    formData.append("file", file);
    fetch(`http://127.0.0.1:5000/api/text/${data.idx}/${data.declaration}`, {
        method: "POST",
        body: formData,
    })
        .then((response) => response.json())
        .then((result) => {
            console.log("Success:", result);
        })
        .catch((error) => {
            console.error("Error:", error);
        });
*/








}

/*삽질2
    // Create a new XMLHTTP req
    var xhr = new XMLHttpRequest();

    //request 메소드를 post에 set
    xhr.open("POST", "http://127.0.0.1:5000/api/text/{idx}/{declaration}", true);

    // Set the request content type to multipart/form-data.
    xhr.setRequestHeader("Content-Type", "multipart/form-data");

    // Add the parameters to the request body.
    xhr.send("file=" + content);

    // Handle the response.
    xhr.onload = function() {
        if (xhr.status === 200) {
            // The request was successful.
            console.log("The request was successful.");
            alert("재학습 요청 되었습니다.");
        } else {
            // The request failed.
            console.log("The request failed.");
            alert("재학습 요청 실패. status code : " + xhr.status);
        }
    };

    // Add a confirmation dialog.
    var dialog = confirm("재학습 요청하시겠습니까?");

    // If the user clicks OK, then execute the request.
    if (dialog) {
        console.log("requestReroll 함수가 실행되었습니다.");
        requestReroll(idx, declaration, content);
    } else {
        console.log("실패했습니다.");
    }
}
*/



    //삽질 흔적 JSON @RequestBody 주의
    // const data = {
    //     idx: $("#idx").val(),
    //     declaration: $("#declaration").val()
    // };
    //
    // var confirm = window.confirm("재학습 요청 하시겠습니까?");
    //
    // if(confirm) {
    //     $.ajax({
    //         type: "POST",
    //         url: "/api/text/" + data.idx + "/" + data.declaration,
    //         data: JSON.stringify(data),
    //         contentType: "application/json; charset=utf-8",
    //         dataType: "json"
    //     }).done(function () {
    //         alert("요청되었습니다");
    //     }).fail(function (error) {
    //         alert("실패했습니다.")
    //     });
    // }
