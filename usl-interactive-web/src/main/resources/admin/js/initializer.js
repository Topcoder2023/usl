layui.use(['table', 'treetable'], function () {
    const $ = layui.jquery;
    const table = layui.table;
    const treetable = layui.treetable;

    // 渲染表格
    layer.load(2);
    treetable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: '/usl/admin/api/initializer',
        cols: [[
            {type: 'numbers'},
            {field: 'name', width: 250, title: '初始化器名称'},
            {field: 'belongs', width: 150, title: '模块名'},
            {field: 'className', width: 500, title: '全类名'},
            {field: 'order', width: 150, align: 'center', title: '优先级'},
            {
                field: 'byUsl', width: 80, align: 'center', templet: function (data) {
                    if (data.byUsl) {
                        return '<span class="layui-badge layui-bg-blue">预定义</span>';
                    } else {
                        return '<span class="layui-badge layui-bg-green">自定义</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 120, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });

    $('#btn-expand').click(function () {
        treetable.expandAll('#munu-table');
    });

    $('#btn-fold').click(function () {
        treetable.foldAll('#munu-table');
    });

    // 监听操作事件
    table.on('tool(munu-table)', function (obj) {
        const data = obj.data;
        const layEvent = obj.event;

        if (layEvent === 'download') {
            window.open('/usl/admin/api/download?downloadUrl=' + data.viewUrl);
        }
        if (layEvent === 'view') {
            window.open(data.viewUrl);
        }
    });
});