layui.use(['form', 'table'], function () {
    const $ = layui.jquery,
        form = layui.form,
        table = layui.table;

    table.render({
        elem: '#currentTableId',
        url: '/usl/admin/api/script',
        toolbar: '#toolbarDemo',
        defaultToolbar: [],
        cols: [[
            {type: "checkbox", width: 50},
            {field: 'scriptName', width: 250, title: '脚本名称'},
            {field: 'belongs', width: 250, title: '功能分组', sort: true},
            {field: 'lastUpdatedTime', width: 250, title: '最近更新时间', sort: true},
            {field: 'fileSize', width: 200, title: '脚本字节数'},
            {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
        ]],
        page: false,
        skin: 'grid',
        even: true,
        parseData: function (result) {
            if (result.code === 'success') {
                return {
                    "code": 0,
                    "msg": result.message,
                    "data": result.data
                }
            } else {
                layer.msg('数据获取失败', {
                    icon: 2,
                    time: 2000,
                    anim: 5
                })
            }
        }
    });

    // 监听搜索操作
    form.on('submit(data-search-btn)', function (data) {
        const result = data.field;
        console.log(result);
        // 执行搜索重载
        table.reload('currentTableId', {
            where: {
                scriptName: result.scriptName,
                belongs: result.belongs
            }
        });
        return false;
    });

    /**
     * toolbar监听事件
     */
    table.on('toolbar(currentTableFilter)', function (obj) {
        if (obj.event === 'add') {  // 监听添加操作
            const index = layer.open({
                title: '新增脚本',
                type: 2,
                shade: 0.2,
                maxmin: true,
                shadeClose: true,
                area: ['100%', '100%'],
                content: 'add-script.html',
                end: function () {
                    // 执行搜索重载
                    table.reload('currentTableId');
                }
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        }
        if (obj.event === 'delete') {  // 监听删除操作
            const checkStatus = table.checkStatus('currentTableId')
                , data = checkStatus.data;
            $.ajax({
                type: 'post',
                url: '/usl/admin/api/delete-script',
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (result) {
                    if (result.code === 'success') {
                        layer.msg('删除成功', {
                            icon: 1,
                            time: 1000,
                            anim: 5
                        }, function () {
                            table.reload('currentTableId');
                        })
                    }
                    if (result.code === 'failure') {
                        layer.msg(result.message, {
                            icon: 2,
                            time: 2000,
                            anim: 5
                        }, function () {
                            if (result.data != null) {
                                window.location.href = result.data;
                            }
                        })
                    }
                }
            })
        }
    });

    // 监听表格复选框选择
    table.on('checkbox(currentTableFilter)', function (obj) {
        console.log(obj)
    });

    table.on('tool(currentTableFilter)', function (obj) {
        const data = obj.data;
        console.log(data);
        if (obj.event === 'edit') {
            const index = layer.open({
                title: '查看脚本',
                type: 2,
                shade: 0.2,
                maxmin: true,
                shadeClose: true,
                area: ['100%', '100%'],
                content: 'edit-script.html?scriptName=' + data.scriptName + '&belongs=' + data.belongs,
                end: function () {
                    // 执行搜索重载
                    table.reload('currentTableId');
                }
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
            return false;
        }
        if (obj.event === 'test') {
            $.ajax({
                type: 'post',
                url: '/usl/admin/api/script/run',
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (result) {
                    if (result.code === 100) {
                        layer.alert(result.data, {
                            title: '脚本运行成功',
                            anim: 5
                        })
                    } else if (result.code === 'failure') {
                        layer.msg(result.message, {
                            time: 2000,
                            anim: 5
                        })
                    } else {
                        layer.alert(result.data, {
                            title: '脚本运行失败',
                            anim: 5
                        })
                    }
                }
            })
        }
    });

});