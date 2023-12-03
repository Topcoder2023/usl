layui.use(['form'], function () {
    const form = layui.form;
    const layer = layui.layer;

    if (top.location !== self.location) {
        top.location = self.location
    }

    // 粒子背景
    $(document).ready(function () {
        $('.layui-container').particleground({
            dotColor: '#7ec7fd',
            lineColor: '#7ec7fd'
        });
    });

    // 登录操作
    form.on('submit(login)', function (data) {
        data = data.field;
        if (data.runner === '') {
            layer.msg('实例名不能为空');
            return false;
        }

        $.post('/usl/api/login', data, function (result) {
            if (result.code === 'success') {
                layer.msg('登录成功', {
                    icon: 1,
                    time: 1000,
                    anim: 5
                }, function () {
                    $.removeCookie('access_token');
                    $.cookie.raw = true;
                    $.cookie('access_token', result.data, {
                        path: '/usl',
                        expires: 1
                    });
                    window.location.href = '../admin/page/index';
                })
            } else if (result.code === 'failure') {
                layer.msg('账户或密码错误', {
                    icon: 2,
                    time: 1500,
                    anim: 5
                }, function () {
                    $('#runner').val('')
                    $('#password').val('')
                })
            } else {
                layer.msg('服务器出现错误', {
                    icon: 2,
                    time: 1500,
                    anim: 5
                }, function () {
                    $('#runner').val('')
                    $('#password').val('')
                })
            }
        })

        return false;
    });
});