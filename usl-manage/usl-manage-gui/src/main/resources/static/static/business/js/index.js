// 获取系统信息
const system_info = '/system/info';

$(document).ready(function () {
    $(function () {
        getMenu();
    });

    $.get(system_info, function (res) {
        if (res.code === 200) {
            for (let item of res.data) {
                const html = '<tr>' +
                    '<td>' + item.description + '</td>' +
                    '<td>' + item.value + '</td>' +
                    '</tr>';
                $('#index-tbody').append(html);
            }
        }
    });
});