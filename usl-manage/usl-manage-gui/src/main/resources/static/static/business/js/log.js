const log_list = '/log/list';

$(function () {
    getMenu();
    selectLogList();
});

function selectLogList() {
    params = {
        level: $('#search-input-level').val(),
        time: $('#search-input-time').val(),
        type: $('#search-input-type').val()
    };

    $.get(log_list, params, function (res) {
        if (res.code === 200) {
            const data = res.data;
            let content = '';
            if (data.length > 0) {
                for (let i = 0; i < data.length; i++) {
                    content +=
                        '<tr>' +
                        '<td style="text-align: center">' + data[i].type + '</td>' +
                        '<td style="text-align: center">' + data[i].level + '</td>' +
                        '<td style="text-align: center">' + data[i].time + '</td>' +
                        '<td style="text-align: center">' + data[i].message + '</td>' +
                        '<td style="text-align: center">' + data[i].source + '</td></tr>'
                }
                $("#log-body").html(content);
            }
        }
    })
}