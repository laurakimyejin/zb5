// 인덱스로 쓰고 싶었지만 자꾸 안먹혀서 보류, 따로 빼놨음

let index = {
    init: function () {
        $("#btn-admindata-submit").on("click", () => {
            this.submit();
        });
        $("#btn-request-reoll").on("click", () =>{
           this.requestReroll();
        });
    },

    submit: function () {
        const data = {
            id: $("#id").val(),
            admindata: $("#admindata").val()
        };

        const con_check = confirm("제출하시겠습니까?");
        if(con_check === true){
            $.ajax({
                type: "PUT",
                url: "/api/voice/" + id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function () {
                alert("제출하였습니다");
                location.href = "/voice/read/" + id;
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }

    },

    requestReroll: function (){

        const data = {
            userid: $("#userid").val(),
            declaration: $("#declaration").val()
        };

        var confirm = window.confirm("재학습 요청 하시겠습니까?");

        if(confirm){
            $.ajax({
                type: "PUT",
                url: "/api/text/{userid}/{declaration}",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function() {
                alert("요청되었습니다");
            }).fail(function (error){
                alert("실패했습니다.")
            });
            // var xhr = new XMLHttpRequest();
            // //set the request method to POST
            // xhr.open("POST" , "/api/text/"+ userid + declaration);
            // //send request
            // xhr.send();
            //
            // //check the request status code
            // if(xhr.status === 200){
            //     alert("요청되었습니다");
            // }else{
            //     alert("실패했습니다.");
            // }
        }
    }
}