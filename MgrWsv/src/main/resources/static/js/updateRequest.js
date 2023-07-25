function updateRequest() {

    const chk = confirm("모델에 적용하시겠습니까?");
    if (chk === true) {
        $.ajax({
            type: "GET",
            url: "/api/modelupdate/",
            data: null,
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function () {
            alert("제출하였습니다");
        }).fail(function (error) {
            alert(JSON.stringify(error));
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