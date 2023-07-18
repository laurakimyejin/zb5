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
            id: $("id").val(),
            del
        }
        const con_check = confirm("재학습요청 하시겠습니까?");
        if(con_check === true){

        }
    }
}