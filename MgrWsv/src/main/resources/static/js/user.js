let index = {
    init: function () {
        $("#btn-save").on("click", () => {
            this.save();
        });
        $("#btn-update").on("click", () => {
            this.update();
        });
        // $("#btn-role").on("click", () => {
        //     this.role();
        // });
    },
    save: function () {
        let data = {
            username: $("#username").val(),
            userid: $("#userid").val(),
            password: $("#password").val(),
            password2: $("#password2").val(),
            phoneNumber: $("#phoneNumber").val(),
        }
        var username = $("#username").val();
        var userid = $("#userid").val();
        var password = $("#password").val();
        var password2 = $("#password2").val();
        var phoneNumber = $("#phoneNumber").val();

        if (userid.length == 0) {
            alert("아이디를 입력해 주세요");
            $("#userid").focus();
            return false;
        }
        if (userid.length < 3 || userid.length > 12) {
            alert("아이디는 4~12자 사이의 영어만 사용해 주세요");
            $("#username").focus();
            return false;
        }
        if (password.length == 0) {
            $("#password").focus();
            return false;
        }

        if (password == password2) {
            if (password.length < 5 || password.length > 16) {
                alert("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
                $("#password").focus();
                return false;
            }
        } else if (password != password2) {
            alert("비밀번호가 서로 다릅니다. 비밀번호를 확인해 주세요.");
            $("#password2").focus();
            return false;
        }

        if (phoneNumber.length == 0) {
            alert("전화번호를 입력해주세요");
            $("#phoneNumber").focus();
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset=utf-8", //보내는 body 데이터가 어떤 타입인지
            //MIME 유형을 기반으로 유추한다(default =Intelligent Guess (xml, json, script, or html)
            dataType: "json"//서버에서 어떤 타입을 받을 것인지를 의미 (요청이 서버로 응답이 왔을 때,javascript 오브젝트로 변경)
        }).done(function (res) {
            if (res.status === 500) {
                alert("회원가입에 실패하였습니다!");
            } else {
                alert("회원가입 완료!🎉");
                location.href = "/";
            }
        }).fail(function (error) {
            alert("양식에 맞게 정보를 기입해 주세요!!");
        });
    },

    // role: function (){
    //     const data ={
    //         idx: $("#idx").val(),
    //         role: $("#role").val()
    //     }
    //     const con_check = confirm("권한 변경하시겠습니까?");
    //     if (con_check === true) {
    //         if (data.role === "ADMIN") {
    //             data.role = "USER";
    //         } else {
    //             data.role = "ADMIN";
    //         }
    //         $.ajax({
    //             type: "PUT",
    //             url: "/api/user/" + data.idx,
    //             data: JSON.stringify(data),
    //             contentType: "application/json; charset=utf-8",
    //             dataType: "json"
    //         }).done(function () {
    //             alert("제출하였습니다");
    //             location.href = "/voice/listForm";
    //         }).fail(function (error) {
    //             alert(JSON.stringify(error));
    //         });
    //     }
    // }

    // update:
    //
    //     function () {
    //         let data = {
    //             id: $("#id").val(),
    //             username: $("#username").val(),
    //             password: $("#password").val(),
    //             phoneNumber: $("#phoneNumber").val(),
    //         }
    //         $.ajax({
    //             type: "PUT",
    //             url: "/user",
    //             data: JSON.stringify(data), //http body 데이터
    //             contentType: "application/json; charset=utf-8", //보내는 body 데이터가 어떤 타입인지
    //             //MIME 유형을 기반으로 유추한다(default =Intelligent Guess (xml, json, script, or html)
    //             dataType: "json"//서버에서 어떤 타입을 받을 것인지를 의미 (요청이 서버로 응답이 왔을 때,javascript 오브젝트로 변경)
    //         }).done(function (res) {
    //             alert("회원 정보 수정 완료!🎉")
    //             location.href = "/";
    //         }).fail(function (error) {
    //             alert(JSON.stringify(error));
    //         });
    //     }
    //
    // ,
}

index.init();