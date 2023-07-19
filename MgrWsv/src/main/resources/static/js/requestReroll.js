function requestReroll(){

    const data = {
        idx: $("#idx").val(),
        declaration: $("#declaration").val()
    };

    var confirm = window.confirm("재학습 요청 하시겠습니까?");

    if(confirm) {
        $.ajax({
            type: "POST",
            url: "/api/text/{idx}/{declaration}",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function () {
            alert("요청되었습니다");
        }).fail(function (error) {
            alert("실패했습니다.")
        });
    }
}