//관리자가 임의로 데이터를 admindata에 집어넣는 코드
function adminSubmit() {
    const data = {
        id: $("#id").val(),
        admindata: $("#admindata").val()
    };

    const con_check = confirm("제출하시겠습니까?");
    if (con_check === true) {
        $.ajax({
            type: "PUT",
            url: "/api/voice/" + data.id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function () {
            alert("제출하였습니다");
            location.href = "/voice/read/" + data.id;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}