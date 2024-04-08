const file_list = '/file/list';
const file_view = '/file/view';
const file_load = '/file/load';
const file_clear = '/file/clear';

$(function () {
    getMenu();
    selectData();
});

function reload() {
    $.post(file_load, function (res) {
        if (res.code === 200) {
            layer.msg('加载成功', {
                icon: 1,
                time: 2000
            }, function () {
                window.location.reload();
            });
        }
    });
}

function clearIndex() {
    $.post(file_clear, function (res) {
        if (res.code === 200) {
            layer.msg('清除成功', {
                icon: 1,
                time: 2000
            }, function () {
                window.location.reload();
            });
        }
    })
}

function selectData() {
    params = {
        name: $('#search-input-name').val(),
        suffix: $('#search-input-suffix').val()
    };

    $.get(file_list, params, function (res) {
        if (res.code === 200) {
            const data = res.data;
            let content = '';
            if (data.length > 0) {
                for (let i = 0; i < data.length; i++) {
                    content +=
                        '<tr>' +
                        '<td>' + data[i].name + '</td>' +
                        '<td>' + (parseInt(data[i].size / 1024)) + 'KB</td>' +
                        '<td>' + data[i].suffix + '</td>' +
                        '<td class="text-l"><u style="cursor:pointer" class="text-primary" onClick="view(\'' + data[i].uuid + '\', 1)" title="查看">' + data[i].path + '</u></td>' +
                        '<td class="text-l"><u style="cursor:pointer" class="text-primary" onClick="view(\'' + data[i].uuid + '\', 0)" title="查看">' + data[i].category + '</u></td>'
                    if (data[i].status === 1) {
                        content += '<td class="td-status"><span class="label label-success radius">已同步</span></td>';
                    } else {
                        content += '<td class="td-status"><span class="label label-danger radius">未同步</span></td>';
                    }
                    content += '</tr>';
                }
                $(".getData-list").html(content);
            }
        }
    })
}

function view(uuid, fileFlag) {
    let params = {
        uuid: uuid,
        fileFlag: fileFlag
    }
    $.get(file_view, params, function (res) {
        if (res.code !== 200) {
            alert(res.message);
        }
    })
}