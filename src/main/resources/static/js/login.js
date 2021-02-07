$(document).ready(function () {
    const userName = localStorage.getItem("userName");
    const userPass = localStorage.getItem("userPass");
    if (userName != null) {
        $("#userName").val(userName);
    }
    if (userPass != null) {
        $("#userPass").val(userPass);
    }
});

$('#user-login').click(function () {
    let prevLink = document.referrer;
    $('#user-login').button('loading');
    const userName = $("#userName").val();
    const userPassword = $("#userPass").val();
    const rememberMe = $("#rememberMe").prop("checked");
    if (userName == "" || userPassword == "") {
        showMsg("请输入完整信息！", "error", 1000);
        $('#user-login').button('reset');
    } else {
        $.ajax({
            type: 'POST',
            url: '/userLogin',
            async: false,
            data: {
                'userName': userName,
                'userPassword': userPassword
            },
            success: function (data) {
                if (rememberMe) {
                    localStorage.setItem('userName', $("#userName").val());
                    localStorage.setItem('userPass', $("#userPass").val());
                } else {
                    localStorage.setItem('userName', '');
                    localStorage.setItem('userPass', '');
                }
                if (data.code == 1) {
                    if ($.trim(prevLink) == '' || $.trim(prevLink).indexOf('/login') != -1 || $.trim(prevLink).indexOf('/register') != -1 || $.trim(prevLink).indexOf('/forget') != -1) {
                        prevLink = '/post_publish';
                    }
                    showMsgAndRedirect(data.msg, "success", 1000, prevLink);
                } else {
                    showMsg(data.msg, "error", 1000);
                    $('#user-login').button('reset');
                }
            }
        });
    }
});


$('#userRegister').click(function () {
    $('#userRegister').button('loading');
    const userName = $("#userName").val();
    const userPassword = $("#userPassword").val();
    const reUserPassword = $("#reUserPassword").val();
    const userEmail = $("#userEmail").val();
    const userSex = $("input[type='radio']:checked").val();
    const userAge = $("#userAge").val();
    if (userName == "" || userPassword == "" || userEmail == "" || reUserPassword == "" || userSex == "" || userAge == "") {
        showMsg("请输入完整信息！", "error", 1000);
        $('#userRegister').button('reset');
    } else {
        $.ajax({
            type: 'POST',
            url: '/userRegister',
            async: false,
            data: {
                'userName': userName,
                'userPassword': userPassword,
                'userEmail': userEmail,
                'reUserPassword' : reUserPassword,
                'userSex' : userSex,
                'userAge' : userAge
            },
            success: function (data) {
                if (data.code == 1) {
                    showMsgAndRedirect(data.msg, "success", 1000, "/login");
                } else {
                    showMsg(data.msg, "error", 1000);
                    $('#userRegister').button('reset');
                    localStorage.setItem('userName', userName);
                }
            }
        });
    }
});


$('#btn-forget').click(function () {
    const userName = $("#userName").val();
    const userEmail = $("#userEmail").val();
    if (userName == "" || userEmail == "") {
        showMsg("请输入完整信息！", "error", 1000);
        $('#btn-register').button('reset');
    } else {
        $.ajax({
            type: 'POST',
            url: '/forget',
            async: false,
            data: {
                'userName': userName,
                'userEmail': userEmail
            },
            success: function (data) {
                if (data.code == 1) {
                    showMsgAndRedirect(data.msg, "success", 2000, "/login");
                } else {
                    showMsg(data.msg, "error", 1000);
                    $('#btn-forget').button('reset');
                    localStorage.setItem('userName', userName);
                }
            }
        });
    }
});
